package com.crm.userapplication.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

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
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/1/11.
 */
public class RetrofitUtils {
    public final String MICAL_PREF = "common_pref";
    public final String KEY_ACCESS_TOKEN = "access_token";

    private static volatile RetrofitUtils singleton;
    private final SharedPreferences prefs;
    private String accessToken;
    private boolean isLoggedIn = false;
    private Retrofit api;

    public static RetrofitUtils get(Context context) {
        if (singleton == null) {
            synchronized (RetrofitUtils.class) {
                singleton = new RetrofitUtils(context);
            }
        }
        return singleton;
    }

    private RetrofitUtils(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(MICAL_PREF, Context
                .MODE_PRIVATE);
        accessToken = prefs.getString(KEY_ACCESS_TOKEN, null);
        isLoggedIn = !TextUtils.isEmpty(accessToken);
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
                                .header("Authorization", getAccessToken())
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
                .baseUrl(APIService.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson));

        api = builder.build();
    }

    private String getAccessToken() {
        return !TextUtils.isEmpty(accessToken) ? accessToken
                : "";
    }
}
