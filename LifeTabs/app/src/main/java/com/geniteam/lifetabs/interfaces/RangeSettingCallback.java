package com.geniteam.lifetabs.interfaces;

public interface RangeSettingCallback {
    public void onSuccess(float minValue , float maxValue);
    public void onFailure(String result);
}