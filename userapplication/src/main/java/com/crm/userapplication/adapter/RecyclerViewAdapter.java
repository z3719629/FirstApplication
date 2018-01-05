package com.crm.userapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.crm.userapplication.R;

import java.util.List;

/**
 * Created by Administrator on 2018/1/2.
 */
public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewAdapter.RecycleViewHolder> {
    private List<T> mDatas;

    public RecyclerViewAdapter(List<T> datas){
        this.mDatas = datas;
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
        return super.getItemViewType(position);
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RecycleViewHolder.get(parent, getLayoutId(viewType), getPopupId(viewType));
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {
        convert(holder, mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public abstract void convert(final RecycleViewHolder holder, T data, int position);

    protected static class RecycleViewHolder extends RecyclerView.ViewHolder{
        private SparseArray<View> mViews;
        private View mConvertView;
        private PopupWindow popupView;

        private RecycleViewHolder(View v, View popView){
            super(v);
            mConvertView = v;
            mViews = new SparseArray<>();

            popupView = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupView.setTouchable(true);
            popupView.setOutsideTouchable(true);
        }

        public static RecycleViewHolder get(ViewGroup parent, int layoutId, int popId){
            Context context = parent.getContext();
            View convertView = LayoutInflater.from(context).inflate(layoutId, null, false);
            View popView = LayoutInflater.from(context).inflate(R.layout.layout_popwindow_recyclerview, parent, false);
            return new RecycleViewHolder(convertView, popView);
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
