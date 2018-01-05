package com.crm.userapplication.contract;


import com.crm.userapplication.activity.base.IBaseView;
import com.crm.userapplication.presenter.base.IBasePresenter;

import org.xutils.ex.DbException;

/**
 * Created by Administrator on 2017/12/29.
 */
public interface LoginContract {
    interface ILoginView extends IBaseView {

    }

    interface ILoginPresenter extends IBasePresenter {
        void processLogin(String userName, String userPassword) throws DbException;
    }
}
