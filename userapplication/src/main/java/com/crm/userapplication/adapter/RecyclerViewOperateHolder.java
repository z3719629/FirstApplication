package com.crm.userapplication.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Administrator on 2018/1/11.
 */
public interface RecyclerViewOperateHolder<T> {

    void updateData(List<T> data);

    void deleteData(int position);

    void moveData(T data);

    void insertData(T data);

}
