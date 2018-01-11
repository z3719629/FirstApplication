package com.crm.userapplication.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.crm.userapplication.R;
import com.crm.userapplication.activity.LoginActivity;
import com.crm.userapplication.adapter.PageingViewAdapter;
import com.crm.userapplication.adapter.RecyclerViewAdapter;
import com.crm.userapplication.adapter.SimplePaddingDecoration;
import com.crm.userapplication.contract.BaseContract;
import com.crm.userapplication.data.BaseDataManager;
import com.crm.userapplication.data.PagingDataManager;
import com.crm.userapplication.databinding.FragmentLoginBinding;
import com.crm.userapplication.data.DataLoadingSubject;
import com.crm.userapplication.listener.InfiniteScrollListener;
import com.crm.userapplication.rxbus.Events;
import com.crm.userapplication.rxbus.RxBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/4.
 */
public class LoginTabOneFragment extends BaseFragment {

    private FragmentLoginBinding mBinding;

    private GestureDetector mGestureDetector;

    private PageingViewAdapter mRecycleAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_login, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView(mBinding.recyclerView);
        initSwipeRefresh(mBinding.layoutSwipeRefresh);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == ContextMenu.FIRST + 1) {

            AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            RxBus.getInstance().send(this, Events.EVENT_TAP, item.getTitle().toString() + menuInfo.position);
        }
        return super.onContextItemSelected(item);
    }

    private void initRecyclerView(RecyclerView recyclerView) {

        List<String> l = new ArrayList();
        for(String s : this.lvs) {
            l.add(s);
        }

        // 可以不写
        // registerForContextMenu(recyclerView);
        //设置菜单弹出事件
//        recyclerView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                menu.add(0, ContextMenu.FIRST+1, 0, "删除");
//            }
//        });

        mRecycleAdapter = new PageingViewAdapter<String, LoginActivity>((LoginActivity) getActivity(), l, false, true);

        final PagingDataManager mDataManager = new PagingDataManager(getContext()) {

            @Override
            protected void loadData(int page) {
                mRecycleAdapter.dataStartedLoading();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onDataLoaded("aaaaaa");
                    }
                }, 2000);
            }

            @Override
            public void onDataLoaded(Object data) {
                mRecycleAdapter.getmDatas().add(data);
                mRecycleAdapter.dataFinishedLoading();
                super.onDataLoaded(data);
            }
        };

        mDataManager.registerCallback(mRecycleAdapter);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);

        if(mRecycleAdapter.isHaveHeader()) {
            // 滚动到1位置
            layoutManager.scrollToPositionWithOffset(1, 0);
        }

        //layoutManager.setStackFromEnd(true);
        //设置分隔线
        //recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator( new DefaultItemAnimator());

        recyclerView.setAdapter(mRecycleAdapter);
        //recyclerView.setOnItemClickListener(this);

        recyclerView.addItemDecoration(new SimplePaddingDecoration(getActivity()));

        recyclerView.addOnScrollListener(new InfiniteScrollListener(layoutManager, mDataManager) {
            @Override
            public void onLoadMore(DataLoadingSubject dataLoadingSubject) {
                mDataManager.loadData();
            }
        });
    }

    private void initSwipeRefresh(SwipeRefreshLayout mSwipeRefreshLayout) {
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        this.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RxBus.getInstance().send(LoginTabOneFragment.this, Events.EVENT_UPDATE_WIDGET, 1000);
            }
        });
    }

    @Override
    protected void rxBusEventProcess(Events events) {
        if(!events.getTarget().equals(this)) {
            return;
        }
        if(events.getCode() == Events.EVENT_UPDATE_WIDGET) {
            mRecycleAdapter.notifyDataSetChanged();
            this.mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void rxBusDoNext(Events events) throws Exception {
        if(!events.getTarget().equals(this)) {
            return;
        }
        if(events.getCode() == Events.EVENT_UPDATE_WIDGET) {
            Thread.sleep((int)events.getContent().get("content"));
        }
    }

    @Override
    protected BaseContract.IBasePresenter getP() {
        return null;
    }
}
