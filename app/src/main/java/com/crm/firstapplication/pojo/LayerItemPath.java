package com.crm.firstapplication.pojo;

/**
 * 図形パス情報
 */
public class LayerItemPath {

    /**
     * （0：始点アンカーポイント｜1：その他のアンカーポイント|2：コントロールポイント）
     */
    private int type;
    /**
     * x座標（ミリ）
     */
    private double x;
    /**
     * y座標（ミリ）
     */
    private double y;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
}
