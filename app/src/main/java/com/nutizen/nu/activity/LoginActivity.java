package com.nutizen.nu.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.common.Constants;
import com.nutizen.nu.common.MyApplication;
import com.nutizen.nu.dialog.NormalDialog;
import com.nutizen.nu.presenter.LoginPresenter;
import com.nutizen.nu.utils.SPUtils;
import com.nutizen.nu.utils.SubscribeNotificationUtile;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.LoginView;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.ShareSDK;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginView, View.OnClickListener {

    private EditText mEmailEt;
    private EditText mPasswordEt;
    private ProgressDialog progressDialog;

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public int getBarColor() {
        return Constants.NULL_COLOR;
    }

    @Override
    protected LoginPresenter initPresenter() {
        return new LoginPresenter(this, this);
    }

    @Override
    protected void initView() {
        mEmailEt = findViewById(R.id.et_email);
        mPasswordEt = findViewById(R.id.et_password);
    }

    @Override
    protected void initData() {
        progressDialog = new ProgressDialog(this);
        Drawable drawable = getResources().getDrawable(R.drawable.bg_progressdialog_white);
        progressDialog.setIndeterminateDrawable(drawable);
        progressDialog.setMessage("Please wait, logging in...");
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small_Inverse);

        String username = (String) SPUtils.get(MyApplication.getMyApplicationContext(), Constants.USERNAME, "");
        if (!TextUtils.isEmpty(username)) {
            mEmailEt.setText(username);
            mPasswordEt.requestFocus();
        }
        showNotificationDialog();
    }

    private void showNotificationDialog() {
        boolean firstTime = (boolean) SPUtils.get(this, Constants.FIRST_TIME_LAUNCH, true);
        if (!firstTime) {
            return;
        }
        SPUtils.put(this, Constants.FIRST_TIME_LAUNCH, false);
        new NormalDialog(this, getString(R.string.yes), getString(R.string.no), getString(R.string.receive_notification),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SubscribeNotificationUtile.subscribeAll(LoginActivity.this);
                    }
                }, null).show();
    }

    @Override
    protected void initEvent() {
        findViewById(R.id.bt_sign_up).setOnClickListener(this);
        findViewById(R.id.bt_facebook).setOnClickListener(this);
        findViewById(R.id.bt_guest).setOnClickListener(this);
        findViewById(R.id.bt_forgetpassword).setOnClickListener(this);
        findViewById(R.id.bt_register_account).setOnClickListener(this);
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mPresenter.cancelAllRequest();
            }
        });
    }


    @Override
    public void onClick(View v) {
        mPresenter.cancelAllRequest();
        switch (v.getId()) {
            case R.id.bt_sign_up:
                progressDialog.show();
                mPresenter.login(mEmailEt.getText().toString(), mPasswordEt.getText().toString());
                break;
            case R.id.bt_facebook:
                progressDialog.show();
                progressDialog.setCancelable(false);
                mPresenter.loginByFacebook(ShareSDK.getPlatform(Facebook.NAME));
                break;
            case R.id.bt_guest:
                jumpToActivity(MainActivity.class);
                finish();
                break;
            case R.id.bt_forgetpassword:
                jumpToActivity(ForgetEmailActivity.class);
                break;
            case R.id.bt_register_account:
                jumpToActivity(RegisterActivity.class);
                break;
        }
    }

    @Override
    public void loginSuccess(LoginResponseBean loginResponseBean) {
        progressDialog.dismiss();
        jumpToActivity(MainActivity.class);
        finish();
    }

    @Override
    public void loginFailure(String errorMsg) {
        progressDialog.dismiss();
        ToastUtils.showShort(errorMsg);
    }

    @Override
    public void facebookSdkCallback(boolean onComplete) {
        if (!onComplete) {
            progressDialog.dismiss();
        }
        progressDialog.setCancelable(true);
    }
}
