package com.cjq.yicaijiaoyu.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.cjq.yicaijiaoyu.adapter.CourseCategoryAdapter;
import com.cjq.yicaijiaoyu.adapter.CourseListAdapter;
import com.cjq.yicaijiaoyu.adapter.PagerAdapter;
import com.cjq.yicaijiaoyu.entities.AllCourseRequestEntity;
import com.cjq.yicaijiaoyu.entities.CategoryEntity;
import com.cjq.yicaijiaoyu.entities.CourseCategory;
import com.cjq.yicaijiaoyu.entities.CourseEntity;
import com.cjq.yicaijiaoyu.entities.CourseListRequestEvent;
import com.cjq.yicaijiaoyu.entities.CourseListResultEvent;
import com.cjq.yicaijiaoyu.entities.LectureEntity;
import com.cjq.yicaijiaoyu.entities.MainMenuEvent;
import com.cjq.yicaijiaoyu.utils.AccountUtil;
import com.cjq.yicaijiaoyu.utils.CourseHistoryUtil;
import com.cjq.yicaijiaoyu.utils.CourseUtil;
import com.cjq.yicaijiaoyu.utils.NetStateUtil;
import com.cjq.yicaijiaoyu.utils.PopWindowUtil;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CJQ on 2015/6/25.
 */
public class MyCourseFragment extends Fragment implements View.OnClickListener {

    private View view;
    private View title;
    private List<CourseEntity> courseEntityList;
    private CourseListAdapter courseListAdapter;
    private ImageView arrowImage;
    private CourseCategoryAdapter categoryAdapter;
    private PopupWindow window;
    private View windowView;
    private LayoutInflater inflater;
    private int tab;
    private View soap;
    private TextView careFor;//关注
    private TextView bought;//已购
    private TextView history;//历史
    private ViewPager pager;

    public void onEventBackgroundThread(CourseListRequestEvent e){
        //收到了请求
        //生成Adapter
        courseListAdapter = new CourseListAdapter(null,getActivity());
        final CourseListResultEvent event = new CourseListResultEvent().setNO(e.getNO());
        switch (e.getNO()){
            case 0:
                event.setRequestCode(CommonDataObject.COURSE_CARE_REQUEST_CODE);
                AllCourseRequestEntity.Data data = new AllCourseRequestEntity.Data(1,CommonDataObject.COURSE_NUM_SHOWING);
                data.setUserId(AccountUtil.getUserId(getActivity()));
                AllCourseRequestEntity entity = new AllCourseRequestEntity(CommonDataObject.COURSE_CARE_REQUEST_CODE,data);
                getCourseListAdapterEvent(event,entity);
                break;
            case 1:
                event.setRequestCode(CommonDataObject.COURSE_BOUGHT_REQUEST_CODE);
                AllCourseRequestEntity.Data data2 = new AllCourseRequestEntity.Data(1,CommonDataObject.COURSE_NUM_SHOWING);
                data2.setUserId(AccountUtil.getUserId(getActivity()));
                AllCourseRequestEntity entity2 = new AllCourseRequestEntity(CommonDataObject.COURSE_BOUGHT_REQUEST_CODE,data2);
                getCourseListAdapterEvent(event,entity2);
                break;
            case 2:
                //查询播放历史
                List<CourseEntity> temp = CourseHistoryUtil.listHistory(getActivity());
                event.setAdapter(new CourseListAdapter(temp,getActivity()));
                event.setRequestCode(null);
                EventBus.getDefault().post(event);
                break;
        }
    }

