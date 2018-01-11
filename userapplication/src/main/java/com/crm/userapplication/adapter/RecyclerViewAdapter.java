package com.crm.userapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2018/1/2.
 */
public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 2;
    public static final int TYPE_BODY = 0;
    public static final int TYPE_FOOTER = 1;

    private List<T> mDatas;
    private boolean isHaveHeader;
    private boolean isHaveFooter;

    public List<T> getmDatas() {
        return mDatas;
    }

    public RecyclerViewAdapter(List<T> datas, boolean isHaveHeader, boolean isHaveFooter){
        this.isHaveHeader = isHaveHeader;
        this.isHaveFooter = isHaveFooter;
        this.mDatas = datas;
        notifyItemRangeChanged(0, this.mDatas.size());
    }

    public boolean isHaveHeader() {
        return isHaveHeader;
    }

    public boolean isHaveFooter() {
        return isHaveFooter;
    }

    public abstract int getLayoutId(int viewType);

    public abstract int getPopupId(int viewType);

    /**
     * 可以根据类型设置xml布局 默认0
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (isHaveHeader && position == 0) {
            return TYPE_HEADER;
        } else if (isHaveFooter && isHaveHeader && position >= mDatas.size() + 1) {
            return TYPE_FOOTER;
        } else if (isHaveFooter && !isHaveHeader && position >= mDatas.size()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_BODY;
        }
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RecycleViewHolder.get(parent, getLayoutId(viewType), getPopupId(viewType), viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(isHaveHeader&&isHaveFooter) {
            // 内容+footer
            if(position == 0) {
                convert(holder, null, position);
            } else if(position > mDatas.size() - 1) {
                convert(holder, null, position);
            } else {
                convert(holder, mDatas.get(position - 1), position);
            }
        } else if(!isHaveHeader && isHaveFooter) {
            if(position > mDatas.size() - 1) {
                convert(holder, null, position);
            } else {
                convert(holder, mDatas.get(position), position);
            }
        } else if(isHaveHeader && !isHaveFooter) {
            if(position == 0) {
                convert(holder, null, position);
            } else {
                convert(holder, mDatas.get(position - 1), position);
            }
        } else {
            // 内容
            convert(holder, mDatas.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        if(isHaveHeader&&isHaveFooter) {
            // 内容+footer
            return mDatas.size() + 2;
        } else if(isHaveHeader || isHaveFooter) {
            // 内容+footer||header
            return mDatas.size() + 1;
        } else {
            // 内容
            return mDatas.size();
        }
    }

    public abstract void convert(RecyclerView.ViewHolder holder, T data, int position);

    protected static class RecycleViewHolder extends RecyclerView.ViewHolder{
        private SparseArray<View> mViews;
        private View mConvertView;
        private PopupWindow popupView;
        private int viewType;

        public int getViewType() {
            return viewType;
        }

        private RecycleViewHolder(View v, View popView, int viewType){
            super(v);
            mConvertView = v;
            mViews = new SparseArray<>();
            this.viewType = viewType;

            if(popView != null) {
                popupView = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupView.setTouchable(true);
                popupView.setOutsideTouchable(true);
            }

        }

        public static RecycleViewHolder get(ViewGroup parent, int layoutId, int popId, int viewType){

            Context context = parent.getContext();
            View convertView = LayoutInflater.from(context).inflate(layoutId, null, false);
            View popView = null;
            if(popId != -1) {
                popView = LayoutInflater.from(context).inflate(popId, null, false);
            }

            return new RecycleViewHolder(convertView, popView, viewType);
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
