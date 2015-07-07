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
import com.cjq.yicaijiaoyu.entities.TeamDataEntity;
import com.cjq.yicaijiaoyu.utils.ImageUtil;

import java.text.DateFormat;
import java.util.List;

/**
 * Created by CJQ on 2015/7/6.
 */
public class TeamListAdapter extends BaseAdapter {

    List<TeamDataEntity> teamDataEntities;
    Context context;

    public TeamListAdapter(List<TeamDataEntity> teamDataEntities, Context context) {
        this.teamDataEntities = teamDataEntities;
        this.context = context;
    }

    public List<TeamDataEntity> getTeamDataEntities() {
        return teamDataEntities;
    }

    public void setTeamDataEntities(List<TeamDataEntity> teamDataEntities) {
        this.teamDataEntities = teamDataEntities;
    }

    @Override
    public int getCount() {
        return teamDataEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return teamDataEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TeamDataEntity entity = teamDataEntities.get(position);
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.team_index_item,null);
            ViewHolder holder =new ViewHolder();
            holder.category = (TextView) convertView.findViewById(R.id.category);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.captain = (TextView) convertView.findViewById(R.id.captain);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.info = (TextView) convertView.findViewById(R.id.info);
            holder.menu_portrait = (ImageView) convertView.findViewById(R.id.menu_portrait);
            holder.position = (TextView) convertView.findViewById(R.id.position);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.category.setText(entity.getCategory());
        holder.title.setText(entity.getName());
        String ca;
        if(entity.getCaptain().length()>4 || entity.getCaptain().getBytes().length>6){
            ca = entity.getCaptain().substring(0,2)+"…";
        }else{
            ca =entity.getCaptain();
        }
        holder.captain.setText(ca);
        holder.time.setText(CommonDataObject.FORMAT.format(entity.getDate()));
        holder.info.setText(entity.getInfo());
        holder.position.setText(String.valueOf(position+1));
        ImageUtil.LoadImage(context,entity.getPortrait(),holder.menu_portrait);

        return convertView;
    }

    private class ViewHolder{
        TextView category;
        ImageView menu_portrait;
        TextView title;
        TextView captain;
        TextView time;
        TextView info;
        TextView position;
    }
}
