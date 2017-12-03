package com.nerdz.planb.models;

/**
 * Created by orcunozyurt on 12/3/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Percent {

    @SerializedName("week")
    @Expose
    private Double weekly;
    @SerializedName("month")
    @Expose
    private Double monthly;
    @SerializedName("day")
    @Expose
    private Double daily;

    public Percent() {
    }

    @Override
    public String toString() {
        return "Percent{" +
                "weekly=" + weekly +
                ", monthly=" + monthly +
                ", daily=" + daily +
                '}';
    }

    public Double getWeekly() {
        return weekly;
    }

    public void setWeekly(Double weekly) {
        this.weekly = weekly;
    }

    public Double getMonthly() {
        return monthly;
    }

    public void setMonthly(Double monthly) {
        this.monthly = monthly;
    }

    public Double getDaily() {
        return daily;
    }

    public void setDaily(Double daily) {
        this.daily = daily;
    }

}
