package com.crm.mylibrary.data;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by Administrator on 2018/1/11.
 */
public abstract class PagingDataManager extends BaseDataManager {
    public PagingDataManager(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onDataLoaded(Object data) {
        loadFinished();
    }

    @Override
    public void cancelLoading() {

    }

    private int page = 0;
    protected boolean moreDataAvailable = true;

    public void loadData() {
        if (!moreDataAvailable) return;
        page++;
        loadStarted();
        loadData(page);
    }

    protected abstract void loadData(int page);
}
