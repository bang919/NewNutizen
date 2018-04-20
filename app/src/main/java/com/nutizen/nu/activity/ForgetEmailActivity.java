package com.nutizen.nu.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nutizen.nu.R;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.presenter.ForgetPasswordPresenter;
import com.nutizen.nu.utils.PhoneUtils;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.NormalView;

public class ForgetEmailActivity extends BaseActivity<ForgetPasswordPresenter> implements View.OnClickListener, NormalView {

    private EditText emailEt;
    private ImageView submit;
    private String email;
    private ImageView back;
    private TextView hint;
    private View mLine;

    @Override
    protected int getLayout() {
        return R.layout.activity_forget_email;
    }

    @Override
    public int getBarColor() {
        return R.color.colorBlack;
    }

    @Override
    protected ForgetPasswordPresenter initPresenter() {
        return new ForgetPasswordPresenter(this, this);
    }

    @Override
    protected void initView() {
        emailEt = findViewById(R.id.reset_email);
        submit = findViewById(R.id.submit);
        back = findViewById(R.id.back);
        mLine = findViewById(R.id.line);
        hint = findViewById(R.id.hint);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                email = emailEt.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    hint.setVisibility(View.VISIBLE);
                    mLine.setBackgroundColor(Color.parseColor("#D1FF2828"));
                    hint.setText(getString(R.string.empty_email));
                    return;
                }
                if (!PhoneUtils.isEmail(email)) {
                    hint.setVisibility(View.VISIBLE);
                    mLine.setBackgroundColor(Color.parseColor("#D1FF2828"));
                    hint.setText(getString(R.string.err_email));
                    return;
                }
                mPresenter.searchEmailToRestPassword(email);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.reset_email:
                hint.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess() {
        startActivity(new Intent(this,ForgetPasswordActivity.class));
        finish();
    }

    @Override
    public void onFailure(String error) {
        ToastUtils.showShort(error);
        hint.setVisibility(View.VISIBLE);
        mLine.setBackgroundColor(Color.parseColor("#D1FF2828"));
        hint.setText(getString(R.string.err_email));
    }
}
