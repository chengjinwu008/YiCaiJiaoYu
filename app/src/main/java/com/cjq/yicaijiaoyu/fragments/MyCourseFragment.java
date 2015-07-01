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

import com.cjq.yicaijiaoyu.CommonDataObject;
import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.adapter.CourseCategoryAdapter;
import com.cjq.yicaijiaoyu.adapter.CourseListAdapter;
import com.cjq.yicaijiaoyu.adapter.PagerAdapter;
import com.cjq.yicaijiaoyu.entities.CategoryEntity;
import com.cjq.yicaijiaoyu.entities.CourseCategory;
import com.cjq.yicaijiaoyu.entities.CourseEntity;
import com.cjq.yicaijiaoyu.entities.CourseListRequestEvent;
import com.cjq.yicaijiaoyu.entities.CourseListResultEvent;
import com.cjq.yicaijiaoyu.entities.LectureEntity;
import com.cjq.yicaijiaoyu.entities.MainMenuEvent;
import com.cjq.yicaijiaoyu.utils.NetStateUtil;
import com.cjq.yicaijiaoyu.utils.PopWindowUtil;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

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
        CourseListResultEvent event = new CourseListResultEvent();
        switch (tab){
            case 0:
                event.setRequestCode(CommonDataObject.COURSE_CARE_REQUEST_CODE);
                //todo 获取课程列表
                courseEntityList = new ArrayList<>();
                LectureEntity lectureEntity = new LectureEntity("陈昶","呵呵呵","https://www.baidu.com/img/bd_logo1.png");

                CourseEntity courseEntity3 = new CourseEntity();
                courseEntity3.setCover_image_url("https://www.baidu.com/img/bd_logo1.png");
                courseEntity3.setTitle("资产负载要素产妇1");
                courseEntity3.setFree(true);
                courseEntity3.setId("sl8da4jjbxc377d0a79c7224552b6ee4_s");
                courseEntity3.setCategory(CourseCategory.FOR_PRIMARY);
                courseEntity3.setLecture(lectureEntity);
                courseEntityList.add(courseEntity3);

                CourseEntity courseEntity4 = new CourseEntity();
                courseEntity4.setCover_image_url("https://www.baidu.com/img/bd_logo1.png");
                courseEntity4.setTitle("资产负载要素产妇1");
                courseEntity4.setFree(false);
                courseEntity4.setId("sl8da4jjbx5d715bc3a8ce8f8194afab_s");
                courseEntity4.setCategory(CourseCategory.FOR_INTERMEDIATE);
                courseEntity4.setIntro("这个是收费视频，你看不到的~");
                courseEntity4.setLecture(lectureEntity);
                courseEntityList.add(courseEntity4);
                courseListAdapter.setCourses(courseEntityList);
                event.setAdapter(courseListAdapter);
                break;
            case 1:
                event.setRequestCode(CommonDataObject.COURSE_BOUGHT_REQUEST_CODE);
                break;
            case 2:
                event.setRequestCode(null);
                break;
        }
        EventBus.getDefault().post(event);
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
        fragments.add(new MyCourseListFragment());
        fragments.add(new MyCourseListFragment());
        fragments.add(new MyCourseListFragment());

        pager.setAdapter(new PagerAdapter(getFragmentManager(),fragments));

        //下拉分类点击注册
        arrowImage = (ImageView)view.findViewById(R.id.main_drop_arrow);
        view.findViewById(R.id.main_click_drop).setOnClickListener(this);

        //初始化课程分类下拉适配器
        List<CategoryEntity> categorys = new ArrayList<>();
        categorys.add(new CategoryEntity(R.drawable.guanzhu_dianji,getActivity().getString(R.string.all_video)));
        categorys.add(new CategoryEntity(R.drawable.guanzhu_dianji,getActivity().getString(R.string.for_job)));
        categorys.add(new CategoryEntity(R.drawable.guanzhu_dianji,getActivity().getString(R.string.for_primary)));
        categorys.add(new CategoryEntity(R.drawable.guanzhu_dianji,getActivity().getString(R.string.for_intermediate)));

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
