package com.nutizen.nu.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.nutizen.nu.R;
import com.nutizen.nu.adapter.SearchResultPagerAdapter;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.KanalRspBean;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.presenter.SearchPresenter;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.SearchView;

public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchView, View.OnClickListener, TextView.OnEditorActionListener {

    private View mProgressbar;
    private AutoCompleteTextView mEditText;
    private View mCleanBtn;
    private View mSearchBtn;
    private TabLayout mTablayout;
    private ViewPager mResultViewpager;
    private SearchResultPagerAdapter mResultPagerAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    public int getBarColor() {
        return R.color.colorBlack;
    }

    @Override
    protected SearchPresenter initPresenter() {
        return new SearchPresenter(this, this);
    }

    @Override
    protected void initView() {
        mProgressbar = findViewById(R.id.progress_bar_layout);
        mEditText = findViewById(R.id.completetv_search);
        mCleanBtn = findViewById(R.id.iv_clean_btn);
        mSearchBtn = findViewById(R.id.iv_search);
        mTablayout = findViewById(R.id.search_tablayout);
        mResultViewpager = findViewById(R.id.search_viewpager);
    }

    @Override
    protected void initData() {
        mTablayout.setupWithViewPager(mResultViewpager);
        mResultPagerAdapter = new SearchResultPagerAdapter(this);
        mResultViewpager.setAdapter(mResultPagerAdapter);
    }

    @Override
    protected void initEvent() {
        findViewById(R.id.back).setOnClickListener(this);
        mCleanBtn.setOnClickListener(this);
        mSearchBtn.setOnClickListener(this);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mCleanBtn.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        });
        mEditText.setOnEditorActionListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.iv_clean_btn:
                mEditText.setText("");
                break;
            case R.id.iv_search:
                search();
                break;
        }
    }

    private void search() {
        mPresenter.search(mEditText.getText().toString().trim());
        hideSoftKeyboard();
        setProgressbarVisibility(true);
    }

    @Override
    public void onVideoSearch(ContentResponseBean contentResponseBean) {
        mResultPagerAdapter.setVideoDatas(contentResponseBean.getSearch());
    }

    @Override
    public void onKanalSearch(KanalRspBean kanalRspBean) {
        mResultPagerAdapter.setKanalDatas(kanalRspBean.getSearch());
    }

    public void requestMoreContent() {
        mPresenter.searchMoreMovie();
    }

    @Override
    public void noMoreContents() {
        ToastUtils.showShort("No more contents");
        mResultPagerAdapter.hideVideoLoadingMore();
    }

    @Override
    public void onMoreVideoSearch(ContentResponseBean contentResponseBean) {
        mResultPagerAdapter.setMoreVieoDatas(contentResponseBean.getSearch());
        mResultPagerAdapter.hideVideoLoadingMore();
    }

    @Override
    public void onSuccess() {
        setProgressbarVisibility(false);
    }

    @Override
    public void onFailure(String errorMessage) {
        ToastUtils.showShort(errorMessage);
        setProgressbarVisibility(false);
    }

    private void setProgressbarVisibility(boolean visibility) {
        mProgressbar.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search();
            return true;
        }
        return false;
    }
}
