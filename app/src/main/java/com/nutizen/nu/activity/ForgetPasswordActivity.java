package com.nutizen.nu.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nutizen.nu.R;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.presenter.ForgetPasswordPresenter;
import com.nutizen.nu.utils.PhoneUtils;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.NormalView;

public class ForgetPasswordActivity extends BaseActivity<ForgetPasswordPresenter> implements View.OnClickListener, NormalView {

    private EditText passwordEt;
    private EditText confirmEt;
    private ImageView submit;
    private String password;
    private String confirm;
    private ImageView back;
    private EditText codeEt;
    private String code;
    private TextView hint;
    private Handler mHandler = new Handler();
    private View mColdLine;
    private View mPasswordLine;
    private View mConfirmLine;

    @Override
    protected int getLayout() {
        return R.layout.activity_forget_password;
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
        hint = findViewById(R.id.hint);
        back = findViewById(R.id.back);
        passwordEt = findViewById(R.id.password);
        confirmEt = findViewById(R.id.confirm);
        codeEt = findViewById(R.id.code);
        submit = findViewById(R.id.submit);
        mColdLine = findViewById(R.id.code_line);
        mPasswordLine = findViewById(R.id.password_line);
        mConfirmLine = findViewById(R.id.confirm_line);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
        passwordEt.setOnClickListener(this);
        confirmEt.setOnClickListener(this);
        codeEt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mColdLine.setBackgroundColor(Color.parseColor("#979797"));
        mConfirmLine.setBackgroundColor(Color.parseColor("#979797"));
        mPasswordLine.setBackgroundColor(Color.parseColor("#979797"));
        hint.setVisibility(View.INVISIBLE);
        switch (v.getId()) {
            case R.id.submit:
                password = passwordEt.getText().toString().trim();
                confirm = confirmEt.getText().toString().trim();
                code = codeEt.getText().toString().trim();

                if (TextUtils.isEmpty(code)) {
                    mColdLine.setBackgroundColor(Color.parseColor("#D1FF2828"));
                    hint.setText(getString(R.string.empty_code));
                    hint.setVisibility(View.VISIBLE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPasswordLine.setBackgroundColor(Color.parseColor("#D1FF2828"));
                    hint.setText(getString(R.string.empty_password));
                    hint.setVisibility(View.VISIBLE);
                    return;
                }

                if (!PhoneUtils.isRightPwd(password)) {
                    mPasswordLine.setBackgroundColor(Color.parseColor("#D1FF2828"));
                    hint.setText(getString(R.string.password_length));
                    hint.setVisibility(View.VISIBLE);
                    return;
                }

                if (TextUtils.isEmpty(confirm)) {
                    mConfirmLine.setBackgroundColor(Color.parseColor("#D1FF2828"));
                    hint.setVisibility(View.VISIBLE);
                    hint.setText(getString(R.string.confirm));
                    return;
                }

                if (!password.equals(confirm)) {
                    mConfirmLine.setBackgroundColor(Color.parseColor("#D1FF2828"));
                    hint.setVisibility(View.VISIBLE);
                    hint.setText(getString(R.string.different));
                    return;
                }
                mPresenter.resetPassword(password, code);
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
            if (codeEt.isFocused()) {
                passwordEt.requestFocus();
                return true;
            } else if (passwordEt.isFocused()) {
                confirmEt.requestFocus();
                return true;
            } else if (confirmEt.isFocused()) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(ForgetPasswordActivity.this.getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    @Override
    public void onSuccess() {
        Toast t = new Toast(this);
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.mipmap.forgetpassword_success);
        t.setView(iv);
        t.setDuration(Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                finish();
            }
        }, 2000);
    }

    @Override
    public void onFailure(String error) {
        hint.setVisibility(View.VISIBLE);
        hint.setText(getString(R.string.loadfail));
        ToastUtils.showShort(error);
    }
}
