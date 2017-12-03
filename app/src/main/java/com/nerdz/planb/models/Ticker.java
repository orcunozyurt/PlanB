package com.nerdz.planb.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by orcunozyurt on 12/3/17.
 */

public class Ticker {
    @SerializedName("ticket")
    @Expose
    private String ticket;

    public Ticker() {
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
