package com.nutizen.nu.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.utils.AnimUtil;
import com.nutizen.nu.utils.GlideUtils;

import java.util.ArrayList;
import java.util.TreeMap;

public abstract class DownloadListAdapter extends RecyclerView.Adapter<DownloadListAdapter.DownloadViewHolder> {

    private RecyclerView mRecyclerView;
    private View mEmptyView;
    protected ArrayList<ContentResponseBean.SearchBean> mDatas;
    protected TreeMap<Integer, ContentResponseBean.SearchBean> mSelectMap;
    protected boolean mEditing;
    protected int mFirstVisibleItemPosition = -1, mLasttVisibleItemPosition = -1;//用于记录可见范围，只有可见范围的checkbox才出现动画
    protected int mSelectViewWidth;

    public DownloadListAdapter() {
        mSelectMap = new TreeMap<>();
    }

    public void changeEditStatus(boolean edit) {
        mEditing = edit;
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mFirstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        mLasttVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        if (mEditing) {
            mSelectMap.clear();
        }
        notifyDataSetChanged();
    }

    public ArrayList<ContentResponseBean.SearchBean> getSelectDownload() {
        return new ArrayList<ContentResponseBean.SearchBean>(mSelectMap.values());
    }

    public View initView(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.pager_datas_detail, null);
        mRecyclerView = inflate.findViewById(R.id.pager_item_recycler);
        mEmptyView = inflate.findViewById(R.id.pager_item_nodata_layout);
        initRecyclerView();
        return inflate;
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(this);
    }

    public void setData(ArrayList<ContentResponseBean.SearchBean> datas) {
        mDatas = datas;
        notifyDataSetChanged();
        mEmptyView.setVisibility(datas != null && datas.size() > 0 ? View.GONE : View.VISIBLE);
    }


    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_downlaod, parent, false);
        if (mSelectViewWidth == 0) {
            View selectView = inflate.findViewById(R.id.cb_select);
            selectView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), 0);
            mSelectViewWidth = selectView.getMeasuredWidth();
        }
        return new DownloadViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull final DownloadViewHolder holder, final int position) {
        final ContentResponseBean.SearchBean data = mDatas.get(position);

        //set UIs
        holder.mCheckBox.setChecked(mSelectMap.get(position) != null);

        if (position >= mFirstVisibleItemPosition && position <= mLasttVisibleItemPosition) {//如果对用户可见，则启动动画，否则直接设置width
            AnimUtil.setEditDownloadButton(holder.mCheckBox, holder.mGuideline, mEditing);
        } else {
            holder.mCheckBox.getLayoutParams().width = mEditing ? mSelectViewWidth : 1;
            ((ConstraintLayout.LayoutParams) holder.mGuideline.getLayoutParams()).guideEnd = mEditing ? 1 : (int) (mSelectViewWidth * 1.3);
        }

        GlideUtils.loadImage(holder.mPicIv, -1, data.getThumbnail(), new CenterCrop());
        holder.mTitle.setText(data.getTitle());

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
                    holder.mCheckBox.getLayoutParams().width = mEditing ? mSelectViewWidth : 1;
                    ((ConstraintLayout.LayoutParams) holder.mGuideline.getLayoutParams()).guideEnd = mEditing ? 1 : (int) (mSelectViewWidth * 1.3);
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(holder.itemView.getContext(), holder, data);
                }
            });
        }
    }

    public abstract void onItemClick(Context context, DownloadViewHolder holder, ContentResponseBean.SearchBean bean);

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    class DownloadViewHolder extends RecyclerView.ViewHolder {

        CheckBox mCheckBox;
        ImageView mPicIv;
        TextView mTitle;
        ProgressBar mProgressBar;
        TextView mStateTv;
        ImageView mPlayBtn;
        Guideline mGuideline;

        public DownloadViewHolder(View itemView) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.cb_select);
            mPicIv = itemView.findViewById(R.id.iv);
            mTitle = itemView.findViewById(R.id.title);
            mProgressBar = itemView.findViewById(R.id.progress);
            mStateTv = itemView.findViewById(R.id.state);
            mPlayBtn = itemView.findViewById(R.id.play_btn);
            mGuideline = itemView.findViewById(R.id.guideline);
        }
    }
}
