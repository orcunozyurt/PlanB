package com.nerdz.planb;

import android.content.Context;
import android.net.Uri;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.google.gson.Gson;
import com.nerdz.planb.models.CryptoCurrencyData;
import com.nerdz.planb.utils.ApiUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


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
    private LineChart vPriceChart;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    // TODO: Rename and change types and number of parameters
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
        tvRangeHour = (TextView) view.findViewById(R.id.tvRangeHour);
        tvRangeDay = (TextView) view.findViewById(R.id.tvRangeDay);
        tvRangeWeek = (TextView) view.findViewById(R.id.tvRangeWeek);
        tvRangeMonth = (TextView) view.findViewById(R.id.tvRangeMonth);
        tvRangeYear = (TextView) view.findViewById(R.id.tvRangeYear);
        tvRangeAll = (TextView) view.findViewById(R.id.tvRangeAll);
        vPriceChart = (LineChart) view.findViewById(R.id.vPriceChart);

        getCryptoCurrencyData("BTC");

    }

    private View getPriceSeparator(){
        return (View) getView().findViewById(R.id.price_separator);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    // Gets Data from our endpoint
    public void getCryptoCurrencyData(String CCURRENCY){

        String tag_json_arry = "GLOBAL";
        String url = ApiUtils.BASEURL + "/indices/global/ticker/"+CCURRENCY+"USD";

        Log.d("URL", "-->"+url);


        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE:", response.toString());

                        Gson gson = new Gson();

                        CryptoCurrencyData ccd = gson.fromJson(String.valueOf(response),
                                CryptoCurrencyData.class);


                        Log.d("GSONPARSEDEBUG", "-------->>"+ccd.toString());

                        tvCurrentPrice.setText(String.valueOf(ccd.getAsk()));
                        tvPriceChange.setText(String.valueOf(ccd.getChanges().getPrice().getDaily()));


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


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
}
