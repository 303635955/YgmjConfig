package com.yunguo.ygmjconfig.Adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yunguo.ygmjconfig.R;
import com.yunguo.ygmjconfig.Entity.Controller;

public class AddressAdapter extends BaseAdapter{
	private List<Map<String,String>> list = new ArrayList<Map<String,String>>();
    private Context context;
    private  ViewHolder viewHolder = null;

    public AddressAdapter(List<Map<String,String>> list,Context context){
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
       
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.addressadapteritem, null);
            viewHolder =new ViewHolder();

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        
        Map<String,String> map = list.get(position);
        
        viewHolder.textView1 = (TextView) convertView.findViewById(R.id.textaddress);
        viewHolder.textView1.setText(map.get("Name"));
        return convertView;
    }

    class ViewHolder{
        TextView textView1;
        RadioButton rdBtn;
    }
}
