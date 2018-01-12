package com.crm.userapplication.activity.base;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;

import com.crm.userapplication.contract.BaseContract;
import com.crm.userapplication.rxbus.Events;
import com.crm.userapplication.rxbus.RxBus;
import com.crm.userapplication.sqllite.DBManager;
import com.crm.userapplication.util.ZUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.xutils.ex.DbException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/28.
 */
public abstract class BaseActivity<T extends BaseContract.IBasePresenter> extends RxAppCompatActivity implements BaseContract.IBaseView {

    protected ZUtil util;

    protected T presenter;

    protected Vibrator mVibrator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        DBManager.initDb();

        // ZUtil
        this.util = ZUtil.getInstance();
        this.util.setContext(this);

        this.presenter = getP();

        try {
            this.initCreate(savedInstanceState);
        } catch (DbException e) {
            e.printStackTrace();
        }

        // 震动
        mVibrator = (Vibrator)getApplication().getSystemService(Service.VIBRATOR_SERVICE);

    }

    protected abstract void rxBusEventProcess(@NonNull Events<?> events) throws Exception;

    protected abstract void initCreate(@Nullable Bundle savedInstanceState) throws DbException;

    protected abstract T getP();

    public Vibrator getmVibrator() {
        return mVibrator;
    }
}
