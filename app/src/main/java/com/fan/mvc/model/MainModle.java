package com.fan.mvc.model;

import android.content.Context;

/**
 * 主页获取天气信息接口
 */
public interface MainModle {
        void getWeather(Context context, String cityNum, OnWeatherListener listener);
}