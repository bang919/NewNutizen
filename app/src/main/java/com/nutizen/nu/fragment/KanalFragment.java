package com.nutizen.nu.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.nutizen.nu.R;
import com.nutizen.nu.adapter.KanalListAdapter;
import com.nutizen.nu.bean.response.KanalRspBean;
import com.nutizen.nu.common.BaseMainFragment;
import com.nutizen.nu.presenter.KanalPresenter;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.KanalView;

import java.util.ArrayList;

public class KanalFragment extends BaseMainFragment<KanalPresenter> implements KanalView, KanalListAdapter.ItemOnClickListener, View.OnClickListener {

    private RecyclerView mKanalsRv;
    private KanalListAdapter mKanalListAdapter;
    private Button mShowMoreBtn;
    private ArrayList<KanalRspBean.SearchBean> mKanals;

    @Override
    protected int getLayout() {
        return R.layout.fragment_kanal;
    }

    @Override
    protected KanalPresenter initPresenter() {
        return new KanalPresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mKanalsRv = rootView.findViewById(R.id.rv_kanal_list);
        mShowMoreBtn = rootView.findViewById(R.id.bt_kanal_show_more);
    }

    @Override
    protected void initEvent() {
        mKanalsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mKanalListAdapter = new KanalListAdapter(getContext(), 9);
        mKanalListAdapter.setItemOnClickListener(this);
        mKanalsRv.setAdapter(mKanalListAdapter);
        mShowMoreBtn.setOnClickListener(this);
    }

    @Override
    public void refreshData() {
        mPresenter.requestKanals("");
    }

    @Override
    public void onKanalsResponse(KanalRspBean kanalRspBean) {
        onDataRefreshFinish(true);
        mKanals = kanalRspBean.getSearch();
        if (mKanals != null && mKanals.size() > 9) {
            mShowMoreBtn.setVisibility(View.VISIBLE);
        }
        mKanalListAdapter.setData(mKanals);
    }

    @Override
    public void onFailure(String error) {
        onDataRefreshFinish();
        ToastUtils.showShort(error);
    }

    @Override
    public void onItemClickListener(KanalRspBean.SearchBean kanalBean) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_kanal_show_more:
                Bundle bundle = new Bundle();
                bundle.putSerializable(MoreKanalFragment.KANAL_BEANS, mKanals);
                MoreKanalFragment.getInstance(bundle).show(getFragmentManager(), MoreKanalFragment.TAG);
                break;
        }
    }
}
