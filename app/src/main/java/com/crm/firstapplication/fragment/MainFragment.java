package com.crm.firstapplication.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.crm.firstapplication.R;
import com.crm.firstapplication.RxBus;
import com.crm.firstapplication.databinding.MainContentBinding;
import com.crm.firstapplication.events.Events;

import java.util.HashMap;

import com.crm.firstapplication.model.MMainFragment;
import com.crm.firstapplication.rxandroid.impl.APIServiceImpl;
import com.crm.firstapplication.vo.PanelsVO;
import com.crm.firstapplication.vo.ResVO;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/25.
 */
public class MainFragment extends BaseFragment implements View.OnClickListener {

    private MainContentBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.main_content, container, false);
        MMainFragment mMainFragment = new MMainFragment();
        mMainFragment.textView.set("textView");
        mMainFragment.editText.set("editText");
        mMainFragment.buttonName.set("Button");
        mBinding.setMainFragment(mMainFragment);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBinding.button.setOnClickListener(this);
    }

    @Override
    protected void rxBusEventProcess(@NonNull Events<?> events) throws Exception {
        Snackbar.make(mBinding.textView,"Click Share",Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        TextView textView = mBinding.textView;
        EditText editText = mBinding.editText;
        RxBus.getInstance().send(Events.TAP, editText.getText());
        textView.setText(editText.getText());

        HashMap<String, String> hashMap = new HashMap<String, String>();
        APIServiceImpl.getInstance().login(hashMap) //传入参数
                .compose(this.<ResVO<PanelsVO>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResVO<PanelsVO>>() {//简单的回调

                    @Override
                    public void accept(@NonNull ResVO<PanelsVO> o) throws Exception {
                        RxBus.getInstance().send(Events.TAP, o.getResult().getMessage());
                    }

                });

//        APIServiceImpl.getInstance().url("http://www.baidu.com") //传入参数
//                .compose(this.<ResponseBody>bindToLifecycle())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<ResponseBody>() {//简单的回调
//
//                    @Override
//                    public void accept(@NonNull ResponseBody responseBody) throws Exception {
//                        RxBus.getInstance().send(Events.TAP, responseBody);
//                    }
//                });

    }
}
