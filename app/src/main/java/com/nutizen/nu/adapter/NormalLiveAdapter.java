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


public class NormalLiveAdapter extends RecyclerView.Adapter<NormalLiveAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<LiveResponseBean> mLiveResponseBeans;
    private ItemOnLiveClickListener mItemOnLiveClickListener;
    private boolean mChangeClickStyle;//是否让item点击后变色
    private int mCurrentPosition = -1;

    public NormalLiveAdapter(Context context) {
        this(context, true);
    }

    public NormalLiveAdapter(Context context, boolean changeClickStyle) {
        mChangeClickStyle = changeClickStyle;
        mContext = context;
        mLiveResponseBeans = new ArrayList<>();
    }

    public void setItemOnLiveClickListener(ItemOnLiveClickListener itemOnLiveClickListener) {
        this.mItemOnLiveClickListener = itemOnLiveClickListener;
    }

    public void setCurrentPosition(int position) {
        mCurrentPosition = position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_pic_title_2text, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final LiveResponseBean liveResponseBean = mLiveResponseBeans.get(position);
        holder.title.setText(liveResponseBean.getTitle());
        if (mChangeClickStyle) {
            holder.title.setSelected(position == mCurrentPosition);
            holder.title.setTextSize(position == mCurrentPosition ? 22 : 18);
        }
        holder.video.setText(liveResponseBean.getSynopsis());

        GlideUtils.loadImage(holder.iv, -1, liveResponseBean.getThumbnail(), new CenterCrop());

        if (mItemOnLiveClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemOnLiveClickListener.onLiveClick(liveResponseBean);
                    mCurrentPosition = position;
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mLiveResponseBeans.size();
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

    public interface ItemOnLiveClickListener {
        void onLiveClick(LiveResponseBean liveBean);
    }
}
