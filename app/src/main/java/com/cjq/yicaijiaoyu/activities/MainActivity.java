package com.cjq.yicaijiaoyu.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cjq.yicaijiaoyu.CommonDataObject;
import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.adapter.MenuAdapter;
import com.cjq.yicaijiaoyu.adapter.PagerAdapter;
import com.cjq.yicaijiaoyu.entities.MainMenuEvent;
import com.cjq.yicaijiaoyu.entities.MenuItemEntity;
import com.cjq.yicaijiaoyu.entities.ShowLoginEvent;
import com.cjq.yicaijiaoyu.entities.SkipLeaderPageEvent;
import com.cjq.yicaijiaoyu.entities.UserInfoRequestEntity;
import com.cjq.yicaijiaoyu.entities.UserLoginEvent;
import com.cjq.yicaijiaoyu.fragments.AllCourseFragment;
import com.cjq.yicaijiaoyu.fragments.MyCourseFragment;
import com.cjq.yicaijiaoyu.fragments.MySettingFragment;
import com.cjq.yicaijiaoyu.fragments.WelcomeOneFragment;
import com.cjq.yicaijiaoyu.fragments.WelcomeThreeFragment;
import com.cjq.yicaijiaoyu.fragments.WelcomeTwoFragment;
import com.cjq.yicaijiaoyu.utils.AccountUtil;
import com.cjq.yicaijiaoyu.utils.ImageUtil;
import com.cjq.yicaijiaoyu.utils.NetStateUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private SlidingMenu menu;
    private FragmentManager manager;
    private ImageView menu_portrait;
    private TextView menu_username;
    private Handler mHandler=new Handler();
    private View content;
    private ViewPager welcome;

    public void onEventMainThread(UserLoginEvent e){
        checkLogin();
    }

    public void onEventMainThread(ShowLoginEvent e){
        showMain();
        showLoginActivity();
    }

    private void showMain() {
        //隐藏引导界面
        welcome.setVisibility(View.GONE);
    }

    public void onEventMainThread(SkipLeaderPageEvent e){
        showMain();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        welcome = (ViewPager) findViewById(R.id.welcome);
        if(savedInstanceState==null){
            //隐藏欢迎页
            findViewById(R.id.loading).setVisibility(View.VISIBLE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.loading).setVisibility(View.GONE);
                }
            },3000);
            welcome.setVisibility(View.VISIBLE);
            if(AccountUtil.isLoggedIn(this)){
                //跳过引导界面
                showMain();
            }else{
                //显示引导界面
                List<Fragment> fragments = new ArrayList<>();
                fragments.add(new WelcomeOneFragment());
                fragments.add(new WelcomeTwoFragment());
                fragments.add(new WelcomeThreeFragment());
                welcome.setAdapter(new PagerAdapter(getSupportFragmentManager(),fragments));
            }
        }

        content = findViewById(R.id.content);
        //判断登录

        CommonDataObject.CM = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //注册弹出菜单监听
        EventBus.getDefault().register(this);

        //构造侧滑菜单
        menu = new SlidingMenu(this);
        //设置划出方向
        menu.setMode(SlidingMenu.LEFT);

        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        //设置菜单给主界面的剩余空间
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);

        // 设置阴影的宽度
        menu.setShadowWidth(0);
        // 设置阴影的颜色
        menu.setShadowDrawable(R.drawable.shadow);

        // 设置渐变的程度，范围是0-1.0f,设置的越大，则在侧滑栏刚划出的时候，颜色就越暗。1.0f的时候，颜色为全黑
        menu.setFadeDegree(0.3f);

        // 设置触摸模式，可以选择全屏划出，或者是边缘划出，或者是不可划出
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

        menu.setMenu(R.layout.leftmenu);

        //todo 注册头像和未登录点击登录
        menu_portrait = (ImageView) menu.findViewById(R.id.menu_portrait);
        menu_username = (TextView) menu.findViewById(R.id.menu_username);
        menu_portrait.setOnClickListener(this);
        menu_username.setOnClickListener(this);


        //为侧滑菜单添加菜单项
        List<MenuItemEntity> items = new ArrayList<>();
        items.add(new MenuItemEntity(getString(R.string.all_courses), R.drawable.kecheng_icon, R.drawable.kecheng_icon_dian));
        items.add(new MenuItemEntity(getString(R.string.my_courses), R.drawable.wode_icon, R.drawable.wode_icon_dian));
