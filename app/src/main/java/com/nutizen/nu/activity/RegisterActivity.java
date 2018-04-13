package com.nutizen.nu.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nutizen.nu.R;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.presenter.RegisterPresenter;
import com.nutizen.nu.utils.PhoneUtils;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.NormalView;


public class RegisterActivity extends BaseActivity<RegisterPresenter> implements View.OnClickListener, TextWatcher, NormalView {

    private static final String TAG = "RegisterActivity";
    private ImageView back;
    private EditText emailEt;
    private EditText passwordEt;
    private EditText confirmEt;
    private ImageView submit;
    private String password;
    private String confirm;
    private String email;
    private EditText usenameEt;
    private TextView hint;
    private String username;
    private View mEmailLine;
    private View mUsenameLine;
    private View mPasswordLine;
    private View mConfirmLine;
    private LinearLayout mRoot;
    private Handler mHandler = new Handler();
    private int editStart;
    private int editEnd;
    private int charMaxNum = 50;
    private CharSequence temp;
    private String mContent;

    @Override
    protected int getLayout() {
        return R.layout.activity_regrister;
    }

    @Override
    protected int getBarColor() {
        return R.color.colorBlack;
    }

    @Override
    protected RegisterPresenter initPresenter() {
        return new RegisterPresenter(this, this);
    }

    @Override
    protected void initView() {
        back = (ImageView) findViewById(R.id.ic_back);
        emailEt = (EditText) findViewById(R.id.email);
        usenameEt = (EditText) findViewById(R.id.usename);
        passwordEt = (EditText) findViewById(R.id.password);
        confirmEt = (EditText) findViewById(R.id.confirm);
        submit = (ImageView) findViewById(R.id.submit);
        hint = (TextView) findViewById(R.id.hint);
        mEmailLine = findViewById(R.id.email_line);
        mUsenameLine = findViewById(R.id.usename_line);
        mPasswordLine = findViewById(R.id.password_line);
        mConfirmLine = findViewById(R.id.confirm_line);
        mRoot = (LinearLayout) findViewById(R.id.root);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        back.setOnClickListener(this);
        emailEt.setOnClickListener(this);
        usenameEt.setOnClickListener(this);
        passwordEt.setOnClickListener(this);
        confirmEt.setOnClickListener(this);
        submit.setOnClickListener(this);
        emailEt.addTextChangedListener(this);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
            if (emailEt.isFocused()) {
                usenameEt.requestFocus();
                return true;
            } else if (usenameEt.isFocused()) {
                passwordEt.requestFocus();
                return true;
            } else if (passwordEt.isFocused()) {
                confirmEt.requestFocus();
                return true;
            } else if (confirmEt.isFocused()) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(RegisterActivity.this.getCurrentFocus().getWindowToken(), 0);
                }
                return true;
            }
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ic_back:
                finish();
                break;
            case R.id.submit:
                if (IsAllRight()) {
                    mPresenter.registerAccount(username, password, email);
                }
                break;
            case R.id.email:
            case R.id.usename:
            case R.id.password:
            case R.id.confirm:
                hint.setVisibility(View.INVISIBLE);
                mEmailLine.setBackgroundColor(Color.parseColor("#979797"));
                mUsenameLine.setBackgroundColor(Color.parseColor("#979797"));
                mPasswordLine.setBackgroundColor(Color.parseColor("#979797"));
                mConfirmLine.setBackgroundColor(Color.parseColor("#979797"));
                break;
            default:
                break;
        }
    }

    private boolean IsAllRight() {

        username = usenameEt.getText().toString().trim();
        email = emailEt.getText().toString().trim();
        password = passwordEt.getText().toString().trim();
        confirm = confirmEt.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            mEmailLine.setBackgroundColor(Color.parseColor("#D1FF2828"));
            hint.setVisibility(View.VISIBLE);
            hint.setText(this.getString(R.string.empty_email));
        } else if (!PhoneUtils.isEmail(email)) {
            mEmailLine.setBackgroundColor(Color.parseColor("#D1FF2828"));
            hint.setVisibility(View.VISIBLE);
            hint.setText(this.getString(R.string.err_email));
        } else if (TextUtils.isEmpty(username)) {
            mUsenameLine.setBackgroundColor(Color.parseColor("#D1FF2828"));
            hint.setVisibility(View.VISIBLE);
            hint.setText(this.getString(R.string.empty_username));
        } else if (TextUtils.isEmpty(password)) {
            mPasswordLine.setBackgroundColor(Color.parseColor("#D1FF2828"));
            hint.setVisibility(View.VISIBLE);
            hint.setText(this.getString(R.string.empty_password));
        } else if (!PhoneUtils.isRightPwd(password)) {
            mPasswordLine.setBackgroundColor(Color.parseColor("#D1FF2828"));
            hint.setVisibility(View.VISIBLE);
            hint.setText(this.getString(R.string.input_alph));
        } else if (TextUtils.isEmpty(confirm)) {
            mConfirmLine.setBackgroundColor(Color.parseColor("#D1FF2828"));
            hint.setVisibility(View.VISIBLE);
            hint.setText(this.getString(R.string.empty_confirm));
        } else if (!password.equals(confirm)) {
            mConfirmLine.setBackgroundColor(Color.parseColor("#D1FF2828"));
            hint.setText(this.getString(R.string.password_match));
            hint.setVisibility(View.VISIBLE);
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        temp = s;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        editStart = emailEt.getSelectionStart();
        editEnd = emailEt.getSelectionEnd();
        mContent = s.toString().trim();
        if (temp.length() > charMaxNum) {
            s.delete(editStart - 1, editEnd);
            emailEt.setText(s);
            emailEt.setSelection(s.length());
        }
    }

    @Override
    public void onSuccess() {
        Toast t = new Toast(this);
        t.setGravity(Gravity.CENTER, 0, 0);
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.mipmap.forgetpassword_success);
        t.setView(iv);
        t.setDuration(Toast.LENGTH_SHORT);
        t.show();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                logout();
            }
        }, 2500);
    }

    @Override
    public void onFailure(String error) {
        ToastUtils.showShort(error);
        hint.setText(RegisterActivity.this.getString(R.string.loadfail));
        hint.setVisibility(View.VISIBLE);
    }
}
