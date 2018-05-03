package com.nutizen.nu.fragment;

import com.nutizen.nu.bean.response.ContentResponseBean;

import java.util.ArrayList;

public class EditorMoreFragment extends BaseHomeMoreFragment {

    public static EditorMoreFragment getInstance(ArrayList<ContentResponseBean.SearchBean> datas, String title) {
        return getInstance(EditorMoreFragment.class, datas, title);
    }

    @Override
    protected void refreshData() {
        mPresenter.requestEditors();
    }

    @Override
    protected void requestMoreData() {
        //No request more
    }
}
