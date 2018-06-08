package com.nutizen.nu.fragment;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;

import com.nutizen.nu.R;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.common.Constants;
import com.nutizen.nu.utils.SPUtils;
import com.nutizen.nu.utils.SubscribeNotificationUtile;

public class NotificationSettingFragment extends BaseDialogFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String TAG = "NotificationSettingFragment";

    public static NotificationSettingFragment getInstance() {
        return new NotificationSettingFragment();
    }

    private SwitchCompat mAllSwitch;
    private SwitchCompat mImportantSwitch;
    private SwitchCompat mHomebannerSwitch;
    private SwitchCompat mPilihaneditorSwitch;
    private SwitchCompat mKanalSwitch;
    private SwitchCompat mLiveSwitch;
    private boolean initingStatus = false;

    @Override
    protected int getLayout() {
        return R.layout.fragment_notification_setting;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(View rootView) {
        rootView.findViewById(R.id.back).setOnClickListener(this);

        mAllSwitch = rootView.findViewById(R.id.switch_all);
        mImportantSwitch = rootView.findViewById(R.id.switch_important);
        mHomebannerSwitch = rootView.findViewById(R.id.switch_home_banner);
        mPilihaneditorSwitch = rootView.findViewById(R.id.switch_pilihan_editor);
        mKanalSwitch = rootView.findViewById(R.id.switch_kanal);
        mLiveSwitch = rootView.findViewById(R.id.switch_live);

        initSwitchStatus();
    }

    private void initSwitchStatus() {
        initingStatus = true;
        boolean subscribeImportant = (boolean) SPUtils.get(getContext(), Constants.NOTIFICATION_IMPORTANT, false);
        boolean subscribeHomeBanner = (boolean) SPUtils.get(getContext(), Constants.NOTIFICATION_HOME_BANNER, false);
        boolean subscribePilihanEditor = (boolean) SPUtils.get(getContext(), Constants.NOTIFICATION_PILIHAN_EDITOR, false);
        boolean subscribeKanal = (boolean) SPUtils.get(getContext(), Constants.NOTIFICATION_KANAL, false);
        boolean subscribeLive = (boolean) SPUtils.get(getContext(), Constants.NOTIFICATION_LIVE, false);

        mAllSwitch.setChecked(subscribeImportant && subscribeHomeBanner && subscribePilihanEditor && subscribeKanal && subscribeLive);
        mImportantSwitch.setChecked(subscribeImportant);
        mHomebannerSwitch.setChecked(subscribeHomeBanner);
        mPilihaneditorSwitch.setChecked(subscribePilihanEditor);
        mKanalSwitch.setChecked(subscribeKanal);
        mLiveSwitch.setChecked(subscribeLive);

        initingStatus = false;
    }


    @Override
    protected void initEvent() {
        mAllSwitch.setOnCheckedChangeListener(this);
        mImportantSwitch.setOnCheckedChangeListener(this);
        mHomebannerSwitch.setOnCheckedChangeListener(this);
        mPilihaneditorSwitch.setOnCheckedChangeListener(this);
        mKanalSwitch.setOnCheckedChangeListener(this);
        mLiveSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                dismiss();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (initingStatus) {
            return;
        }
        switch (buttonView.getId()) {
            case R.id.switch_all:
                //Do nothing , 会在initSwitchStatus处理了
                if (isChecked) {
                    SubscribeNotificationUtile.subscribeAll(getContext());
                } else {
                    SubscribeNotificationUtile.unsubscribeAll(getContext());
                }
                break;
            case R.id.switch_important:
                if (isChecked) {
                    SubscribeNotificationUtile.subscribeImportant();
                } else {
                    SubscribeNotificationUtile.unsubscribeImportant();
                }
                break;
            case R.id.switch_home_banner:
                if (isChecked) {
                    SubscribeNotificationUtile.subscribeHomeBanner();
                } else {
                    SubscribeNotificationUtile.unsubscribeHomeBanner();
                }
                break;
            case R.id.switch_pilihan_editor:
                if (isChecked) {
                    SubscribeNotificationUtile.subscribePilihanEditor();
                } else {
                    SubscribeNotificationUtile.unsubscribePilihanEditor();
                }
                break;
            case R.id.switch_kanal:
                if (isChecked) {
                    SubscribeNotificationUtile.subscribeAllContributorsVod(getContext());
                } else {
                    SubscribeNotificationUtile.unsubscribeAllContributorsVod(getContext());
                }
                break;
            case R.id.switch_live:
                if (isChecked) {
                    SubscribeNotificationUtile.subscribeAllContributorsLive(getContext());
                } else {
                    SubscribeNotificationUtile.unsubscribeAllContributorsLive(getContext());
                }
                break;
        }
        initSwitchStatus();
    }
}
