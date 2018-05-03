package com.nutizen.nu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.nutizen.nu.R;


/**
 * Created by Administrator on 2017/11/21.
 */

public class NormalDialog extends Dialog {//姑且不考虑继承Dialog这种创建Dialog的方法，没办法，历史遗留问题。Dialog的创建方法官方已经建议使用DialogFragment进行创建了。


    private TextView mTvDesc;
    private TextView mTvYes;
    private TextView mTvNo;

    private String mTextDesc;
    private String mTextYes;
    private String mTextNo;
    private View.OnClickListener mYesClickListener;
    private View.OnClickListener mNoClickListener;

    public NormalDialog(Context context, String buttonYesTitle, String buttonNoTitle, String description,
                        View.OnClickListener clickListener, View.OnClickListener noClickListener) {
        super(context, R.style.FullScreenDialog);
        mTextDesc = description;
        mTextYes = buttonYesTitle;
        mTextNo = buttonNoTitle;
        mYesClickListener = clickListener;
        mNoClickListener = noClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setBackgroundDrawable(new ColorDrawable());
//        getWindow().setBackgroundDrawableResource(R.mipmap.icon_bg);

        setContentView(R.layout.dialog_normal);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //初始化界面控件
        initView();
        initData();
    }


    private void initData() {
        mTvDesc.setText(mTextDesc);
        if (!TextUtils.isEmpty(mTextYes)) {
            mTvYes.setText(mTextYes);
        } else {
            mTvYes.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mTextNo)) {
            mTvNo.setText(mTextNo);
        } else {
            mTvNo.setVisibility(View.GONE);
        }
        mTvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mYesClickListener!=null){
                    mYesClickListener.onClick(view);
                }
                dismiss();
            }
        });
        mTvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mNoClickListener!=null){
                    mNoClickListener.onClick(view);
                }
                dismiss();
            }
        });
    }

    private void initView() {
        mTvDesc = findViewById(R.id.live_content);
        mTvYes = findViewById(R.id.tv_yes);
        mTvNo = findViewById(R.id.tv_no);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
//        return true;
//    }
}
