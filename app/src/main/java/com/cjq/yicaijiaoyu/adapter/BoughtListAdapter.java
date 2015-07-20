package com.cjq.yicaijiaoyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.dao.Bought;
import com.cjq.yicaijiaoyu.dao.Cared;
import com.cjq.yicaijiaoyu.dao.Lecture;
import com.cjq.yicaijiaoyu.utils.ImageUtil;

import java.util.List;

/**
 * Created by android on 2015/6/24.
 */
public class BoughtListAdapter extends BaseAdapter {
    List<Bought> courses;
    Context context;
    private boolean showFree = true;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isShowFree() {
        return showFree;
    }

    public void setShowFree(boolean showFree) {
        this.showFree = showFree;
    }

    public List<Bought> getCourses() {
        return courses;
    }

    public void setCourses(List<Bought> courses) {
        this.courses = courses;
    }

    public BoughtListAdapter(List<Bought> courses, Context context) {
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
            convertView= LayoutInflater.from(context).inflate(R.layout.course_list_item,null);
            ViewHolder holder = new ViewHolder();
            holder.cover = (ImageView) convertView.findViewById(R.id.cover);
            holder.courseTitle = (TextView) convertView.findViewById(R.id.course_title);
            holder.lecture = (TextView) convertView.findViewById(R.id.lecture);
            holder.category = (TextView) convertView.findViewById(R.id.category);
            holder.free  = (TextView) convertView.findViewById(R.id.free);
            convertView.setTag(holder);
        }
        ViewHolder holder  = (ViewHolder) convertView.getTag();
        Bought entity = courses.get(position);
        if(showFree)
        switch (entity.getAuthority()) {
            case 0:
                holder.free.setText(context.getResources().getString(R.string.for_free));
                holder.free.setBackgroundColor(context.getResources().getColor(R.color.main_titlebar_background));
                break;
            case 1:
                holder.free.setText(context.getResources().getString(R.string.free_for_member));
                holder.free.setBackgroundColor(context.getResources().getColor(R.color.main_titlebar_background));
                break;
            case 2:
                holder.free.setText(context.getResources().getString(R.string.not_free));
                holder.free.setBackgroundColor(context.getResources().getColor(R.color.orange_background));
                break;
            case 3:
                holder.free.setText(context.getResources().getString(R.string.card));
                holder.free.setBackgroundColor(context.getResources().getColor(R.color.main_titlebar_background));
                break;
        }
        ImageUtil.LoadImage(context, entity.getImage(), holder.cover);
        holder.courseTitle.setText(entity.getName());
        StringBuilder sb = new StringBuilder();
        if(entity.getLectureList()!=null)
            for(Lecture l:entity.getLectureList()){
                sb.append(l.getName()).append("、");
            }
        if(sb.lastIndexOf("、")>0)
        sb.deleteCharAt(sb.lastIndexOf("、"));
        int len = sb.length();
        if(len>7){
            holder.lecture.setText(sb.toString().substring(0,6)+"…");
        }else{
            holder.lecture.setText(sb.toString());
        }
        holder.category.setText(entity.getCategoryName());
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
