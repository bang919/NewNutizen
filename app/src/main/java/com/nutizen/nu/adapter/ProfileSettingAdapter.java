package com.nutizen.nu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.ContentPlaybackBean;

import java.util.ArrayList;

public class ProfileSettingAdapter extends RecyclerView.Adapter<ProfileSettingAdapter.ProfileSettingHolder> {

    private ArrayList<ContentPlaybackBean.VideoProfileBean> mVideoProfile;
    private OnProfileSelectListener mOnProfileSelectListener;
    private int mItemSelect;

    public void setVideoProfile(ArrayList<ContentPlaybackBean.VideoProfileBean> profiles) {
        mVideoProfile = profiles;
        notifyDataSetChanged();
    }

    public void setOnProfileSelectListener(OnProfileSelectListener onProfileSelectListener) {
        mOnProfileSelectListener = onProfileSelectListener;
    }

    @Override
    public ProfileSettingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProfileSettingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile_setting, parent, false));
    }

    @Override
    public void onBindViewHolder(ProfileSettingHolder holder, final int position) {
        holder.mTextView.setText(mVideoProfile.get(position).getProfile_name());
        if (mItemSelect == position) {
            holder.mTextView.setSelected(true);
        } else {
            holder.mTextView.setSelected(false);
        }
        if (mOnProfileSelectListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == mItemSelect) {
                        return;
                    }
                    mOnProfileSelectListener.onProfileSelect(mVideoProfile.get(position));
                    mItemSelect = position;
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mVideoProfile == null ? 0 : mVideoProfile.size();
    }

    class ProfileSettingHolder extends RecyclerView.ViewHolder {

        TextView mTextView;

        public ProfileSettingHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_profile);
        }
    }

    public interface OnProfileSelectListener {
        void onProfileSelect(ContentPlaybackBean.VideoProfileBean videoProfileBean);
    }
}
