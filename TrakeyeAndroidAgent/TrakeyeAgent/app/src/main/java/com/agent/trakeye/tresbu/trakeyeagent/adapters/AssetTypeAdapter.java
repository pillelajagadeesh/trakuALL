package com.agent.trakeye.tresbu.trakeyeagent.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.model.AssetType;

import java.util.ArrayList;

/**
 * Created by Tresbu on 21-Oct-16.
 */

public class AssetTypeAdapter extends BaseAdapter {

    Context c;
    ArrayList<AssetType> objects;
    LayoutInflater inflater;

    public AssetTypeAdapter(Context context, ArrayList<AssetType> objects) {
        this.c = context;
        this.objects = objects;


        //this.list= list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spinner_layout, parent, false);
        AssetType cur_obj = objects.get(position);
        TextView label = (TextView) row.findViewById(R.id.tvName);
        label.setText(objects.get(position).getName());
        return row;
    }
}
