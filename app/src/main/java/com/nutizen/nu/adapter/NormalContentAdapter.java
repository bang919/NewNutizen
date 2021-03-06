package com.nutizen.nu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.listener.ContentItemClickListener;
import com.nutizen.nu.utils.GlideUtils;

import java.util.ArrayList;

public class NormalContentAdapter extends RecyclerView.Adapter<NormalContentAdapter.ContentHolder> {

    private ArrayList<ContentResponseBean.SearchBean> datas;
    private ContentItemClickListener mAdapterClickListener;

    public NormalContentAdapter() {
        this.datas = new ArrayList<>();
    }

    public void setDatas(ArrayList<ContentResponseBean.SearchBean> datas) {
        this.datas = datas != null ? datas : new ArrayList<ContentResponseBean.SearchBean>();
        notifyDataSetChanged();
    }

    public void addDatas(ArrayList<ContentResponseBean.SearchBean> datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public void setOnContentItemClickListener(ContentItemClickListener listener) {
        mAdapterClickListener = listener;
    }

    @Override
    public ContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pic_title_2text, parent, false));
    }


    @Override
    public void onBindViewHolder(ContentHolder holder, int position) {
        final int _position = position % datas.size();
        ContentResponseBean.SearchBean searchBean = datas.get(_position);
        GlideUtils.loadImage(holder.mImageView, -1, searchBean.getThumbnail(), new CenterCrop());
        holder.mTextView.setText(searchBean.getTitle());
        holder.mAuthorView.setText(searchBean.getWritter());
        if (mAdapterClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterClickListener.onContentItemClick(datas.get(_position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class ContentHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;
        TextView mTextView;
        TextView mAuthorView;

        public ContentHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv);
            mTextView = itemView.findViewById(R.id.title);
            mAuthorView = itemView.findViewById(R.id.rb_video);
        }
    }
}
