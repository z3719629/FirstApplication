package com.crm.firstapplication.presenter;

import android.view.View;

import com.crm.firstapplication.RxBus;
import com.crm.firstapplication.activity.IBaseView;
import com.crm.firstapplication.contract.LoginContract;
import com.crm.firstapplication.dao.DBManager;
import com.crm.firstapplication.events.Events;
import com.crm.firstapplication.vo.User;

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
        RxBus.getInstance().send(Events.TAP, "用户名：" + userName + " 密码：" + userPassword);
        if(this.doProcessLogin(userName, userPassword)) {
            RxBus.getInstance().send(Events.TAP, "登录成功");
        } else {
            RxBus.getInstance().send(Events.TAP, "登录失败");
        }
    }

    public boolean doProcessLogin(String userName, String userPassword) throws DbException {
        boolean flag = false;

        User user = DBManager.findById(User.class, 1);


        if(user!= null && user.getName().equals(userName) && user.getPassword().equals(userPassword)) {
            flag = true;
        }

        return flag;
    }
}
