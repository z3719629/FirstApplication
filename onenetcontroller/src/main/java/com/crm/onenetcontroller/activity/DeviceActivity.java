package com.crm.onenetcontroller.activity;

import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.chinamobile.iot.onenet.module.DataPoint;
import com.crm.mylibrary.activity.base.BaseActivity;
import com.crm.mylibrary.contract.BaseContract;
import com.crm.mylibrary.rxbus.Events;
import com.crm.mylibrary.view.ChangeColorIconWithTextView;
import com.crm.onenetcontroller.R;
import com.crm.onenetcontroller.databinding.ActivityDeviceBinding;
import com.crm.onenetcontroller.databinding.ActivityMainBinding;
import com.crm.onenetcontroller.fragment.MainTabOneFragment;
import com.crm.onenetcontroller.listener.TabItemOnTouchListener;
import com.crm.onenetcontroller.onenet.DeviceItem;

import org.xutils.ex.DbException;

public class DeviceActivity extends BaseActivity {

    private ActivityDeviceBinding mBinding;
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    @Override
    protected void rxBusEventProcess(@NonNull Events events) throws Exception {

    }

    @Override
    protected void initCreate(@Nullable Bundle savedInstanceState) throws DbException {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_device);
        Intent intent = getIntent();
        DeviceItem deviceItem = (DeviceItem)intent.getSerializableExtra("data0");
        this.mBinding.deviceDeviceTitle.setText(deviceItem.getTitle());
        this.deviceId = deviceItem.getId();
    }

    @Override
    protected BaseContract.IBasePresenter getP() {
        return null;
    }
}
