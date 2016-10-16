package com.fan.mvc.model;

import android.content.Context;
import android.util.Log;

import com.fan.mvc.api.ApiManager;
import com.fan.mvc.entity.Weather;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fan on 2016/10/16.
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽保佑,代码无bug
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * <p/>
 * ━━━━━━感觉萌萌哒━━━━━━
 */
public class MainModelImpl implements MainModle{
    private static final String TAG = "MainModelImpl";
    /**
     * 实现MainModole获取天气信息
     * Created by fan on 2016/10/16.
     * cityNum:城市代码
     * listener:网络连接是否成功接口
     */
    @Override
    public void getWeather(Context context, String cityNum, final OnWeatherListener listener) {
        ApiManager.getWeather(cityNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Weather>() {
            @Override
            public void call(Weather weather) {
                Log.e(TAG, "MainModelImpl:" + weather);
                listener.onSuccess(weather);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, "MainModelImpl:" + throwable);
                listener.onError();
            }
        });
    }
}
