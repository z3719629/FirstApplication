package com.crm.firstapplication.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ProgressBar;
import android.widget.Toast;

import com.crm.firstapplication.R;
import com.crm.firstapplication.RxBus;
import com.crm.firstapplication.databinding.ActivityWelcomeBinding;
import com.crm.firstapplication.events.Events;
import com.crm.firstapplication.presenter.IBasePresenter;

import org.xutils.ex.DbException;
import io.reactivex.annotations.NonNull;

public class WelcomeActivity extends BaseActivity {

    private ActivityWelcomeBinding mBinding;

    private Thread t;



    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) throws DbException {
        //setContentView(R.layout.activity_welcome);

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


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition explode = TransitionInflater.from(this).inflateTransition(R.transition.explode);
            //退出时使用
            getWindow().setExitTransition(explode);
            //第一次进入时使用
            getWindow().setEnterTransition(explode);
            //再次进入时使用
            getWindow().setReenterTransition(explode);
        }

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
                        RxBus.getInstance().send(Events.JUMP, "");
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
        if(Events.JUMP == events.getCode()) {
            Toast.makeText(WelcomeActivity.this,"加载完毕",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.default_anim_in, R.anim.default_anim_out);
            WelcomeActivity.this.finish();
        }
    }

}
