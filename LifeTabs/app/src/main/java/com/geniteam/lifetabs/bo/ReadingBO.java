package com.geniteam.lifetabs.bo;

import java.io.Serializable;

/**
 * Created by Tayyab on 3/10/2016.
 */
public class ReadingBO implements Serializable{

    public ReadingBO(String pdate ,String pTime , String pValue ,String pUnit){
        this.dateString = pdate;
        this.timeString = pTime;
        this.value = pValue;
        this.unit = pUnit;
    }

    String dateString;

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    String timeString;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    String value;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    String unit;


}
