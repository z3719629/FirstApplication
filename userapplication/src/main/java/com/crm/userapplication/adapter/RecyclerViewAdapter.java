package com.crm.userapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.crm.userapplication.listener.DataLoadingSubject;

import java.util.List;

/**
 * Created by Administrator on 2018/1/2.
 */
public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DataLoadingSubject.DataLoadingCallbacks {
    private List<T> mDatas;

    public List<T> getmDatas() {
        return mDatas;
    }

    private boolean showLoadingMore = false;

    public RecyclerViewAdapter(List<T> datas){
        this.mDatas = datas;
    }

    public abstract int getLayoutId(int viewType);

    public abstract int getPopupId(int viewType);

    @Override
    public void dataStartedLoading() {
        if (showLoadingMore) return;
        showLoadingMore = true;
        notifyItemInserted(getLoadingMoreItemPosition());
    }

    @Override
    public void dataFinishedLoading() {
        if (showLoadingMore) return;
        int loadingPos = getLoadingMoreItemPosition();
        showLoadingMore = false;
        notifyItemRemoved(loadingPos);
    }

    public int getLoadingMoreItemPosition() {
        return showLoadingMore ? getItemCount() - 1 : RecyclerView.NO_POSITION;
    }

    /**
     * 可以根据类型设置xml布局 默认0
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position < mDatas.size())
            return 0;
        else
            return 1;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RecycleViewHolder.get(parent, getLayoutId(viewType), getPopupId(viewType), viewType, this);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        convert(holder, mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public abstract void convert(RecyclerView.ViewHolder holder, T data, int position);

    protected static class RecycleViewHolder extends RecyclerView.ViewHolder{
        private RecyclerViewAdapter p;
        private SparseArray<View> mViews;
        private View mConvertView;
        private PopupWindow popupView;
        private int viewType;

        public int getViewType() {
            return viewType;
        }

        public RecyclerViewAdapter getP() {
            return p;
        }

        private RecycleViewHolder(View v, View popView, int viewType, RecyclerViewAdapter recyclerViewAdapter){
            super(v);
            mConvertView = v;
            mViews = new SparseArray<>();
            this.viewType = viewType;
            this.p = recyclerViewAdapter;

            popupView = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupView.setTouchable(true);
            popupView.setOutsideTouchable(true);

        }

        public static RecycleViewHolder get(ViewGroup parent, int layoutId, int popId, int viewType, RecyclerViewAdapter recyclerViewAdapter){

            Context context = parent.getContext();
            View convertView = LayoutInflater.from(context).inflate(layoutId, null, false);
            View popView = LayoutInflater.from(context).inflate(popId, parent, false);
            return new RecycleViewHolder(convertView, popView, viewType, recyclerViewAdapter);
        }

        public <VIEW extends View> VIEW getView(int id){
            View v = mViews.get(id);
            if(v == null){
                v = mConvertView.findViewById(id);
                mViews.put(id, v);
            }
            return (VIEW)v;
        }

        public void setText(int id, String value){
            TextView view = getView(id);
            view.setText(value);
        }

        public PopupWindow getPopUpView() {
            return this.popupView;
        }

    }

}
