package com.crm.onenetcontroller.fragment;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chinamobile.iot.onenet.module.Device;
import com.chinamobile.iot.onenet.module.Mqtt;
import com.crm.mylibrary.adapter.RecyclerViewAdapter;
import com.crm.mylibrary.adapter.SimplePaddingDecoration;
import com.crm.mylibrary.contract.BaseContract;
import com.crm.mylibrary.data.DataLoadingSubject;
import com.crm.mylibrary.fragment.base.BaseFragment;
import com.crm.mylibrary.listener.InfiniteScrollListener;
import com.crm.mylibrary.rxbus.Events;
import com.crm.mylibrary.rxbus.RxBus;
import com.crm.onenetcontroller.R;
import com.crm.onenetcontroller.activity.DSItemViewActivity;
import com.crm.onenetcontroller.activity.DeviceActivity;
import com.crm.onenetcontroller.activity.MainActivity;
import com.crm.onenetcontroller.adapter.PageingViewAdapter;
import com.crm.onenetcontroller.data.PagingDataManager;
import com.crm.onenetcontroller.databinding.FragmentDeviceBinding;
import com.crm.onenetcontroller.databinding.FragmentMainBinding;
import com.crm.onenetcontroller.onenet.APIService;
import com.crm.onenetcontroller.onenet.DSItem;
import com.crm.onenetcontroller.onenet.DeviceItem;
import com.crm.onenetcontroller.onenet.OneNetRetrofitUtils;
import com.crm.onenetcontroller.onenet.ResData;
import com.crm.onenetcontroller.onenet.utils.DeviceItemDeserializer;
import com.crm.onenetcontroller.onenet.utils.GsonConvertUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/1/4.
 */
public class DeviceTabOneFragment extends BaseFragment {

    private FragmentDeviceBinding mBinding;

    private String deviceId;

    private PageingViewAdapter<DSItem, DeviceActivity> mRecycleAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private PagingDataManager mDataManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_device, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.deviceId = ((DeviceActivity)getActivity()).getDeviceId();
        initSwipeRefresh(mBinding.layoutSwipeRefresh);
        initRecyclerView(mBinding.recyclerView);
    }

    private void initRecyclerView(RecyclerView recyclerView) {

        final List<DSItem> l = new ArrayList<>();

        mRecycleAdapter = new PageingViewAdapter<DSItem, DeviceActivity>((DeviceActivity) getActivity(), l, false, true) {
            @Override
            public void editData(RecyclerView.ViewHolder holderOld, DSItem data, int position) {
                final RecycleViewHolder holder = (RecycleViewHolder)holderOld;

                if(holder.getViewType() == RecyclerViewAdapter.TYPE_BODY) {

                    final TextView dsId = holder.getView(R.id.ds_id);
                    final TextView dsUnit = holder.getView(R.id.ds_unit);
                    final TextView dsUnitSymbol = holder.getView(R.id.ds_unit_symbol);

                    dsId.setText("数据点id : " + data.getId());
                    dsUnit.setText("数据点单位 : " + data.getUnit());
                    dsUnitSymbol.setText("数据点符号 : " + data.getUnitSymbol());

                } else if(holder.getViewType() == RecyclerViewAdapter.TYPE_FOOTER) {
                    TextView tv = holder.getView(R.id.textViewFooter);
                    ProgressBar progressBar = holder.getView(R.id.footerProgressBar);
                    if(isDataLoading()) {
                        progressBar.setVisibility(View.VISIBLE);
                        tv.setText("读取中");
                    } else {
                        progressBar.setVisibility(View.GONE);
                        tv.setText("已经是最后一条数据了");
                    }

                } else if(holder.getViewType() == RecyclerViewAdapter.TYPE_HEADER) {
                    final TextView tv = holder.getView(R.id.textViewHeader);
                    tv.setText("Header");
                    //final ProgressBar progressBar = holder.getView(R.id.headerProgressBar);

                }
            }

            @Override
            public int getLayoutId(int viewType) {
                if(viewType == RecyclerViewAdapter.TYPE_BODY) {
                    return R.layout.recycle_view_item_device;
                } else if(viewType == RecyclerViewAdapter.TYPE_FOOTER) {
                    return R.layout.recycle_view_item_footer;
                } else if(viewType == RecyclerViewAdapter.TYPE_HEADER){
                    return R.layout.recycle_view_item_header;
                } else {
                    return -1;
                }
            }

            @Override
            public void doOnTouchActionUpSub(View v, MotionEvent event, DSItem data) {
                // 跳转到详细页面 并传递参数
                ((DeviceActivity)getActivity()).getUtil().startNewActivity(getActivity(), DSItemViewActivity.class, false, data, ((DeviceActivity) getActivity()).getDeviceId());
            }
        };

        mDataManager = new PagingDataManager(getContext()) {

            @Override
            protected void loadData(final int page) {
                String url = Device.urlForQueryingSingle(deviceId);
                Observable<ResponseBody> observable = OneNetRetrofitUtils.get().getRetrofit().create(APIService.class).getByUrl(url);
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResponseBody>() {
                            @Override
                            public void accept(@NonNull ResponseBody responseBody) throws Exception {
                                DeviceItem deviceItem = GsonConvertUtil.convert(responseBody.string(), DeviceItem.class);
                                mRecycleAdapter.clearData();
                                for (DSItem dsItem : deviceItem.getDatastreams()) {
                                    onDataLoaded(dsItem);
                                }
                                onDataLoaded(null);
                            }
                        });
            }

            @Override
            public void onDataLoaded(Object data) {
                if(data != null) {
                    mRecycleAdapter.insertData((DSItem) data);
                }
                super.onDataLoaded(data);
            }
        };

        mDataManager.registerCallback(mRecycleAdapter);

        // 绑定下拉刷新
        mDataManager.setSwipeRefreshLayout(this.mSwipeRefreshLayout);
        // 绑定数据操作类
        mDataManager.setRecyclerViewOperateHolder(mRecycleAdapter);

        mDataManager.reLoadData();

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
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mRecycleAdapter);
        //recyclerView.setOnItemClickListener(this);

        recyclerView.addItemDecoration(new SimplePaddingDecoration(getActivity()));

//        recyclerView.addOnScrollListener(new InfiniteScrollListener(layoutManager, mDataManager) {
//            @Override
//            public void onLoadMore(DataLoadingSubject dataLoadingSubject) {
//                mDataManager.loadData();
//            }
//        });
    }

    private void initSwipeRefresh(SwipeRefreshLayout mSwipeRefreshLayout) {
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        this.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RxBus.getInstance().send(DeviceTabOneFragment.this, Events.EVENT_UPDATE_WIDGET, 100);
            }
        });
    }

    @Override
    protected void rxBusEventProcess(Events events) {
        if(!events.getTarget().equals(this)) {
            return;
        }
        if(events.getCode() == Events.EVENT_UPDATE_WIDGET) {
            mDataManager.reLoadData();
            // 应该在加载完成时关闭
            //this.mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void rxBusDoNext(Events events) throws Exception {
        if(!events.getTarget().equals(this)) {
            return;
        }
        if(events.getCode() == Events.EVENT_UPDATE_WIDGET) {
            // 延时
            Thread.sleep((int)events.getContent().get("content"));
        }
    }

    @Override
    protected BaseContract.IBasePresenter getP() {
        return null;
    }
}
