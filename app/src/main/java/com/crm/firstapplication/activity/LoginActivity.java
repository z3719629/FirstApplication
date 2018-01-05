package com.crm.firstapplication.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.crm.firstapplication.R;
import com.crm.firstapplication.RxBus;
import com.crm.firstapplication.contract.LoginContract;
import com.crm.firstapplication.dao.DBManager;
import com.crm.firstapplication.databinding.ActivityLoginBinding;
import com.crm.firstapplication.events.Events;
import com.crm.firstapplication.model.MUser;

import com.crm.firstapplication.presenter.LoginPresenter;
import com.crm.firstapplication.vo.User;

import org.xutils.ex.DbException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseActivity<LoginContract.ILoginPresenter> implements LoginContract.ILoginView {

    private ActivityLoginBinding mBinding;

    @Override
    protected void rxBusEventProcess(@NonNull Events<?> events) throws Exception {
        Toast.makeText(LoginActivity.this,"LoginActivity提交文本："+events.getContent().toString(),Toast.LENGTH_SHORT).show();
        if(events.getCode() == Events.TAP) {
            if("登录成功".equals(events.getContent().toString())) {
                Intent intent =new Intent(LoginActivity.this, CrmMainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.default_anim_in, R.anim.default_anim_out);
                LoginActivity.this.finish();
            }
        } else if(events.getCode() == Events.JUMP) {
            Toast.makeText(LoginActivity.this,"LoginActivity JUMP："+events.getContent().toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) throws DbException {
        //setContentView(R.layout.activity_login);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        User u = new User();
        u.setName("admin");
        u.setAge(11);
        u.setPassword("admin");
        DBManager.dbInsert(u);

        MUser user = new MUser();
        user.userTitle.set("title");
        user.userName.set("admin");
        user.userPassword.set("admin");
        user.loginButtonName.set("登录");
        mBinding.setModelUser(user);

    }

    @Override
    protected LoginContract.ILoginPresenter getP() {
        return new LoginPresenter(this);
    }

    public void onLoginButtonClick(View view) throws DbException {
        MUser user = mBinding.getModelUser();
        this.presenter.processLogin(user.userName.get(), user.userPassword.get());
    }

}
