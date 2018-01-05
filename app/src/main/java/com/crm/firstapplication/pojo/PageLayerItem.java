package com.crm.firstapplication.pojo;

import java.util.List;

/**
 * アイテム情報
 */
public class PageLayerItem {


    /**
     * アイテムID（画像ID、文字ID）
     */
    private String id;

    /**
     * アイテム種別（0：付箋｜1：手書｜2：文字）
     */
    private int type;
    /**
     * アイテム名
     */
    private String name;
    /**
     * 原点
     */
    private int gen;
    /**
     * x座標
     */
    private double x;
    /**
     * y座標
     */
    private double y;
    /**
     * width
     */
    private int w;
    /**
     * height
     */
    private int h;
    /**
     * 回転角度
     */
    private String deg;
    /**
     * 塗り色種別（0:rgb|1:cmyk)
     */
    private String ftype;
    /**
     * 塗り色
     */
    private String fcolor;
    /**
     * 塗りアルファ値
     */
    private int falpha;
    /**
     * 線色種別（0:rgb|1:cmyk)
     */
    private int stype;
    /**
     * 線色
     */
    private String scolor;
    /**
     * 線アルファ値
     */
    private int salpha;
    /**
     * イメージパス
     */
    private String imgurl;
    /**
     * 画像実サイズ
     */
    private int imgw;
    /**
     * 画像実サイズ
     */
    private int imgh;
    /**
     * 図形パス情報
     */
    private List<LayerItemPath> pathes;
    /**
     * 文字アイテム種別（0：単数行｜1：複数行）
     */
    private int mtype;
    /**
     * 文字原点
     */
    private int mgen;
    /**
     * 文字列
     */
    private String mtext;
    /**
     * フォント名
     */
    private String mfont;
    /**
     * 文字width
     */
    private int mw;
    /**
     * 文字height
     */
    private int mh;
    /**
     * 文字間
     */
    private int mgap;
    /**
     * 行間
     */
    private int ggap;
    /**
     * 文字画像
     */
    private String mimgurl;


    public int getFalpha() {
        return falpha;
    }

    public void setFalpha(int falpha) {
        this.falpha = falpha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGen() {
        return gen;
    }

    public void setGen(int gen) {
        this.gen = gen;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public String getDeg() {
        return deg;
    }

    public void setDeg(String deg) {
        this.deg = deg;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public String getFcolor() {
        return fcolor;
    }

    public void setFcolor(String fcolor) {
        this.fcolor = fcolor;
    }



    public int getStype() {
        return stype;
    }

    public void setStype(int stype) {
        this.stype = stype;
    }

    public String getScolor() {
        return scolor;
    }

    public void setScolor(String scolor) {
        this.scolor = scolor;
    }

    public int getSalpha() {
        return salpha;
    }

    public void setSalpha(int salpha) {
        this.salpha = salpha;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getImgw() {
        return imgw;
    }

    public void setImgw(int imgw) {
        this.imgw = imgw;
    }

    public int getImgh() {
        return imgh;
    }

    public void setImgh(int imgh) {
        this.imgh = imgh;
    }

    public List<LayerItemPath> getPathes() {
        return pathes;
    }

    public void setPathes(List<LayerItemPath> pathes) {
        this.pathes = pathes;
    }

    public int getMtype() {
        return mtype;
    }

    public void setMtype(int mtype) {
        this.mtype = mtype;
    }

    public int getMgen() {
        return mgen;
    }

    public void setMgen(int mgen) {
        this.mgen = mgen;
    }

    public String getMtext() {
        return mtext;
    }

    public void setMtext(String mtext) {
        this.mtext = mtext;
    }

    public String getMfont() {
        return mfont;
    }

    public void setMfont(String mfont) {
        this.mfont = mfont;
    }

    public int getMw() {
        return mw;
    }

    public void setMw(int mw) {
        this.mw = mw;
    }

    public int getMh() {
        return mh;
    }

    public void setMh(int mh) {
        this.mh = mh;
    }

    public int getMgap() {
        return mgap;
    }

    public void setMgap(int mgap) {
        this.mgap = mgap;
    }

    public int getGgap() {
        return ggap;
    }

    public void setGgap(int ggap) {
        this.ggap = ggap;
    }

    public String getMimgurl() {
        return mimgurl;
    }

    public void setMimgurl(String mimgurl) {
        this.mimgurl = mimgurl;
    }
}
