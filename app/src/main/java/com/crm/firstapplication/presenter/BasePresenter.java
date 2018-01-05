package com.crm.firstapplication.presenter;

import com.crm.firstapplication.activity.IBaseView;

/**
 * Created by Administrator on 2017/12/29.
 */
public class BasePresenter<T extends IBaseView> implements IBasePresenter {

    protected T iView;

    public BasePresenter(T iView) {
        this.iView = iView;
    }

    @Override
    public IBasePresenter getP() {
        return this;
    }
}
