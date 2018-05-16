package com.nutizen.nu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.FavouriteRspBean;
import com.nutizen.nu.utils.GlideUtils;

import java.util.ArrayList;
import java.util.TreeMap;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    private ArrayList<FavouriteRspBean> mDataList;
    private TreeMap<Integer, FavouriteRspBean> mSelectMap;
    private OnFavouriteItemClickListener mOnFavouriteItemClickListener;
    private boolean mEditing;

    public FavouriteAdapter() {
        mSelectMap = new TreeMap<>();
    }

    public void setOnFavouriteItemClickListener(OnFavouriteItemClickListener listener) {
        mOnFavouriteItemClickListener = listener;
    }

    public void setData(ArrayList<FavouriteRspBean> list) {
        mDataList = list;
        notifyDataSetChanged();
    }

    public void changeEditStatus(boolean edit) {
        mEditing = edit;
        if (mEditing) {
            mSelectMap.clear();
        }
        notifyDataSetChanged();
    }

    public ArrayList<FavouriteRspBean> getSelectFavourite() {
        return new ArrayList<FavouriteRspBean>(mSelectMap.values());
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavouriteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favourite, parent, false));
    }

    @Override
    public void onBindViewHolder(FavouriteViewHolder holder, final int position) {
        final FavouriteRspBean data = mDataList.get(position);
        holder.mCheckBox.setChecked(mSelectMap.get(position) != null);
        holder.mCheckBox.setVisibility(mEditing ? View.VISIBLE : View.GONE);
        GlideUtils.loadImage(holder.mImageView, -1, data.getContent_thumbnail(), new CenterCrop());
        holder.mTitle.setText(data.getContent_title());
        if (mEditing) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectMap.get(position) != null) {
                        mSelectMap.remove(position);
                    } else {
                        mSelectMap.put(position, data);
                    }
                    notifyDataSetChanged();
                }
            });
        } else if (mOnFavouriteItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnFavouriteItemClickListener.onFavouriteItemClick(data);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    class FavouriteViewHolder extends RecyclerView.ViewHolder {

        CheckBox mCheckBox;
        ImageView mImageView;
        TextView mTitle;

        public FavouriteViewHolder(View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.cb_select);
            mImageView = itemView.findViewById(R.id.iv);
            mTitle = itemView.findViewById(R.id.title);
        }
    }

    public interface OnFavouriteItemClickListener {
        void onFavouriteItemClick(FavouriteRspBean favouriteRspBean);
    }


}