    private void getCourseListAdapterEvent(final CourseListResultEvent event, final AllCourseRequestEntity entity) {
        StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.COURSE_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    if("0000".equals(object.getString("code"))){
                        List<CourseEntity> temp = new ArrayList<>();
                        CourseUtil.chargeCourseList(object.getJSONObject("data").getJSONArray("categories"), temp);

                        CourseListAdapter adapter = new CourseListAdapter(temp,getActivity());
                        event.setAdapter(adapter);
                        EventBus.getDefault().post(event);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("opjson",CommonDataObject.GSON.toJson(entity));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
        queue.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;

        view = inflater.inflate(R.layout.my_courses,container,false);
        //注册菜单键
        view.findViewById(R.id.main_left_menu_button).setOnClickListener(this);
        initialRR();
        //标题栏
        title = view.findViewById(R.id.title);

        //事件注册
        EventBus.getDefault().register(this);

        pager= (ViewPager) view.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(4);

        //初始化三碎片
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MyCourseListFragment().setNO(0));
        fragments.add(new MyCourseListFragment().setNO(1));
        fragments.add(new MyCourseListFragment().setNO(2));

        pager.setAdapter(new PagerAdapter(getFragmentManager(),fragments));

        //下拉分类点击注册
        arrowImage = (ImageView)view.findViewById(R.id.main_drop_arrow);
        view.findViewById(R.id.main_click_drop).setOnClickListener(this);

        //初始化课程分类下拉适配器
        List<CategoryEntity> categorys = new ArrayList<>();
        categorys.add(new CategoryEntity(CommonDataObject.NO_CATE_ID,null,getActivity().getString(R.string.all_courses),R.drawable.all_icon));

        categoryAdapter = new CourseCategoryAdapter(categorys,getActivity());

        return view;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    private void initialRR() {
        soap = view.findViewById(R.id.soap);
        //初始化肥皂的长宽
        careFor = (TextView) view.findViewById(R.id.detail);
        bought = (TextView) view.findViewById(R.id.pinlun);
        history = (TextView) view.findViewById(R.id.chapter);

        LinearLayout rr = (LinearLayout) view.findViewById(R.id.rrs);
        int count = rr.getChildCount();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) soap.getLayoutParams();

        int width = getResources().getDisplayMetrics().widthPixels;

        params.width = width / count;
        soap.setLayoutParams(params);

        //初始化三按钮
        careFor.setTextColor(getResources().getColor(R.color.main_titlebar_background));

        careFor.setOnClickListener(this);
        bought.setOnClickListener(this);
        history.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_left_menu_button:
                EventBus.getDefault().post(new MainMenuEvent());
                break;
            case R.id.main_click_drop:
                //点击下拉课程分类
                if(window ==null ){
                    window = new PopupWindow(view.getMeasuredWidth(), 400);
                    window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            arrowImage.setImageResource(R.drawable.jiantou);
                        }
                    });
                }
                if(windowView ==null){
                    windowView = inflater.inflate(R.layout.my_sort_window, null);
                    ListView listView = (ListView) windowView;
                    listView.setAdapter(categoryAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            CommonDataObject.categoryChecked=position;
                            categoryAdapter.notifyDataSetChanged();
                            NetStateUtil.checkNetwork(new NetStateUtil.NetWorkStateListener() {
                                @Override
                                public void doWithNetWork() {
                                }

                                @Override
                                public void doWithoutNetWork() {

                                }
                            });
                        }
                    });
                }
                PopWindowUtil.show(windowView, title, null, window, R.style.course_category_list);
                arrowImage.setImageResource(R.drawable.jiantou_shang);
                break;
            case R.id.detail:
                tab = 0;
                changeColor();
                changeFragment();
                break;
            case R.id.pinlun:
                tab = 1;
                changeColor();
                changeFragment();
                break;
            case R.id.chapter:
                tab = 2;
                changeColor();
                changeFragment();
                break;
        }
    }

    /**
     * 改变头颜色的方法
     */
    private void changeColor() {
        //首先改变颜色值
        RelativeLayout.LayoutParams params;
        int left;
        switch (tab) {
            case 0:
                //改变颜色值
                careFor.setTextColor(getResources().getColor(R.color.main_titlebar_background));
                bought.setTextColor(getResources().getColor(R.color.menu_text_color));
                history.setTextColor(getResources().getColor(R.color.menu_text_color));
                //移动滑块
                params = (RelativeLayout.LayoutParams) soap.getLayoutParams();
                params.leftMargin=careFor.getLeft();
                soap.setLayoutParams(params);
                break;
            case 1:
//改变颜色值
                bought.setTextColor(getResources().getColor(R.color.main_titlebar_background));
                careFor.setTextColor(getResources().getColor(R.color.menu_text_color));
                history.setTextColor(getResources().getColor(R.color.menu_text_color));
                //移动滑块
                params = (RelativeLayout.LayoutParams) soap.getLayoutParams();
                params.leftMargin=bought.getLeft();

                soap.setLayoutParams(params);
                break;
            case 2:
//改变颜色值
                history.setTextColor(getResources().getColor(R.color.main_titlebar_background));
                bought.setTextColor(getResources().getColor(R.color.menu_text_color));
                careFor.setTextColor(getResources().getColor(R.color.menu_text_color));
                //移动滑块
                params = (RelativeLayout.LayoutParams) soap.getLayoutParams();
                params.leftMargin=history.getLeft();

                soap.setLayoutParams(params);
                break;
        }
    }

    private void changeFragment(){
        pager.setCurrentItem(tab);
    }
}
