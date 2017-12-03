package com.nerdz.planb.utils;

import com.nerdz.planb.services.RESTTickerService;
import com.nerdz.planb.services.RetrofitClient;

/**
 * Created by orcunozyurt on 12/3/17.
 */

public class ApiUtils {

    public static final String BASE_URL = "https://apiv2.bitcoinaverage.com/";

    public static RESTTickerService getSOService() {
        return RetrofitClient.getClient(BASE_URL).create(RESTTickerService.class);
    }

}
