package com.nutizen.nu.view;

import java.io.File;

public interface PortraitEditView {

    void onError(String error);

    void onLuBanSuccess(File picFile);

    void onUpdatePortraitSuccess();
}
