package com.nutizen.nu.fragment;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nutizen.nu.R;
import com.nutizen.nu.adapter.KanalIndexAdapter;
import com.nutizen.nu.adapter.KanalListAdapter;
import com.nutizen.nu.bean.response.KanalRspBean;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.widget.MoreKanalItemHeaderDecoration;
import com.nutizen.nu.widget.MyLinearLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MoreKanalFragment extends TransNutizenTitleFragment implements MoreKanalItemHeaderDecoration.OnHeadChangeListener, KanalIndexAdapter.OnIndexFocusListener, KanalListAdapter.ItemOnClickListener {

    public static final String TAG = "MoreKanalFragment";
    public static final String KANAL_BEANS = "kanal beans";
    private ArrayList<KanalRspBean.SearchBean> mKanals;
    private ArrayList<String> mStringToPositionList;//MoreKanalintemHeaderDecoration需要用到的索引
    private ArrayMap<String, Integer> mHeaderBeansMap;//KanalIndexAdapter需要用到的索引
    private RecyclerView mKanalsRv;
    private RecyclerView mIndexRv;
    private KanalListAdapter mKanalListAdapter;
    private MyLinearLayoutManager mLayoutManager;
    private MoreKanalItemHeaderDecoration mKanalsItemDecoration;
    private KanalIndexAdapter mIndexAdapter;

    public static MoreKanalFragment getInstance(Bundle bundle) {
        return getInstance(MoreKanalFragment.class, true, bundle);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }


    @Override
    protected int getBodyLayout() {
        return R.layout.fragment_more_kanal;
    }

    @Override
    protected void initBodyView(View rootView) {
        mKanalsRv = rootView.findViewById(R.id.rv_more_kanal);
        mIndexRv = rootView.findViewById(R.id.rv_index_kanal);
    }

    @Override
    protected void initEvent() {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mKanals = new ArrayList<KanalRspBean.SearchBean>();
            mKanals.addAll((ArrayList<KanalRspBean.SearchBean>) arguments.getSerializable(KANAL_BEANS));//防止改原数据
        }

        Collections.sort(mKanals, new Comparator<KanalRspBean.SearchBean>() {
            @Override
            public int compare(KanalRspBean.SearchBean o1, KanalRspBean.SearchBean o2) {
                return o1.getUsername().toLowerCase().compareTo(o2.getUsername().toLowerCase());
            }
        });

        initRecyclerView();

    }

    private void initRecyclerView() {
        mKanalListAdapter = new KanalListAdapter(getContext(), -1);
        handleData();
        mKanalListAdapter.setData(mKanals);
        mKanalListAdapter.setItemOnClickListener(this);

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
    public void onHeadChange(int position) {
        mIndexAdapter.jumpToKanalPosition(position);
    }

    @Override
    public void onIndexClick(int jumpPosition) {
        mKanalsRv.smoothScrollToPosition(jumpPosition);
    }

    @Override
    public void onItemClickListener(KanalRspBean.SearchBean kanalBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KanalDetailFragment.KANAL_BEAN_DETAIL, kanalBean);
        KanalDetailFragment.getInstance(false, bundle).show(getFragmentManager(), KanalDetailFragment.TAG);
    }
}
