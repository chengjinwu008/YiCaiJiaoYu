package com.cjq.yicaijiaoyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.dao.Video;

import java.util.List;

/**
 * Created by CJQ on 2015/6/26.
 */
public class LChapterAdapter extends BaseAdapter {
    List<Video> courses;
    Context context;

    public List<Video> getCourses() {
        return courses;
    }

    public void setCourses(List<Video> courses) {
        this.courses = courses;
    }

    public LChapterAdapter(List<Video> courses, Context context) {
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

        Video course = courses.get(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();
//        if(CommonDataObject.nowPlayingId!=null && CommonDataObject.nowPlayingId.equals(course.getVid())){
//            ImageUtil.LoadImage(context, R.drawable.bofang_xuan, holder.imageView);
//            holder.title.setText(course.getName());
//            holder.time.setText(getLength(course.getLength()));
//            holder.title.setTextColor(context.getResources().getColor(R.color.main_titlebar_background));
//            holder.time.setTextColor(context.getResources().getColor(R.color.main_titlebar_background));
//        }else{
//            ImageUtil.LoadImage(context,R.drawable.bofang,holder.imageView);
//            holder.title.setText(course.getName());
//            holder.time.setText(getLength(course.getLength()));
//        }
        return convertView;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView title;
        TextView time;
    }

    private String getLength(Long length){
        long len = length/1000;
        int h= (int) (len/3600);
        int m= (int) ((len-h*3600)/60);
        int s = (int) (len-h*3600-m*60);
        return String.format("%02d", h) + ":" + String.format("%02d", h) + ":" + String.format("%02d", h);
    }
}
