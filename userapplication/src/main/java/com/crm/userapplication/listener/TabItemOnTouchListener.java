package com.crm.userapplication.listener;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.crm.mylibrary.listener.BaseOnTouchListener;
import com.crm.userapplication.R;

/**
 * Created by Administrator on 2018/1/10.
 */
public class TabItemOnTouchListener extends BaseOnTouchListener {

    protected ViewPager mViewPager;
    protected TabLayout.Tab mTab;

    public TabItemOnTouchListener(Context context, ViewPager viewPager, TabLayout.Tab tab) {
        super(context);
        this.mViewPager = viewPager;
        this.mTab = tab;
    }

    @Override
    protected void doOnLongPress(MotionEvent e, int x, int y) {

    }

    @Override
    protected void doOnTouchActionDown(View v, MotionEvent event) {
        super.doOnTouchActionDown(v, event);
        v.startAnimation(AnimationUtils.loadAnimation(this.context, R.anim.default_anim_tab_down));
    }

    @Override
    protected void doOnTouchActionUp(View v, MotionEvent event) {
        super.doOnTouchActionUp(v, event);
        mViewPager.setCurrentItem(mTab.getPosition(), false);
    }
}
