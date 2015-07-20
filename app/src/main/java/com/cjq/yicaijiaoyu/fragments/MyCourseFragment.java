package com.cjq.yicaijiaoyu.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.adapter.PagerAdapter;
import com.cjq.yicaijiaoyu.entities.MainMenuEvent;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJQ on 2015/6/25.
 */
public class MyCourseFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private View view;
    private int tab;
    private View soap;
    private TextView careFor;//关注
    private TextView bought;//已购
    private TextView history;//历史
    private ViewPager pager;
    private List<Fragment> fragments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.my_courses,container,false);
        //注册菜单键
        view.findViewById(R.id.main_left_menu_button).setOnClickListener(this);
        initialRR();

        pager= (ViewPager) view.findViewById(R.id.pager);
        pager.setOffscreenPageLimit(4);

        //初始化三碎片
        fragments = new ArrayList<>();
        fragments.add(new MyCaredListFragment());
        fragments.add(new MyBoughtListFragment());
        fragments.add(new MyHistoryListFragment());

        pager.setAdapter(new PagerAdapter(getFragmentManager(),fragments));

        pager.addOnPageChangeListener(this);

        return view;
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
//            case R.id.main_click_drop:
//                //点击下拉课程分类
//                if(window ==null ){
//                    window = new PopupWindow(view.getMeasuredWidth(), 400);
//                    window.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                        @Override
//                        public void onDismiss() {
//                            arrowImage.setImageResource(R.drawable.jiantou);
//                        }
//                    });
//                }
//                if(windowView ==null){
//                    windowView = inflater.inflate(R.layout.my_sort_window, null);
//                    ListView listView = (ListView) windowView;
//                    listView.setAdapter(categoryAdapter);
//
//                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            CommonDataObject.categoryChecked=position;
//                            categoryAdapter.notifyDataSetChanged();
//                            NetStateUtil.checkNetwork(new NetStateUtil.NetWorkStateListener() {
//                                @Override
//                                public void doWithNetWork() {
//                                }
//
//                                @Override
//                                public void doWithoutNetWork() {
//
//                                }
//                            });
//                        }
//                    });
//                }
//                PopWindowUtil.show(windowView, title, null, window, R.style.course_category_list);
//                arrowImage.setImageResource(R.drawable.jiantou_shang);
//                break;
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        tab=position;
        changeColor();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
