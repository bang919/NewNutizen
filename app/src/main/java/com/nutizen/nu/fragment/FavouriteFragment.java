package com.nutizen.nu.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nutizen.nu.R;
import com.nutizen.nu.adapter.FavouriteAdapter;
import com.nutizen.nu.bean.response.FavouriteRspBean;
import com.nutizen.nu.presenter.FavouritePresenter;
import com.nutizen.nu.utils.ToastUtils;
import com.nutizen.nu.view.FavouriteView;

import java.util.ArrayList;
import java.util.TreeMap;

public abstract class FavouriteFragment extends BaseDialogFragment<FavouritePresenter> implements View.OnClickListener, FavouriteView, FavouriteAdapter.OnFavouriteItemClickListener {

    public static final String TAG = "FavouriteFragment";
    private RecyclerView mFavouriteRv;
    private FavouriteAdapter mFavouriteAdapter;
    private View mBackBtn;
    private TextView mTitleTv;
    private TextView mCancelBtn, mEditBtn, mDoneBtn;
    private View mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                //如果按退出键的时候是Editing状态，取消Editing状态
                if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE) && event.getAction() == KeyEvent.ACTION_DOWN && mCancelBtn.getVisibility() == View.VISIBLE) {
                    setEditStatus(false);
                    return true;
                }
                return false;
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_favourite_content;
    }

    @Override
    protected FavouritePresenter initPresenter() {
        return new FavouritePresenter(getContext(), this);
    }

    @Override
    protected void initView(View rootView) {
        mProgressBar = rootView.findViewById(R.id.progress_bar_layout);
        mFavouriteRv = rootView.findViewById(R.id.recyclerv_favourite);
        mBackBtn = rootView.findViewById(R.id.back);
        mTitleTv = rootView.findViewById(R.id.title_text_title_fragment);
        mCancelBtn = rootView.findViewById(R.id.btn_cancel);
        mEditBtn = rootView.findViewById(R.id.btn_edit);
        mDoneBtn = rootView.findViewById(R.id.btn_remove);
        if (mTitleTv != null) {
            mTitleTv.setText(setTitle());
        }
    }

    @Override
    protected void initEvent() {
        mBackBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        mEditBtn.setOnClickListener(this);
        mDoneBtn.setOnClickListener(this);

        mFavouriteAdapter = new FavouriteAdapter();
        mFavouriteAdapter.setOnFavouriteItemClickListener(this);
        mFavouriteRv.setAdapter(mFavouriteAdapter);
        mFavouriteRv.setLayoutManager(new LinearLayoutManager(getContext()));
        setEditStatus(false);
        requestData();
    }

    private void requestData() {
        if (mPresenter != null) {
            mPresenter.requestFavouriteContent();
            setProgressBarVisibility(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                dismiss();
                break;
            case R.id.btn_edit:
                setEditStatus(true);
                break;
            case R.id.btn_remove:
                removeFavouriteSelected();
                break;
            case R.id.btn_cancel:
                setEditStatus(false);
                break;
        }
    }

    protected abstract String setTitle();

    protected abstract String setType();

    @Override
    public void onFavouriteResponse(TreeMap<String, ArrayList<FavouriteRspBean>> favouriteMap) {
        mFavouriteAdapter.setData(favouriteMap.get(setType()));
        setProgressBarVisibility(false);
        setEditStatus(false);
    }

    @Override
    public void onFailure(String errorMsg) {
        ToastUtils.showShort(errorMsg);
        setProgressBarVisibility(false);
        setEditStatus(false);
    }

    protected void setProgressBarVisibility(boolean visibility) {
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void setEditStatus(boolean edit) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mFavouriteRv.getLayoutManager();
        mFavouriteAdapter.changeEditStatus(edit, layoutManager.findFirstVisibleItemPosition(), layoutManager.findLastVisibleItemPosition());
        mCancelBtn.setVisibility(edit ? View.VISIBLE : View.GONE);
        mDoneBtn.setVisibility(edit ? View.VISIBLE : View.GONE);
        mEditBtn.setVisibility(edit ? View.GONE : View.VISIBLE);
        mBackBtn.setVisibility(edit ? View.GONE : View.VISIBLE);
    }

    private void removeFavouriteSelected() {
        ArrayList<FavouriteRspBean> selectFavourites = mFavouriteAdapter.getSelectFavourite();
        if (selectFavourites.size() > 0) {
            setProgressBarVisibility(true);
            mPresenter.removeFavourites(selectFavourites);
        } else {
            setEditStatus(false);
        }
    }

}
