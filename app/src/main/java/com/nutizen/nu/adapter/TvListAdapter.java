package com.nutizen.nu.adapter;

import android.content.Context;
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


public class TvListAdapter extends RecyclerView.Adapter<TvListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<LiveResponseBean> mLiveResponseBeans;
    private ItemOnClickListener itemOnClickListener;

    public TvListAdapter(Context context) {
        mContext = context;
        mLiveResponseBeans = new ArrayList<>();
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_fragment_kanal, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final LiveResponseBean liveResponseBean = mLiveResponseBeans.get(position);
        holder.title.setText(liveResponseBean.getTitle());
        holder.video.setText(liveResponseBean.getSynopsis());

        GlideUtils.loadImage(holder.iv, -1, liveResponseBean.getThumbnail(), new CenterCrop());

        if (itemOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnClickListener.onItemClickListener(liveResponseBean);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(mLiveResponseBeans.size(), 9);
    }


    public void setData(ArrayList<LiveResponseBean> list) {
        mLiveResponseBeans = list != null ? list : new ArrayList();
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView follow;
        private final TextView video;
        private final ImageView iv;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            title = (TextView) itemView.findViewById(R.id.title);
            video = (TextView) itemView.findViewById(R.id.rb_video);
            follow = (TextView) itemView.findViewById(R.id.follow);
        }
    }

    public interface ItemOnClickListener {
        void onItemClickListener(LiveResponseBean liveBean);
    }
}
