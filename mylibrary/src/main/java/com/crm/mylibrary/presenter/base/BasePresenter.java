package com.crm.mylibrary.presenter.base;


import com.crm.mylibrary.contract.BaseContract;

/**
 * Created by Administrator on 2017/12/29.
 */
public class BasePresenter<T> implements BaseContract.IBasePresenter {

    protected T iView;

    public BasePresenter(T iView) {
        this.iView = iView;
    }

    @Override
    public BaseContract.IBasePresenter getP() {
        return this;
    }
}
