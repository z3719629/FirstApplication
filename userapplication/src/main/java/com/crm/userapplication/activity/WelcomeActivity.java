package com.crm.userapplication.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crm.userapplication.R;
import com.crm.userapplication.activity.base.BaseActivity;
import com.crm.userapplication.databinding.ActivityWelcomeBinding;
import com.crm.userapplication.presenter.base.IBasePresenter;
import com.crm.userapplication.rxbus.Events;
import com.crm.userapplication.rxbus.RxBus;

import org.xutils.ex.DbException;

import io.reactivex.annotations.NonNull;

public class WelcomeActivity extends BaseActivity {

    private ActivityWelcomeBinding mBinding;

    private Thread t;

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) throws DbException {

        //设置为无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置为全屏模式
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // activity切换使用动画
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);


        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);

        mBinding.progressBar.setProgress(0);
        mBinding.progressBar.setMax(100);

        t = new Thread(new Runnable() {

            boolean flag = true;

            @Override
            public void run() {
                ProgressBar progressBar = mBinding.progressBar;
                while(flag) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBar.setProgress(progressBar.getProgress()+10);

                    if(progressBar.getProgress() == progressBar.getMax()) {
                        flag = false;
                        RxBus.getInstance().send(Events.EVENT_JUMP, "");
                    }
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        t.start();
    }

    @Override
    protected IBasePresenter getP() {
        return null;
    }

    @Override
    protected void rxBusEventProcess(@NonNull Events events) throws Exception {
        if(Events.EVENT_JUMP == events.getCode()) {
            Toast.makeText(WelcomeActivity.this,"加载完毕",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.default_anim_in, R.anim.default_anim_out);
            WelcomeActivity.this.finish();
        }
    }

}
