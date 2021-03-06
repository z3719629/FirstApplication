package com.crm.onenetcontroller;

import android.app.Application;

import com.chinamobile.iot.onenet.OneNetApi;

import org.xutils.x;

/**
 * Created by Administrator on 2017/12/25.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        OneNetApi.init(this, true);
    }
}
