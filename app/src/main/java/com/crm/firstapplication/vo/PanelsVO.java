package com.crm.firstapplication.vo;


import com.crm.firstapplication.pojo.Panel;

import java.util.List;

public class PanelsVO {

    /**
     * 画像データ
     */
    private List<Panel> lists;

    public PanelsVO() {
    }

    /**
     * @param lists 画像データ
     */
    public PanelsVO(List<Panel> lists) {
        this.lists = lists;
    }

    public List<Panel> getLists() {
        return lists;
    }

    public void setLists(List<Panel> lists) {
        this.lists = lists;
    }
}
