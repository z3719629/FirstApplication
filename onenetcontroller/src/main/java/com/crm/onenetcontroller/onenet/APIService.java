package com.crm.onenetcontroller.onenet;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2018/1/24.
 */
public interface APIService {
    @GET
    Observable<ResponseBody> getByUrl(@Url String url);

    @POST
    Observable<ResponseBody> postByUrl(@Url String url, @Body Map<String, String> body);
}
