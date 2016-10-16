package com.fan.mvc.api;


import com.fan.mvc.entity.Weather;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by sysadminl on 2015/12/18.
 */
public interface ApiService {


    @GET("/apistore/weatherservice/weather")
    Observable<Weather> getWeather(@Header("apikey") String apikey, @Query("citypinyin") String pinyin);
}
