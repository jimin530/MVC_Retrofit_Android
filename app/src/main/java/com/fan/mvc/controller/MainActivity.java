package com.fan.mvc.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fan.mvc.R;
import com.fan.mvc.entity.Weather;
import com.fan.mvc.loaddingview.NetWorkUtil;
import com.fan.mvc.model.MainModelImpl;
import com.fan.mvc.model.MainModle;
import com.fan.mvc.model.OnWeatherListener;
import com.fan.mvc.view.MainView;
import com.fan.mvc.view.RequestWeatherView;

public class MainActivity extends AppCompatActivity implements RequestWeatherView,OnWeatherListener{
    private MainView mainView;//对应的View层
    private static MainModle mainModle = null;//对应的Modle抽象类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = new MainView(this,this);//实例化View
        if(null == mainModle){
            mainModle = new MainModelImpl();//实例化抽象，多态
        }
    }

    @Override
    public void onSuccess(Weather weather) {
       mainView.showSuccess(weather);
    }

    @Override
    public void onError() {
        mainView.showFailed();
    }

    @Override
    public void sendRequest(String num) {
        if(NetWorkUtil.isNetWorkConnected(this)) {
            mainModle.getWeather(this, num, this);
        }else {
            mainView.showNoNet();
        }
    }
}
