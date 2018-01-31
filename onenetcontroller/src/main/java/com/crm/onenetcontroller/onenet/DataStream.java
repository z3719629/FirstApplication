package com.crm.onenetcontroller.onenet;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/1/30.
 */
public class DataStream implements Serializable {
    private List<DataPointItem> datapoints;

    public List<DataPointItem> getDatapoints() {
        return datapoints;
    }

    public void setDatapoints(List<DataPointItem> datapoints) {
        this.datapoints = datapoints;
    }
}
