package com.nutizen.nu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.ContentBean;
import com.nutizen.nu.utils.GlideUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by bigbang on 2018/3/22.
 */

public class NotificationAdatper extends RecyclerView.Adapter<NotificationAdatper.NotificaitonHolder> {


    private ArrayList<ContentBean> mItemBeans = new ArrayList<>();
    private NotificationClick mNotificationClick;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public NotificationAdatper(ArrayList<ContentBean> itemBeans, NotificationClick notificationClick) {
        if (itemBeans != null) {
            mItemBeans = itemBeans;
        }
        mNotificationClick = notificationClick;
    }

    public void setData(ArrayList<ContentBean> itemBeans) {
        mItemBeans = itemBeans;
        notifyDataSetChanged();
    }

    @Override
    public NotificaitonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificaitonHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(NotificaitonHolder holder, int position) {
        final ContentBean itemBean = mItemBeans.get(position);
        holder.mTitle.setText(itemBean.getTitle());
        holder.mTime.setText(simpleDateFormat.format(new Date(itemBean.getCreateTimeL())));
        GlideUtils.loadImage(holder.mImageView, -1, itemBean.getThumbnail(), new CenterCrop());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNotificationClick != null) {
                    mNotificationClick.onNotificationClick(itemBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItemBeans.size();
    }

    class NotificaitonHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mTime;
        public ImageView mImageView;


        public NotificaitonHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.notificaiton_title);
            mTime = itemView.findViewById(R.id.notification_time);
            mImageView = itemView.findViewById(R.id.notificaiton_pic);
        }
    }

    public interface NotificationClick {
        void onNotificationClick(ContentBean itemBean);
    }
}
