package com.nerdz.planb;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.nerdz.planb.auth.Authentication;
import com.nerdz.planb.ui.MaterialProgressBar;
import com.nerdz.planb.utils.ApiUtils;


import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements
        PriceChartFragment.OnFragmentInteractionListener {

    private MaterialProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        // Please see PriceChartFragment, layout uses  PriceChartFragment as nested fragment.
        */
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onFragmentInteraction(Boolean shouldShow) {

        progressBar = (MaterialProgressBar) findViewById(R.id.progress);

        try{
            if(shouldShow){

                progressBar.setVisibility(View.VISIBLE);

            }else{

                progressBar.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }



}
