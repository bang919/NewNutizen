package com.nutizen.nu.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.nutizen.nu.R;

/**
 * Created by bigbang on 2018/4/4.
 */

public class LeftMenuItemView extends ConstraintLayout {


    public LeftMenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.main_leftmenu_item, this);
        ConstraintLayout constraintLayout = findViewById(R.id.leftmenu_root);
        TextView textView = findViewById(R.id.leftmenu_tv);


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LeftMenuItemView);

        int height = typedArray.getDimensionPixelOffset(R.styleable.LeftMenuItemView_layout_height, 55);
        constraintLayout.setMinHeight(height);
        int backgroundRes = typedArray.getResourceId(R.styleable.LeftMenuItemView_background, R.drawable.bg_main_leftmenu_item_profile);
        constraintLayout.setBackgroundResource(backgroundRes);
        String text = typedArray.getString(R.styleable.LeftMenuItemView_title);
        textView.setText(text);
    }

}
