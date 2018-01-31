package com.crm.onenetcontroller.onenet;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/30.
 */
public class DataPointData implements Serializable {
    private Integer count;
    private List<DataStream> datastreams;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<DataStream> getDatastreams() {
        return datastreams;
    }

    public void setDatastreams(List<DataStream> datastreams) {
        this.datastreams = datastreams;
    }
}
