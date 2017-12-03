package com.nerdz.planb.models;

/**
 * Created by orcunozyurt on 12/3/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Open {

    @SerializedName("day")
    @Expose
    private Double day;
    @SerializedName("week")
    @Expose
    private Double week;
    @SerializedName("month")
    @Expose
    private Double month;

    public Open() {
    }

    @Override
    public String toString() {
        return "Open{" +
                "day='" + day + '\'' +
                ", week='" + week + '\'' +
                ", month='" + month + '\'' +
                '}';
    }

    public Double getDay() {
        return day;
    }

    public void setDay(Double day) {
        this.day = day;
    }

    public Double getWeek() {
        return week;
    }

    public void setWeek(Double week) {
        this.week = week;
    }

    public Double getMonth() {
        return month;
    }

    public void setMonth(Double month) {
        this.month = month;
    }
}
