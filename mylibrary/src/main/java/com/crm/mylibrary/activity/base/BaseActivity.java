package com.crm.mylibrary.activity.base;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.crm.mylibrary.contract.BaseContract;
import com.crm.mylibrary.rxbus.Events;
import com.crm.mylibrary.rxbus.RxBus;
import com.crm.mylibrary.sqllite.DBManager;
import com.crm.mylibrary.util.ZUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.xutils.ex.DbException;

import io.reactivex.android.schedulers.AndroidSchedulers;
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
                .compose(this.<Events<BaseContract.BaseInterface>>bindToLifecycle())
                .subscribe(new Consumer<Events<BaseContract.BaseInterface>>() {
                    @Override
                    public void accept(@NonNull Events<BaseContract.BaseInterface> events) throws Exception {
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

    public ZUtil getUtil() {
        return util;
    }

    public Vibrator getmVibrator() {
        return mVibrator;
    }
}
