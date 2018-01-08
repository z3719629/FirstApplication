package com.crm.userapplication.rxbus;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/4.
 */
public class Events<T> {
    //所有事件的CODE
    public static final int EVENT_TAP = 1; // 点击事件
    public static final int EVENT_UPDATE_WIDGET = 11; // 更新控件
    public static final int EVENT_SEND = 98; // 发送事件
    public static final int EVENT_JUMP = 99; // 跳转事件
    public static final int EVENT_OTHER = 100; // 其它事件

    //枚举
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventCode {}

    public @Events.EventCode int code;
    private Map<String, Object> content;
    private T target;

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public T getTarget() {
        return target;
    }

    public void setTarget(T target) {
        this.target = target;
    }

    public int getCode() {
        return code;
    }
}
