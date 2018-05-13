package com.nutizen.nu.adapter;

import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nutizen.nu.R;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.system.email.Email;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.whatsapp.WhatsApp;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareViewHolder> {

    private ArrayMap<String, Integer> platformMaps;
    private OnShareAdapterClickListener mOnShareAdapterClickListener;

    public ShareAdapter(OnShareAdapterClickListener listener) {
        mOnShareAdapterClickListener = listener;
        platformMaps = new ArrayMap<>();
        platformMaps.put(Facebook.NAME, R.mipmap.share_facebook);
        platformMaps.put(Twitter.NAME, R.mipmap.share_twitter);
        platformMaps.put(WhatsApp.NAME, R.mipmap.share_whatsapp);
        platformMaps.put(ShortMessage.NAME, R.mipmap.share_shortmessage);
        platformMaps.put(Email.NAME, R.mipmap.share_email);
    }

    @Override
    public ShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShareViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share, parent, false));
    }

    @Override
    public void onBindViewHolder(ShareViewHolder holder, final int position) {
        holder.mIcon.setImageResource(platformMaps.valueAt(position));
        holder.mTitle.setText(platformMaps.keyAt(position));

        if (mOnShareAdapterClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnShareAdapterClickListener.onShareAdapterClick(platformMaps.keyAt(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return platformMaps.size();
    }

    class ShareViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIcon;
        private TextView mTitle;

        public ShareViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.iv_item_share);
            mTitle = itemView.findViewById(R.id.tv_item_share);
        }
    }

    public interface OnShareAdapterClickListener {
        void onShareAdapterClick(String plateName);
    }
}
