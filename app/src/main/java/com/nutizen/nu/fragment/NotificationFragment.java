package com.nutizen.nu.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nutizen.nu.R;
import com.nutizen.nu.activity.ContentPlayerActivity;
import com.nutizen.nu.activity.LivePlayerActivity;
import com.nutizen.nu.adapter.NotificationAdatper;
import com.nutizen.nu.bean.response.ContentBean;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.utils.SubscribeNotificationUtile;

import java.util.ArrayList;

public class NotificationFragment extends BaseDialogFragment implements View.OnClickListener, NotificationAdatper.NotificationClick {

    public static final String TAG = "NotificationFragment";

    private RecyclerView mNotificationRv;

    public static NotificationFragment getInstance() {
        return new NotificationFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_notification;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        rootView.findViewById(R.id.back).setOnClickListener(this);
        rootView.findViewById(R.id.settings).setOnClickListener(this);
        mNotificationRv = rootView.findViewById(R.id.notification_list);
    }

    @Override
    protected void initEvent() {
        ArrayList<ContentBean> itemBeans = SubscribeNotificationUtile.getItemBeans();
        mNotificationRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mNotificationRv.setAdapter(new NotificationAdatper(itemBeans, this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                dismiss();
                break;
            case R.id.settings:
                NotificationSettingFragment.getInstance().show(getFragmentManager(), NotificationSettingFragment.TAG);
                break;
        }
    }

    @Override
    public void onNotificationClick(ContentBean itemBean) {
        if (itemBean.getType().equals("movie")) {
            ContentPlayerActivity.startContentPlayActivity(getContext(), itemBean.getMovieBean());
        } else if (itemBean.getType().equals("live")) {
            LivePlayerActivity.startLivePlayActivity(getContext(), itemBean.getLiveBean());
        }
    }
}
