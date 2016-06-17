package com.yunguo.ygmjconfig.Adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunguo.ygmjconfig.R;

public class RtspListAdapter extends BaseAdapter{
	private List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();
    private Context context;
    private  ViewHolder viewHolder = null;

    public RtspListAdapter(List<Map<Object,Object>> list,Context context){
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
    public View getView(final int position, View convertView, ViewGroup parent) {
       
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.rtsplist_adapter, null);
            viewHolder =new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        viewHolder.rtsptxt = (TextView) convertView.findViewById(R.id.rtsptxt);
        viewHolder.rtsptype = (TextView) convertView.findViewById(R.id.rtsptype);
        
        HashMap<Object,Object> map = (HashMap<Object, Object>) list.get(position);

        viewHolder.rtsptxt.setText(map.get("RtspStr")+"");
        viewHolder.rtsptype.setText(map.get("RtspType")+"");

        return convertView;
    }

    class ViewHolder{
        TextView rtsptxt,rtsptype;
    }
}
