package com.crm.userapplication.presenter;


import com.crm.userapplication.contract.LoginContract;
import com.crm.userapplication.presenter.base.BasePresenter;
import com.crm.userapplication.rxbus.Events;
import com.crm.userapplication.rxbus.RxBus;

import org.xutils.ex.DbException;

/**
 * Created by Administrator on 2017/12/29.
 */
public class LoginPresenter extends BasePresenter<LoginContract.ILoginView> implements LoginContract.ILoginPresenter {

    public LoginPresenter(LoginContract.ILoginView iView) {
        super(iView);
    }

    @Override
    public void processLogin(String userName, String userPassword) throws DbException {
        RxBus.getInstance().send(this, Events.EVENT_TAP, "用户名：" + userName + " 密码：" + userPassword);
        if(this.doProcessLogin(userName, userPassword)) {
            RxBus.getInstance().send(this, Events.EVENT_TAP, "登录成功");
        } else {
            RxBus.getInstance().send(this, Events.EVENT_TAP, "登录失败");
        }
    }

    private boolean doProcessLogin(String userName, String userPassword) throws DbException {
        boolean flag = true;

        return flag;
    }
}
