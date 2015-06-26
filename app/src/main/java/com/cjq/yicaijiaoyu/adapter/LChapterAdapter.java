package com.cjq.yicaijiaoyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjq.yicaijiaoyu.CommonDataObject;
import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.entities.CourseEntity;
import com.cjq.yicaijiaoyu.utils.ImageUtil;

import java.util.List;

/**
 * Created by CJQ on 2015/6/26.
 */
public class LChapterAdapter extends BaseAdapter {
    List<CourseEntity> courses;
    Context context;

    public List<CourseEntity> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseEntity> courses) {
        this.courses = courses;
    }

    public LChapterAdapter(List<CourseEntity> courses, Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.l_chapter_item,parent,false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);

            convertView.setTag(viewHolder);
        }

        CourseEntity course = courses.get(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if(CommonDataObject.nowPlayingId!=null && CommonDataObject.nowPlayingId.equals(course.getId())){
            ImageUtil.LoadImage(context,R.drawable.bofang_xuan,holder.imageView);
            holder.title.setText(course.getTitle());
            holder.time.setText(course.getLength());
            holder.title.setTextColor(context.getResources().getColor(R.color.main_titlebar_background));
            holder.time.setTextColor(context.getResources().getColor(R.color.main_titlebar_background));
        }else{
            ImageUtil.LoadImage(context,R.drawable.bofang,holder.imageView);
            holder.title.setText(course.getTitle());
            holder.time.setText(course.getLength());
        }
        return convertView;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView title;
        TextView time;
    }
}
