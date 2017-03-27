package com.agent.trakeye.tresbu.trakeyeagent.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.interfaces.OnAssetItemClickListener;
import com.agent.trakeye.tresbu.trakeyeagent.model.Asset;
import com.agent.trakeye.tresbu.trakeyeagent.utils.util;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Tresbu on 6/23/2016.
 */
public class AssetAdapter extends RecyclerView.Adapter<AssetAdapter.MyViewHolder> {

    ArrayList<Asset> result;
    Context context;
    ArrayList<Asset> mFilteredlist = new ArrayList<>();
    OnAssetItemClickListener listener;


    public AssetAdapter(Context ctx, ArrayList<Asset> result, OnAssetItemClickListener listener) {
        this.context = ctx;
        this.result = result;
        this.listener= listener;
        this.mFilteredlist.addAll(result);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_all_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.bind(mFilteredlist.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return mFilteredlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvId, tvDesc, tvDate, tvStatus;
        public View mView;

        public MyViewHolder(View view) {
            super(view);
            this.mView= view;
            tvId = (TextView) view.findViewById(R.id.tvId);
            tvDesc = (TextView) view.findViewById(R.id.tvDescription);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvStatus = (TextView) view.findViewById(R.id.tvStatus);
        }

        public void bind(final Asset item, final OnAssetItemClickListener listener) {
            tvId.setText(String.valueOf(item.getId()));
            tvDesc.setText(item.getName());
            tvDate.setText(util.getDate(Long.parseLong(item.getCreateDate()), "dd/MM/yyyy"));
            tvStatus.setTextSize(10.0f);
            if(item.getAssetType() != null) {
                if(item.getAssetType().getLayout() != null) {
                    tvStatus.setText(item.getAssetType().getLayout());
                }
            }
            tvId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAssetItemClick(item);
                }
            });

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAssetItemClick(item);
                }
            });
        }
    }

    public void clearData() {
        int size = this.result.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.result.remove(0);
            }
            this.notifyItemRangeRemoved(0, size);
        }
    }

    // Filter Class
    public void filter(String filterText) {
        if (null != filterText && filterText.length() > 0) {
            filterText = filterText.toLowerCase(Locale.getDefault());
            mFilteredlist.clear();
            for (Asset notification : result) {
                if (notification.getName().toLowerCase(Locale.getDefault()).contains(filterText)) {
                    mFilteredlist.add(notification);
                }
            }
        } else {
            mFilteredlist.clear();
            mFilteredlist.addAll(result);
        }

        notifyDataSetChanged();
    }


    public void setAllData(ArrayList<Asset> allContacts) {
        result = allContacts;
        mFilteredlist.clear();
        mFilteredlist.addAll(allContacts);
        //notifyDataSetChanged();
    }


}
