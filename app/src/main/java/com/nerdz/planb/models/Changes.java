package com.nerdz.planb.models;

/**
 * Created by orcunozyurt on 12/3/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Changes {

    @SerializedName("price")
    @Expose
    private Price price;
    @SerializedName("percent")
    @Expose
    private Percent percent;

    public Changes() {
    }

    @Override
    public String toString() {
        return "Changes{" +
                "price=" + price +
                ", percent=" + percent +
                '}';
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Percent getPercent() {
        return percent;
    }

    public void setPercent(Percent percent) {
        this.percent = percent;
    }

}
