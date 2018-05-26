package com.nutizen.nu.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nutizen.nu.R;
import com.nutizen.nu.common.BasePresenter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HelpFaqDetailFragment extends TextTitleFragment {

    public static final String TAG = "HelpFaqDetailFragment";
    public static final String FILE_PATH = "file path";
    private TextView mContentTv;
    public String mFilePath;


    public static HelpFaqDetailFragment getInstance(String filePath) {
        HelpFaqDetailFragment helpFaqDetailFragment = new HelpFaqDetailFragment();
        Bundle args = new Bundle();
        args.putString(FILE_PATH, filePath);
        helpFaqDetailFragment.setArguments(args);
        return helpFaqDetailFragment;
    }

    @Override
    protected String setTitle() {
        mFilePath = getArguments().getString(FILE_PATH);
        return mFilePath;
    }

    @Override
    protected int getBodyLayout() {
        return R.layout.fragment_help_faq_detail;
    }

    @Override
    protected void initBodyView(View rootView) {
        mContentTv = rootView.findViewById(R.id.detail_text);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEvent() {
        setText(mContentTv, mFilePath);
    }

    public void setText(TextView mTv, String file) {
        try {
            //Return an AssetManager instance for your application's package
            // test
            InputStream is = getContext().getAssets().open(file);
            String text = readTextFromSDcard(is);
            mTv.setText(text);
        } catch (IOException e) {
            // Should never happen!
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readTextFromSDcard(InputStream is) throws Exception {
        BufferedInputStream reader = new BufferedInputStream(is);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(reader));
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
