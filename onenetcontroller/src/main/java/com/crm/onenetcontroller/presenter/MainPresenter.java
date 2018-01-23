package com.crm.onenetcontroller.presenter;


import com.crm.mylibrary.presenter.base.BasePresenter;
import com.crm.mylibrary.rxbus.Events;
import com.crm.mylibrary.rxbus.RxBus;
import com.crm.onenetcontroller.contract.MainContract;

import org.xutils.ex.DbException;

/**
 * Created by Administrator on 2017/12/29.
 */
public class MainPresenter extends BasePresenter<MainContract.IMainView> implements MainContract.IMainPresenter {

    public MainPresenter(MainContract.IMainView iView) {
        super(iView);
    }

}
