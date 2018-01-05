package com.crm.firstapplication.pojo;

import java.util.List;

public class PageLayer {

    /**
     * レイヤー種別（0：背景｜1：自由エリア）
     */
    private int type;

    /**
     * アイテム情報
     */
    private List<PageLayerItem> items;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<PageLayerItem> getItems() {
        return items;
    }

    public void setItems(List<PageLayerItem> items) {
        this.items = items;
    }
}
