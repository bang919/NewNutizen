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
import com.nutizen.nu.utils.AnimUtil;
import com.nutizen.nu.utils.GlideUtils;

import java.util.ArrayList;
import java.util.TreeMap;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    private ArrayList<FavouriteRspBean> mDataList;
    private TreeMap<Integer, FavouriteRspBean> mSelectMap;
    private OnFavouriteItemClickListener mOnFavouriteItemClickListener;
    private boolean mEditing;
    private int mFirstVisibleItemPosition = -1, mLasttVisibleItemPosition = -1;//用于记录可见范围，只有可见范围的checkbox才出现动画
    private int mSelectViewWidth;

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

    public void changeEditStatus(boolean edit, int firstVisibleItemPosition, int lastVisibleItemPosition) {
        mEditing = edit;
        mFirstVisibleItemPosition = firstVisibleItemPosition;
        mLasttVisibleItemPosition = lastVisibleItemPosition;
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
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favourite, parent, false);

        if (mSelectViewWidth == 0) {
            View selectView = inflate.findViewById(R.id.cb_select);
            selectView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), 0);
            mSelectViewWidth = selectView.getMeasuredWidth();
        }
        return new FavouriteViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final FavouriteViewHolder holder, final int position) {
        final FavouriteRspBean data = mDataList.get(position);
        holder.mCheckBox.setChecked(mSelectMap.get(position) != null);

        if (position >= mFirstVisibleItemPosition && position <= mLasttVisibleItemPosition) {//如果对用户可见，则启动动画，否则直接设置width
            AnimUtil.setEditFavouriteButton(holder.mCheckBox, mEditing);
        } else {
            holder.mCheckBox.getLayoutParams().width = mEditing ? mSelectViewWidth : 0;
        }

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
                    holder.mCheckBox.setChecked(mSelectMap.get(position) != null);
                    holder.mCheckBox.getLayoutParams().width = mEditing ? mSelectViewWidth : 0;
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
