package com.crm.firstapplication.rxandroid.impl;

import com.crm.firstapplication.retrofit.RetrofitUtil;
import com.crm.firstapplication.rxandroid.APIService;

/**
 * 请求生成类。Retrofit一次生成,并作为单例.
 */
public class APIServiceImpl {
    private APIServiceImpl() {

    }
    public static APIService getInstance() {
        return createAPIService.apiService;
    }

    /**
     * Retrofit生成接口对象.
     */
    private static class createAPIService {
        //Retrofit会根据传入的接口类.生成实例对象.
        private static final APIService apiService = RetrofitUtil.getRetrofit().create(APIService.class);
    }
}
