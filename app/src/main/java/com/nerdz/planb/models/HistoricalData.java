package com.nerdz.planb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by orcunozyurt on 12/3/17.
 */

public class HistoricalData {
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("average")
    @Expose
    private Double average;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public long getStamp(){

        int offset = TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = (Date)formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long output=date.getTime()/1000L;
        String str=Long.toString(output );
        long timestamp = (Long.parseLong(str) * 1000) + offset;

        return  timestamp;
    }
}
