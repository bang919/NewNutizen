package com.nutizen.nu.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.OverScroller;

import com.nutizen.nu.R;

public class DetailKanalSwipeRefreshLayout extends MySwipeRefreshLayout {

    private int mTopMargin;
    private View mHeadView;
    private View mContentView;

    private OverScroller mScroller;

    public DetailKanalSwipeRefreshLayout(@NonNull Context context) {
        this(context, null);
    }

    public DetailKanalSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new OverScroller(getContext());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeadView = findViewById(R.id.head_kanal_detail);
        mContentView = findViewById(R.id.kanal_detail_tablayout);
        mTopMargin = ((ConstraintLayout.LayoutParams) findViewById(R.id.kanal_detail_tablayout).getLayoutParams()).topMargin;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int headViewMeasuredHeight = mHeadView.getMeasuredHeight();
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() + headViewMeasuredHeight, MeasureSpec.EXACTLY);//最下面加个头啦
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return super.onStartNestedScroll(child, target, nestedScrollAxes);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
        if (consumed[1] != 0) {//已经被父控件处理过了
            return;
        }
        if (dy > 0) {//向上滑动
            if (mContentView.getY() > getScrollY() + mTopMargin) {
                offset(dy, consumed);
                onNestedPreFling(target, 0, dy);
            } else {
                offset((int) (mContentView.getY() - getScrollY() - mTopMargin), consumed);
            }
        } else {//向下滑动
            if (!ViewCompat.canScrollVertically(target, dy)) {//当target不能向下滑时
                if (getScrollY() + dy > 0) {
                    offset(dy, consumed);
                } else {
                    offset(-getScrollY(), consumed);
                }
            }
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (velocityY > 0 && mContentView.getY() > getScrollY() + mTopMargin) {
            fling((int) (mContentView.getY() - getScrollY() - mTopMargin));
        }
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if (velocityY < 0 && !ViewCompat.canScrollVertically(target, -1) && getScrollY() > 0) {
            fling(-getScrollY());
        }
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    private void offset(int dy, int[] consumed) {
        //第二个参数为正代表向下，为负代表向上
        scrollBy(0, dy);
        consumed[0] = 0;
        consumed[1] = dy;
    }

    public void fling(int velocityY) {
        mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, getMeasuredHeight());
        mScroller.startScroll(0, getScrollY(), 0, velocityY);
        invalidate();
    }

    @Override
    public void computeScroll() {
        // 重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
