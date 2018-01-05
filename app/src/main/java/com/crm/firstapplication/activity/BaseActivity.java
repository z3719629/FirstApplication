package com.crm.firstapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.crm.firstapplication.RxBus;
import com.crm.firstapplication.dao.DBManager;
import com.crm.firstapplication.events.Events;
import com.crm.firstapplication.presenter.BasePresenter;
import com.crm.firstapplication.presenter.IBasePresenter;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.xutils.ex.DbException;
import org.xutils.x;

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
