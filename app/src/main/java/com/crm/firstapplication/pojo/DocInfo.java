package com.crm.firstapplication.pojo;

/**
 * ペネル基本情報
 */
public class DocInfo {
    /*
     * ペネルID
     */
    private String id;

    /**
     * ペネルの名前
     */
    private String title;

    /**
     * ペネルwidth
     */
    private int pw;
    /**
     * ペネルheight
     */
    private int ph;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
