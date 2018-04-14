package com.nutizen.nu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.utils.GlideUtils;

import java.util.ArrayList;

public class MainLiveAdapter extends RecyclerView.Adapter<MainLiveAdapter.LiveHolder> {

    private ArrayList<LiveResponseBean> datas;
    private MainLiveAdapter.OnAdapterClickListener mAdapterClickListener;

    public MainLiveAdapter() {
        this.datas = new ArrayList<>();
    }

    public void setDatas(ArrayList<LiveResponseBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void setOnAdapterClickListener(MainLiveAdapter.OnAdapterClickListener listener) {
        mAdapterClickListener = listener;
    }

    @Override
    public LiveHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LiveHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_list, parent, false));
    }


    @Override
    public void onBindViewHolder(LiveHolder holder, int position) {
        final int _position = position % datas.size();
        GlideUtils.loadImage(holder.mImageView, -1, datas.get(_position).getThumbnail(), new CenterCrop());
        holder.mTextView.setText(datas.get(_position).getTitle());
        if (mAdapterClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterClickListener.onItemClick(datas.get(_position), _position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class LiveHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView mTextView;

        public LiveHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_home_iv);
            mTextView = itemView.findViewById(R.id.item__home_tv);
        }
    }

    public interface OnAdapterClickListener {
        void onItemClick(LiveResponseBean liveBean, int position);
    }

}
