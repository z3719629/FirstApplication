package com.crm.mylibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import com.crm.mylibrary.R;

import java.io.Serializable;


/**
 * Created by Administrator on 2018/1/5.
 */
public class ZUtil {
    private static ZUtil instance;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    private ZUtil() {
    }

    public static ZUtil getInstance() {
        if(instance == null) {
            synchronized (ZUtil.class) {
                if (instance == null) {
                    instance = new ZUtil();
                }
            }
        }
        return instance;
    }

    public Drawable getDrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(this.context, id);
    }

    public void startNewActivity(Activity activity, Class clazz, boolean isClose, Serializable ... data) {
        Intent intent = new Intent(activity, clazz);
        for(int i=0;i<data.length;i++) {
            intent.putExtra("data"+i, data[i]);
        }
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.default_anim_in, R.anim.default_anim_out);
        if(isClose) {
            activity.finish();
        }
    }

}
