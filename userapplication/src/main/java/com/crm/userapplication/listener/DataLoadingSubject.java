package com.crm.userapplication.listener;

/**
 * Created by Administrator on 2018/1/8.
 */
public interface DataLoadingSubject {
    boolean isDataLoading();
    void registerCallback(DataLoadingCallbacks callBack);
    void unregistereCallBack(DataLoadingCallbacks callBack);
    interface DataLoadingCallbacks{
        void dataStartedLoading();
        void dataFinishedLoading();
    }
}
