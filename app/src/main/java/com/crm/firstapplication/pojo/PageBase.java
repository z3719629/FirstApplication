package com.crm.firstapplication.pojo;

/**
 * ページ情報　※ペネルwidth、heightの左上隅を原点（0,0）とする
 */
public class PageBase {
    /**
     * ペネルID
     */
    private String id;
    /**
     * ペネルwidth
     */
    private int pw;
    /**
     * ペネルheight
     */
    private int ph;

    public PageBase() {
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPw() {
        return pw;
    }

    public void setPw(int pw) {
        this.pw = pw;
    }

    public int getPh() {
        return ph;
    }

    public void setPh(int ph) {
        this.ph = ph;
    }
}
