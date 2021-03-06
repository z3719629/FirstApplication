package com.crm.onenetcontroller.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.chinamobile.iot.onenet.module.ApiKey;
import com.chinamobile.iot.onenet.module.Device;
import com.chinamobile.iot.onenet.module.Mqtt;
import com.crm.mylibrary.adapter.RecyclerViewAdapter;
import com.crm.mylibrary.adapter.SimplePaddingDecoration;
import com.crm.mylibrary.contract.BaseContract;
import com.crm.mylibrary.data.DataLoadingSubject;
import com.crm.mylibrary.data.RetrofitUtils;
import com.crm.mylibrary.fragment.base.BaseFragment;
import com.crm.mylibrary.listener.InfiniteScrollListener;
import com.crm.mylibrary.rxbus.Events;
import com.crm.mylibrary.rxbus.RxBus;
import com.crm.onenetcontroller.R;
import com.crm.onenetcontroller.activity.DeviceActivity;
import com.crm.onenetcontroller.activity.MainActivity;
import com.crm.onenetcontroller.adapter.PageingViewAdapter;
import com.crm.onenetcontroller.data.PagingDataManager;
import com.crm.onenetcontroller.databinding.FragmentMainBinding;
import com.crm.onenetcontroller.onenet.APIService;
import com.crm.onenetcontroller.onenet.DeviceItem;
import com.crm.onenetcontroller.onenet.OneNetRetrofitUtils;
import com.crm.onenetcontroller.onenet.utils.DeviceItemDeserializer;
import com.crm.onenetcontroller.onenet.utils.GsonConvertUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/1/4.
 */
public class MainTabOneFragment extends BaseFragment {

    private FragmentMainBinding mBinding;

    private PageingViewAdapter<DeviceItem, MainActivity> mRecycleAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private PagingDataManager mDataManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_main, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initSwipeRefresh(mBinding.layoutSwipeRefresh);
        initRecyclerView(mBinding.recyclerView);
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

        List<DeviceItem> l = new ArrayList<>();

        // 可以不写
        // registerForContextMenu(recyclerView);
        //设置菜单弹出事件
//        recyclerView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                menu.add(0, ContextMenu.FIRST+1, 0, "删除");
//            }
//        });

        mRecycleAdapter = new PageingViewAdapter<DeviceItem, MainActivity>((MainActivity) getActivity(), l, false, true) {
            @Override
            public void doOnTouchActionUpSub(View v, MotionEvent event, DeviceItem data) {
                // 跳转到详细页面 并传递参数
                ((MainActivity)getActivity()).getUtil().startNewActivity(getActivity(), DeviceActivity.class, false, data);
            }

            @Override
            public void editData(RecyclerView.ViewHolder holderOld, DeviceItem data, int position) {
                final RecycleViewHolder holder = (RecycleViewHolder)holderOld;

                if(holder.getViewType() == RecyclerViewAdapter.TYPE_BODY) {

                    final TextView deviceId = holder.getView(R.id.device_id);
                    final TextView deviceCreateTime = holder.getView(R.id.device_create_time);
                    final ImageView deviceOnline = holder.getView(R.id.device_online);
                    final TextView deviceProtocol = holder.getView(R.id.device_protocol);
                    final TextView deviceTitle = holder.getView(R.id.device_title);

                    deviceId.setText("设备编号 : " + data.getId());
                    deviceCreateTime.setText("创建时间 : " + data.getCreateTime());
                    if(data.isOnline()) {
                        deviceOnline.setImageAlpha(250);
                    } else {
                        deviceOnline.setImageAlpha(0);
                    }
                    deviceProtocol.setText("传输协议 : " + data.getProtocol());
                    deviceTitle.setText(data.getTitle());

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

                    if(!isDataLoading() && mRecycleAdapter.getmDatas().isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                        tv.setText("没有数据");
                    }

                } else if(holder.getViewType() == RecyclerViewAdapter.TYPE_HEADER) {
                    final TextView tv = holder.getView(R.id.textViewHeader);
                    tv.setText("Header");
                    //final ProgressBar progressBar = holder.getView(R.id.headerProgressBar);

                }
            }
        };

        mDataManager = new PagingDataManager(getContext()) {

            @Override
            protected void loadData(final int page) {

                final String url = Mqtt.urlForQueryingDevicesByTopic("slimenohouse/controller", page, 1);
                Observable<ResponseBody> observable = OneNetRetrofitUtils.get().getRetrofit().create(APIService.class).getByUrl(url);

                observable
                        .subscribeOn(Schedulers.io())

                        .flatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {
                            @Override
                            public ObservableSource<ResponseBody> apply(@NonNull ResponseBody responseBody) throws Exception {
                                JsonObject resp = new JsonParser().parse(responseBody.string()).getAsJsonObject();
                                int errno = resp.get("errno").getAsInt();
                                String deviceId = null;
                                if (errno == 0) {
                                    JsonArray ja = resp.get("data").getAsJsonObject().get("devices").getAsJsonArray();
                                    for (int i = 0; i < ja.size(); i++) {
                                        deviceId = ja.get(i).getAsString();
                                    }
                                }
                                Observable<ResponseBody> observable2 = null;
                                if(deviceId != null) {
                                    String urlForQuerySingle = Device.urlForQueryingSingle(deviceId);
                                    observable2 = OneNetRetrofitUtils.get().getRetrofit().create(APIService.class).getByUrl(urlForQuerySingle);
                                    return observable2;
                                }

                                if(observable2 == null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            onDataLoaded(null);
                                        }
                                    });
                                }

                                return observable2;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResponseBody>() {
                            @Override
                            public void accept(@NonNull ResponseBody responseBody) throws Exception {
                                DeviceItem deviceItem = GsonConvertUtil.convert(responseBody.string(), DeviceItem.class);
                                onDataLoaded(deviceItem);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                onDataLoaded(null);
                            }
                        });

            }

            @Override
            public void onDataLoaded(Object data) {
                if(data != null) {
                    mRecycleAdapter.insertData((DeviceItem) data);
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
                RxBus.getInstance().send(MainTabOneFragment.this, Events.EVENT_UPDATE_WIDGET, 100);
            }
        });
    }

    @Override
    protected void rxBusEventProcess(Events events) {
        if(!events.getTarget().equals(this)) {
            return;
        }
        if(events.getCode() == Events.EVENT_UPDATE_WIDGET) {
            // 应该在加载完成时关闭
            //this.mSwipeRefreshLayout.setRefreshing(false);
            mDataManager.reLoadData();
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
