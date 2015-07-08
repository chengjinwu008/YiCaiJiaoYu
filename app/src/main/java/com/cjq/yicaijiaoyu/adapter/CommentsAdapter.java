package com.cjq.yicaijiaoyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.entities.CommentsEntity;
import com.cjq.yicaijiaoyu.utils.ImageUtil;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by CJQ on 2015/6/26.
 */
public class CommentsAdapter extends BaseAdapter {
    List<CommentsEntity> commentsEntities;
    Context context;

    public List<CommentsEntity> getCommentsEntities() {
        return commentsEntities;
    }

    public void setCommentsEntities(List<CommentsEntity> commentsEntities) {
        this.commentsEntities = commentsEntities;
    }

    public CommentsAdapter(List<CommentsEntity> commentsEntities, Context context) {
        this.commentsEntities = commentsEntities;
        this.context = context;
    }

    @Override
    public int getCount() {
        return commentsEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return commentsEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false);
            ViewHolder holder = new ViewHolder();
            holder.comments_content = (TextView) convertView.findViewById(R.id.comments_content);
            holder.menu_portrait = (ImageView) convertView.findViewById(R.id.menu_portrait);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        CommentsEntity entity = commentsEntities.get(position);

        holder.comments_content.setText(entity.getContent());
        holder.name.setText(entity.getUser().getName());

        //生成时间字符串
        Calendar now = Calendar.getInstance(Locale.CHINA);

        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTimeInMillis(entity.getTime());
        String timeString;
        if(now.getTime().getTime()-entity.getTime()<10*60*1000){
            timeString = context.getString(R.string.just_now);
        }else if(now.getTime().getTime()-entity.getTime()<60*60*1000){
            timeString=now.get(Calendar.MINUTE)-calendar.get(Calendar.MINUTE)+context.getString(R.string.minute_ago);
        }else{
            timeString = calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DATE);
        }
        holder.time.setText(timeString);
        ImageUtil.LoadImage(context,entity.getUser().getProtrait_url(),holder.menu_portrait);
        return convertView;
    }

    private class ViewHolder{
        ImageView menu_portrait;
        TextView name;
        TextView time;
        TextView comments_content;
    }
}
