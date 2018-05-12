package com.nutizen.nu.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.LiveResponseBean;

public class LivePlayerAdapter extends BasePlayerAdapter<LivePlayerAdapter.LiveHeadHolder> {

    private LiveResponseBean mLiveInfo;
    private View mFollowBtn;

    public LivePlayerAdapter(LiveResponseBean liveInfo) {
        super();
        mLiveInfo = liveInfo;
    }

    public View getFollowBtn(){
        return mFollowBtn;
    }

    @Override
    protected String getTitle() {
        return mLiveInfo.getTitle();
    }

    @Override
    protected int setHeadLayoutSource() {
        return R.layout.item_liveplayer_head;
    }

    @Override
    protected LiveHeadHolder getHeadViewHolder(View inflate) {
        return new LiveHeadHolder(inflate);
    }

    @Override
    protected void onBindHeadViewHolder(LiveHeadHolder holder, int position) {
        mFollowBtn = holder.mFollowBtn;
        holder.mAuthor.setText(mLiveInfo.getSynopsis().split(";")[0]);
    }

    public class LiveHeadHolder extends BasePlayerAdapter.BaseHeadHolder {

        private ImageView mFollowBtn;
        private TextView mAuthor;

        public LiveHeadHolder(View itemView) {
            super(itemView);
            mFollowBtn = itemView.findViewById(R.id.follow_bt);
            mAuthor = itemView.findViewById(R.id.author);
        }
    }
}
