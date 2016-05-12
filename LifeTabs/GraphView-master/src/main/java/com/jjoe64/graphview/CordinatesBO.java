package com.jjoe64.graphview;

/**
 * Created by Tayyab on 3/11/2016.
 */
public class CordinatesBO {
    float x;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    float y;

    int dataPointX;
    int dataPointY;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getColorType() {
        return colorType;
    }

    public void setColorType(int colorType) {
        this.colorType = colorType;
    }

    public int getDataPointY() {
        return dataPointY;
    }

    public void setDataPointY(int dataPointY) {
        this.dataPointY = dataPointY;
    }

    public int getDataPointX() {
        return dataPointX;
    }

    public void setDataPointX(int dataPointX) {
        this.dataPointX = dataPointX;
    }

    int value;
    int colorType;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

}
