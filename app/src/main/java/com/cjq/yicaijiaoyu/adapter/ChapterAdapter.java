package com.cjq.yicaijiaoyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.entities.ChapterEntity;
import com.cjq.yicaijiaoyu.entities.CourseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJQ on 2015/6/26.
 */
public class ChapterAdapter extends BaseAdapter{

    List<ChapterEntity> chapters;
    Context context;

    public ChapterAdapter(List<ChapterEntity> chapters, Context context) {
        this.chapters = chapters;
        this.context = context;
    }

    @Override
    public int getCount() {
        return chapters.size();
    }

    @Override
    public Object getItem(int position) {
        return chapters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.chapter_item,parent,false);
            ViewHolder holder  = new ViewHolder();

            holder.chapter= (ListView) convertView.findViewById(R.id.chapter);
            holder.title = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(holder);
        }
        ChapterEntity entity = chapters.get(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.title.setText(entity.getName());
        if(holder.chapter.getAdapter()==null){
            if(entity.getVideos()!=null)
            holder.chapter.setAdapter(new LChapterAdapter(entity.getVideos(),context));
        }else{
            LChapterAdapter lChapterAdapter = (LChapterAdapter) holder.chapter.getAdapter();
            if(entity.getVideos()!=null){
                lChapterAdapter.setCourses(entity.getVideos());
            }else{
                lChapterAdapter.setCourses(new ArrayList<CourseEntity>());
            }
            lChapterAdapter.notifyDataSetChanged();
        }
        return convertView;
    }

    private class ViewHolder{
        TextView title;
        ListView chapter;
    }
}
