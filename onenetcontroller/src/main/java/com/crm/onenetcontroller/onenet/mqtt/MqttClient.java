package com.crm.onenetcontroller.onenet.mqtt;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.chinamobile.iot.onenet.http.Urls;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Iterator;
import java.util.Map;

import okhttp3.HttpUrl;

/**
 * Created by Administrator on 2018/2/5.
 */
public class MqttClient {

    private String TAG = "MqttClient ------ ";
    private static String HOST = "tcp://183.230.40.39:6002";

    private String userName = "71971";
    private String passWord = "zhaiyuxiu";
    private String clientId = "6192824";

    private int qos = 1;

    private static MqttClient instance;

    private Context context;

    private MqttAndroidClient client;
    private MqttConnectOptions conOpt;

    private MqttClient() {}

    public static String urlForQueryingStatus(Map<String, String> params) {
        HttpUrl.Builder builder = (new HttpUrl.Builder()).scheme("http").host(Urls.sHost).addPathSegment("devices").addPathSegment("status");
        if(params != null) {
            Iterator iterator = params.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry entry = (Map.Entry)iterator.next();
                String key = (String)entry.getKey();
                String value = (String)entry.getValue();
                builder.addEncodedQueryParameter(key, value);
            }
        }

        return builder.toString();
    }

    public static MqttClient getInstance(Context context, String userName, String passWord, String clientId) {
        if(instance == null) {
            synchronized (MqttClient.class) {
                if (instance == null) {
                    instance = new MqttClient();
                }
            }
        }
        instance.context = context;
        instance.userName = userName;
        instance.passWord = passWord;
        instance.clientId = clientId;
        return instance;
    }

    public boolean haveClient() {
        return client != null;
    }

    public void connect(MqttCallback mqttCallback, IMqttActionListener iMqttActionListener, String myTopic) {

        client = new MqttAndroidClient(context, HOST, clientId);
        // 设置MQTT监听并且接受消息
        client.setCallback(mqttCallback);

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
            Boolean retained = false;
            if ((!message.equals("")) || (!topic.equals(""))) {
                // 最后的遗嘱
                try {
                    conOpt.setWill(topic, message.getBytes(), qos, retained.booleanValue());
                } catch (Exception e) {
                    Log.i(TAG, "Exception Occured", e);
                    doConnect = false;
                    iMqttActionListener.onFailure(null, e);
                }
            }
            if (doConnect) {
                doClientConnection(iMqttActionListener);
            }
        }

    }

    public void publish(String topic, String msg){
        Boolean retained = false;
        try {
            client.publish(topic, msg.getBytes(), qos, retained);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() throws MqttException {
        client.disconnect();
    }

    public void subscribe(String topic, int qos) throws MqttException {
        client.subscribe(topic, qos);
    }

    /** 连接MQTT服务器 */
    private void doClientConnection(IMqttActionListener iMqttActionListener) {
        if (!client.isConnected() && isConnectIsNomarl()) {
            try {
                client.connect(conOpt, null, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    /** 判断网络是否连接 */
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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

}
