package com.nutizen.nu.fragment;

import com.nutizen.nu.bean.response.ContentResponseBean;

import java.util.ArrayList;

public class NewlyMoreFragment extends BaseHomeMoreFragment {

    public static NewlyMoreFragment getInstance(ArrayList<ContentResponseBean.SearchBean> datas, String title) {
        return getInstance(NewlyMoreFragment.class, datas, title);
    }

    @Override
    protected void refreshData() {
        mPresenter.requestNewlys();
    }

    @Override
    protected void requestMoreData() {
        mPresenter.requestMoreNewlys();
    }
}
