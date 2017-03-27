package com.agent.trakeye.tresbu.trakeyeagent.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.interfaces.OnCaseItemClickListener;
import com.agent.trakeye.tresbu.trakeyeagent.model.Case;
import com.agent.trakeye.tresbu.trakeyeagent.utils.util;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Tresbu on 6/23/2016.
 */
public class CasesAdapter extends RecyclerView.Adapter<CasesAdapter.MyViewHolder> {

    ArrayList<Case> result;
    Context context;
    ArrayList<Case> mFilteredlist = new ArrayList<Case>();
    OnCaseItemClickListener listener;


    public CasesAdapter(Context ctx, ArrayList<Case> result, OnCaseItemClickListener listener) {
        this.context = ctx;
        this.result = result;
        this.listener = listener;
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
        public ImageView ivArrow;
        public View mView;


        public MyViewHolder(View view) {
            super(view);
            this.mView= view;
            tvId = (TextView) view.findViewById(R.id.tvId);
            tvDesc = (TextView) view.findViewById(R.id.tvDescription);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvStatus = (TextView) view.findViewById(R.id.tvStatus);
            ivArrow=(ImageView)view.findViewById(R.id.ivArrow);
        }

        public void bind(final Case item, final OnCaseItemClickListener listener) {
            tvId.setText(String.valueOf(item.getId()));
//            tvDesc.setText(item.getDescription());
//            tvDate.setText( util.printDate(item.getCreateDate()));
            if(item.getCaseType() != null){
                tvDesc.setText(item.getCaseType().getName());
            }else{
                tvDesc.setText("");
            }
            try {
                tvDate.setText(util.getDate(Long.parseLong(item.getCreateDate()), "dd/MM/yyyy"));
            } catch (NumberFormatException e) {
//                e.printStackTrace();
                tvDate.setText("");
            }
            if(item.getEscalated()){
                tvStatus.setText("Yes");
            }else{
                tvStatus.setText("No");
            }

            tvId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCaseItemClick(item);
                }
            });

            ivArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCaseItemClick(item);
                }
            });

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCaseItemClick(item);
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
            for (Case notification : result) {
                if (notification.getDescription().toLowerCase(Locale.getDefault()).contains(filterText)) {
                    mFilteredlist.add(notification);
                }
            }
        } else {
            mFilteredlist.clear();
            mFilteredlist.addAll(result);
        }

        notifyDataSetChanged();
    }


    public void setAllData(ArrayList<Case> allContacts) {
        result = allContacts;
        mFilteredlist.clear();
        mFilteredlist.addAll(allContacts);

    }


}
