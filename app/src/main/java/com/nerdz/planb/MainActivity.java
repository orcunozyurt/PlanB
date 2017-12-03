package com.nerdz.planb;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.nerdz.planb.auth.Authentication;
import com.nerdz.planb.utils.ApiUtils;


import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements
        PriceChartFragment.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        String secretKey = getResources().getString(R.string.secretkey);
//        String publicKey = getResources().getString(R.string.publickey);
//
//        try {
//            String xxx = Authentication.encode("abcdef123456","1234.YzQxNGYyMGI1YzJjNDg3YThkOGU1MTgwZWNhYjY4ODI=");
//            Log.d(getLocalClassName(), "XXXXXXX: "+xxx);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            String signature = Authentication.getSignature(secretKey, publicKey);
//            Log.d(getLocalClassName(), "SIGNATURE: "+ signature);
//            getTicket(signature);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }



}
