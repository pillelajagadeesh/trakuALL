package com.agent.trakeye.tresbu.trakeyeagent.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomAlertAdapter extends BaseAdapter {

    private Context ctx = null;
    private ArrayList<String> listarray = null;
    private LayoutInflater mInflater = null;

    public CustomAlertAdapter(Activity activty, ArrayList<String> list) {
        this.ctx = activty;
        mInflater = activty.getLayoutInflater();
        this.listarray = list;
    }

    public CustomAlertAdapter(Activity activty, String[] list1) {
        this.ctx = activty;
        mInflater = activty.getLayoutInflater();
        this.listarray = new ArrayList<>(Arrays.asList(list1));
    }

    @Override
    public int getCount() {

        return listarray.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.alertlistrow, null);

            holder.titlename = (TextView) convertView.findViewById(R.id.textView_titllename);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String datavalue = listarray.get(position);
        holder.titlename.setText(datavalue);

        return convertView;
    }

    private static class ViewHolder {
        TextView titlename;
    }
}