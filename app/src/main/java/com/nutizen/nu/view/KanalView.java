package com.nutizen.nu.view;

import com.nutizen.nu.bean.response.KanalRspBean;

public interface KanalView {
    void onKanalsResponse(KanalRspBean kanalRspBean);

    void onFailure(String error);
}
