package com.nutizen.nu.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * 结合MyLinearSmoothScroller，让Recyclerview的smoothScroolTo滚动后，position在最上面
 */
public class MyLinearLayoutManager extends LinearLayoutManager {
    public MyLinearLayoutManager(Context context) {
        super(context);
    }

    public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        MyLinearSmoothScroller linearSmoothScroller =
                new MyLinearSmoothScroller(recyclerView.getContext());
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}
