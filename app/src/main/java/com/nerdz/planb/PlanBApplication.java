package com.nerdz.planb;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by orcunozyurt on 12/3/17.
 */

public class PlanBApplication extends Application {
    public static final String TAG = PlanBApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static PlanBApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }
    public static synchronized PlanBApplication getInstance() {
        if (mInstance == null) {
            mInstance = new PlanBApplication();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
