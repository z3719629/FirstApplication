package com.crm.userapplication.contract;


import com.crm.mylibrary.contract.BaseContract;

import org.xutils.ex.DbException;

/**
 * Created by Administrator on 2017/12/29.
 */
public interface LoginContract {
    interface ILoginView {

    }

    interface ILoginPresenter extends BaseContract.IBasePresenter {
        void processLogin(String userName, String userPassword) throws DbException;
    }
}
