package com.nutizen.nu.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nutizen.nu.R;
import com.nutizen.nu.common.BasePresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HelpFaqFragment extends TextTitleFragment {

    public static final String TAG = "HelpFaqFragment";
    private List<String> files = Collections.emptyList();
    private RecyclerView mPolicyRv;

    public static HelpFaqFragment getInstance() {
        return new HelpFaqFragment();
    }

    @Override
    protected String setTitle() {
        return getString(R.string.help_amp_faq);
    }

    @Override
    protected int getBodyLayout() {
        return R.layout.fragment_help_faq;
    }

    @Override
    protected void initBodyView(View rootView) {
        mPolicyRv = rootView.findViewById(R.id.policy_ry);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void initEvent() {
        try {
            files = getTextAssets();
        } catch (IOException e) {
            e.printStackTrace();
        }


        mPolicyRv.setLayoutManager(new LinearLayoutManager(getContext()));

        mPolicyRv.setAdapter(new RecyclerView.Adapter<MyHolder>() {
            @Override
            public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.policy_text, parent, false);
                return new MyHolder(view);
            }

            @Override
            public void onBindViewHolder(MyHolder holder, final int position) {
                TextView textView = holder.mTextView;
                textView.setText(files.get(position).replace(".txt", ""));
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HelpFaqDetailFragment.getInstance(files.get(position)).show(getFragmentManager(), HelpFaqDetailFragment.TAG);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return files.size();
            }
        });
    }

    private List<String> getTextAssets() throws IOException {
        String[] assetList = getContext().getAssets().list("");
        List<String> files = new ArrayList<>();
        for (String asset : assetList) {
            if (asset.toLowerCase().endsWith(".txt") && asset.contains("privacy policy")) {//Only show "Read privacy policy"
                files.add(asset);
            }
        }
        return files;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public MyHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_policy);
        }
    }
}
