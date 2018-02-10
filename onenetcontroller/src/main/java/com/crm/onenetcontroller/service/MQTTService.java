package com.crm.onenetcontroller.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.chinamobile.iot.onenet.http.Urls;
import com.chinamobile.iot.onenet.module.DataPoint;
import com.chinamobile.iot.onenet.module.Device;
import com.crm.mylibrary.util.ZUtil;
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
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

/**
 * MQTT长连接服务
 */
public class MQTTService extends Service {

    public static final String TAG = MQTTService.class.getSimpleName() + "-------MQTT------";

    //    private String host = "tcp://10.0.2.2:61613";

    private String userName = "71971";
    private String passWord = "zhaiyuxiu";
    private static String myTopic = "slimenohouse/controller";
    private String clientId = "6192824";

    private String myLedDeviceId = "6196343";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MqttClient mqttClient = MqttClient.getInstance(getApplicationContext(), userName, passWord, clientId);
        if(!mqttClient.haveClient()) {
            Log.i(TAG, "mqtt service onStartCommand -- have client");
            // 更新 Widget
            RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_music);
            remoteViews.setTextViewText(R.id.widget_button_change, "更新中");
            remoteViews.setInt(R.id.widget_button_change, "setBackgroundColor", Color.GREEN);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
            appWidgetManager.updateAppWidget(new ComponentName(getApplicationContext(), MusicWidgetProvider.class), remoteViews);

            mqttClient.connect(mqttCallback, iMqttActionListener, myTopic);
        } else {
            Log.i(TAG, "mqtt service onStartCommand -- no client");
            getDataPointAndUpdateWidgetButtonChange();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        MqttClient mqttClient = MqttClient.getInstance(getApplicationContext(), userName, passWord, clientId);
        if(mqttClient.haveClient()) {
            try {
                mqttClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    // MQTT是否连接成功
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "连接成功 ");
            MqttClient mqttClient = MqttClient.getInstance(getApplicationContext(), userName, passWord, clientId);

            try {
                // 订阅myTopic话题
                mqttClient.subscribe(myTopic, 0);
                mqttClient.subscribe("/"+ myLedDeviceId +"/led", 0);
                Log.i(TAG, "订阅myTopic话题");
            } catch (MqttException e) {
                e.printStackTrace();
            }

            getDataPointAndUpdateWidgetButtonChange();
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            // 连接失败，重连
        }
    };

    // MQTT监听并且接受消息
    private MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            Log.i(TAG, "messageArrived");

            if(topic.equals("/"+myLedDeviceId+"/led")) {
                String str1 = new String(message.getPayload());
                Gson gson = new Gson();
                DataPointItem dataPointItem = gson.fromJson(str1, DataPointItem.class);
                Log.i(TAG, "messageArrived:" + str1);
                updateWidgetButtonChange(dataPointItem.getValue().toString(), dataPointItem.getValue()==0?Color.BLACK:Color.YELLOW);
            }

            String str2 = topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained() + "msg : " + new String(message.getPayload());

            Log.i(TAG, str2);

        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {
            Log.i(TAG, "deliveryComplete");
        }

        @Override
        public void connectionLost(Throwable arg0) {
            // 失去连接，重连
        }
    };

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

    private void getDataPointAndUpdateWidgetButtonChange() {

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
                        updateWidgetButtonChange("离线", Color.RED);
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
                                   if(responseBody.contentLength() > 0) {
                                       DataPointData dataPointData = GsonConvertUtil.convert(responseBody.string(), DataPointData.class);

                                       for (DataStream dataStream : dataPointData.getDatastreams()) {
                                           for (int i = 0; i < dataStream.getDatapoints().size(); i++) {
                                               DataPointItem dataPointItem = dataStream.getDatapoints().get(i);
                                               Log.i(TAG, "获取数据点");
                                               Log.i(TAG, String.valueOf(dataPointItem.getValue()));

                                               updateWidgetButtonChange(dataPointItem.getValue().toString(), dataPointItem.getValue() == 0 ? Color.BLACK : Color.YELLOW);
                                           }
                                       }
                                   }
                               }
                           });
    }
}
