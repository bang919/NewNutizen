package com.nutizen.nu.model;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LiveResponseBean;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.common.Constants;
import com.nutizen.nu.common.MyApplication;
import com.nutizen.nu.presenter.LoginPresenter;
import com.nutizen.nu.utils.ToastUtils;

public class ShareModel {

    private final String TAG = "ShareModel";

    private OnShortDynamicCompleteListener mShortDynamicLinkOnCompleteListener;

    public ShareModel(OnShortDynamicCompleteListener shortDynamicLinkOnCompleteListener) {
        mShortDynamicLinkOnCompleteListener = shortDynamicLinkOnCompleteListener;
    }

    public void shareContent(ContentResponseBean.SearchBean contentBean, String platformName) {

        int viewerId = 0;
        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        if (accountMessage != null) {
            viewerId = accountMessage.getViewer_id();
        }

        Uri link = Uri.parse("https://nutizen.com/play/"
                .concat("movie").concat("/")
                .concat(String.valueOf(contentBean.getId()))
                .concat("?utm_source=" + "app." + platformName + "&utm_medium=" + viewerId)
        );
        buildDynamicLink(contentBean.getTitle(), contentBean.getThumbnail(), contentBean.getDescription(), viewerId, link, platformName);
    }

    public void shareLive(LiveResponseBean liveBean, String platformName) {
        int viewerId = 0;
        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        if (accountMessage != null) {
            viewerId = accountMessage.getViewer_id();
        }
        Uri link = Uri.parse("https://nutizen.com/play/"
                .concat(liveBean.getType()).concat("/")
                .concat(String.valueOf(liveBean.getId())).concat("/")
                .concat(liveBean.getTitle()).concat("/")
                .concat(String.valueOf(viewerId)).concat("/")
                .concat(liveBean.getAuthorName())
                .concat("?utm_source=" + "app." + platformName + "&utm_medium=" + viewerId)
        );
        String description = MyApplication.getMyApplicationContext().getString(R.string.live_create_by) + liveBean.getAuthorName();
        buildDynamicLink(liveBean.getTitle(), liveBean.getThumbnail(), description, viewerId, link, platformName);
    }

    private void buildDynamicLink(String title, String imageUrl, String description, int viewerId, Uri link, final String platformName) {

        DynamicLink.SocialMetaTagParameters.Builder socialBuilder = new DynamicLink.SocialMetaTagParameters.Builder()
                .setTitle(title);

        if (!TextUtils.isEmpty(imageUrl)) {
            socialBuilder.setImageUrl(Uri.parse(imageUrl));
        }
        socialBuilder.setDescription(!TextUtils.isEmpty(description) ? description : Constants.SHARE_DEFAULT_DESCRIPTION);
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(link)
                .setDynamicLinkDomain("e54yw.app.goo.gl")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder()
//                        .setFallbackUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.nutizen.nu&referrer=utm_source%3D" + sharePlatform + "%26utm_medium%3D" + viewerId))
                                .setFallbackUrl(link)
                                .build()
                )
                .setSocialMetaTagParameters(socialBuilder
                        .build()
                )
                .setGoogleAnalyticsParameters(new DynamicLink.GoogleAnalyticsParameters.Builder()
                        .setSource("app.".concat(platformName))
                        .setMedium(String.valueOf(viewerId))
                        .build()
                )
                .buildShortDynamicLink()
                .addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        mShortDynamicLinkOnCompleteListener.onComplete(platformName, task);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure() called with: e = [" + e + "]");
                ToastUtils.showShort(e.getMessage());
            }
        });
    }

    public interface OnShortDynamicCompleteListener {
        void onComplete(String platformName, @NonNull Task<ShortDynamicLink> task);
    }
}
