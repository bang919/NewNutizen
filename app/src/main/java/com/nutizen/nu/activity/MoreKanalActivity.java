package com.nutizen.nu.activity;

import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nutizen.nu.R;
import com.nutizen.nu.adapter.KanalIndexAdpater;
import com.nutizen.nu.adapter.KanalListAdapter;
import com.nutizen.nu.adapter.MoreKanalItemHeaderDecoration;
import com.nutizen.nu.bean.response.KanalRspBean;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.common.BasePresenter;
import com.nutizen.nu.utils.AnimUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MoreKanalActivity extends BaseActivity implements View.OnClickListener, KanalIndexAdpater.OnIndexFocusListener, MoreKanalItemHeaderDecoration.OnHeadChangeListener {

    public static final String KANAL_BEANS = "kanal beans";
    private ArrayList<KanalRspBean.SearchBean> mKanals;
    private ArrayMap<String, Integer> mHeaderBeans;
    private View mBackBtn;
    private RecyclerView mKanalsRv;
    private RecyclerView mIndexRv;
    private KanalListAdapter mKanalListAdapter;
    private LinearLayoutManager mLayoutManager;
    private MoreKanalItemHeaderDecoration mKanalsItemDecoration;
    private KanalIndexAdpater mIndexAdapter;

    @Override
    public int getBarColor() {
        return R.color.colorBlack;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_more_kanal;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void onDestroy() {
        AnimUtil.setViewAlphaAnim(mBackBtn, false, 100);
        super.onDestroy();
    }

    @Override
    protected void initView() {
        mKanalsRv = findViewById(R.id.rv_more_kanal);
        mIndexRv = findViewById(R.id.rv_index_kanal);
        mBackBtn = findViewById(R.id.back);
        mBackBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        mKanals = (ArrayList<KanalRspBean.SearchBean>) getIntent().getSerializableExtra(KANAL_BEANS);
        Collections.sort(mKanals, new Comparator<KanalRspBean.SearchBean>() {
            @Override
            public int compare(KanalRspBean.SearchBean o1, KanalRspBean.SearchBean o2) {
                return o1.getUsername().toLowerCase().compareTo(o2.getUsername().toLowerCase());
            }
        });

        mKanalListAdapter = new KanalListAdapter(this, -1, true);
        handleData();
        mKanalListAdapter.setData(mKanals);

        mLayoutManager = new LinearLayoutManager(this);

        mKanalsItemDecoration = new MoreKanalItemHeaderDecoration();
        mKanalsItemDecoration.setSearchBeanList(mKanals);
        mKanalsItemDecoration.setOnHeadChangeListener(this);
        mKanalsRv.setHasFixedSize(true);
        mKanalsRv.setAdapter(mKanalListAdapter);
        mKanalsRv.setLayoutManager(mLayoutManager);
        mKanalsRv.addItemDecoration(mKanalsItemDecoration);

        mIndexRv.setLayoutManager(new LinearLayoutManager(this));
        mIndexAdapter = new KanalIndexAdpater();
        mIndexAdapter.setOnIndexClickListener(this);
        mIndexAdapter.setData(mHeaderBeans);
        mIndexRv.setAdapter(mIndexAdapter);
        mKanalsRv.addOnScrollListener(new RecyclerViewListener());
    }

    private void handleData() {
        if (mKanals.size() > 0) {
            mHeaderBeans = new ArrayMap<>();
            char c0 = mKanals.get(mKanals.size() - 1).getUsername().toLowerCase().charAt(0);
            int number = 0;
            for (int i = mKanals.size() - 1; i >= -1; i--) {//i == - 1，在遍历Kanals(0)后再走一遍处理数据
                char c = i == -1 ? ' ' : mKanals.get(i).getUsername().toLowerCase().charAt(0);
                if (c0 != c) {
                    KanalRspBean.SearchBean bean = new KanalRspBean.SearchBean();
                    bean.setUsername(String.valueOf(c0).toUpperCase());
                    bean.setHead(true);
                    mKanals.add(i + 1, bean);
                    mHeaderBeans.put(String.valueOf(c0).toUpperCase(), number);
                    number = 0;
                }
                c0 = c;
                number++;
            }
        }
    }

    @Override
    protected void initEvent() {
        AnimUtil.setViewAlphaAnim(mBackBtn, true, 800);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void onHeadChange(String c) {
        mIndexAdapter.jumpToKanalPosition(c);
    }

    @Override
    public void onIndexClick(int jumpPosition) {
        smoothMoveToPostion(jumpPosition);
    }

    /**
     * 以下用于点击mIndexRv后让mKanalsRv跳转到对应地方
     */
    private boolean needMove = false;

    private void smoothMoveToPostion(int n) {
        int firstItem = mLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLayoutManager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mKanalsRv.smoothScrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mKanalsRv.getChildAt(n - firstItem).getTop();
            mKanalsRv.smoothScrollBy(0, top);
        } else {
            mKanalsRv.smoothScrollToPosition(n);
            needMove = true;
        }
    }


    private class RecyclerViewListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (needMove && newState == RecyclerView.SCROLL_STATE_IDLE) {
                needMove = false;
                int n = mLayoutManager.findLastVisibleItemPosition() - mLayoutManager.findFirstVisibleItemPosition();
                int top = mKanalsRv.getChildAt(n).getTop();
                mKanalsRv.smoothScrollBy(0, top);
            }
        }
    }

}
