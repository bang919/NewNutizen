package com.nutizen.nu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.listener.ContentItemClickListener;
import com.nutizen.nu.utils.GlideUtils;

import java.util.ArrayList;

public class MainBannerAdapter extends RecyclerView.Adapter<MainBannerAdapter.BannerHolder> {

    public static final int BANNER_MAX = 1000;
    private ArrayList<ContentResponseBean.SearchBean> datas;
    private ContentItemClickListener mBannerClickListener;

    public MainBannerAdapter() {
        this.datas = new ArrayList<>();
    }

    public void setDatas(ArrayList<ContentResponseBean.SearchBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void setOnBannerClickListener(ContentItemClickListener listener) {
        mBannerClickListener = listener;
    }

    @Override
    public MainBannerAdapter.BannerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BannerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false));
    }


    @Override
    public void onBindViewHolder(MainBannerAdapter.BannerHolder holder, int position) {
        final int _position = position % datas.size();
        GlideUtils.loadImage(holder.mImageView, -1, datas.get(_position).getThumbnail(), new CenterCrop());
        if (mBannerClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBannerClickListener.onContentItemClick(datas.get(_position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas == null || datas.size() == 0 ? 0 : BANNER_MAX;
    }

    class BannerHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        public BannerHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_banner);
        }
    }
}
