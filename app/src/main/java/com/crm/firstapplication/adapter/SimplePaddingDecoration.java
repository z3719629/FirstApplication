package com.crm.firstapplication.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.crm.firstapplication.R;

/**
 * Created by Administrator on 2018/1/3.
 */
public class SimplePaddingDecoration extends RecyclerView.ItemDecoration {

    private int dividerHeight;


    public SimplePaddingDecoration(Context context) {
        dividerHeight = context.getResources().getDimensionPixelSize(R.dimen.divider_height);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //outRect.set(0, 0, 0, dividerHeight);
        outRect.bottom = dividerHeight;//类似加了一个bottom padding
    }
}
