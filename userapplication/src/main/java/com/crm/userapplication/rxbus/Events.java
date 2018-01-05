package com.crm.userapplication.rxbus;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2018/1/4.
 */
public class Events<T> {
    //所有事件的CODE
    public static final int EVENT_TAP = 1; // 点击事件
    public static final int EVENT_SEND = 98; // 发送事件
    public static final int EVENT_JUMP = 99; // 跳转事件
    public static final int EVENT_OTHER = 100; // 其它事件

    //枚举
    @IntDef({EVENT_TAP, EVENT_SEND, EVENT_JUMP, EVENT_OTHER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EventCode {}

    public @Events.EventCode int code;
    public T content;

    public static <O> Events<O> setContent(O t) {
        Events<O> events = new Events<>();
        events.content = t;
        return events;
    }

    public <T> T getContent() {
        return (T) content;
    }

    public int getCode() {
        return code;
    }
}
