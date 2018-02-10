package com.crm.onenetcontroller.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.crm.mylibrary.contract.BaseContract;
import com.crm.mylibrary.rxbus.Events;
import com.crm.mylibrary.rxbus.RxBus;
import com.crm.onenetcontroller.R;
import com.crm.onenetcontroller.activity.MainActivity;
import com.crm.onenetcontroller.service.MQTTService;
import com.crm.onenetcontroller.service.UpdateWidgetService;

import org.eclipse.paho.android.service.MqttAndroidClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MusicWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        // Update each requested appWidgetId
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_music);

        Intent intentRefrash = new Intent(context, MQTTService.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context,
//                0 /* no requestCode */, intent, 0 /* no flags */);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intentRefrash, 0);
        views.setOnClickPendingIntent(R.id.widget_button_refresh, pendingIntent);
        views.setInt(R.id.widget_button_change, "setBackgroundColor", Color.RED);

        Intent intentChange = new Intent(context, UpdateWidgetService.class);
        pendingIntent = PendingIntent.getService(context, 0, intentChange, 0);
        views.setOnClickPendingIntent(R.id.widget_button_change, pendingIntent);

        Intent startUpdateIntent = new Intent(context, MQTTService.class);
        context.startService(startUpdateIntent);

        for (int i = 0; i < appWidgetIds.length; i++) {
            appWidgetManager.updateAppWidget(appWidgetIds[i], views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Intent startUpdateIntent = new Intent(context, MQTTService.class);
        context.startService(startUpdateIntent);
    }

    public void onEnabled(Context context) {
        //super.onEnabled();


    }

    public void onDisabled(Context context) {
        //super.onDisabled();
        Intent stopUpdateIntent = new Intent(context, MQTTService.class);
        context.stopService(stopUpdateIntent);
    }

}
