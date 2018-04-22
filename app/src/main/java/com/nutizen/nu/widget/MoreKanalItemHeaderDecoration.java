package com.nutizen.nu.widget;

import android.graphics.Canvas;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.KanalRspBean;

import java.util.ArrayList;

public class MoreKanalItemHeaderDecoration extends RecyclerView.ItemDecoration {

    private ArrayList<KanalRspBean.SearchBean> mSearchBeanList;
    private KanalRspBean.SearchBean mCurrentTag;
    private OnHeadChangeListener mOnHeadChangeListener;

    public MoreKanalItemHeaderDecoration() {
        mSearchBeanList = new ArrayList<>();
        mCurrentTag = new KanalRspBean.SearchBean();
        mCurrentTag.setHead(true);
        mCurrentTag.setUsername("A");
    }


    public void setSearchBeanList(ArrayList<KanalRspBean.SearchBean> list) {
        mSearchBeanList = list;
    }

    public void setOnHeadChangeListener(OnHeadChangeListener listener) {
        mOnHeadChangeListener = listener;
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (mSearchBeanList == null || mSearchBeanList.size() == 0) {
            return;
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int pos = layoutManager.findFirstVisibleItemPosition();
        int posNext = layoutManager.findFirstCompletelyVisibleItemPosition();
        View childNext = parent.findViewHolderForAdapterPosition(posNext).itemView;
        if (mSearchBeanList.size() > 0) {
            mCurrentTag = mSearchBeanList.get(pos);
        }
        boolean isTranslate = mSearchBeanList.get(posNext).isHead() && childNext.getTop() < childNext.getHeight();
        if (isTranslate) {
            canvas.save();
            canvas.translate(0, childNext.getTop() - childNext.getHeight());
        }
        drawHeader(parent, canvas);
        if (isTranslate) {
            canvas.restore();
        }
        if (mOnHeadChangeListener != null) {
            mOnHeadChangeListener.onHeadChange(mCurrentTag.getUsername().substring(0, 1).toUpperCase());
        }
    }

    private void drawHeader(RecyclerView parent, Canvas canvas) {
        View topTitleView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kanal_head, parent, false);
        TextView tvTitle = topTitleView.findViewById(R.id.tv_head);
        tvTitle.setText(mCurrentTag.getUsername().substring(0, 1).toUpperCase());

        int toDrawWidthSpec;//用于测量的widthMeasureSpec
        int toDrawHeightSpec;//用于测量的heightMeasureSpec
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) topTitleView.getLayoutParams();
        if (lp == null) {
            lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//这里是根据复杂布局layout的width height，new一个Lp
            topTitleView.setLayoutParams(lp);
        }
        topTitleView.setLayoutParams(lp);
        if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            //如果是MATCH_PARENT，则用父控件能分配的最大宽度和EXACTLY构建MeasureSpec
            toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.EXACTLY);
        } else if (lp.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            //如果是WRAP_CONTENT，则用父控件能分配的最大宽度和AT_MOST构建MeasureSpec
            toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.AT_MOST);
        } else {
            //否则则是具体的宽度数值，则用这个宽度和EXACTLY构建MeasureSpec
            toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(lp.width, View.MeasureSpec.EXACTLY);
        }
        //高度同理
        if (lp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom(), View.MeasureSpec.EXACTLY);
        } else if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom(), View.MeasureSpec.AT_MOST);
        } else {
            toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(lp.height, View.MeasureSpec.EXACTLY);
        }
        //依次调用 measure,layout,draw方法，将复杂头部显示在屏幕上
        topTitleView.measure(toDrawWidthSpec, toDrawHeightSpec);
        topTitleView.layout(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getPaddingLeft() + topTitleView.getMeasuredWidth(), parent.getPaddingTop() + topTitleView.getMeasuredHeight());
        topTitleView.draw(canvas);//Canvas默认在视图顶部，无需平移，直接绘制
        //绘制title结束
    }

    public interface OnHeadChangeListener {
        void onHeadChange(String s);
    }
}
