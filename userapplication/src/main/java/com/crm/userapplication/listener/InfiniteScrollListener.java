package com.crm.userapplication.listener;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.crm.userapplication.data.DataLoadingSubject;

public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    // 底部还剩下几个的时候开始触发加载更多的回调接口
    private static final int VISIBLE_THRESHOLD = 1;

    private final LinearLayoutManager layoutManager;
    private final DataLoadingSubject dataLoading;

    public InfiniteScrollListener(@NonNull LinearLayoutManager layoutManager,
                                  @NonNull DataLoadingSubject dataLoading) {
        this.layoutManager = layoutManager;
        this.dataLoading = dataLoading;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        // bail out if scrolling upward or already loading data
        if (dy < 0 || dataLoading.isDataLoading()) return;

        final int visibleItemCount = recyclerView.getChildCount();
        final int totalItemCount = layoutManager.getItemCount();
        final int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

        if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {
            onLoadMore(dataLoading);
        }
    }

    public abstract void onLoadMore(DataLoadingSubject dataLoadingSubject);

}
