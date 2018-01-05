package com.crm.userapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        return RecycleViewHolder.get(parent,getLayoutId(viewType));
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {
        convert(holder, mDatas.get(position), position);
    }



    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public abstract void convert(RecycleViewHolder holder, T data, int position);

    protected static class RecycleViewHolder extends RecyclerView.ViewHolder{
        private SparseArray<View> mViews;
        private View mConvertView;

        private RecycleViewHolder(View v){
            super(v);
            mConvertView = v;
            mViews = new SparseArray<>();
        }

        public static RecycleViewHolder get(ViewGroup parent, int layoutId){
            View convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, null, false);
            return new RecycleViewHolder(convertView);
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
    }

}
