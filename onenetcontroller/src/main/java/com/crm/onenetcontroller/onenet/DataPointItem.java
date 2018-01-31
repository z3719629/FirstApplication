package com.crm.onenetcontroller.onenet;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/30.
 */
public class DataPointItem implements Serializable {
    private String at;
    private Integer value;

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
