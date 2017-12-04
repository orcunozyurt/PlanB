package com.nerdz.planb;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.gson.Gson;
import com.nerdz.planb.models.CryptoCurrencyData;
import com.nerdz.planb.models.HistoricalData;
import com.nerdz.planb.utils.ApiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PriceChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PriceChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PriceChartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RelativeLayout multiCurrency;
    private LinearLayout currencyPicker;
    private TextView tvCurrencyBitcoin;
    private TextView tvCurrencyEther;
    private TextView tvCurrencyLitecoin;
    private RelativeLayout priceContainerLayout;
    private LinearLayout rlPriceSection;
    private LinearLayout priceContainer;
    private TextView tvPriceCurrencySymbol;
    private TextView tvCurrentPrice;
    private TextView tvCryptoPrice;
    private LinearLayout priceChangeContainer;
    private TextView tvPriceChangeCurrencySymbol;
    private TextView tvPriceChange;
    private TextView tvChangeScope;
    private RelativeLayout rlPriceHighlightSection;
    private LinearLayout priceHighlightContainer;
    private TextView tvHighlightedPriceCurrencySymbol;
    private TextView tvHighlightedPrice;
    private TextView tvHighlightedDate;
    private RelativeLayout rangeSection;
    private LinearLayout rangePicker;
    private TextView tvRangeHour;
    private TextView tvRangeDay;
    private TextView tvRangeWeek;
    private TextView tvRangeMonth;
    private TextView tvRangeYear;
    private TextView tvRangeAll;
    private LineChart mChart;
    private List<HistoricalData> mHistoricalDataList = new ArrayList<>();
    private SharedPreferences prefs;

    private String mParam1;
    private String mParam2;
    private static String mCurrencyPref = "BTC"; // App starts with BTC Currency default
    private static String mPeriodPref = "daily"; // App starts with daily period default

    private OnFragmentInteractionListener mListener;

    public PriceChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PriceChartFragment.
     */

    public static PriceChartFragment newInstance(String param1, String param2) {
        PriceChartFragment fragment = new PriceChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_price_chart, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefs = getContext().getSharedPreferences(
                "bitwatch", Context.MODE_PRIVATE);

        initUI(view);
        prepareUI();
        Timer timer = new Timer();

        // get data and update ui in every 50 seconds.
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(isAdded()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startDataFlow();
                        }
                    });
                }
            }
        };


        timer.schedule(timerTask, 0, 50000);


        prepareChart();


    }


    /**
     * Layout initialization is provided in this method.
     * @param view parent view
     */
    public void initUI(View view){

        multiCurrency = (RelativeLayout) view.findViewById(R.id.multi_currency);
        currencyPicker = (LinearLayout) view.findViewById(R.id.currency_picker);
        tvCurrencyBitcoin = (TextView) view.findViewById(R.id.tvCurrencyBitcoin);
        tvCurrencyEther = (TextView) view.findViewById(R.id.tvCurrencyEther);
        tvCurrencyLitecoin = (TextView) view.findViewById(R.id.tvCurrencyLitecoin);
        priceContainerLayout = (RelativeLayout) view.findViewById(R.id.price_container_layout);
        rlPriceSection = (LinearLayout) view.findViewById(R.id.rlPriceSection);
        priceContainer = (LinearLayout) view.findViewById(R.id.price_container);
        tvPriceCurrencySymbol = (TextView) view.findViewById(R.id.tvPriceCurrencySymbol);
        tvCurrentPrice = (TextView) view.findViewById(R.id.tvCurrentPrice);
        tvCryptoPrice = (TextView) view.findViewById(R.id.tvCryptoPrice);
        priceChangeContainer = (LinearLayout) view.findViewById(R.id.price_change_container);
        tvPriceChangeCurrencySymbol = (TextView) view.findViewById(R.id.tvPriceChangeCurrencySymbol);
        tvPriceChange = (TextView) view.findViewById(R.id.tvPriceChange);
        tvChangeScope = (TextView) view.findViewById(R.id.tvChangeScope);
        rlPriceHighlightSection = (RelativeLayout) view.findViewById(R.id.rlPriceHighlightSection);
        priceHighlightContainer = (LinearLayout) view.findViewById(R.id.price_highlight_container);
        tvHighlightedPriceCurrencySymbol = (TextView) view.findViewById(R.id.tvHighlightedPriceCurrencySymbol);
        tvHighlightedPrice = (TextView) view.findViewById(R.id.tvHighlightedPrice);
        tvHighlightedDate = (TextView) view.findViewById(R.id.tvHighlightedDate);
        rangeSection = (RelativeLayout) view.findViewById(R.id.range_section);
        rangePicker = (LinearLayout) view.findViewById(R.id.range_picker);
        tvRangeDay = (TextView) view.findViewById(R.id.tvRangeDay);
        tvRangeMonth = (TextView) view.findViewById(R.id.tvRangeMonth);
        tvRangeAll = (TextView) view.findViewById(R.id.tvRangeAll);
        mChart = (LineChart) view.findViewById(R.id.vPriceChart);





    }


    /**
     * Initial UI preparations for setting click listeners
     * and clicable tv states
     */
    public void prepareUI(){


        rlPriceHighlightSection.setVisibility(View.GONE);
        rlPriceSection.setVisibility(View.VISIBLE);

        if(mPeriodPref.equalsIgnoreCase("daily")){
            tvRangeDay.setSelected(true);
            tvRangeMonth.setSelected(false);
            tvRangeAll.setSelected(false);

        }else if(mPeriodPref.equalsIgnoreCase("monthly")){
            tvRangeDay.setSelected(false);
            tvRangeMonth.setSelected(true);
            tvRangeAll.setSelected(false);

        }else if(mPeriodPref.equalsIgnoreCase("alltime")){
            tvRangeDay.setSelected(false);
            tvRangeMonth.setSelected(false);
            tvRangeAll.setSelected(true);

        }

        if(mCurrencyPref.equalsIgnoreCase("BTC")){
            tvCurrencyBitcoin.setSelected(true);
            tvCurrencyEther.setSelected(false);
            tvCurrencyLitecoin.setSelected(false);

        }else if(mCurrencyPref.equalsIgnoreCase("ETH")){
            tvCurrencyBitcoin.setSelected(false);
            tvCurrencyEther.setSelected(true);
            tvCurrencyLitecoin.setSelected(false);

        }else if(mCurrencyPref.equalsIgnoreCase("LTC")){
            tvCurrencyBitcoin.setSelected(false);
            tvCurrencyEther.setSelected(false);
            tvCurrencyLitecoin.setSelected(true);

        }

        tvRangeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPeriodPref = "daily";
                changeDataScope();
                view.setSelected(true);
                tvRangeMonth.setSelected(false);
                tvRangeAll.setSelected(false);

            }
        });
        tvRangeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPeriodPref = "monthly";
                changeDataScope();
                view.setSelected(true);
                tvRangeDay.setSelected(false);
                tvRangeAll.setSelected(false);

            }
        });
        tvRangeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPeriodPref = "alltime";
                changeDataScope();
                view.setSelected(true);
                tvRangeDay.setSelected(false);
                tvRangeMonth.setSelected(false);

            }
        });

        tvCurrencyBitcoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCurrencyPref = "BTC";
                startDataFlow();
                view.setSelected(true);
                tvCurrencyEther.setSelected(false);
                tvCurrencyLitecoin.setSelected(false);

            }
        });

        tvCurrencyEther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCurrencyPref = "ETH";
                startDataFlow();
                view.setSelected(true);
                tvCurrencyBitcoin.setSelected(false);
                tvCurrencyLitecoin.setSelected(false);

            }
        });

        tvCurrencyLitecoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCurrencyPref = "LTC";
                startDataFlow();
                view.setSelected(true);
                tvCurrencyBitcoin.setSelected(false);
                tvCurrencyEther.setSelected(false);

            }
        });


    }

    /**
     * Method prepares the Chart that is seen on screen
     * A line Chart With X and Y Axis
     */
    public void prepareChart(){

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);


        // OnClick event is consumed when user removes contact with chart.
        // Good time to fade out chart data and fade in current price Data
        // Simple animation is provided for smooth transition
        mChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("YYYY", "onClick: ");
                rlPriceHighlightSection.animate().setDuration(200).alpha(0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rlPriceHighlightSection.setVisibility(View.GONE);

                    }
                });
                rlPriceSection.animate().setDuration(200).alpha(1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rlPriceSection.setVisibility(View.VISIBLE);

                    }
                });
            }
        });

        // On Chart Value selected, when user selects a point on chart
        // Good time to fade out current price Data and fade in chart data
        // Simple animation is provided for smooth transition
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(final Entry e, Highlight h) {

                Log.d("YYYY", "onValueSelected: "+e.getY());
                rlPriceHighlightSection.animate().setDuration(200).alpha(1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rlPriceHighlightSection.setVisibility(View.VISIBLE);

                        tvHighlightedPrice.setText(String.valueOf(e.getY()));
                        tvHighlightedDate.setText(String.valueOf(e.getX()));
                    }
                });
                rlPriceSection.animate().setDuration(200).alpha(0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        rlPriceSection.setVisibility(View.GONE);

                    }
                });

            }

            @Override
            public void onNothingSelected() {

            }
        });


    }

    /**
     * Method calls async http request methods
     * gets recent price and historical data at the same time
     * for drawing a chart.
     */
    public void startDataFlow(){

        getRecentCurrencyData(mCurrencyPref);
        getHistoricalCurrencyData(mCurrencyPref,mPeriodPref);

    }

    /**
     * Method gets Historical Data again
     * with changed currency or period
     *
     */
    public void changeDataScope(){

        getHistoricalCurrencyData(mCurrencyPref,mPeriodPref);

    }

    /**
     * updates current price and change views
     * adds those values in sharedpreferences
     * so that in mainactivity share function
     * can create a nice text to share
     * @param current current price
     * @param change change in price (-,+)
     */
    private void updateCurrentPriceUI(Double current, Double change) {

        tvCurrentPrice.setText(String.valueOf(current));
        tvPriceChange.setText(String.valueOf(change));
        if(change < 0){
            tvPriceChange.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.indicator_down, 0);
        }else if(change > 0){
            tvPriceChange.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.indicator_up, 0);
        }

        prefs.edit().putString("ccurrency", mCurrencyPref).apply();
        prefs.edit().putString("price", String.valueOf(current)).apply();
        prefs.edit().putString("change",
                String.valueOf(change)).apply();
        if(change>0) {
            prefs.edit().putString("trend", "UP!").apply();
        }else {
            prefs.edit().putString("trend", "DOWN!").apply();
        }
    }


    /**
     * Use this method to provide data to linechart
     *
     */
    private void setData() {

        ArrayList<Entry> values = new ArrayList<Entry>();

        float last = 0;

        // We dont want same different values for same date
        // Since daily data is incremented by minutes in api
        // when you filter hourly, there are many data for same hour
        // It creates problem on scope of chart
        // therefore eleminate if (t == last)

        for (HistoricalData hd : mHistoricalDataList) {

            long t = 0;
            if(mPeriodPref.equalsIgnoreCase("daily")){
                t = TimeUnit.MILLISECONDS.toHours(hd.getStamp());

            }else if(mPeriodPref.equalsIgnoreCase("monthly")){
                t = TimeUnit.MILLISECONDS.toHours(hd.getStamp());

            }else if(mPeriodPref.equalsIgnoreCase("alltime")){
                t = TimeUnit.MILLISECONDS.toDays(hd.getStamp());

            }

            BigDecimal number = new BigDecimal(String.valueOf(hd.getAverage()));

            float y = number.floatValue();


            if((float)t != last) {
                //Log.d("PRICECHART", "setData: "+ y + " - "+(float)t + " - "+ hd.getStamp());
                values.add(new Entry((float) t, y)); // add one entry per hour
                last = (float)t;
            }
        }

        // library has a bug. Thats why we need to sort it before use
        Collections.sort(values, new EntryXComparator());

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(Color.WHITE);
        set1.setValueTextColor(Color.WHITE);
        set1.setLineWidth(1f);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setDrawHorizontalHighlightIndicator(false);
        set1.setHighlightEnabled(true);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.WHITE);
        set1.setDrawCircleHole(false);
        set1.disableDashedLine();

        // create a data object with the datasets
        LineData data = new LineData(set1);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        mChart.setData(data);
        mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f); // one hour
        if(mPeriodPref.equalsIgnoreCase("daily")) {
            xAxis.setValueFormatter(new IAxisValueFormatter() {

                private SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm");

                @Override
                public String getFormattedValue(float value, AxisBase axis) {

                    long millis = TimeUnit.HOURS.toMillis((long) value);
                    return mFormat.format(new Date(millis));
                }
            });
        }else if(mPeriodPref.equalsIgnoreCase("monthly")){
            xAxis.setValueFormatter(new IAxisValueFormatter() {

                private SimpleDateFormat mFormat = new SimpleDateFormat("ddMMM");

                @Override
                public String getFormattedValue(float value, AxisBase axis) {

                    long millis = TimeUnit.HOURS.toMillis((long) value);
                    return mFormat.format(new Date(millis));
                }
            });

        }else{

            xAxis.setValueFormatter(new IAxisValueFormatter() {

                private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy");

                @Override
                public String getFormattedValue(float value, AxisBase axis) {

                    long millis = TimeUnit.DAYS.toMillis((long) value);
                    return mFormat.format(new Date(millis));
                }
            });
        }

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawAxisLine(true);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(set1.getYMin());
        leftAxis.setAxisMaximum(set1.getYMax());
        leftAxis.setYOffset(-9f);


        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);


    }


    private View getPriceSeparator(){
        return (View) getView().findViewById(R.id.price_separator);
    }


    /**
     * Material progress spinner is in MainActivity
     * Here we can communicate through this interface method
     * @param shouldShow should show spinner or not
     */
    public void loadStatusChange(Boolean shouldShow) {
        if (mListener != null) {
            mListener.onFragmentInteraction(shouldShow);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Boolean shouldShow);
    }

    /**
     * Sends a HTTP  GET Request to endpoint to get current currency data
     * @param CCURRENCY Currency type : BTC, ETH, LTC
     */
    public void getRecentCurrencyData(String CCURRENCY){

        String tag_json_arry = "GLOBAL";
        String url = ApiUtils.BASEURL + "/indices/global/ticker/"+CCURRENCY+"USD";

        Log.d("URL", "-->"+url);
        loadStatusChange(true);


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE:", response.toString());


                        Gson gson = new Gson();

                        CryptoCurrencyData ccd = gson.fromJson(String.valueOf(response),
                                CryptoCurrencyData.class);


                        Log.d("GSONPARSEDEBUG", "-------->>"+ccd.toString());


                        updateCurrentPriceUI(ccd.getAsk(), ccd.getChanges().getPrice().getDaily());




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        try {
                            loadStatusChange(false);
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                }) {


            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        PlanBApplication.getInstance().addToRequestQueue(jsObjRequest, tag_json_arry);



    }

    /**
     * Sends HTTP GET Request to endpoint to get historical data
     * @param CCURRENCY BTC,ETH,LTC
     * @param period daily, monthly, alltime
     */
    public void getHistoricalCurrencyData(String CCURRENCY, String period){

        String tag_json_arry = "GLOBAL";
        String url = ApiUtils.BASEURL + "/indices/global/history/"+CCURRENCY+"USD?period="+period+"&format=json";

        Log.d("URL", "-->"+url);
        loadStatusChange(true);


        JsonArrayRequest jsArrRequest = new JsonArrayRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("RESPONSE:", response.toString());

                        try {
                            loadStatusChange(false);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        mHistoricalDataList.clear();

                        Gson gson = new Gson();

                        for( int i = 0; i < response.length(); i++){

                            try {
                                HistoricalData hd = gson.fromJson(
                                        String.valueOf(response.get(i)),HistoricalData.class);

                                mHistoricalDataList.add(hd);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        setData();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        try {
                            loadStatusChange(false);
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                    }
                }) {


            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        jsArrRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        PlanBApplication.getInstance().addToRequestQueue(jsArrRequest, tag_json_arry);



    }
}
