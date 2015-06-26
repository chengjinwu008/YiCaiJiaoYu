package com.cjq.yicaijiaoyu.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cjq.yicaijiaoyu.CommonDataObject;
import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.adapter.CourseCategoryAdapter;
import com.cjq.yicaijiaoyu.adapter.CourseListAdapter;
import com.cjq.yicaijiaoyu.adapter.RecommendCourseAdapter;
import com.cjq.yicaijiaoyu.entities.CategoryEntity;
import com.cjq.yicaijiaoyu.entities.CourseCategory;
import com.cjq.yicaijiaoyu.entities.CourseEntity;
import com.cjq.yicaijiaoyu.entities.LectureEntity;
import com.cjq.yicaijiaoyu.entities.MainMenuEvent;
import com.cjq.yicaijiaoyu.utils.ImageUtil;
import com.cjq.yicaijiaoyu.utils.NetStateUtil;
import com.cjq.yicaijiaoyu.utils.PopWindowUtil;
import com.cjq.yicaijiaoyu.utils.TimerForSeconds;
import com.cjq.yicaijiaoyu.utils.VideoUtil;
import com.markmao.pulltorefresh.widget.XScrollView;
import com.ypy.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by CJQ on 2015/6/24.
 */
public class AllCourseFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, XScrollView.IXScrollViewListener, AdapterView.OnItemClickListener {

    private boolean TIMER_NOT_DESTROYED = true;
    private boolean TIMER_PAUSED = false;
    private List<CourseEntity> courseEntityList;//课程总表
    private ListView courseList;
    private CourseListAdapter courseListAdapter;
    private TextView titleText;
    private ImageView arrowImage;
    private CourseCategoryAdapter categoryAdapter;
    private PopupWindow window;
    private View view;
    private View windowView;
    private LayoutInflater inflater;
    private CheckBox checkBoxFee;
    private CheckBox checkBoxFree;
    private View title;
    private Handler mHandler = new Handler();
    private XScrollView mRefreshLayout;
    private ViewPager banner_pager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;

        view = inflater.inflate(R.layout.activity_main,container,false);
        //注册菜单键
        view.findViewById(R.id.main_left_menu_button).setOnClickListener(this);

        //标题栏
        title = view.findViewById(R.id.title);

        //注册下拉刷新和上拉加载
        mRefreshLayout = (XScrollView) view.findViewById(R.id.refresh);
        mRefreshLayout.setPullRefreshEnable(true);
        mRefreshLayout.setPullLoadEnable(true);
        mRefreshLayout.setAutoLoadEnable(true);

        mRefreshLayout.setIXScrollViewListener(this);
        mRefreshLayout.setRefreshTime(getTime());

        View content = inflater.inflate(R.layout.course_list_content, mRefreshLayout,false);

        mRefreshLayout.setView(content);


        //初始化课程视频列表
        //todo 请求课程列表 课程列表要筛选 提升为属性
        courseEntityList = new ArrayList<>();

        LectureEntity lectureEntity = new LectureEntity("陈昶","呵呵呵","https://www.baidu.com/img/bd_logo1.png");

        CourseEntity courseEntity3 = new CourseEntity();
        courseEntity3.setCover_image_url("https://www.baidu.com/img/bd_logo1.png");
        courseEntity3.setTitle("资产负载要素产妇");
        courseEntity3.setFree(true);
        courseEntity3.setId("sl8da4jjbxc377d0a79c7224552b6ee4_s");
        courseEntity3.setCategory(CourseCategory.FOR_PRIMARY);
        courseEntity3.setLecture(lectureEntity);
        courseEntityList.add(courseEntity3);


        CourseEntity courseEntity4 = new CourseEntity();
        courseEntity4.setCover_image_url("https://www.baidu.com/img/bd_logo1.png");
        courseEntity4.setTitle("资产负载要素产妇");
        courseEntity4.setFree(false);
        courseEntity4.setId("sl8da4jjbx5d715bc3a8ce8f8194afab_s");
        courseEntity4.setCategory(CourseCategory.FOR_INTERMEDIATE);
        courseEntity4.setIntro("这个是收费视频，你看不到的~");
        courseEntity4.setLecture(lectureEntity);
        courseEntityList.add(courseEntity4);

