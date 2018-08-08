package me.imid.swipebacklayout.lib.app;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.lang.ref.WeakReference;

import me.imid.swipebacklayout.lib.SwipeBackLayout;

/**
 * Created by laysionqet on 2018/4/24.
 */
public class SwipeBackListenerDialogFragmentAdapter implements SwipeBackLayout.SwipeListenerEx {
    private final WeakReference<DialogFragment> mFragment;

    public SwipeBackListenerDialogFragmentAdapter(@NonNull DialogFragment dialogFragment) {
        mFragment = new WeakReference<>(dialogFragment);
    }

    @Override
    public void onScrollStateChange(int state, float scrollPercent) {

    }

    @Override
    public void onEdgeTouch(int edgeFlag) {
        DialogFragment dialogFragment = mFragment.get();
        if (null != dialogFragment) {
//            Utils.convertActivityToTranslucent(activity);//这里本来就透明，不用设置了
        }
    }

    @Override
    public void onScrollOverThreshold() {

    }

    @Override
    public void onContentViewSwipedBack() {
        DialogFragment fragment = mFragment.get();
        if (null != fragment) {
            if (fragment.getDialog() != null && fragment.getDialog().getWindow() != null) {
                fragment.getDialog().getWindow().setWindowAnimations(0);
            }
            fragment.dismiss();
        }
    }
}
