package com.crm.mylibrary.data;

/**
 * Created by Administrator on 2017/12/27.
 */
import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 *所有的网络请求都可以写在这个接口类里面.
 */
public interface APIService {

    String BASE_URL = "http://10.0.2.2:8082";

    @GET("/WebAPI/getPanelList")
    Observable<Object> login(@QueryMap HashMap<String, String> params);

    @GET
    Observable<ResponseBody> url(@Url String url);

}
