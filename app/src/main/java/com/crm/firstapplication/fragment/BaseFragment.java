package com.crm.firstapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crm.firstapplication.RxBus;
import com.crm.firstapplication.databinding.MainContentBinding;
import com.crm.firstapplication.events.Events;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/25.
 */
public abstract class BaseFragment extends RxFragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // RxBus
        RxBus.getInstance().getBus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Events<?>>bindToLifecycle())
                .subscribe(new Consumer<Events<?>>() {
                    @Override
                    public void accept(@NonNull Events<?> events) throws Exception {
                        rxBusEventProcess(events);
                    }
                });
    }

    protected abstract void rxBusEventProcess(@NonNull Events<?> events) throws Exception;

}
