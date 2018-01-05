package com.crm.firstapplication.pojo;

import java.util.List;

public class Doc {

    /**
     * ペネル基本情報
     */
    DocInfo info;
    /**
     * ページ情報　※ペネルwidth、heightの左上隅を原点（0,0）とする
     */
    List<DocPages> pages;

    public Doc() {
    }

    /**
     * @param info ペネル基本情報
     * @param pages ページ情報　※ペネルwidth、heightの左上隅を原点（0,0）とする
     */
    public Doc(DocInfo info, List<DocPages> pages) {
        this.info = info;
        this.pages = pages;
    }

    public DocInfo getInfo() {
        return info;
    }

    public void setInfo(DocInfo info) {
        this.info = info;
    }

    public List<DocPages> getPages() {
        return pages;
    }

    public void setPages(List<DocPages> pages) {
        this.pages = pages;
    }
}
