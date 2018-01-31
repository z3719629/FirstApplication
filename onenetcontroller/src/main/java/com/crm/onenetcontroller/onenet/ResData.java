package com.crm.onenetcontroller.onenet;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/30.
 */
public class ResData<T> implements Serializable {

    private Integer errno;
    private String error;
    private T data;

    public Integer getErrno() {
        return errno;
    }

    public void setErrno(Integer errno) {
        this.errno = errno;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
