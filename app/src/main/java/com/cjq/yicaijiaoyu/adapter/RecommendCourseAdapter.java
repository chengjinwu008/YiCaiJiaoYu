package com.cjq.yicaijiaoyu.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.entities.CourseEntity;
import com.cjq.yicaijiaoyu.utils.ImageUtil;

import java.util.List;

/**
 * Created by android on 2015/6/23.
 */
public class RecommendCourseAdapter extends PagerAdapter {

    List<CourseEntity> courses;
    private Context context;

    public List<CourseEntity> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseEntity> courses) {
        this.courses = courses;
    }

    public RecommendCourseAdapter(List<CourseEntity> courses, Context context) {
        this.courses = courses;
        this.context = context;
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(View view, int position, Object object)                       //销毁Item
    {
        ((ViewPager) view).removeView(courses.get(position).getView());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = courses.get(position).getView();
        ImageUtil.LoadImage(context,courses.get(position).getCover_image_url(), (ImageView) view.findViewById(R.id.image));
        ((TextView) view.findViewById(R.id.text)).setText(courses.get(position).getTitle());
        container.addView(view);
        return view;
    }
}
