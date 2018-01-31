package com.crm.onenetcontroller.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crm.mylibrary.activity.base.BaseActivity;
import com.crm.mylibrary.adapter.RecyclerViewAdapter;
import com.crm.mylibrary.adapter.RecyclerViewOperateHolder;
import com.crm.mylibrary.data.DataLoadingSubject;
import com.crm.mylibrary.listener.RecyclerViewItemOnTouchListener;
import com.crm.mylibrary.util.ZUtil;
import com.crm.onenetcontroller.R;
import com.crm.onenetcontroller.activity.DeviceActivity;
import com.crm.onenetcontroller.activity.MainActivity;
import com.crm.onenetcontroller.onenet.DeviceItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/10.
 */
public abstract class PageingViewAdapter<T extends Serializable, V extends BaseActivity> extends RecyclerViewAdapter<T> implements DataLoadingSubject.DataLoadingCallbacks, RecyclerViewOperateHolder<T> {

    private V activityContext;

    private boolean showLoadingMore;

    public PageingViewAdapter(V activityContext, List<T> datas, boolean isHaveHeader, boolean isHaveFooter) {
        super(datas, isHaveHeader, isHaveFooter);
        this.activityContext = activityContext;
    }

    @Override
    public int getLayoutId(int viewType) {
        if(viewType == RecyclerViewAdapter.TYPE_BODY) {
            return R.layout.recycle_view_item;
        } else if(viewType == RecyclerViewAdapter.TYPE_FOOTER) {
            return R.layout.recycle_view_item_footer;
        } else if(viewType == RecyclerViewAdapter.TYPE_HEADER){
            return R.layout.recycle_view_item_header;
        } else {
            return -1;
        }
    }

    @Override
    public int getPopupId(int viewType) {
        return R.layout.layout_popwindow_recyclerview;
    }

    @Override
    public void convert(RecyclerView.ViewHolder holderOld, final T data, int position) {

        final RecycleViewHolder holder = (RecycleViewHolder)holderOld;

        final Drawable recycleViewNormal = ZUtil.getInstance().getDrawable(R.drawable.recycle_view_normal);
        final Drawable recycleViewPressed = ZUtil.getInstance().getDrawable(R.drawable.recycle_view_pressed);

        holder.itemView.setOnTouchListener(new RecyclerViewItemOnTouchListener(activityContext, recycleViewNormal, recycleViewPressed) {
            @Override
            protected void doOnLongPress(MotionEvent e, int x, int y) {
                activityContext.getmVibrator().vibrate(30);
                // 弹框左上角显示在触摸点，偏移x 50 y 50
                holder.getPopUpView().showAsDropDown(holder.itemView, x + 50, y + 50 - holder.itemView.getHeight());
                holder.getPopUpView().setAnimationStyle(R.style.windowAnimBottom);
                LinearLayout linearLayout = (LinearLayout)holder.getPopUpView().getContentView();
                for(int i=0; i<linearLayout.getChildCount(); i++) {
                    View v = linearLayout.getChildAt(i);
                    v.setOnTouchListener(new RecyclerViewItemOnTouchListener(activityContext, v.getBackground(), recycleViewPressed) {

                        @Override
                        protected void doOnTouchActionUp(View v, MotionEvent event) {
                            super.doOnTouchActionUp(v, event);
                            if(event.getX() > 0 && event.getY() > 0) {
                                switch (v.getId()) {
                                    case R.id.popup_window_menu1:
                                        deleteData(holder.getAdapterPosition());
                                        holder.getPopUpView().dismiss();
                                        break;
                                    case R.id.popup_window_menu2:
                                        insertData(data);
                                        holder.getPopUpView().dismiss();
                                        break;
                                    case R.id.popup_window_menu3:
                                        deleteData(holder.getAdapterPosition());
                                        holder.getPopUpView().dismiss();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    });
                }
            }


            @Override
            protected void doOnTouchActionUp(View v, MotionEvent event) {
                super.doOnTouchActionUp(v, event);
                doOnTouchActionUpSub(v, event, data);
            }
        });

        this.editData(holder, data, position);

    }

    public abstract void doOnTouchActionUpSub(View v, MotionEvent event, T data);

    @Override
    public void updateData(List<T> data) {
        int startPosition = isHaveHeader() ? 1 : 0;
        int size = this.mDatas.size();
        if(size > 0) {
            this.mDatas.clear();
            notifyItemRangeRemoved(startPosition, size);
            notifyItemRangeChanged(startPosition, size);
        }

        this.mDatas.addAll(data);
        notifyItemRangeInserted(startPosition, data.size());
        notifyItemRangeChanged(startPosition, data.size());
    }

    @Override
    public void deleteData(int position) {
        int size = this.mDatas.size();
        if(size > 0) {
            this.mDatas.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public void moveData(T data) {

    }

    @Override
    public void insertData(T data) {
        int size = this.mDatas.size();
        if(size >= 0) {
            this.mDatas.add(data);
            notifyItemInserted(this.mDatas.size());
        }
    }

    public void insertDataList(List<T> data) {
        int size = this.mDatas.size();
        if(size >= 0) {
            this.mDatas.addAll(data);
            notifyItemRangeInserted(size, this.mDatas.size());
        }
    }

    @Override
    public void clearData() {
        int size = this.mDatas.size();
        if(size > 0) {
            this.mDatas.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public void notifyDataChanged() {
        notifyDataSetChanged();
    }

    public abstract void editData(RecyclerView.ViewHolder holderOld, final T data, int position);

    public boolean isDataLoading() {
        return showLoadingMore;
    }

    @Override
    public void dataStartedLoading() {
        if (showLoadingMore) return;
        showLoadingMore = true;

    }

    @Override
    public void dataFinishedLoading() {
        if (!showLoadingMore) return;
        int loadingPos = getLoadingMoreItemPosition();
        showLoadingMore = false;
        notifyItemChanged(loadingPos);
    }

    public int getLoadingMoreItemPosition() {
        return showLoadingMore ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }
}
