package com.nutizen.nu.utils;

import android.animation.ValueAnimator;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.nutizen.nu.R;

public class DownArrowAnimUtil {
    public static void switchDownArrow(final View downArrowIv, final View changeHeighView) {
        final int heigh = (int) ScreenUtils.dip2px(downArrowIv.getContext(), 22f);
        final int downArrowMove = heigh + (int) ScreenUtils.dip2px(downArrowIv.getContext(), 45f);
        ValueAnimator valueAnimator;
        if (!downArrowIv.isSelected()) {
            Animation downArrowAnim = AnimationUtils.loadAnimation(downArrowIv.getContext(), R.anim.anim_arrow_down2up);
            downArrowIv.startAnimation(downArrowAnim);
            downArrowIv.setSelected(true);
            valueAnimator = ValueAnimator.ofInt(1, heigh);
        } else {
            Animation downArrowAnim = AnimationUtils.loadAnimation(downArrowIv.getContext(), R.anim.anim_arrow_up2down);
            downArrowIv.startAnimation(downArrowAnim);
            downArrowIv.setSelected(false);
            valueAnimator = ValueAnimator.ofInt(heigh, 1);
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int h = (int) animation.getAnimatedValue();
                changeHeighView.getLayoutParams().height = h;

                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) downArrowIv.getLayoutParams();
                layoutParams.topMargin = (int) (h / (float) heigh * downArrowMove);

                changeHeighView.requestLayout();
            }
        });
        valueAnimator.setDuration(200);
        valueAnimator.start();
    }
}
