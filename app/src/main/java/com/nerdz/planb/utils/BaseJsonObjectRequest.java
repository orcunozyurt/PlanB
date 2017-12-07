package com.nerdz.planb.utils;

import android.app.Activity;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by btdays on 12/7/17.
 */

public class BaseJsonObjectRequest extends JsonObjectRequest {
    Activity Act;
    Priority priority;

    public BaseJsonObjectRequest(int method, String url,
                                 JSONObject jsonRequest, Response.Listener<JSONObject> listener,
                                 Response.ErrorListener errorListener, Activity act, Priority p) {
        super(method, url, jsonRequest, listener, errorListener);
        this.Act=act;
        this.priority=p;
    }

    public BaseJsonObjectRequest(int method, String url,
                                 Response.Listener<JSONObject> listener,
                                 Response.ErrorListener errorListener, Activity act, Priority p) {
        super(method, url, null, listener, errorListener);
        this.Act=act;
        this.priority=p;
    }

    @Override
    public Map<String, String> getHeaders()  {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }

    //it make posible send parameters into the body.
    @Override
    public Priority getPriority(){
        return priority;
    }

    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String je = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(je), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException var3) {
            return Response.error(new ParseError(var3));
        } catch (JSONException var4) {
            return Response.error(new ParseError(var4));
        }
    }
}
