package com.crm.onenetcontroller.data;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.crm.mylibrary.adapter.RecyclerViewAdapter;
import com.crm.mylibrary.adapter.RecyclerViewOperateHolder;
import com.crm.mylibrary.data.BaseDataManager;

/**
 * Created by Administrator on 2018/1/11.
 */
public abstract class PagingDataManager extends BaseDataManager {

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerViewOperateHolder recyclerViewOperateHolder;

    public void setRecyclerViewOperateHolder(RecyclerViewOperateHolder recyclerViewOperateHolder) {
        this.recyclerViewOperateHolder = recyclerViewOperateHolder;
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public PagingDataManager(@NonNull Context context) {
        super(context);
    }

    @Override
    public void onDataLoaded(Object data) {
        Log.i("iiiiiiiiiiiiiii", "MainThread : " + String.valueOf(Looper.getMainLooper() == Looper.myLooper()));
        loadFinished();
        this.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void cancelLoading() {

    }

    private int page = 0;
    protected boolean moreDataAvailable = true;

    public void setMoreDataAvailable(boolean isHaveMoreData) {
        this.moreDataAvailable = isHaveMoreData;
    }

    public void reLoadData() {
        page = 0;
        moreDataAvailable = true;
        recyclerViewOperateHolder.clearData();
        loadData();
    }

    public void loadData() {
        if (!moreDataAvailable) return;
        page++;
        loadStarted();
        loadData(page);
    }

    protected abstract void loadData(int page);
}
