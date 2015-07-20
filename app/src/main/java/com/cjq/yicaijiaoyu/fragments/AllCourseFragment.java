package com.cjq.yicaijiaoyu.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cjq.yicaijiaoyu.CommonDataObject;
import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.activities.SearchActivity;
import com.cjq.yicaijiaoyu.adapter.CourseCategoryAdapter;
import com.cjq.yicaijiaoyu.adapter.CourseListAdapter;
import com.cjq.yicaijiaoyu.adapter.RecommendCourseAdapter;
import com.cjq.yicaijiaoyu.dao.Ad;
import com.cjq.yicaijiaoyu.dao.Category;
import com.cjq.yicaijiaoyu.dao.Course;
import com.cjq.yicaijiaoyu.entities.AdRequestEntity;
import com.cjq.yicaijiaoyu.entities.AllCourseRequestEntity;
import com.cjq.yicaijiaoyu.entities.MainMenuEvent;
import com.cjq.yicaijiaoyu.utils.CourseHistoryUtil;
import com.cjq.yicaijiaoyu.utils.JsonUtil;
import com.cjq.yicaijiaoyu.utils.NetStateUtil;
import com.cjq.yicaijiaoyu.utils.PopWindowUtil;
import com.cjq.yicaijiaoyu.utils.TimerForSeconds;
import com.cjq.yicaijiaoyu.utils.VideoUtil;
import com.markmao.pulltorefresh.widget.XScrollView;
import com.ypy.eventbus.EventBus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by CJQ on 2015/6/24.
 */
public class AllCourseFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, XScrollView.IXScrollViewListener, AdapterView.OnItemClickListener {

    private boolean TIMER_NOT_DESTROYED = true;
    private boolean TIMER_PAUSED = false;
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
    private List<Ad> bannerList;
    private List<Category> categoryEntityList;
    private Category all;
    private List<Course> courses; //课程总表

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;

        view = inflater.inflate(R.layout.activity_main, container, false);
        //注册菜单键
        view.findViewById(R.id.main_left_menu_button).setOnClickListener(this);

        //标题栏
        title = view.findViewById(R.id.title);

        view.findViewById(R.id.refresh).setVisibility(View.VISIBLE);
        //注册下拉刷新和上拉加载
        mRefreshLayout = (XScrollView) view.findViewById(R.id.refresh);
        mRefreshLayout.setPullRefreshEnable(true);
//        mRefreshLayout.setPullLoadEnable(true);
//        mRefreshLayout.setAutoLoadEnable(true);

        mRefreshLayout.setIXScrollViewListener(AllCourseFragment.this);
        mRefreshLayout.setRefreshTime(getTime());

        View content = inflater.inflate(R.layout.course_list_content, mRefreshLayout, false);
        mRefreshLayout.setView(content);
        //初始化课程list
        courseList = (ListView) content.findViewById(R.id.course_list);
        courseList.setOnItemClickListener(AllCourseFragment.this);
        //读取缓存进行加载
        courses = CourseHistoryUtil.listCourses(getActivity(), 0, CommonDataObject.COURSE_NUM_SHOWING, "");
        courseListAdapter = new CourseListAdapter(courses, getActivity());
        courseList.setAdapter(courseListAdapter);

        //下拉分类点击注册
        titleText = (TextView) view.findViewById(R.id.main_title_text);
        arrowImage = (ImageView) view.findViewById(R.id.main_drop_arrow);
        view.findViewById(R.id.main_click_drop).setOnClickListener(AllCourseFragment.this);
        //初始化下拉分类列表
        if (all == null) {
            all = new Category();
            all.setId(CommonDataObject.NO_CATE_ID);
            all.setImageId(R.drawable.all_icon);
            all.setName(getString(R.string.all_courses));
        }
        categoryEntityList = new ArrayList<>();
        categoryEntityList.add(all);
        categoryEntityList.addAll(CourseHistoryUtil.listCategory(getActivity()));

