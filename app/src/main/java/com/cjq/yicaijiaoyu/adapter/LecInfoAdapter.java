package com.cjq.yicaijiaoyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.dao.Course;
import com.cjq.yicaijiaoyu.dao.Lecture;
import com.cjq.yicaijiaoyu.utils.ImageUtil;

import java.util.List;

/**
 * Created by CJQ on 2015/7/20.
 */
public class LecInfoAdapter extends BaseAdapter {
    List<Lecture> lectures;
    Context context;

    public LecInfoAdapter(Course course, Context context) {
        this.lectures = course.getLectureList();
        this.context = context;
    }

    @Override
    public int getCount() {
        return lectures.size();
    }

    @Override
    public Object getItem(int position) {
        return lectures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Lecture lecture = lectures.get(position);
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.lec_info_item,null);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.lec_portrait);
        ImageUtil.LoadImage(context, lecture.getImage(), imageView);
        TextView lec_name = (TextView) convertView.findViewById(R.id.lec_name);
        lec_name.setText(lecture.getName());
        TextView lec_info = (TextView) convertView.findViewById(R.id.lec_tro);
        lec_info.setText(lecture.getInfo());
        return convertView;
    }
}
