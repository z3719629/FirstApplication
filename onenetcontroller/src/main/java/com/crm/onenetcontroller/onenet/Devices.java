package com.crm.onenetcontroller.onenet;

import android.bluetooth.BluetoothClass;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/2/5.
 */
public class Devices {
    private List<DeviceItem> devices;

    @SerializedName("total_count")
    private Integer totalCount;

    public List<DeviceItem> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceItem> devices) {
        this.devices = devices;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
