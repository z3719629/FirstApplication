package com.crm.userapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

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
public abstract class BaseFragment extends RxFragment {

    protected ZUtil util;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // ZUtil
        this.util = ZUtil.getInstance();
        this.util.setContext(getContext());

    }

}
