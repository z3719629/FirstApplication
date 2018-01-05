package com.crm.firstapplication.vo;

import com.crm.firstapplication.pojo.Result;

public class ResVO<T> {

    private Result result;

    private T data;

    public ResVO() {
    }

    public ResVO(Result result, T data) {
        this.result = result;
        this.data = data;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
