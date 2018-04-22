package com.nutizen.nu.adapter;

import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nutizen.nu.R;

public class KanalIndexAdpater extends RecyclerView.Adapter<KanalIndexAdpater.IndexHolder> {

    private ArrayMap<String, Integer> mData;
    private OnIndexFocusListener mOnIndexFocusListener;
    private int mCurrentPosition = 0;
    private String mCurrentC = "";

    public void setData(ArrayMap<String, Integer> list) {
        mData = list;
        notifyDataSetChanged();
    }

    public void setOnIndexClickListener(OnIndexFocusListener listener) {
        mOnIndexFocusListener = listener;
    }

    public void jumpToKanalPosition(String c) {
        if (mCurrentC.equals(c)) {
            return;
        }
        mCurrentC = c;
        for (int i = 0; i < mData.size(); i++) {
            if (mData.keyAt(i).equals(c)) {
                mCurrentPosition = i;
                break;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public IndexHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new IndexHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index_kanal, parent, false));
    }

    @Override
    public void onBindViewHolder(IndexHolder holder, final int position) {
        holder.mIndexTv.setText(mData.keyAt(position));
        holder.itemView.setSelected(position == mCurrentPosition);

        if (mOnIndexFocusListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int p = 0;
                    for (int i = 0; i < position; i++) {
                        p = p + 1 + mData.valueAt(i);
                    }
                    mOnIndexFocusListener.onIndexClick(p);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class IndexHolder extends RecyclerView.ViewHolder {

        TextView mIndexTv;

        public IndexHolder(View itemView) {
            super(itemView);
            mIndexTv = itemView.findViewById(R.id.tv_index);
        }
    }

    public interface OnIndexFocusListener {
        void onIndexClick(int jumpPosition);
    }
}
