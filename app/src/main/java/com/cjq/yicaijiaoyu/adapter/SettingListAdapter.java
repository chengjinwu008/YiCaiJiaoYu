package com.cjq.yicaijiaoyu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cjq.yicaijiaoyu.R;
import com.cjq.yicaijiaoyu.entities.SettingEntity;

import java.util.List;

/**
 * Created by CJQ on 2015/6/26.
 */
public class SettingListAdapter extends BaseAdapter {

    List<SettingEntity> settingEntities;
    Context context;

    public SettingListAdapter(List<SettingEntity> settingEntities, Context context) {
        this.settingEntities = settingEntities;
        this.context = context;
    }

    @Override
    public int getCount() {
        return settingEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return settingEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.setting_item,parent,false);
            ViewHolder holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.declare = (TextView) convertView.findViewById(R.id.declare);
            holder.image = convertView.findViewById(R.id.image);

            convertView.setTag(holder);
        }

        SettingEntity entity = settingEntities.get(position);

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.title.setText(entity.getTitle());
        holder.declare.setText(entity.getDeclare());
        if(entity.isCanJump()){
            holder.image.setVisibility(View.VISIBLE);
        }else{
            holder.image.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    private class ViewHolder{
        TextView title;
        TextView declare;
        View image;
    }
}
