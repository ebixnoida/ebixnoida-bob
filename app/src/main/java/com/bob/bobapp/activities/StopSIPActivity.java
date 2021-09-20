package com.bob.bobapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.StopSIPListAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.StopSipRequestObject;
import com.bob.bobapp.api.request_object.OrdersObject;
import com.bob.bobapp.api.request_object.SIPSWPSTPRequestBodyModel;
import com.bob.bobapp.api.request_object.SIPSWPSTPRequestModel;
import com.bob.bobapp.api.request_object.StopSipBodyObject;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.SIPDueReportResponse;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.FontManager;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StopSIPActivity extends BaseFragment {
    private TextView calender, buy, swp;
    private Button btnStop;
    private RecyclerView rv;
    private StopSIPListAdapter adapter;
    private ArrayList<SIPDueReportResponse> sipArrayList = new ArrayList<>();
    private ArrayList<SIPDueReportResponse> stpArrayList = new ArrayList<>();
    private APIInterface apiInterface;
    private Util util;
    private LinearLayout llBuy, llSWP, viewBuy, viewSWP;
    double count = 0;
    private Context context;
    ArrayList<OrdersObject> requestBodyObjectArrayList = new ArrayList<OrdersObject>();
    private String orderStatus = "1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.activity_stop_s_i_p, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void getIds(View view) {

        calender = view.findViewById(R.id.calender);

        rv = view.findViewById(R.id.rv);

        llBuy = view.findViewById(R.id.llBuy);
        llSWP = view.findViewById(R.id.llSWP);
        buy = view.findViewById(R.id.buy);
        swp = view.findViewById(R.id.swp);
        viewBuy = view.findViewById(R.id.viewBuy);
        viewSWP = view.findViewById(R.id.viewSWP);
        btnStop = view.findViewById(R.id.btnStop);

    }

    @Override
    public void handleListener() {
        BOBActivity.imgBack.setOnClickListener(this);
        llBuy.setOnClickListener(this);
        llSWP.setOnClickListener(this);
        btnStop.setOnClickListener(this);

    }

    @Override
    public void initializations() {
        BOBActivity.llMenu.setVisibility(View.GONE);
        BOBActivity.title.setText("Stop SIP");
        apiInterface = BOBApp.getApi(context, Constants.ACTION_SIP_SWP_STP_DUE);
        util = new Util(context);
        getApiCall();
    }

    @Override
    public void setIcon(Util util) {
        FontManager.markAsIconContainer(calender, util.iconFont);
    }

    private void getApiCall() {
        util.showProgressDialog(context, true);
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;
        SIPSWPSTPRequestBodyModel requestBodyModel = new SIPSWPSTPRequestBodyModel();
        requestBodyModel.setUserId("admin");
        requestBodyModel.setClientCode(Integer.parseInt(authenticateResponse.getUserCode()));
        requestBodyModel.setClientType("H");
        requestBodyModel.setFamCode(0);
        requestBodyModel.setFromDate("2020/06/14");
        requestBodyModel.setHeadCode(32);
        requestBodyModel.setReportType("SUMMARY");
        requestBodyModel.setToDate("2020/06/21");

        SIPSWPSTPRequestModel model = new SIPSWPSTPRequestModel();
        model.setRequestBodyObject(requestBodyModel);
        model.setSource(Constants.SOURCE);
        UUID uuid = UUID.randomUUID();
        String uniqueIdentifier = String.valueOf(uuid);
        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);
        model.setUniqueIdentifier(uniqueIdentifier);


        apiInterface.getSIPDueReportApiCall(model).enqueue(new Callback<ArrayList<SIPDueReportResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SIPDueReportResponse>> call, Response<ArrayList<SIPDueReportResponse>> response) {
                util.showProgressDialog(context, false);

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {

                    for (SIPDueReportResponse item : response.body()) {
                        if (item.getType().equalsIgnoreCase("swp")) {
                            stpArrayList.add(item);
                        } else if (item.getType().equalsIgnoreCase("SIP")) {
                            sipArrayList.add(item);
                        }

                    }
                    setAdapter(sipArrayList);

                    if (response.body() != null) {
                        btnStop.setVisibility(View.VISIBLE);
                    } else {
                        btnStop.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SIPDueReportResponse>> call, Throwable t) {
                util.showProgressDialog(context, false);
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setAdapter(ArrayList<SIPDueReportResponse> arrayList) {

        if (arrayList != null && arrayList.size() > 0) {
            adapter = new StopSIPListAdapter(context, arrayList);
            rv.setAdapter(adapter);
        } else {
            Toast.makeText(context, "No data found", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.menu) {
            getActivity().onBackPressed();
        } else if (id == R.id.llBuy) {
            orderStatus = "1";
            buy.setTextColor(getResources().getColor(R.color.black));
            swp.setTextColor(getResources().getColor(R.color.colorGray));
            viewBuy.setBackgroundColor(getResources().getColor(R.color.color_light_orange));
            viewSWP.setBackgroundColor(getResources().getColor(R.color.colorGray));
            adapter.updateList(sipArrayList);
        } else if (id == R.id.llSWP) {
            orderStatus = "2";
            buy.setTextColor(getResources().getColor(R.color.colorGray));
            swp.setTextColor(getResources().getColor(R.color.black));
            viewBuy.setBackgroundColor(getResources().getColor(R.color.colorGray));
            viewSWP.setBackgroundColor(getResources().getColor(R.color.color_light_orange));
            adapter.updateList(stpArrayList);
        } else if (id == R.id.imgBack) {
            getActivity().onBackPressed();
        } else if (id == R.id.btnStop) {
            if (orderStatus.equalsIgnoreCase("1")) {
                requestBodyObjectArrayList.clear();
                count = 0;
                for (SIPDueReportResponse model : sipArrayList) {
                    if (model.isSelected()) {
                        count = count + 1;
                        OrdersObject requestBodyObject = new OrdersObject();
                        requestBodyObject.setNextInstallmentDate(model.getNextInstallmentDate());
                        requestBodyObject.setRequestId(model.getRequestId());
                        requestBodyObject.setCode("" + model.getSchemeCode());
                        requestBodyObject.setOrderNumber(model.getOrderNo());
                        requestBodyObject.setTransactionType(9);
                        requestBodyObject.setFundCode(0);
                        requestBodyObjectArrayList.add(requestBodyObject);
                    }
                }

                if (count > 0) {
                    stopSipAPIResponse();
                } else {
                    Toast.makeText(getContext(), "please select fund", Toast.LENGTH_SHORT).show();
                }
            } else {
                requestBodyObjectArrayList.clear();
                count = 0;
                for (SIPDueReportResponse model : stpArrayList) {
                    if (model.isSelected()) {
                        count = count + 1;
                        OrdersObject requestBodyObject = new OrdersObject();
                        requestBodyObject.setNextInstallmentDate(model.getNextInstallmentDate());
                        requestBodyObject.setRequestId(model.getRequestId());
                        requestBodyObject.setCode("" + model.getSchemeCode());
                        requestBodyObject.setOrderNumber(model.getOrderNo());
                        requestBodyObject.setTransactionType(12);
                        requestBodyObject.setFundCode(0);
                        requestBodyObjectArrayList.add(requestBodyObject);
                    }
                }

                if (count > 0) {
                    stopSWPAPIResponse();
                } else {
                    Toast.makeText(getContext(), "please select fund", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }


    // api calling
    private void stopSipAPIResponse() {

        util.showProgressDialog(context, true);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_STOP_SIP);

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        StopSipBodyObject stopSipBodyObject = new StopSipBodyObject();

        stopSipBodyObject.setRequestBodyObjectArrayList(requestBodyObjectArrayList);

        stopSipBodyObject.setFundCode("0");
        stopSipBodyObject.setClientCode(authenticateResponse.getUserCode());


        StopSipRequestObject globalRequestObject = new StopSipRequestObject();

        globalRequestObject.setRequestBodyObject(stopSipBodyObject);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        System.out.println("REQUEST :" + new Gson().toJson(globalRequestObject));

        apiInterface.stopSip(globalRequestObject).enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    requestBodyObjectArrayList.clear();
                    Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                } else {
                    requestBodyObjectArrayList.clear();
                    System.out.println("VALIDATION RESPONSEsss: " + response.message());
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

                util.showProgressDialog(context, false);

                System.out.println("VALIDATION RESPONSEsss: " + t.getLocalizedMessage());


                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // api calling
    private void stopSWPAPIResponse() {

        util.showProgressDialog(context, true);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_STOP_SIP);

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        StopSipBodyObject stopSipBodyObject = new StopSipBodyObject();

        stopSipBodyObject.setRequestBodyObjectArrayList(requestBodyObjectArrayList);

        stopSipBodyObject.setFundCode("0");
        stopSipBodyObject.setClientCode(authenticateResponse.getUserCode());


        StopSipRequestObject globalRequestObject = new StopSipRequestObject();

        globalRequestObject.setRequestBodyObject(stopSipBodyObject);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        System.out.println("REQUEST :" + new Gson().toJson(globalRequestObject));

        apiInterface.stopSwp(globalRequestObject).enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    requestBodyObjectArrayList.clear();
                    Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                } else {
                    requestBodyObjectArrayList.clear();
                    System.out.println("VALIDATION RESPONSEsss: " + response.message());
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

                util.showProgressDialog(context, false);

                System.out.println("VALIDATION RESPONSEsss: " + t.getLocalizedMessage());


                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

