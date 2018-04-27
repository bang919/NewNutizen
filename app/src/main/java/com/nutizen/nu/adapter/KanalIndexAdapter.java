package com.nutizen.nu.adapter;

import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nutizen.nu.R;

public class KanalIndexAdapter extends RecyclerView.Adapter<KanalIndexAdapter.IndexHolder> {

    private ArrayMap<String, Integer> mData;
    private OnIndexFocusListener mOnIndexFocusListener;
    private int mCurrentPosition = 0;

    public void setData(ArrayMap<String, Integer> list) {
        mData = list;
        notifyDataSetChanged();
    }

    public void setOnIndexClickListener(OnIndexFocusListener listener) {
        mOnIndexFocusListener = listener;
    }

    public void jumpToKanalPosition(int position) {
        if (mCurrentPosition == position) {
            return;
        }
        int positionBefore = mCurrentPosition;
        mCurrentPosition = position;
        notifyItemChanged(mCurrentPosition);
        notifyItemChanged(positionBefore);
    }

    @Override
    public IndexHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IndexHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index_kanal, parent, false));
    }

    @Override
    public void onBindViewHolder(IndexHolder holder, final int position) {
        holder.mIndexTv.setText(mData.keyAt(position));
        holder.mSelectView.setSelected(position == mCurrentPosition);

        if (mOnIndexFocusListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnIndexFocusListener.onIndexClick(mData.valueAt(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class IndexHolder extends RecyclerView.ViewHolder {

        View mSelectView;
        TextView mIndexTv;

        public IndexHolder(View itemView) {
            super(itemView);
            mSelectView = itemView.findViewById(R.id.view_selector);
            mIndexTv = itemView.findViewById(R.id.tv_index);
        }
    }

    public interface OnIndexFocusListener {
        void onIndexClick(int jumpPosition);
    }
}
