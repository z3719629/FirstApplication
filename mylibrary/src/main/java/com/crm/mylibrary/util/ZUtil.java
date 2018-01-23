package com.crm.mylibrary.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

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

}
