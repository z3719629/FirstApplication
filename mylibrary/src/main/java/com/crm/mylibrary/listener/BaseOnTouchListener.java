package com.crm.mylibrary.listener;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/1/10.
 */
public class BaseOnTouchListener implements View.OnTouchListener {

    protected Context context;
    protected float pointerX = 0;
    protected float pointerY = 0;
    protected final GestureDetector mGestureDetector;

    public BaseOnTouchListener(final Context context) {
        this.context = context;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                int x = (int)e.getX();
                int y = (int)e.getY();
                Toast.makeText(context, "x:" + x+ " y:" + y, Toast.LENGTH_SHORT).show();

                doOnLongPress(e, x, y);
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            Log.i("TTTTTTTTTTTTTT", "ACTION_UP");
            doOnTouchActionUp(v, event);
        } else if(event.getAction() == MotionEvent.ACTION_CANCEL){
            Log.i("TTTTTTTTTTTTTT", "ACTION_CANCEL");
            doOnTouchActionCancel(v, event);
        } else if(event.getAction() == MotionEvent.ACTION_DOWN){
            pointerX = MotionEventCompat.getAxisValue(event, MotionEventCompat.AXIS_X);
            pointerY = MotionEventCompat.getAxisValue(event, MotionEventCompat.AXIS_Y);
            Log.i("TTTTTTTTTTTTTT", "ACTION_DOWN");
            doOnTouchActionDown(v, event);
        } else if(event.getAction() == MotionEvent.ACTION_MOVE){
            float pointerXTmp = MotionEventCompat.getAxisValue(event, MotionEventCompat.AXIS_X);
            float pointerYTmp = MotionEventCompat.getAxisValue(event, MotionEventCompat.AXIS_Y);
            if (pointerXTmp != pointerX || pointerYTmp != pointerY) {
                Log.i("TTTTTTTTTTTTTT", "ACTION_MOVE");
                doOnTouchActionMove(v, event);
            } else {
                Log.i("TTTTTTTTTTTTTT", "ACTION_DOWN_NOT_MOVE");
                doOnTouchActionDownNotMove(v, event);
            }
        }

        mGestureDetector.onTouchEvent(event);
        // 一定要返回true，不然获取不到完整的事件  不能使用onclick
        return true;
    }

    /**
     * 手指抬起（有时候不触发）
     * @param v
     * @param event
     */
    protected void doOnTouchActionUp(View v, MotionEvent event) {}

    /**
     * 手指第一次按下
     * @param v
     * @param event
     */
    protected void doOnTouchActionDown(View v, MotionEvent event) {}

    /**
     * 手指移动
     * @param v
     * @param event
     */
    protected void doOnTouchActionMove(View v, MotionEvent event) {}

    /**
     * 手指抬起
     * @param v
     * @param event
     */
    protected void doOnTouchActionCancel(View v, MotionEvent event) {}

    /**
     * 手指按下并且不滑动
     * @param v
     * @param event
     */
    protected void doOnTouchActionDownNotMove(View v, MotionEvent event) {}

    protected void doOnLongPress(MotionEvent e, int x, int y) {}

}
