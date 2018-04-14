package com.nutizen.nu.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.nutizen.nu.R;
import com.nutizen.nu.adapter.KanalListAdapter;
import com.nutizen.nu.bean.response.KanalRspBean;
import com.nutizen.nu.common.BaseFragment;
import com.nutizen.nu.presenter.KanalPresenter;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.KanalView;

import java.util.ArrayList;

public class KanalFragment extends BaseFragment<KanalPresenter> implements KanalView, KanalListAdapter.ItemOnClickListener {

    private RecyclerView mKanalsRv;
    private KanalListAdapter mKanalListAdapter;
    private Button mShowMoreBtn;

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
        mKanalListAdapter = new KanalListAdapter(getContext());
        mKanalListAdapter.setItemOnClickListener(this);
        mKanalsRv.setAdapter(mKanalListAdapter);
    }

    @Override
    public void refreshData() {
        mPresenter.requestKanals("");
    }

    @Override
    public void onKanalsResponse(KanalRspBean kanalRspBean) {
        onDataRefreshFinish(true);
        ArrayList<KanalRspBean.SearchBean> searchs = kanalRspBean.getSearch();
        if (searchs != null && searchs.size() > 9) {
            mShowMoreBtn.setVisibility(View.VISIBLE);
        }
        mKanalListAdapter.setData(searchs);
    }

    @Override
    public void onFailure(String error) {
        onDataRefreshFinish(false);
        ToastUtils.showShort(error);
    }

    @Override
    public void onItemClickListener(KanalRspBean.SearchBean kanalBean) {

    }
}
