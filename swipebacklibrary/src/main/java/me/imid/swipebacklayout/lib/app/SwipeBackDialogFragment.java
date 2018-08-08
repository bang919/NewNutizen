package me.imid.swipebacklayout.lib.app;

import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

public class SwipeBackDialogFragment extends DialogFragment {

    private SwipeBackLayout mSwipeBackLayout;

    protected View createSwipeBackView(View createView) {
        mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(getContext()).inflate(
                me.imid.swipebacklayout.lib.R.layout.swipeback_layout, null);
        return mSwipeBackLayout.createDialogFragmentView(this, (ViewGroup) createView);
    }
}
