package com.nerdz.planb.services;

import com.nerdz.planb.models.Ticker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by orcunozyurt on 12/3/17.
 */

public interface RESTTickerService {

    @GET("/websocket/get_ticket")
    Call<Ticker> getTicker();
}
