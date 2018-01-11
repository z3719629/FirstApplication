package com.crm.userapplication.data;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2018/1/11.
 */
public abstract class BaseDataManager<T> implements DataLoadingSubject {

    //计数器
    private final AtomicInteger loadingCount;
    private final RetrofitUtils retrofitUtils;
    //数据加载情况的回调接口集合
    private List<DataLoadingCallbacks> loadingCallbacks;

    private APIService apiService;

    public BaseDataManager(@NonNull Context context) {
        //初始化
        retrofitUtils = RetrofitUtils.get(context);
        loadingCount = new AtomicInteger(0);
    }

    //数据加载完成的回调接口
    public abstract void onDataLoaded(T data);
    //取消数据加载的接口
    public abstract void cancelLoading();

    @Override
    public boolean isDataLoading() {
        return loadingCount.get() > 0;
    }

    public RetrofitUtils getMicalPrefs() {
        return retrofitUtils;
    }

    //相当于实现一个懒加载
    public APIService getApiService() {
        if (apiService == null) {
            apiService = createAPIService();
        }
        return apiService;
    }

    private APIService createAPIService() {
        return retrofitUtils.getRetrofit().create(APIService.class);
    }

    //注册回调接口
    @Override
    public void registerCallback(DataLoadingCallbacks callback) {
        if (loadingCallbacks == null) {
            loadingCallbacks = new ArrayList<>(1);
        }
        loadingCallbacks.add(callback);
    }

    @Override
    public void unregisterCallBack(DataLoadingCallbacks callback) {
        if (loadingCallbacks != null && loadingCallbacks.contains(callback)) {
            loadingCallbacks.remove(callback);
        }
    }

    //分发开始加载数据的回调接口
    protected void loadStarted() {
        if (0 == loadingCount.getAndIncrement()) {
            dispatchLoadingStartedCallbacks();
        }
    }

    //分发开始加载完成的回调接口
    protected void loadFinished() {
        if (0 == loadingCount.decrementAndGet()) {
            dispatchLoadingFinishedCallbacks();
        }
    }

    //重置
    protected void resetLoadingCount() {
        loadingCount.set(0);
    }

    protected void dispatchLoadingStartedCallbacks() {
        if (loadingCallbacks == null || loadingCallbacks.isEmpty()) return;
        for (DataLoadingCallbacks loadingCallback : loadingCallbacks) {
            loadingCallback.dataStartedLoading();
        }
    }

    protected void dispatchLoadingFinishedCallbacks() {
        if (loadingCallbacks == null || loadingCallbacks.isEmpty()) return;
        for (DataLoadingCallbacks loadingCallback : loadingCallbacks) {
            loadingCallback.dataFinishedLoading();
        }
    }
}
