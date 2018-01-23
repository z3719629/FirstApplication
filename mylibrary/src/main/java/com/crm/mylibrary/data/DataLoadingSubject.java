package com.crm.mylibrary.data;

/**
 * Created by Administrator on 2018/1/8.
 */
public interface DataLoadingSubject {
    boolean isDataLoading();
    void registerCallback(DataLoadingCallbacks callBack);
    void unregisterCallBack(DataLoadingCallbacks callBack);
    interface DataLoadingCallbacks{
        void dataStartedLoading();
        void dataFinishedLoading();
    }
}
