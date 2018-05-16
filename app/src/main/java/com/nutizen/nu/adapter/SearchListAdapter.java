package com.nutizen.nu.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.KanalRspBean;
import com.nutizen.nu.utils.ScreenUtils;

import java.util.ArrayList;

public class SearchListAdapter<D> extends RecyclerView.Adapter<SearchListAdapter.TextViewHolder> {

    private RecyclerView mRecyclerView;
    private View mLoadingView;
    private SearchListAdapter mSearchListAdapter;
    private View mEmptyView;
    private OnSearchListListener<D> mOnSearchListListener;

    private ArrayList<D> mDatas;

    //public final Observable<T> doOnNext(Consumer<? super T> onNext) {
    public void setOnSearchListListener(OnSearchListListener<D> listener) {
        mOnSearchListListener = listener;
    }

    public View initView(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.pager_datas_detail, null);
        mRecyclerView = inflate.findViewById(R.id.pager_item_recycler);
        mEmptyView = inflate.findViewById(R.id.pager_item_nodata_layout);
        mLoadingView = inflate.findViewById(R.id.loading);
        return inflate;
    }

    public void requestingMoreDataUi() {
        mLoadingView.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(mLoadingView.getContext(), R.anim.anim_loading_more_show);
        mLoadingView.startAnimation(animation);
    }

    public void hideLoadingView() {
        if (mLoadingView.getVisibility() == View.VISIBLE) {
            Animation animation = AnimationUtils.loadAnimation(mLoadingView.getContext(), R.anim.anim_loading_more_hide);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mLoadingView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mLoadingView.startAnimation(animation);
        }
    }

    public void setDatas(ArrayList<D> datas) {
        if (mSearchListAdapter == null) {
            mSearchListAdapter = this;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
            mRecyclerView.setAdapter(this);
            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.top = (int) ScreenUtils.dip2px(parent.getContext(), 2);
                    outRect.left = (int) ScreenUtils.dip2px(parent.getContext(), 5);
                    outRect.right = (int) ScreenUtils.dip2px(parent.getContext(), 5);
                }
            });
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && !mRecyclerView.canScrollVertically(1) && mDatas != null && mOnSearchListListener != null) {//到达底部，加载更多
                        requestingMoreDataUi();
                        mOnSearchListListener.onBottomShowMore();
                    }
                }
            });
        }
        mDatas = datas;
        notifyDataSetChanged();
        mEmptyView.setVisibility(datas != null && datas.size() > 0 ? View.GONE : View.VISIBLE);
    }

    public void setMoreDatas(ArrayList<D> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public SearchListAdapter.TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        textView.setMaxLines(1);
        textView.setTextColor(parent.getContext().getResources().getColor(R.color.colorTintWhite));
        textView.setTextSize(ScreenUtils.dip2px(parent.getContext(), 12));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        return new SearchListAdapter.TextViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(SearchListAdapter.TextViewHolder holder, final int position) {
        D data = mDatas.get(position);
        String text = "";
        if (data instanceof ContentResponseBean.SearchBean) {
            text = ((ContentResponseBean.SearchBean) data).getTitle();
        } else if (data instanceof KanalRspBean.SearchBean) {
            text = ((KanalRspBean.SearchBean) data).getUsername();
        }
        ((TextView) holder.itemView).setText(text);
        if (mOnSearchListListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnSearchListListener.onSearchClick(mDatas.get(position), position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public interface OnSearchListListener<D> {
        void onSearchClick(D data, int position);

        void onBottomShowMore();
    }

    class TextViewHolder extends RecyclerView.ViewHolder {

        public TextViewHolder(View itemView) {
            super(itemView);
        }
    }
}
