package com.crm.onenetcontroller.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.chinamobile.iot.onenet.module.DataPoint;
import com.crm.mylibrary.util.ZUtil;
import com.crm.onenetcontroller.R;
import com.crm.onenetcontroller.onenet.APIService;
import com.crm.onenetcontroller.onenet.DataPointData;
import com.crm.onenetcontroller.onenet.DataPointItem;
import com.crm.onenetcontroller.onenet.DataStream;
import com.crm.onenetcontroller.onenet.OneNetRetrofitUtils;
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
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * MQTT长连接服务
 *
 * @author 一口仨馍 联系方式 : yikousamo@gmail.com
 * @version 创建时间：2016/9/16 22:06
 */
public class MQTTService extends Service {

    public static final String TAG = MQTTService.class.getSimpleName() + "-------MQTT------";

    private static MqttAndroidClient client;
    private MqttConnectOptions conOpt;

    //    private String host = "tcp://10.0.2.2:61613";
    private String host = "tcp://183.230.40.39:6002";
    private String userName = "71971";
    private String passWord = "zhaiyuxiu";
    private static String myTopic = "slimenohouse/controller";
    private String clientId = "6192824";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        init();
        return START_STICKY;
    }

    public static void publish(String msg){
        String topic = myTopic;
        Integer qos = 0;
        Boolean retained = false;
        try {
            client.publish(topic, msg.getBytes(), qos.intValue(), retained.booleanValue());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        // 服务器地址（协议+地址+端口号）
        String uri = host;
        if(client == null) {
            client = new MqttAndroidClient(this, uri, clientId);
            // 设置MQTT监听并且接受消息
            client.setCallback(mqttCallback);
        }

        if(conOpt == null) {
            conOpt = new MqttConnectOptions();
            // 清除缓存
            conOpt.setCleanSession(true);
            // 设置超时时间，单位：秒
            conOpt.setConnectionTimeout(10);
            // 心跳包发送间隔，单位：秒
            conOpt.setKeepAliveInterval(120);
            // 用户名
            conOpt.setUserName(userName);
            // 密码
            conOpt.setPassword(passWord.toCharArray());

            // last will message
            boolean doConnect = true;
            String message = "{\"terminal_uid\":\"" + clientId + "\"}";
            String topic = myTopic;
            Integer qos = 0;
            Boolean retained = false;
            if ((!message.equals("")) || (!topic.equals(""))) {
                // 最后的遗嘱
                try {
                    conOpt.setWill(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
                } catch (Exception e) {
                    Log.i(TAG, "Exception Occured", e);
                    doConnect = false;
                    iMqttActionListener.onFailure(null, e);
                }
            }
            if (doConnect) {
                doClientConnection();
            }
        }

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        if(client != null) {
            try {
                client.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    /** 连接MQTT服务器 */
    private void doClientConnection() {
        if (!client.isConnected() && isConnectIsNomarl()) {
            try {
                client.connect(conOpt, null, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    // MQTT是否连接成功
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "连接成功 ");
            try {
                // 订阅myTopic话题
                client.subscribe(myTopic, 0);
                client.subscribe("/6196343/led", 0);
                Log.i(TAG, "订阅myTopic话题");
            } catch (MqttException e) {
                e.printStackTrace();
            }

            Map<String, String> params = new HashMap<>();
            params.put("datastream_id", "led");
//        params.put("end", new Date().toString());
//        params.put("duration", "3600");
            params.put("limit", "1");
            String url = DataPoint.urlForQuerying("6196343", params);
            Observable<ResponseBody> observable = OneNetRetrofitUtils.get().getRetrofit().create(APIService.class).getByUrl(url);
            observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResponseBody>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull ResponseBody responseBody) throws Exception {
                            DataPointData dataPointData = GsonConvertUtil.convert(responseBody.string(), DataPointData.class);


                            for(DataStream dataStream : dataPointData.getDatastreams()) {
                                for(int i=0;i<dataStream.getDatapoints().size();i++) {
                                    DataPointItem dataPointItem = dataStream.getDatapoints().get(i);
                                    dataPointItem.getValue();
                                }
                            }

                        }
                    });
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
            String str1 = new String(message.getPayload());
//            MQTTMessage msg = new MQTTMessage();
//            msg.setMessage(str1);
//            EventBus.getDefault().post(msg);
            Gson gson = new Gson();
            DataPointItem dataPointItem = gson.fromJson(str1, DataPointItem.class);
            String str2 = topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained();
            Log.i(TAG, "messageArrived:" + str1);
            Log.i(TAG, str2);

            // 更新 Widget
            RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_music);
            remoteViews.setTextViewText(R.id.button, dataPointItem.getValue().toString());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
            appWidgetManager.updateAppWidget(new ComponentName(getApplicationContext(), MusicWidgetProvider.class), remoteViews);
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

    /** 判断网络是否连接 */
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "MQTT当前网络名称：" + name);
            return true;
        } else {
            Log.i(TAG, "MQTT 没有可用网络");
            return false;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