//        items.add(new MenuItemEntity(getString(R.string.sign_up_online), R.drawable.baoming, R.drawable.baoming_dianji));
        items.add(new MenuItemEntity(getString(R.string.my_setting), R.drawable.shezhi, R.drawable.shezhi_dian));

        ListView listView = (ListView) menu.findViewById(R.id.menu_items);
        final MenuAdapter adapter = new MenuAdapter(this, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int preFragment = CommonDataObject.menuChecked;
                if (position == 1) {
                    //判断登录
                    if(AccountUtil.isLoggedIn(MainActivity.this))
                        CommonDataObject.menuChecked = position;
                    else{
                        //todo 未登录提示
                    }
                }else{
                    CommonDataObject.menuChecked = position;
                }

                adapter.notifyDataSetChanged();
                if (preFragment != position)
                    switch (CommonDataObject.menuChecked) {
                        case 0:
                            //全部课程
                            loadFragment();
                            menu.toggle();
                            break;
                        case 1:
                            //我的课程
                            loadFragment();
                            menu.toggle();
                            break;
                        case 2:
                            //在线报名 我的设置
                            loadFragment();
                            menu.toggle();
                            break;
//                    case 3:
//                        //我的设置
//                        loadFragment();
//                        break;
                    }
            }
        });

        //侧滑菜单一键签到按钮
        menu.findViewById(R.id.one_key_sign_in).setOnClickListener(this);

        //获取FragmentManager
        manager = getSupportFragmentManager();

        loadFragment();
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().post(new UserLoginEvent());
        super.onStart();
        //todo 每次展示页面的时候，进行用户信息请求
    }

    private void checkLogin() {
        if(AccountUtil.isLoggedIn(this)){
            //请求
            NetStateUtil.checkNetwork(new NetStateUtil.NetWorkStateListener() {
                @Override
                public void doWithNetWork() {
                    //有网络就请求
                    StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.USER_INFO_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //获取到用户信息的处理
                            try {
                                JSONObject object = new JSONObject(s);
                                String code = object.getString("code");
                                if("0000".equals(code)){
                                    JSONObject data = object.getJSONArray("data").getJSONObject(0);
                                    final String userName = data.getString("user_name");
//                                    final String portrait = data.getString("portrait");
                                    //用户名和头像回显
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //回显头像
//                                            ImageUtil.LoadImage(MainActivity.this,portrait,menu_portrait);
                                            //回显用户名
                                            menu_username.setText(userName);
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            UserInfoRequestEntity.Data data = new UserInfoRequestEntity.Data(AccountUtil.getUserId(MainActivity.this));
                            UserInfoRequestEntity entity = new UserInfoRequestEntity(CommonDataObject.USER_INFO_REQUEST_CODE,data);

                            params.put("opjson",CommonDataObject.GSON.toJson(entity));
                            return params;
                        }
                    };

                    //todo 签到查询接口

                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    queue.add(request);
                    queue.start();
                }

                @Override
                public void doWithoutNetWork() {
                    //没有网络，想做啥你自己想
                }
            });
        }

        if (!AccountUtil.isLoggedIn(this)) {
            menu.findViewById(R.id.one_key_sign_in).setVisibility(View.GONE);
        } else {
            menu.findViewById(R.id.one_key_sign_in).setVisibility(View.VISIBLE);
        }
    }

    private void loadFragment() {
        //不论如何，切换碎片总是会重置筛选条件

        CommonDataObject.categoryChecked = 0;

        switch (CommonDataObject.menuChecked) {
            case 0:
                AllCourseFragment fragment = new AllCourseFragment();
                manager.beginTransaction().replace(R.id.content, fragment).commit();
                break;
            case 1:
                //涉及到用户，所以应该检测用户的登录状态
                MyCourseFragment fragment1 = new MyCourseFragment();
                manager.beginTransaction().replace(R.id.content, fragment1).commit();
                break;
            case 2:
                MySettingFragment fragment2 = new MySettingFragment();
                manager.beginTransaction().replace(R.id.content, fragment2).commit();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        //取消监听
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one_key_sign_in:
                //todo 进行签到
                v.setBackgroundColor(getResources().getColor(R.color.one_key_sign_uped));
                ((TextView) v).setText(R.string.signed_uped);
                v.setClickable(false);
                break;
            case R.id.menu_username:
                if(!AccountUtil.isLoggedIn(this))
                showLoginActivity();
                break;
            case R.id.menu_portrait:
                if(!AccountUtil.isLoggedIn(this))
                showLoginActivity();
                break;
        }
    }

    private void showLoginActivity() {
        //todo 实现跳转到登录活动
        AccountUtil.showLoginActivity(this);
    }

    public void onEventMainThread(MainMenuEvent e) {
        menu.toggle();
    }
}
