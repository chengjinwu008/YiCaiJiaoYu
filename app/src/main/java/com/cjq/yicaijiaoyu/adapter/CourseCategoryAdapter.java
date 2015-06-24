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
import com.cjq.yicaijiaoyu.entities.CategoryEntity;
import com.cjq.yicaijiaoyu.entities.CourseCategory;
import com.cjq.yicaijiaoyu.utils.ImageUtil;

import java.util.List;

/**
 * Created by CJQ on 2015/6/24.
 */
public class CourseCategoryAdapter extends BaseAdapter {
    List<CategoryEntity> list;
    Context context;

    public CourseCategoryAdapter(List<CategoryEntity> list, Context context) {
        this.list = list;
        this.context = context;
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
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.category_list_item,parent,false);
            ViewHolder holder = new ViewHolder();
            holder.imageView= (ImageView) convertView.findViewById(R.id.icon);
            holder.textView = (TextView) convertView.findViewById(R.id.text);
            holder.view =convertView.findViewById(R.id.choose_flag);
            convertView.setTag(holder);
        }

        CategoryEntity categoryEntity = list.get(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();
        ImageUtil.LoadImage(context,categoryEntity.getImageId(),holder.imageView);
        holder.textView.setText(categoryEntity.getName());
        if(CommonDataObject.categoryChecked==position)
            holder.view.setVisibility(View.VISIBLE);
        else
            holder.view.setVisibility(View.INVISIBLE);
        return convertView;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView textView;
        View view;
    }
}
