package com.crm.mylibrary.adapter;

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
