package com.nerdz.planb;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.nerdz.planb.auth.Authentication;
import com.nerdz.planb.models.Ticker;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        PriceChartFragment.OnFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String secretKey = getResources().getString(R.string.secretkey);
        String publicKey = getResources().getString(R.string.publickey);
        try {
            String signature = Authentication.getSignature(secretKey, publicKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
