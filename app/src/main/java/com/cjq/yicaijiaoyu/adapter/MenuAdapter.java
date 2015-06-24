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
import com.cjq.yicaijiaoyu.entities.MenuItemEntity;

import java.util.List;

/**
 * Created by android on 2015/6/23.
 */
public class MenuAdapter extends BaseAdapter{

    List<MenuItemEntity> list;
    Context context;

    public MenuAdapter(Context context,List<MenuItemEntity> list) {
        this.list = list;
        this.context =context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView ==null ){
            convertView = LayoutInflater.from(context).inflate(R.layout.menu_item,parent,false);
            ViewHolder holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            holder.textView = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.textView.setText(list.get(position).getTitleText());
        if(position==CommonDataObject.menuChecked){
            holder.imageView.setImageResource(list.get(position).getIcon_selected_id());
            holder.textView.setTextColor(context.getResources().getColor(R.color.pure_white));
        }else{
            holder.imageView.setImageResource(list.get(position).getIcon_id());
            holder.textView.setTextColor(context.getResources().getColor(R.color.menu_text_color));
        }
        return convertView;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
