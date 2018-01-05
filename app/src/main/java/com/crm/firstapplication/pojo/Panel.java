package com.crm.firstapplication.pojo;

/**
 * 画像リスト
 */
public class Panel {

    /**
     * 画像ID
     */
    private String id;

    /**
     * 画像名
     */
    private String name;

    /**
     * ページの数
     */
    private Integer page;

    /**
     * 画像url
     */
    private String url;

    public Panel() {
    }

    /**
     * @param id 画像ID
     * @param name 画像名
     * @param url 画像url
     */
    public Panel(String id, String name, Integer page, String url) {
        this.id = id;
        this.name = name;
        this.page = page;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPage() {
        return page;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
