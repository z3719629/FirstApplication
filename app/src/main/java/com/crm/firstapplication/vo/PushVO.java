package com.crm.firstapplication.vo;


import com.crm.firstapplication.pojo.Piece;

import java.util.List;

public class PushVO {

    /**
     * 0：表示 | 1:その他
     */
    private String action;

    /**
     * アップした画像の保存場所
     */
    private String img_url;

    /**
     * 块，代表图片中的一小部分
     */
    private List<Piece> items;

    public PushVO() {
    }

    public PushVO(String action, String img_url, List<Piece> items) {
        this.action = action;
        this.img_url = img_url;
        this.items = items;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public List<Piece> getItems() {
        return items;
    }

    public void setItems(List<Piece> items) {
        this.items = items;
    }
}
