package com.crm.userapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.crm.userapplication.contract.BaseContract;
import com.crm.userapplication.rxbus.Events;
import com.crm.userapplication.rxbus.RxBus;
import com.crm.userapplication.util.ZUtil;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/25.
 */
public abstract class BaseFragment<T extends BaseContract.IBasePresenter> extends RxFragment implements BaseContract.IBaseView {

    protected ZUtil util;

    protected T presenter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // RxBus
        RxBus.getInstance().getBus()
                .subscribeOn(Schedulers.io())
                .compose(this.<Events<BaseContract.BaseInterface>>bindToLifecycle())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<Events<BaseContract.BaseInterface>>() {
                    @Override
                    public void accept(@NonNull Events<BaseContract.BaseInterface> events) throws Exception {
                        rxBusDoNext(events);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Events<BaseContract.BaseInterface>>() {
                    @Override
                    public void accept(@NonNull Events<BaseContract.BaseInterface> events) throws Exception {
                        rxBusEventProcess(events);
                    }
                });

        // ZUtil
        this.util = ZUtil.getInstance();
        this.util.setContext(getContext());

    }

    protected abstract void rxBusEventProcess(Events<BaseContract.BaseInterface> events);

    protected abstract void rxBusDoNext(Events<BaseContract.BaseInterface> events) throws Exception;

    protected abstract T getP();

}