        //获取到课程list
        courseList = (ListView) content.findViewById(R.id.course_list);

        courseList.setOnItemClickListener(this);

        courseListAdapter = new CourseListAdapter(courseEntityList,getActivity());
        courseList.setAdapter(courseListAdapter);

        //todo 获取推荐课程banner
        List<CourseEntity> courses = new ArrayList<>();
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setTitle("你猜");
        courseEntity.setCover_image_url("https://www.baidu.com/img/bd_logo1.png");
        courseEntity.setView(getBanner());
        courseEntity.setLecture(lectureEntity);
        courses.add(courseEntity);

        CourseEntity courseEntity2 = new CourseEntity();
        courseEntity2.setTitle("你猜2");
        courseEntity2.setCover_image_url("https://www.baidu.com/img/bd_logo1.png");
        courseEntity2.setView(getBanner());
        courseEntity2.setLecture(lectureEntity);
        courses.add(courseEntity2);

        banner_pager = (ViewPager)content.findViewById(R.id.banner_pager);
        banner_pager.setAdapter(new RecommendCourseAdapter(courses,getActivity()));
        //开启banner滚动线程
        Thread timer = new TimerForSeconds(5000, -1, new TimerForSeconds.TimerListener() {
            @Override
            public void onEverySeconds(int timeLeft) {
                int count = banner_pager.getChildCount();
                final int num = banner_pager.getCurrentItem() + 1;
                if (num < count)
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            banner_pager.setCurrentItem(num);
                        }
                    });

