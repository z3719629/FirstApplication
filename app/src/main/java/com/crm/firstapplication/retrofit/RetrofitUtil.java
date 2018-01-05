package com.crm.firstapplication.retrofit;

import android.util.Log;

import com.crm.firstapplication.RxBus;
import com.crm.firstapplication.events.Events;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

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
 * Created by Administrator on 2017/12/27.
 */
public class RetrofitUtil {
    /**
     * 服务器地址
     */
    private static final String API_HOST ="http://10.0.2.2:8082";
    private RetrofitUtil() {

    }
    public static Retrofit getRetrofit() {
        return Instanace.retrofit;
    }
    //静态内部类,保证单例并在调用getRetrofit方法的时候才去创建.
    private static class Instanace {
        private static final Retrofit retrofit = getInstanace();
        private static Retrofit getInstanace() {

            OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(60, TimeUnit.SECONDS)//设置写入超时时间
                    .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            Log.w("TAG", "log: " + message );
                            RxBus.getInstance().send(Events.TAP, message);
                        }
                    }).setLevel(HttpLoggingInterceptor.Level.BODY)//设置打印数据的级别
                    )//添加其他拦截器
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Interceptor.Chain chain) throws IOException {
                            Request mRequest=chain.request();
                            //在这里你可以做一些想做的事,比如token失效时,重新获取token
                            //或者添加header等等,PS我会在下一篇文章总写拦截token方法
                            // http://blog.csdn.net/yrmao9893/article/details/69791519
                            return chain.proceed(mRequest);
                        }
                    })//添加日志拦截器
                    .build();

            // Gson
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").setLenient().serializeNulls().create();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(mOkHttpClient)
                    .baseUrl(API_HOST)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            return retrofit;
        }
    }
}
