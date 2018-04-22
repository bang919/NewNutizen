package com.nutizen.nu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.KanalRspBean;
import com.nutizen.nu.utils.GlideUtils;

import java.util.ArrayList;


public class KanalListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<KanalRspBean.SearchBean> mSearchBeanList;
    private ItemOnClickListener itemOnClickListener;
    private int limit = -1;
    private boolean isAdvancedAdapter = false;

    public KanalListAdapter(Context context) {
        this(context, -1);
    }

    public KanalListAdapter(Context context, int limit) {
        this(context, limit, false);
    }

    public KanalListAdapter(Context context, int limit, boolean isAdvancedAdapter) {
        mContext = context;
        this.limit = limit;
        this.isAdvancedAdapter = isAdvancedAdapter;
        mSearchBeanList = new ArrayList<>();
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case R.layout.item_kanal_head:
                viewHolder = new HeadHolder(LayoutInflater.from(mContext).inflate(viewType, parent, false));
                break;
            case R.layout.item_fragment_kanal:
                viewHolder = new ItemViewHolder(LayoutInflater.from(mContext).inflate(viewType, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder _holder, final int position) {
        if (_holder instanceof HeadHolder) {
            HeadHolder holder = (HeadHolder) _holder;
            holder.mHeadTv.setText(mSearchBeanList.get(position).getUsername());
        } else if (_holder instanceof ItemViewHolder) {
            ItemViewHolder holder = (ItemViewHolder) _holder;

            String title = mSearchBeanList.get(position).getUsername();
            String thumbnail = mSearchBeanList.get(position).getThumbnail();
            int videoCount = mSearchBeanList.get(position).getMovie_count();
            int favorCount = mSearchBeanList.get(position).getFavor_count();
            holder.title.setText(title);
            holder.video.setText(mContext.getString(R.string.videos, videoCount));
            holder.follow.setText(mContext.getString(R.string.follow, favorCount));

            if (!TextUtils.isEmpty(thumbnail))
                GlideUtils.loadImage(holder.iv, -1, thumbnail, new CenterCrop());

            if (itemOnClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemOnClickListener.onItemClickListener(mSearchBeanList.get(position));
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return limit == -1 ? mSearchBeanList.size() : Math.min(mSearchBeanList.size(), limit);
    }

    @Override
    public int getItemViewType(int position) {
        return isAdvancedAdapter ?
                mSearchBeanList.get(position).isHead() ? R.layout.item_kanal_head : R.layout.item_fragment_kanal
                : R.layout.item_fragment_kanal;
    }

    public void setData(ArrayList<KanalRspBean.SearchBean> list) {
        mSearchBeanList = list != null ? list : new ArrayList();
        notifyDataSetChanged();
    }

    class HeadHolder extends RecyclerView.ViewHolder {

        private final TextView mHeadTv;

        public HeadHolder(View itemView) {
            super(itemView);
            mHeadTv = itemView.findViewById(R.id.tv_head);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView follow;
        private final TextView video;
        private final ImageView iv;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            title = (TextView) itemView.findViewById(R.id.title);
            video = (TextView) itemView.findViewById(R.id.rb_video);
            follow = (TextView) itemView.findViewById(R.id.follow);
        }
    }

    public interface ItemOnClickListener {
        void onItemClickListener(KanalRspBean.SearchBean kanalBean);
    }
}
