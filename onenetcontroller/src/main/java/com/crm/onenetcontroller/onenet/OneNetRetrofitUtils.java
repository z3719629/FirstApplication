package com.crm.onenetcontroller.onenet;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.chinamobile.iot.onenet.OneNetApi;
import com.crm.mylibrary.data.APIService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/1/24.
 */
public class OneNetRetrofitUtils {

    String BASE_URL = "http://10.0.2.2:8082/";

    private static volatile OneNetRetrofitUtils singleton;
    private Retrofit api;

    public static OneNetRetrofitUtils get() {
        if (singleton == null) {
            synchronized (OneNetRetrofitUtils.class) {
                singleton = new OneNetRetrofitUtils();
            }
        }
        return singleton;
    }

    private OneNetRetrofitUtils() {
    }

    public Retrofit getRetrofit() {
        if (api == null) createRetrofit();
        return api;
    }

    private void createRetrofit() {
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                            @Override
                            public void log(String message) {
                                Log.w("TAG", "log: " + message );
                            }
                        }).setLevel(HttpLoggingInterceptor.Level.BODY)//设置打印数据的级别
                )
                .addInterceptor(new Interceptor() {

                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("api-key", OneNetApi.getAppKey())
                                .method(original.method(), original.body());

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(60, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(60, TimeUnit.SECONDS)//设置写入超时时间
                .build();

        // Gson
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setLenient().serializeNulls().create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        api = builder.build();
    }
}
