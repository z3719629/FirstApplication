package com.crm.firstapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.crm.firstapplication.fragment.MainFragment;

/**
 * Created by Administrator on 2018/1/2.
 */
public class CrmPagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;
    public CrmPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MainFragment();
            case 1:
                return new MainFragment();
            case 2:
                return new Fragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
