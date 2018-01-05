package com.crm.firstapplication.contract;

import android.view.View;

import com.crm.firstapplication.activity.IBaseView;
import com.crm.firstapplication.presenter.IBasePresenter;

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
