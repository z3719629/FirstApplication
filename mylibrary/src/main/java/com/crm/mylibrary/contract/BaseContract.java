package com.crm.mylibrary.contract;

/**
 * Created by Administrator on 2018/1/8.
 */
public interface BaseContract {

    interface BaseInterface {

    }

    interface IBaseView extends BaseInterface {

    }

    interface IBasePresenter extends BaseInterface {
        IBasePresenter getP();
    }
}
