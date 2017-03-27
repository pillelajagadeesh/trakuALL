package com.agent.trakeye.tresbu.trakeyeagent.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agent.trakeye.tresbu.trakeyeagent.R;
import com.agent.trakeye.tresbu.trakeyeagent.interfaces.OnNotificationItemClickListener;
import com.agent.trakeye.tresbu.trakeyeagent.model.Notification;
import com.agent.trakeye.tresbu.trakeyeagent.model.NotificationStatus;
import com.agent.trakeye.tresbu.trakeyeagent.utils.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Tresbu on 6/23/2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    ArrayList<Notification> result;
    ArrayList<Notification> mFilteredlist = new ArrayList<Notification>();
    Context context;
    OnNotificationItemClickListener listener;


    public NotificationAdapter(Context ctx, ArrayList<Notification> result, OnNotificationItemClickListener listener) {
        this.context = ctx;
        this.result = result;
        this.mFilteredlist.addAll(result);
        this.listener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list, parent, false);

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
        //        public TextView tvId, tvDesc, tvDate, tvStatus;
        public ImageView ivNotificationType;
        public TextView tvSubVal, tvDescVal, tvDateVal;
        public View mView;
        public RelativeLayout rlNotificationLayout;

        public MyViewHolder(View view) {
            super(view);
            this.mView = view;
            rlNotificationLayout = (RelativeLayout) view.findViewById(R.id.rlNotificationLayout);
            ivNotificationType = (ImageView) view.findViewById(R.id.ivNotificationType);
            tvSubVal = (TextView) view.findViewById(R.id.tvSubject);
            tvDescVal = (TextView) view.findViewById(R.id.tvDesc);
            tvDateVal = (TextView) view.findViewById(R.id.tvDate);
        }

        public void file_download(String uRl) {
            try {
                File direct = new File(Environment.getExternalStorageDirectory()
                        + "");

                if (!direct.exists()) {
                    direct.mkdirs();
                }

                DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

                Uri downloadUri = Uri.parse(uRl);
                DownloadManager.Request request = new DownloadManager.Request(
                        downloadUri);

                request.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI
                                | DownloadManager.Request.NETWORK_MOBILE)
                        .setAllowedOverRoaming(false).setTitle("Trakeye")
                        .setDescription("Trakeye application is downloading...")
                        .setDestinationInExternalPublicDir("", "Trakeye.apk");

                mgr.enqueue(request);
            } catch (Exception e) {
            }
        }


        public void bind(final Notification item, final OnNotificationItemClickListener listener) {
            if (item.getNotificationType() != null) {
                if (item.getNotificationType().equals("A")) {
                    ivNotificationType.setImageResource(R.drawable.a_notification);
                } else if (item.getNotificationType().equals("S")) {
                    ivNotificationType.setImageResource(R.drawable.s_notification);
                } else {
                    ivNotificationType.setImageResource(R.drawable.c_notification);
                }
            } else {
                ivNotificationType.setImageResource(R.drawable.n_notification);
            }

            tvSubVal.setText(item.getSubject());
            tvDescVal.setText(item.getDescription());
            if (item.getStatus().equals(NotificationStatus.SENT)) {
                tvDescVal.setTextColor(Color.BLACK);
                tvDescVal.setTypeface(null, Typeface.BOLD);
            } else {
                tvDescVal.setTextColor(Color.GRAY);
            }
            tvDateVal.setText(util.getDate(Long.parseLong(item.getCreatedDate()), "dd/MM/yyyy"));

            rlNotificationLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onNotificationItemClick(item);
                }
            });


//            tvDateVal.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onNotificationItemClick(item);
//                }
//            });



//            tvId.setText(String.valueOf(item.getId()));
//            tvDesc.setText(item.getSubject());




            if (item.getDownloadLink() != null) {
                tvDescVal.setTextColor(Color.BLUE);
            }



////            tvDate.setText(util.printDate(item.getCreatedDate()));
//            tvDate.setText(getDate(Long.parseLong(item.getCreatedDate()), "dd/MM/yyyy"));
//            tvStatus.setText(item.getStatus().name());
//
//            tvId.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onNotificationItemClick(item);
//                }
//            });
//




//            tvDescVal.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    listener.onNotificationItemClick(item);
//                    if (item.getNotificationType().equals("A")) {
//                        if (item.getDownloadLink() != null) {
////                        Toast.makeText(context, item.getDownloadLink() + "", Toast.LENGTH_SHORT).show();
//                            file_download(item.getDownloadLink());
//                        }
//                    } else {
//                        listener.onNotificationItemClick(item);
//                    }
//                }
//            });
//            mView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onNotificationItemClick(item);
//                }
//            });





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
            for (Notification notification : result) {
                if (notification.getSubject().toLowerCase(Locale.getDefault()).contains(filterText)) {
                    mFilteredlist.add(notification);
                }
            }
        } else {
            mFilteredlist.clear();
            mFilteredlist.addAll(result);
        }

        notifyDataSetChanged();
    }


    public void setAllData(ArrayList<Notification> allContacts) {
        result = allContacts;
        mFilteredlist.clear();
        mFilteredlist.addAll(allContacts);
        notifyDataSetChanged();
    }


}