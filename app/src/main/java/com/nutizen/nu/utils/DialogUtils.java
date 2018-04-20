package com.nutizen.nu.utils;

import android.app.Dialog;
import android.view.View;

import com.nutizen.nu.R;
import com.nutizen.nu.common.BaseActivity;
import com.nutizen.nu.dialog.NormalDialog;

public class DialogUtils {

    public static Dialog getAskLoginDialog(final BaseActivity baseActivity) {
        return new NormalDialog(baseActivity, baseActivity.getString(R.string.yes), baseActivity.getString(R.string.cancel), baseActivity.getString(R.string.do_you_want_to_login),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        baseActivity.logout();
                    }
                }, null);
    }
}