                else
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            banner_pager.setCurrentItem(0);
                        }
                    });
            }

            @Override
            public void onTimeUp() {

            }

            @Override
            public boolean getTimerFlag() {
                return TIMER_NOT_DESTROYED;
            }

            @Override
            public boolean getTimerPauseFlag() {
                return TIMER_PAUSED;
            }
        });
        timer.start();

        banner_pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        TIMER_PAUSED = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        TIMER_PAUSED = false;
                        break;
                }
                return false;
            }
        });

        //下拉分类点击注册
        titleText = (TextView) view.findViewById(R.id.main_title_text);
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

    private View getBanner() {
        View view = inflater.inflate(R.layout.recommend_course, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = banner_pager.getCurrentItem();
                //获取视频信息
                CourseEntity course = ((RecommendCourseAdapter)banner_pager.getAdapter()).getCourses().get(index);
                VideoUtil.startVideo(getActivity(),course);
            }
        });
        return view;
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
                    windowView = inflater.inflate(R.layout.sort_window, null);
                    ListView listView = (ListView) windowView.findViewById(R.id.category_list);
                    listView.setAdapter(categoryAdapter);
                    checkBoxFee = (CheckBox) windowView.findViewById(R.id.check_fee);
                    checkBoxFree = (CheckBox) windowView.findViewById(R.id.check_free);

                    checkBoxFee.setOnCheckedChangeListener(this);
                    checkBoxFree.setOnCheckedChangeListener(this);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            CommonDataObject.categoryChecked=position;
                            categoryAdapter.notifyDataSetChanged();
                            NetStateUtil.checkNetwork(new NetStateUtil.NetWorkStateListener() {
                                @Override
                                public void doWithNetWork() {
                                    sortCourse();
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
        }
    }

    private void sortCourse() {
        boolean fee=true;
        boolean free=true;
        if(windowView!=null){
            fee= checkBoxFee.isChecked();
            free= checkBoxFree.isChecked();
        }

        CourseCategory category = null;
        switch (CommonDataObject.categoryChecked){
            case  0:
                category=null;
                break;
            case 1:
                category=CourseCategory.FOR_JOB;
                break;
            case 2:
                category=CourseCategory.FOR_PRIMARY;
                break;
            case 3:
                category=CourseCategory.FOR_INTERMEDIATE;
                break;
        }

        //执行课程筛选 网络
        if(category!=null){
//            直接请求列表

            //todo 构建请求 fee free category

            courseEntityList.clear();
            LectureEntity lectureEntity = new LectureEntity("陈昶","呵呵呵","https://www.baidu.com/img/bd_logo1.png");
            CourseEntity courseEntity3 = new CourseEntity();
            courseEntity3.setCover_image_url("https://www.baidu.com/img/bd_logo1.png");
            courseEntity3.setTitle("资产负载要素产妇");
            courseEntity3.setFree(true);
            courseEntity3.setCategory(CourseCategory.FOR_PRIMARY);
            courseEntity3.setLecture(lectureEntity);
            courseEntityList.add(courseEntity3);


            CourseEntity courseEntity4 = new CourseEntity();
            courseEntity4.setCover_image_url("https://www.baidu.com/img/bd_logo1.png");
            courseEntity4.setTitle("资产负载要素产妇");
            courseEntity4.setFree(true);
            courseEntity4.setCategory(CourseCategory.FOR_INTERMEDIATE);
            courseEntity4.setLecture(lectureEntity);
            courseEntityList.add(courseEntity4);
            //提醒数据
            courseListAdapter.notifyDataSetChanged();
        }else{
            //todo 构建请求 fee free

            courseEntityList.clear();
            LectureEntity lectureEntity = new LectureEntity("陈昶","呵呵呵","https://www.baidu.com/img/bd_logo1.png");
            CourseEntity courseEntity3 = new CourseEntity();
            courseEntity3.setCover_image_url("https://www.baidu.com/img/bd_logo1.png");
            courseEntity3.setTitle("资产负载要素产妇");
            courseEntity3.setFree(true);
            courseEntity3.setCategory(CourseCategory.FOR_PRIMARY);
            courseEntity3.setLecture(lectureEntity);
            courseEntityList.add(courseEntity3);


            CourseEntity courseEntity4 = new CourseEntity();
            courseEntity4.setCover_image_url("https://www.baidu.com/img/bd_logo1.png");
            courseEntity4.setTitle("资产负载要素产妇");
            courseEntity4.setFree(true);
            courseEntity4.setCategory(CourseCategory.FOR_INTERMEDIATE);
            courseEntity4.setLecture(lectureEntity);
            courseEntityList.add(courseEntity4);

            courseListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    private void onLoad() {
        mRefreshLayout.stopRefresh();
        mRefreshLayout.stopLoadMore();
        mRefreshLayout.setRefreshTime(getTime());
        mRefreshLayout.setPullRefreshEnable(true);
        mRefreshLayout.setPullLoadEnable(true);
        mRefreshLayout.setAutoLoadEnable(true);
        courseListAdapter.notifyDataSetChanged();
    }

    private void isLoading() {
        mRefreshLayout.setPullRefreshEnable(false);
        mRefreshLayout.setPullLoadEnable(false);
        mRefreshLayout.setAutoLoadEnable(false);
    }

    @Override
    public void onRefresh() {
        isLoading();
        NetStateUtil.checkNetwork(new NetStateUtil.NetWorkStateListener() {
            @Override
            public void doWithNetWork() {
                //todo 请求刷新
                sortCourse();
                onLoad();
            }

            @Override
            public void doWithoutNetWork() {
                onLoad();
            }
        });
    }

    @Override
    public void onLoadMore() {
        isLoading();
        NetStateUtil.checkNetwork(new NetStateUtil.NetWorkStateListener() {
            @Override
            public void doWithNetWork() {
                //todo 请求加载
                courseEntityList.add(new CourseEntity("https://www.baidu.com/img/bd_logo1.png","新加的课程",CourseCategory.FOR_JOB,true));
                onLoad();
            }

            @Override
            public void doWithoutNetWork() {
                onLoad();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        跳转到视频
        CourseEntity course = courseEntityList.get(position);

        VideoUtil.startVideo(getActivity(),course);
    }
}
