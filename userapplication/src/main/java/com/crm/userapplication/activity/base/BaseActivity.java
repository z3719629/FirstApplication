package com.crm.userapplication.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.crm.userapplication.presenter.base.IBasePresenter;
import com.crm.userapplication.rxbus.Events;
import com.crm.userapplication.rxbus.RxBus;
import com.crm.userapplication.sqllite.DBManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.xutils.ex.DbException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/28.
 */
public abstract class BaseActivity<T extends IBasePresenter> extends RxAppCompatActivity implements IBaseView {

    protected T presenter;

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

        this.presenter = getP();

        try {
            this.initCreate(savedInstanceState);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    protected abstract void rxBusEventProcess(@NonNull Events<?> events) throws Exception;

    protected abstract void initCreate(@Nullable Bundle savedInstanceState) throws DbException;

    protected abstract T getP();

}
