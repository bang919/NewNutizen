package com.nutizen.nu.fragment;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.nutizen.nu.R;
import com.nutizen.nu.activity.MainActivity;
import com.nutizen.nu.adapter.KanalIndexAdapter;
import com.nutizen.nu.adapter.KanalListAdapter;
import com.nutizen.nu.bean.response.KanalRspBean;
import com.nutizen.nu.utils.AnimUtil;
import com.nutizen.nu.widget.MoreKanalItemHeaderDecoration;
import com.nutizen.nu.widget.MyLinearLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MoreKanalFragment extends DialogFragment implements View.OnClickListener, MoreKanalItemHeaderDecoration.OnHeadChangeListener, KanalIndexAdapter.OnIndexFocusListener {

    public static final String TAG = "MoreKanalFragment";
    public static final String KANAL_BEANS = "kanal beans";
    private ArrayList<KanalRspBean.SearchBean> mKanals;
    private ArrayList<String> mStringToPositionList;//MoreKanalintemHeaderDecoration需要用到的索引
    private ArrayMap<String, Integer> mHeaderBeansMap;//KanalIndexAdapter需要用到的索引
    private View mBackBtn;
    private RecyclerView mKanalsRv;
    private RecyclerView mIndexRv;
    private KanalListAdapter mKanalListAdapter;
    private MyLinearLayoutManager mLayoutManager;
    private MoreKanalItemHeaderDecoration mKanalsItemDecoration;
    private KanalIndexAdapter mIndexAdapter;

    public static MoreKanalFragment getInstance(Bundle bundle) {
        MoreKanalFragment moreKanalFragment = new MoreKanalFragment();
        moreKanalFragment.setArguments(bundle);
        moreKanalFragment.setStyle(0, R.style.FullScreenLightDialog);
        return moreKanalFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mKanals = new ArrayList<KanalRspBean.SearchBean>();
            mKanals.addAll((ArrayList<KanalRspBean.SearchBean>) arguments.getSerializable(KANAL_BEANS));//防止改原数据
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        switchMainIconAppear(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        switchMainIconAppear(false);
    }

    private void switchMainIconAppear(boolean appear) {
        Activity activity = getActivity();
        if (activity == null || !(activity instanceof MainActivity)) {
            return;
        }
        if (appear) {
            ((MainActivity) activity).resumeToShowIcons();
        } else {
            AnimUtil.setViewAlphaAnim(mBackBtn, true, 800);
            ((MainActivity) activity).pauseToHideIcons();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable());
        getDialog().getWindow().setWindowAnimations(R.style.animate_dialog_enter_and_exit);

        View inflate = inflater.inflate(R.layout.fragment_more_kanal, container, false);
        mKanalsRv = inflate.findViewById(R.id.rv_more_kanal);
        mIndexRv = inflate.findViewById(R.id.rv_index_kanal);
        mBackBtn = inflate.findViewById(R.id.back);
        mBackBtn.setOnClickListener(this);

        initData();

        return inflate;
    }

    protected void initData() {
        Collections.sort(mKanals, new Comparator<KanalRspBean.SearchBean>() {
            @Override
            public int compare(KanalRspBean.SearchBean o1, KanalRspBean.SearchBean o2) {
                return o1.getUsername().toLowerCase().compareTo(o2.getUsername().toLowerCase());
            }
        });

        mKanalListAdapter = new KanalListAdapter(getContext(), -1);
        handleData();
        mKanalListAdapter.setData(mKanals);

        mLayoutManager = new MyLinearLayoutManager(getContext());

        mKanalsItemDecoration = new MoreKanalItemHeaderDecoration();
        mKanalsItemDecoration.setSearchBeanList(mKanals, mStringToPositionList);
        mKanalsItemDecoration.setOnHeadChangeListener(this);
        mKanalsRv.setHasFixedSize(true);
        mKanalsRv.setAdapter(mKanalListAdapter);
        mKanalsRv.setLayoutManager(mLayoutManager);
        mKanalsRv.addItemDecoration(mKanalsItemDecoration);

        mIndexRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mIndexAdapter = new KanalIndexAdapter();
        mIndexAdapter.setOnIndexClickListener(this);
        mIndexAdapter.setData(mHeaderBeansMap);
        mIndexRv.setAdapter(mIndexAdapter);
    }

    private void handleData() {
        if (mKanals.size() > 0) {
            mStringToPositionList = new ArrayList<>();
            mHeaderBeansMap = new ArrayMap<>();
            char c0 = mKanals.get(mKanals.size() - 1).getUsername().toLowerCase().charAt(0);
            int number = 0;
            for (int i = mKanals.size() - 1; i >= -1; i--) {//i == - 1，在遍历Kanals(0)后再走一遍处理数据
                char c = i == -1 ? ' ' : mKanals.get(i).getUsername().toLowerCase().charAt(0);
                if (c0 != c) {
                    KanalRspBean.SearchBean bean = new KanalRspBean.SearchBean();
                    bean.setUsername(String.valueOf(c0).toUpperCase());
                    bean.setHead(true);
                    mKanals.add(i + 1, bean);
                    mHeaderBeansMap.put(String.valueOf(c0).toUpperCase(), number);
                    mStringToPositionList.add(0, String.valueOf(c0).toUpperCase());
                    number = 0;
                }
                c0 = c;
                number++;
            }

            int total = 0;//用来记录总postiion
            for (int i = 0; i < mHeaderBeansMap.size(); i++) {
                int temp = total;
                total += 1 + mHeaderBeansMap.valueAt(i);
                mHeaderBeansMap.put(mHeaderBeansMap.keyAt(i), temp);
            }
        }
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
    public void onHeadChange(int position) {
        mIndexAdapter.jumpToKanalPosition(position);
    }

    @Override
    public void onIndexClick(int jumpPosition) {
        mKanalsRv.smoothScrollToPosition(jumpPosition);
    }
}
