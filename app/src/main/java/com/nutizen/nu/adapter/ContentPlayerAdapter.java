package com.nutizen.nu.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.utils.AnimUtil;

import java.text.DecimalFormat;

public class ContentPlayerAdapter extends BasePlayerAdapter<ContentPlayerAdapter.ContentHeadHolder> implements View.OnClickListener {

    private ContentResponseBean.SearchBean mVideoInfo;
    private TextView mDescription;
    private String mWritter;
    private String mViewerNumber = "0";

    public ContentPlayerAdapter(ContentResponseBean.SearchBean videoInfo) {
        super();
        mVideoInfo = videoInfo;
    }

    public void setWritter(String writter) {
        mWritter = writter;
        notifyItemChanged(0);
    }

    public void setViewerNumber(int number) {
        DecimalFormat df = new DecimalFormat("###,###");
        mViewerNumber = df.format(number);
        notifyItemChanged(0);
    }

    @Override
    protected String getTitle() {
        return mVideoInfo.getTitle();
    }

    @Override
    protected int setHeadLayoutSource() {
        return R.layout.item_contentplayer_head;
    }

    @Override
    protected ContentHeadHolder getHeadViewHolder(View inflate) {
        return new ContentHeadHolder(inflate);
    }

    @Override
    protected void onBindHeadViewHolder(final ContentHeadHolder holder, int position) {
        mDescription = holder.mDescription;
        if (!mViewerNumber.equalsIgnoreCase("0")) {
            holder.mViews.setVisibility(View.VISIBLE);
            holder.mViews.setText(holder.itemView.getContext().getString(R.string.viewers, mViewerNumber));
        }
        String detail = TextUtils.isEmpty(mVideoInfo.getDescription()) ?
                holder.mDescription.getContext().getString(R.string.there_is_no_description) : mVideoInfo.getDescription();
        holder.mDescription.setText(detail.replace("\n", ""));
        holder.mShowDescription.setOnClickListener(this);
        if (mWritter != null) {
            holder.mWritter.setText(mWritter);
        }
    }

    /**
     * Holder  两个
     */

    public class ContentHeadHolder extends BasePlayerAdapter.BaseHeadHolder {

        private final ImageView mShowDescription;
        private final TextView mWritter;
        private final TextView mViews;
        private final TextView mDescription;


        private ContentHeadHolder(View itemView) {
            super(itemView);
            mShowDescription = itemView.findViewById(R.id.iv_show_detail);

            mWritter = itemView.findViewById(R.id.tv_writter);
            mViews = itemView.findViewById(R.id.tv_views);
            mDescription = itemView.findViewById(R.id.tv_detail);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_show_detail:
                AnimUtil.switchDownArrow(v, mDescription);
                break;
        }
    }
}