        categoryAdapter = new CourseCategoryAdapter(categoryEntityList, getActivity());

        //注册搜索键
        view.findViewById(R.id.main_search_button).setOnClickListener(AllCourseFragment.this);

        //注册banner
        banner_pager = (ViewPager) content.findViewById(R.id.banner_pager);
        bannerList = CourseHistoryUtil.listAd(getActivity());

        //初始化banner
        for (Ad ad : bannerList) {
            ad.setView(LayoutInflater.from(getActivity()).inflate(R.layout.recommend_course, null));
        }

        banner_pager.setAdapter(new RecommendCourseAdapter(bannerList, getActivity()));

        //检查缓存是否为空
        if (courses.size() == 0 || categoryEntityList.size() == 1 || bannerList.size() == 0) {
            //进行刷新请求
            getCategory();
            AllCourseRequestEntity.Data data = new AllCourseRequestEntity.Data((int) Math.ceil((double) courses.size() / CommonDataObject.COURSE_NUM_SHOWING), CommonDataObject.COURSE_NUM_SHOWING);
            AllCourseRequestEntity requestEntity = new AllCourseRequestEntity(CommonDataObject.SORTED_COURSE_LIST_REQUEST_CODE, data);
            refresh(requestEntity);
        }

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

        return view;
    }

    private void refresh(final AllCourseRequestEntity requestEntity) {
        //凡是进行刷新，清空缓存

        StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.COURSE_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    if ("0000".equals(object.getString("code"))) {
                        JSONArray goods = object.getJSONObject("data").getJSONObject("categories").getJSONArray("goods");
                        //清空课程列表
                        if ("0".equals(requestEntity.getData().getCatId()))
                            CourseHistoryUtil.courseDeleteAll(getActivity(), requestEntity.getData().getIsfree());
                        else
                            CourseHistoryUtil.courseDeleteAllByCategory(getActivity(), Long.parseLong(requestEntity.getData().getCatId()), requestEntity.getData().getIsfree());
                        if (courses == null)
                            courses = new ArrayList<Course>();
                        courses.clear();
                        for (int i = 0; i < goods.length(); i++) {
                            courses.add(JsonUtil.getFromCourseList(getActivity(), goods.getJSONObject(i)));
                        }
                        if (courseListAdapter == null)
                            courseListAdapter = new CourseListAdapter(courses, getActivity());
                        if (courseList.getAdapter() == null)
                            courseList.setAdapter(courseListAdapter);
                    }
                    onLoad();
                    courseListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                System.out.println(volleyError.getMessage());
                onLoad();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opjson", CommonDataObject.GSON.toJson(requestEntity));
                return params;
            }
        };

        //获取推荐课程banner
        StringRequest request1 = new StringRequest(Request.Method.POST, CommonDataObject.AD_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    //2、清空banner缓存
                    CourseHistoryUtil.adDeleteAll(getActivity());
                    if ("0000".equals(object.getString("code"))) {
                        bannerList.clear();
                        CourseHistoryUtil.adDeleteAll(getActivity());
                        JSONArray a = object.getJSONObject("data").getJSONArray("categories");
                        for (int i = 0; i < a.length(); i++) {
                            Ad ad = JsonUtil.getAd(getActivity(), a.getJSONObject(i));
                            ad.setView(LayoutInflater.from(getActivity()).inflate(R.layout.recommend_course, null));
                            bannerList.add(ad);
                        }
                        banner_pager.getAdapter().notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                if(volleyError!=null)
//                System.out.println(volleyError.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                AdRequestEntity requestEntity = new AdRequestEntity(CommonDataObject.AD_REQUEST_CODE);
                params.put("opjson", CommonDataObject.GSON.toJson(requestEntity));
                return params;
            }
        };
        //开启请求队列
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request1);
        queue.add(request);
        queue.start();
    }

    private void getCategory() {
        //初始化课程分类下拉适配器
        categoryEntityList = new ArrayList<>();
        StringRequest request2 = new StringRequest(Request.Method.POST, CommonDataObject.SORT_CAT_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    if ("0000".equals(object.getString("code"))) {
                        categoryEntityList.clear();
                        categoryEntityList.add(all);
                        //清空分类缓存
                        CourseHistoryUtil.categoryDeleteAll(getActivity());
                        JSONArray a = object.getJSONObject("data").getJSONArray("categories");
                        for (int i = 0; i < a.length(); i++) {
                            JSONObject o = a.getJSONObject(i);
                            Category category = JsonUtil.getCategory(getActivity(), o);
                            categoryEntityList.add(category);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                AdRequestEntity entity = new AdRequestEntity(CommonDataObject.SORT_CAT_REQUEST_CODE);
                params.put("opjson", CommonDataObject.GSON.toJson(entity));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request2);
        queue.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_menu_button:
                EventBus.getDefault().post(new MainMenuEvent());
                break;
            case R.id.main_click_drop:
                //点击下拉课程分类
                if (window == null) {
                    window = new PopupWindow(view.getMeasuredWidth(), 400);
                    window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            arrowImage.setImageResource(R.drawable.jiantou);
                        }
                    });
                }
                if (windowView == null) {
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
                            CommonDataObject.categoryChecked = position;
                            categoryAdapter.notifyDataSetChanged();
                            sortCourse();
                        }
                    });
                }
                PopWindowUtil.show(windowView, title, null, window, R.style.course_category_list);
                arrowImage.setImageResource(R.drawable.jiantou_shang);
                break;
            case R.id.main_search_button:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void sortCourse() {
        //刷新当重置页数
        boolean fee = true;
        boolean free = true;
        if (windowView != null) {
            fee = checkBoxFee.isChecked();
            free = checkBoxFree.isChecked();
        }
        String freeString = null;
        if (fee && free)
            freeString = "";
        else if (free)
            freeString = "1";
        else if (fee)
            freeString = "0";

        long category_id;
        if (categoryEntityList.size() <= CommonDataObject.categoryChecked) {
            category_id = CommonDataObject.NO_CATE_ID;
        } else if (categoryEntityList.get(CommonDataObject.categoryChecked).getCategoryId() != null)
            category_id = Long.parseLong(categoryEntityList.get(CommonDataObject.categoryChecked).getCategoryId());
        else
            category_id = CommonDataObject.NO_CATE_ID;

        //执行课程筛选 网络
        if (CommonDataObject.NO_CATE_ID != category_id) {
//            直接请求列表
            //首先判断freeString
            if (freeString == null) {
                //不予以请求，清空list
                courses.clear();
                courseListAdapter.notifyDataSetChanged();
            } else {
                //进行请求
                //读缓存
                courses = CourseHistoryUtil.listCourseByCategory(getActivity(), 0, CommonDataObject.COURSE_NUM_SHOWING, category_id, freeString);
                courseList.setAdapter(new CourseListAdapter(courses, getActivity()));
                if (courses.size() < 5) {
                    AllCourseRequestEntity.Data data = new AllCourseRequestEntity.Data(String.valueOf(category_id), (int) Math.ceil((double) courses.size() / CommonDataObject.COURSE_NUM_SHOWING), CommonDataObject.COURSE_NUM_SHOWING, freeString);
                    AllCourseRequestEntity entity = new AllCourseRequestEntity(CommonDataObject.SORTED_COURSE_LIST_REQUEST_CODE, data);
                    refresh(entity);
                }
            }
        } else {
            //构建全部请求
            if (freeString == null) {
                //不予以请求，清空list
                courses.clear();
                courseListAdapter.notifyDataSetChanged();
            } else {
                //进行请求
                courses = CourseHistoryUtil.listCourses(getActivity(), 0, CommonDataObject.COURSE_NUM_SHOWING, freeString);
                courseList.setAdapter(new CourseListAdapter(courses, getActivity()));
                if (courses.size() < 5) {
                    AllCourseRequestEntity.Data data = new AllCourseRequestEntity.Data(freeString, (int) Math.ceil((double) courses.size() / CommonDataObject.COURSE_NUM_SHOWING), CommonDataObject.COURSE_NUM_SHOWING);
                    AllCourseRequestEntity entity = new AllCourseRequestEntity(CommonDataObject.SORTED_COURSE_LIST_REQUEST_CODE, data);
                    refresh(entity);
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        sortCourse();
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    private void onLoad() {
        mRefreshLayout.stopRefresh();
        mRefreshLayout.stopLoadMore();
        mRefreshLayout.setRefreshTime(getTime());
        mRefreshLayout.setPullRefreshEnable(true);
//        mRefreshLayout.setPullLoadEnable(true);
//        mRefreshLayout.setAutoLoadEnable(true);
    }

    private void isLoading() {
        mRefreshLayout.setPullRefreshEnable(false);
//        mRefreshLayout.setPullLoadEnable(false);
//        mRefreshLayout.setAutoLoadEnable(false);
    }

    @Override
    public void onRefresh() {
        isLoading();
        boolean fee = true;
        boolean free = true;
        if (windowView != null) {
            fee = checkBoxFee.isChecked();
            free = checkBoxFree.isChecked();
        }
        String freeString = null;
        if (fee && free)
            freeString = "";
        else if (free)
            freeString = "1";
        else if (fee)
            freeString = "0";

        long category_id;
        if (categoryEntityList.size() <= CommonDataObject.categoryChecked) {
            category_id = CommonDataObject.NO_CATE_ID;
        } else if (categoryEntityList.get(CommonDataObject.categoryChecked).getCategoryId() != null)
            category_id = Long.parseLong(categoryEntityList.get(CommonDataObject.categoryChecked).getCategoryId());
        else
            category_id = CommonDataObject.NO_CATE_ID;

        //执行课程筛选 网络
        if (CommonDataObject.NO_CATE_ID != category_id) {
//            直接请求列表
            //首先判断freeString
            if (freeString == null) {
                //不予以请求，清空list
                courses.clear();
                courseListAdapter.notifyDataSetChanged();
                onLoad();
            } else {
                AllCourseRequestEntity.Data data = new AllCourseRequestEntity.Data(String.valueOf(category_id), (int) Math.ceil((double) courses.size() / CommonDataObject.COURSE_NUM_SHOWING), CommonDataObject.COURSE_NUM_SHOWING, freeString);
                AllCourseRequestEntity entity = new AllCourseRequestEntity(CommonDataObject.SORTED_COURSE_LIST_REQUEST_CODE, data);
                refresh(entity);
            }
        } else {
            //构建全部请求
            if (freeString == null) {
                //不予以请求，清空list
                courses.clear();
                courseListAdapter.notifyDataSetChanged();
                onLoad();
            } else {
                //进行请求
                AllCourseRequestEntity.Data data = new AllCourseRequestEntity.Data(freeString, (int) Math.ceil((double) courses.size() / CommonDataObject.COURSE_NUM_SHOWING), CommonDataObject.COURSE_NUM_SHOWING);
                AllCourseRequestEntity entity = new AllCourseRequestEntity(CommonDataObject.SORTED_COURSE_LIST_REQUEST_CODE, data);
                refresh(entity);
            }
        }
    }

    @Override
    public void onLoadMore() {
        isLoading();
        NetStateUtil.checkNetwork(new NetStateUtil.NetWorkStateListener() {
            @Override
            public void doWithNetWork() {
                loadNextPage();
            }

            @Override
            public void doWithoutNetWork() {
                onLoad();
            }
        });
    }

    private void loadNextPage() {
        boolean fee = true;
        boolean free = true;
        if (windowView != null) {
            fee = checkBoxFee.isChecked();
            free = checkBoxFree.isChecked();
        }
        String freeString = null;
        if (fee && free)
            freeString = "";
        else if (free)
            freeString = "1";
        else if (fee)
            freeString = "0";

        long category_id;
        if (categoryEntityList.size() <= CommonDataObject.categoryChecked) {
            category_id = CommonDataObject.NO_CATE_ID;
        } else
            category_id = categoryEntityList.get(CommonDataObject.categoryChecked).getId();

        //执行课程筛选 网络
        if (CommonDataObject.NO_CATE_ID != category_id) {
//            直接请求列表
            //首先判断freeString
            if (freeString == null) {
                //不予以请求，清空list
                courses.clear();
                courseListAdapter.notifyDataSetChanged();
                onLoad();
            } else {
                //进行请求
                //加载缓存
                List<Course> courseListTemp = CourseHistoryUtil.listCourseByCategory(getActivity(), (int) Math.ceil((double) courses.size() / CommonDataObject.COURSE_NUM_SHOWING) * CommonDataObject.COURSE_NUM_SHOWING, CommonDataObject.COURSE_NUM_SHOWING, category_id, freeString);
                if (courseListTemp.size() > 0) {
                    courses.addAll(courseListTemp);
                    onLoad();
                } else {
                    AllCourseRequestEntity.Data data = new AllCourseRequestEntity.Data(String.valueOf(category_id), (int) Math.ceil((double) courses.size() / CommonDataObject.COURSE_NUM_SHOWING), CommonDataObject.COURSE_NUM_SHOWING, freeString);
                    AllCourseRequestEntity entity = new AllCourseRequestEntity(CommonDataObject.SORTED_COURSE_LIST_REQUEST_CODE, data);
                    doLoadMore(entity);
                }
            }
        } else {
            //构建全部请求
            if (freeString == null) {
                //不予以请求，清空list
                courses.clear();
                courseListAdapter.notifyDataSetChanged();
                onLoad();
            } else {
                //进行请求
                List<Course> courseListTemp = CourseHistoryUtil.listCourses(getActivity(), (int) Math.ceil((double) courses.size() / CommonDataObject.COURSE_NUM_SHOWING) * CommonDataObject.COURSE_NUM_SHOWING, CommonDataObject.COURSE_NUM_SHOWING, freeString);
                if (courseListTemp.size() > 0) {
                    courses.addAll(courseListTemp);
                    onLoad();
                } else {
                    AllCourseRequestEntity.Data data = new AllCourseRequestEntity.Data(freeString, (int) Math.ceil((double) courses.size() / CommonDataObject.COURSE_NUM_SHOWING), CommonDataObject.COURSE_NUM_SHOWING);
                    AllCourseRequestEntity entity = new AllCourseRequestEntity(CommonDataObject.SORTED_COURSE_LIST_REQUEST_CODE, data);
                    doLoadMore(entity);
                }
            }
        }
    }

    public void doLoadMore(final AllCourseRequestEntity entity) {
        StringRequest request = new StringRequest(Request.Method.POST, CommonDataObject.COURSE_LIST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    if ("0000".equals(object.getString("code"))) {
                        JSONArray goods = object.getJSONObject("data").getJSONObject("categories").getJSONArray("goods");
//                        courseEntityList.clear();
                        final List<Course> temp = new ArrayList<>();

                        if (goods.length() == 0) {
                            Toast.makeText(getActivity(), getActivity().getString(R.string.hint11), Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < goods.length(); i++) {
                                temp.add(JsonUtil.getFromCourseList(getActivity(), goods.getJSONObject(i)));
                            }
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    courseListAdapter.getCourses().addAll(temp);
                                }
                            });
                        }
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onLoad();
                            }
                        }, 500);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("opjson", CommonDataObject.GSON.toJson(entity));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
        queue.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        跳转到视频
        Course course = courses.get(position);

        VideoUtil.startVideo(getActivity(), course);
    }

    public void scrollToTop() {
        if (mRefreshLayout != null)
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.scrollTo(0, 0);
                }
            }, 500);
    }
}