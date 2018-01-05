package com.crm.firstapplication.rxandroid;

/**
 * Created by Administrator on 2017/12/27.
 */

import com.crm.firstapplication.vo.PanelsVO;
import com.crm.firstapplication.vo.ResVO;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 *所有的网络请求都可以写在这个接口类里面.
 */
public interface APIService {

    @GET("/WebAPI/getPanelList")
    Observable<ResVO<PanelsVO>> login(@QueryMap HashMap<String, String> params);

    @GET
    Observable<ResponseBody> url(@Url String url);

}
