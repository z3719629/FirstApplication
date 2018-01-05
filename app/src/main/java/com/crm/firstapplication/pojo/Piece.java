package com.crm.firstapplication.pojo;

/**
 * 块，代表图片中的一小部分
 */
public class Piece {
    /**
     * 0～ の連番
     */
    private int no;

    private String img_url;


    /**
     * 左上のx座標
     */
    private double x1;
    /**
     * 左上のy座標
     */
    private double y1;
    /**
     * 右上のx座標
     */
    private double x2;
    /**
     * 右上のy座標
     */
    private double y2;
    /**
     * 左下のx座標
     */
    private double x3;
    /**
     * 左下のy座標
     */
    private double y3;
    /**
     * 右下のx座標
     */
    private double x4;
    /**
     * 右下のy座標
     */
    private double y4;

    public Piece() {

    }

    /**
     * @param x1 左上のx座標
     * @param y1 左上のy座標
     * @param x2 右上のx座標
     * @param y2 右上のy座標
     * @param x3 左下のx座標
     * @param y3 左下のy座標
     * @param x4 右下のx座標
     * @param y4 右下のy座標
     */
    public Piece(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.x4 = x4;
        this.y4 = y4;
    }

    /**
     * @param no 0～ の連番
     * @param x1 左上のx座標
     * @param y1 左上のy座標
     * @param x2 右上のx座標
     * @param y2 右上のy座標
     * @param x3 左下のx座標
     * @param y3 左下のy座標
     * @param x4 右下のx座標
     * @param y4 右下のy座標
     */
    public Piece(int no, double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        this.no = no;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.x4 = x4;
        this.y4 = y4;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public double getX3() {
        return x3;
    }

    public void setX3(double x3) {
        this.x3 = x3;
    }

    public double getY3() {
        return y3;
    }

    public void setY3(double y3) {
        this.y3 = y3;
    }

    public double getX4() {
        return x4;
    }

    public void setX4(double x4) {
        this.x4 = x4;
    }

    public double getY4() {
        return y4;
    }

    public void setY4(double y4) {
        this.y4 = y4;
    }
}
