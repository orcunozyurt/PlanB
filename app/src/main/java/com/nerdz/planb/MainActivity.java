package com.nerdz.planb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nerdz.planb.ui.MaterialProgressBar;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;


public class MainActivity extends AppCompatActivity implements
        PriceChartFragment.OnFragmentInteractionListener, View.OnClickListener {

    private MaterialProgressBar progressBar;

    private TextView tvPleaseWait;
    private RelativeLayout rlContainer;
    private RelativeLayout rlChartContainer;
    private ImageView logo;
    private LinearLayout bottomSection;
    private TextView tvLoginIntroSignin;

    private String price;
    private String change;
    private String ccurrency;
    private String trend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvPleaseWait = (TextView) findViewById(R.id.tvPleaseWait);
        rlContainer = (RelativeLayout) findViewById(R.id.rlContainer);
        rlChartContainer = (RelativeLayout) findViewById(R.id.rlChartContainer);
        logo = (ImageView) findViewById(R.id.logo);

        bottomSection = (LinearLayout) findViewById(R.id.bottom_section);
        findViewById(R.id.btnShare).setOnClickListener(this);
        tvLoginIntroSignin = (TextView) findViewById(R.id.tvLoginIntroSignin);
        tvLoginIntroSignin.setOnClickListener(this);

        /*
        // Please see PriceChartFragment, layout uses  PriceChartFragment as nested fragment.
        */


        // App rate behaviour, a nice lib if customized well
        AppRate.with(this)
                .setInstallDays(0) // default 10, 0 means install day.
                .setLaunchTimes(3) // default 10
                .setRemindInterval(2) // default 1
                .setShowLaterButton(true) // default true
                .setDebug(false) // default false
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Log.d(MainActivity.class.getName(), Integer.toString(which));
                    }
                })
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);
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
                tvPleaseWait.setVisibility(View.VISIBLE);

            }else{

                progressBar.setVisibility(View.GONE);
                tvPleaseWait.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnShare:
                onShareClicked();
                break;
            case R.id.tvLoginIntroSignin:
                AppRate.with(this).showRateDialog(this);
                break;

        }

    }

    /**
     * Simple Share Intent with ACTION_SEND
     * Uses current price data to form a share text
     */
    private void onShareClicked() {
        SharedPreferences prefs = this.getSharedPreferences(
                "bitwatch", Context.MODE_PRIVATE);

        ccurrency = prefs.getString("ccurrency","btc");
        price = prefs.getString("price","0");
        change = prefs.getString("change","0");
        trend = prefs.getString("trend","UP");

        Resources res = getResources();
        String text = String.format(res.getString(R.string.share_message),
                ccurrency, change,trend,price,ccurrency);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
