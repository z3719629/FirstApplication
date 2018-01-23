package com.crm.mylibrary.listener;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2018/1/10.
 */
public class RecyclerViewItemOnTouchListener extends BaseOnTouchListener {

    private Drawable normal;
    private Drawable pressed;

    public RecyclerViewItemOnTouchListener(final Context context, Drawable normal, Drawable pressed) {
        super(context);
        this.normal = normal;
        this.pressed = pressed;
    }

    @Override
    protected void doOnTouchActionUp(View v, MotionEvent event) {
        super.doOnTouchActionUp(v, event);
        v.setBackground(normal);
    }

    @Override
    protected void doOnTouchActionMove(View v, MotionEvent event) {
        super.doOnTouchActionMove(v, event);
        v.setBackground(normal);
    }

    @Override
    protected void doOnTouchActionCancel(View v, MotionEvent event) {
        super.doOnTouchActionCancel(v, event);
        v.setBackground(normal);
    }

    @Override
    protected void doOnTouchActionDownNotMove(View v, MotionEvent event) {
        super.doOnTouchActionDownNotMove(v, event);
        v.setBackground(pressed);
    }

}
