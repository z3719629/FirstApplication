package com.crm.onenetcontroller.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.chinamobile.iot.onenet.module.DataPoint;
import com.crm.onenetcontroller.R;
import com.crm.onenetcontroller.onenet.APIService;
import com.crm.onenetcontroller.onenet.DataPointData;
import com.crm.onenetcontroller.onenet.DataPointItem;
import com.crm.onenetcontroller.onenet.DataStream;
import com.crm.onenetcontroller.onenet.DeviceItem;
import com.crm.onenetcontroller.onenet.Devices;
import com.crm.onenetcontroller.onenet.OneNetRetrofitUtils;
import com.crm.onenetcontroller.onenet.mqtt.MqttClient;
import com.crm.onenetcontroller.onenet.utils.GsonConvertUtil;
import com.crm.onenetcontroller.widget.MusicWidgetProvider;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/2/2.
 */
public class UpdateWidgetService extends Service {

    private String TAG = "UpdateWidgetService ------ ";

    private String userName = "71971";
    private String passWord = "zhaiyuxiu";
    private static String myTopic = "slimenohouse/controller";
    private String clientId = "6192824";

    private String myLedDeviceId = "6196343";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final MqttClient mqttClient = MqttClient.getInstance(getApplicationContext(), userName, passWord, clientId);
        if(mqttClient.haveClient()) {
            getDataPointAndUpdateWidgetButtonChange(mqttClient);
        } else {
            updateWidgetButtonChange("未连接", Color.RED);
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void updateWidgetButtonChange(String value, int color) {
        // 更新 Widget
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_music);
        remoteViews.setTextViewText(R.id.widget_button_change, value);
        remoteViews.setInt(R.id.widget_button_change, "setBackgroundColor", color);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        appWidgetManager.updateAppWidget(new ComponentName(getApplicationContext(), MusicWidgetProvider.class), remoteViews);
    }

    private void getDataPointAndUpdateWidgetButtonChange(final MqttClient mqttClient) {

        Map<String, String> paramsStatus = new HashMap<>();
        paramsStatus.put("devIds", myLedDeviceId);
        String url2 = MqttClient.urlForQueryingStatus(paramsStatus);
        Log.i(TAG, "url2 : " + url2);

        Observable<ResponseBody> observable2 = OneNetRetrofitUtils.get().getRetrofit().create(APIService.class).getByUrl(url2);
        observable2
                .flatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(@NonNull ResponseBody responseBody) throws Exception {
                        //Log.i(TAG, "res : " + responseBody.string());
                        Devices devices = GsonConvertUtil.convert(responseBody.string(), Devices.class);
                        for(DeviceItem deviceItem: devices.getDevices()) {
                            if(deviceItem.isOnline() && deviceItem.getId().equals(myLedDeviceId)) {
                                Map<String, String> params = new HashMap<>();
                                params.put("datastream_id", "led");
                                params.put("limit", "1");
                                String urlLedPoint = DataPoint.urlForQuerying(myLedDeviceId, params);

                                return OneNetRetrofitUtils.get().getRetrofit().create(APIService.class).getByUrl(urlLedPoint);
                            }
                        }
                        Log.i(TAG, "设备未在线");
                        return new ObservableSource<ResponseBody>() {
                            @Override
                            public void subscribe(@NonNull Observer<? super ResponseBody> observer) {

                            }
                        };
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(@NonNull ResponseBody responseBody) throws Exception {
                        DataPointData dataPointData = GsonConvertUtil.convert(responseBody.string(), DataPointData.class);

                        for (DataStream dataStream : dataPointData.getDatastreams()) {
                            for (int i = 0; i < dataStream.getDatapoints().size(); i++) {
                                DataPointItem dataPointItem = dataStream.getDatapoints().get(i);
                                Log.i(TAG, "获取数据点");
                                Log.i(TAG, String.valueOf(dataPointItem.getValue()));
                                mqttClient.publish(myTopic, dataPointItem.getValue()==0?"led on":"led off");
                            }
                        }
                    }
                });
    }
}
