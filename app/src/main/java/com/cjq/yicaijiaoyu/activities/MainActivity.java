package com.cjq.yicaijiaoyu.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cjq.yicaijiaoyu.CommonDataObject;
import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.adapter.MenuAdapter;
import com.cjq.yicaijiaoyu.entities.MainMenuEvent;
import com.cjq.yicaijiaoyu.entities.MenuItemEntity;
import com.cjq.yicaijiaoyu.fragments.AllCourseFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private SlidingMenu menu;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        CommonDataObject.CM= (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        //注册弹出菜单监听
        EventBus.getDefault().register(this);

        //构造侧滑菜单
        menu = new SlidingMenu(this);
        //设置划出方向
        menu.setMode(SlidingMenu.LEFT);

        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
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

        //为侧滑菜单添加菜单项
        List<MenuItemEntity> items = new ArrayList<>();
        items.add(new MenuItemEntity(getString(R.string.all_courses), R.drawable.kecheng_icon, R.drawable.kecheng_icon_dian));
        items.add(new MenuItemEntity(getString(R.string.my_courses), R.drawable.wode_icon, R.drawable.wode_icon_dian));
        items.add(new MenuItemEntity(getString(R.string.sign_up_online), R.drawable.baoming, R.drawable.baoming_dianji));
        items.add(new MenuItemEntity(getString(R.string.my_setting), R.drawable.shezhi, R.drawable.shezhi_dian));

        ListView listView = (ListView) menu.findViewById(R.id.menu_items);
        final MenuAdapter adapter = new MenuAdapter(this, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonDataObject.menuChecked = position;
                adapter.notifyDataSetChanged();
                //todo 请求课程列表，现在假设请求了
                switch (CommonDataObject.menuChecked) {
                    case 0:
                        //全部课程

                        break;
                    case 1:
                        //我的课程
                        break;
                    case 2:
                        //在线报名
                        break;
                    case 3:
                        //我的设置
                        break;
                }
            }
        });

        //侧滑菜单一键签到按钮
        menu.findViewById(R.id.one_key_sign_in).setOnClickListener(this);

        //todo 检查签到
//        menu.findViewById(R.id.one_key_sign_in).setBackgroundColor(getResources().getColor(R.color.one_key_sign_uped));
//        menu.findViewById(R.id.one_key_sign_in).setClickable(false);
//        ((TextView)menu.findViewById(R.id.one_key_sign_in)).setText(R.string.signed_uped);

        //获取FragmentManager
        manager = getSupportFragmentManager();

        loadFragment();
    }

    private void loadFragment() {
        //不论如何，切换碎片总是会重置筛选条件

        CommonDataObject.categoryChecked=0;
        CommonDataObject.my_categoryChecked=0;

        switch (CommonDataObject.menuChecked){
            case 0:
                AllCourseFragment fragment = new AllCourseFragment();
                manager.beginTransaction().replace(R.id.content,fragment).commit();
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
        }
    }

    public void onEventMainThread(MainMenuEvent e) {
        Log.i("sds","sdsdadasdasdasd");
        menu.toggle();
    }
}
