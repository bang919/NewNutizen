package com.nutizen.nu.utils;

import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.nutizen.nu.R;

public class AnimUtil {

    /**
     * 按向下按钮显示详情
     */
    public static void switchDownArrow(final View downArrowIv, final TextView changeHeighView) {

        Paint textViewPaint = changeHeighView.getPaint();
        float textWidth = (textViewPaint.measureText(changeHeighView.getText().toString()));
        int viewWidth = changeHeighView.getWidth();
        float textSize = changeHeighView.getTextSize();
        int s = (int) (textWidth / viewWidth);

        final int heigh = (int) ScreenUtils.dip2px(downArrowIv.getContext(), 38f) + (int) textSize * s;
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
                changeHeighView.requestLayout();
            }
        });
        valueAnimator.setDuration(200);
        valueAnimator.start();
    }

    /**
     * MainActivity显示/隐藏按钮
     */
    public static void setViewAlphaAnim(View view, boolean appear) {
        setViewAlphaAnim(view, appear, -1);
    }

    public static void setViewAlphaAnim(View view, boolean appear, int duration) {
        Animation animation = AnimationUtils.loadAnimation(view.getContext(), appear ? R.anim.anim_appear : R.anim.anim_disappear);
        if (duration != -1) {
            animation.setDuration(duration);
        }
        view.startAnimation(animation);
    }

    /**
     * FavouriteAdapter的绿色按键弹出
     */
    public static void setEditFavouriteButton(final View button, boolean appear) {
        button.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), 0);
        int width = button.getMeasuredWidth();
        ValueAnimator valueAnimator = appear ? ValueAnimator.ofInt(0, width) : ValueAnimator.ofInt(width, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int w = (int) animation.getAnimatedValue();
                button.getLayoutParams().width = w;
                button.requestLayout();
            }
        });
        valueAnimator.setDuration(200);
        valueAnimator.start();
    }
}
