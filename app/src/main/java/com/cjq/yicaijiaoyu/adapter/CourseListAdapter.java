package com.cjq.yicaijiaoyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.entities.CourseEntity;
import com.cjq.yicaijiaoyu.utils.ImageUtil;

import java.util.List;

/**
 * Created by android on 2015/6/24.
 */
public class CourseListAdapter extends BaseAdapter {
    List<CourseEntity> courses;
    Context context;

    public void setCourses(List<CourseEntity> courses) {
        this.courses = courses;
    }

    public CourseListAdapter(List<CourseEntity> courses, Context context) {
        this.courses = courses;
        this.context = context;
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Object getItem(int position) {
        return courses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.course_list_item,parent,false);
            ViewHolder holder = new ViewHolder();
            holder.cover = (ImageView) convertView.findViewById(R.id.cover);
            holder.courseTitle = (TextView) convertView.findViewById(R.id.course_title);
            holder.lecture = (TextView) convertView.findViewById(R.id.lecture);
            holder.category = (TextView) convertView.findViewById(R.id.category);
            holder.free  = (TextView) convertView.findViewById(R.id.free);
            convertView.setTag(holder);
        }
        ViewHolder holder  = (ViewHolder) convertView.getTag();
        CourseEntity entity = courses.get(position);
        if(entity.isFree()){
            holder.free.setText(context.getResources().getString(R.string.for_free));
            holder.free.setBackgroundColor(context.getResources().getColor(R.color.main_titlebar_background));
        }else{
            holder.free.setText(context.getResources().getString(R.string.not_free));
            holder.free.setBackgroundColor(context.getResources().getColor(R.color.orange_background));
        }
        ImageUtil.LoadImage(context,entity.getCover_image_url(),holder.cover);
        holder.courseTitle.setText(entity.getTitle());
        holder.lecture.setText(entity.getLecture().getName());
        holder.category.setText(entity.getCategory().getName());
        return convertView;
    }

    private class ViewHolder{
        ImageView cover;
        TextView courseTitle;
        TextView lecture;
        TextView category;
        TextView free;
    }
}
