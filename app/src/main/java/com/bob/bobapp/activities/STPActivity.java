package com.bob.bobapp.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.SipDateAdapter;
import com.bob.bobapp.adapters.SipFrequencyAdapter;
import com.bob.bobapp.adapters.SwitchFundAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.request_object.AccountRequestObject;
import com.bob.bobapp.api.request_object.GlobalRequestObject;
import com.bob.bobapp.api.request_object.GlobalRequestObjectArray;
import com.bob.bobapp.api.request_object.MFOrderValidationRequest;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.response_object.AccountResponseObject;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.MFOrderValidationResponse;
import com.bob.bobapp.api.response_object.TranferSchemeResponse;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.IntentKey;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class STPActivity extends BaseFragment {
    private AppCompatTextView txtName, txtFolioNumber, investmentAccountName, txtCurrentFundValue,
            txtSealableUnits, txtAmount, txtUnits;
    private AppCompatEditText edtAMount, edtInstallments;
    private AppCompatSpinner spineerSwitchFund, spineerFrequency, spineerDay;
    private AppCompatButton btnAddToCart;
    private String frequency = "", sipStartDates = "", l4ClientCode, AmountOrUnit = "A", AllorPartial = "P",
            amount, noOfInstallments, finalSipStartDate, IsDividend, LatestNAV, DividendOption, targetFundName,
            targetFundCode, ValueResearchRating, FundOption, selectFrequency = "",finalSipDate,SipDate,year, month,
            dateTime;
    private Context context;
    private Util util;
    private ClientHoldingObject clientHoldingObject;
    private SwitchFundAdapter switchFundAdapter;
    private SipFrequencyAdapter sipFrequencyAdapter;
    private SipDateAdapter sipDateAdapter;
    private ArrayList<TranferSchemeResponse> switchFundArrayList = new ArrayList<>();
    private ArrayList<String> frequencyList;
    private ArrayList<String> sipStartDatesList;
    private ArrayList<String> finalFrequencyList = new ArrayList<>();
    private ArrayList<String> finalSipStartDatesList;
    private ArrayList<AccountResponseObject> accountResponseObjectArrayList = new ArrayList<>();
    private double quantity, AwaitingHoldingUnit;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private DecimalFormat formatter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.stp_activity, container, false);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getExtraDataFromIntent();
        setData();

        getTransferFundResponse();
    }

    // set data here...
    private void setData() {
        txtName.setText(clientHoldingObject.getSchemeName());
        txtFolioNumber.setText(clientHoldingObject.getFolio());
        l4ClientCode = clientHoldingObject.getL4Client_Code();
        txtCurrentFundValue.setText(formatter.format(Double.parseDouble(clientHoldingObject.getMarketValue())));
        quantity = Double.parseDouble(clientHoldingObject.getQuantity());
    }

    // get data
    private void getExtraDataFromIntent() {
        if (getArguments() != null) {
            String response = getArguments().getString(IntentKey.RESPONSE_KEY);
            clientHoldingObject = new Gson().fromJson(response, ClientHoldingObject.class);
        }
    }


    @Override
    protected void getIds(View view) {
        formatter = new DecimalFormat("###,###,##0.00");
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateTime = simpleDateFormat.format(calendar.getTime());
        String s = dateTime;
        String[] arrayString = s.split("-");

        year = arrayString[0];
        month = arrayString[1];


        txtName = view.findViewById(R.id.txtName);
        txtFolioNumber = view.findViewById(R.id.txtFolioNumber);
        spineerSwitchFund = view.findViewById(R.id.spineerSwitchFund);
        spineerFrequency = view.findViewById(R.id.spineerFrequency);
        spineerDay = view.findViewById(R.id.spineerDay);
        investmentAccountName = view.findViewById(R.id.investmentAccountName);
        txtCurrentFundValue = view.findViewById(R.id.txtCurrentFundValue);
        txtSealableUnits = view.findViewById(R.id.txtSealableUnits);
        txtAmount = view.findViewById(R.id.txtAmount);
        txtUnits = view.findViewById(R.id.txtUnits);
        btnAddToCart = view.findViewById(R.id.btnAddToCart);
        edtAMount = view.findViewById(R.id.edtAMount);
        edtInstallments = view.findViewById(R.id.edtInstallments);
    }

    @Override
    protected void handleListener() {
        txtAmount.setOnClickListener(this);
        txtUnits.setOnClickListener(this);
        btnAddToCart.setOnClickListener(this);
    }

    @Override
    protected void initializations() {

    }

    @Override
    protected void setIcon(Util util) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.txtAmount) {
            AmountOrUnit = "A";
            AllorPartial = "P";
            txtAmount.setBackgroundColor(Color.parseColor("#f57222"));
            txtAmount.setTextColor(Color.parseColor("#ffffff"));

            txtUnits.setBackgroundColor(Color.parseColor("#ffffff"));
            txtUnits.setTextColor(Color.parseColor("#817D7D"));


        }

        if (id == R.id.txtUnits) {
            AmountOrUnit = "U";
            AllorPartial = "P";
            txtUnits.setBackgroundColor(Color.parseColor("#f57222"));
            txtUnits.setTextColor(Color.parseColor("#ffffff"));

            txtAmount.setBackgroundColor(Color.parseColor("#ffffff"));
            txtAmount.setTextColor(Color.parseColor("#817D7D"));
        }

        if (id == R.id.btnAddToCart) {
            amount = edtAMount.getText().toString().trim();
            noOfInstallments = edtInstallments.getText().toString().trim();


            String[] arrayString = SipDate.split("-");

            String year = arrayString[0];
            String month = arrayString[1];
            String date = arrayString[2];

            finalSipDate = year + month + date;


            if (noOfInstallments.isEmpty() || noOfInstallments.equalsIgnoreCase("")) {
                edtInstallments.setFocusable(true);
                edtInstallments.requestFocus();
                Toast.makeText(getContext(), "please enter no of installments", Toast.LENGTH_SHORT).show();
            } else if (amount.isEmpty() || amount.equalsIgnoreCase("")) {
                edtAMount.setFocusable(true);
                edtAMount.requestFocus();
                Toast.makeText(getContext(), "please enter saleable amount", Toast.LENGTH_SHORT).show();
            } else {
                addInvCartAPIResponse();
            }
        }
    }

    // sip frequency adapter
    private void setFrequencyAdapter(ArrayList<String> arrayList) {
        sipFrequencyAdapter = new SipFrequencyAdapter(getContext(), arrayList);
        spineerFrequency.setAdapter(sipFrequencyAdapter);

        spineerFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                selectFrequency = arrayList.get(position);
                String Day = sipStartDatesList.get(position);
                finalSipStartDate = Day;

                finalSipStartDatesList = new ArrayList<>(Arrays.asList(Day.split(",")));

                setDateAdapter(finalSipStartDatesList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    // get SealableUnits
    private void getSealableUnits(double AwaitingHoldingUnit) {
        double SalableUnit = quantity - AwaitingHoldingUnit;
        txtSealableUnits.setText(formatter.format(Double.parseDouble(""+SalableUnit)));

    }


    // day frequency adapter
    private void setDateAdapter(ArrayList<String> arrayList) {
        sipDateAdapter = new SipDateAdapter(getContext(), arrayList);
        spineerDay.setAdapter(sipDateAdapter);

        spineerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectDate = arrayList.get(position);
                SipDate=(year + "-" + month + "-" + selectDate);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

    //  adapter
    private void setAdapter(ArrayList<TranferSchemeResponse> arrayList) {
        switchFundAdapter = new SwitchFundAdapter(getContext(), arrayList);
        spineerSwitchFund.setAdapter(switchFundAdapter);
        spineerSwitchFund.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                targetFundName = arrayList.get(position).getName();
                targetFundCode = arrayList.get(position).getCode();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

    // fund transfer api
    private void getTransferFundResponse() {
        util.showProgressDialog(context, true);

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_ORDER);

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setClientCode(clientHoldingObject.getClientCode());

        requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());

        globalRequestObject.setRequestBodyObject(requestBodyObject);

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        MFOrderValidationRequest.createGlobalRequestObject(globalRequestObject);

        apiInterface.getTransferSchemes(MFOrderValidationRequest.getGlobalRequestObject()).enqueue(new Callback<ArrayList<TranferSchemeResponse>>() {

            @Override
            public void onResponse(Call<ArrayList<TranferSchemeResponse>> call, Response<ArrayList<TranferSchemeResponse>> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                //    util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    switchFundArrayList = response.body();
                    setAdapter(switchFundArrayList);

                    getValidationResponse();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TranferSchemeResponse>> call, Throwable t) {
                util.showProgressDialog(context, false);
                System.out.println("error: " + t.getLocalizedMessage());

                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // frequencty and day
    private void getValidationResponse() {

        // util.showProgressDialog(context, true);

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_VALIDATION);

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setMWIClientCode(clientHoldingObject.getClientCode());

        requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());

        requestBodyObject.setTransactionType("STP");

        String tillDate = util.getCurrentDate(false);

        requestBodyObject.setTillDate(tillDate);

        globalRequestObject.setRequestBodyObject(requestBodyObject);

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        MFOrderValidationRequest.createGlobalRequestObject(globalRequestObject);

        apiInterface.getOrderValidationData(MFOrderValidationRequest.getGlobalRequestObject()).enqueue(new Callback<MFOrderValidationResponse>() {

            @Override
            public void onResponse(Call<MFOrderValidationResponse> call, Response<MFOrderValidationResponse> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                //   util.showProgressDialog(context, false);

                if (response.isSuccessful()) {

                    frequency = response.body().getSipFrequency();

                    frequencyList = new ArrayList<>(Arrays.asList(frequency.split(",")));

                    for (String s : frequencyList) {
                        if (s.equalsIgnoreCase("0")) {
                            finalFrequencyList.add("Daily");
                        }
                        if (s.equalsIgnoreCase("1")) {
                            finalFrequencyList.add("Weekly");
                        }
                        if (s.equalsIgnoreCase("4")) {
                            finalFrequencyList.add("Monthly");
                        }
                        if (s.equalsIgnoreCase("5")) {
                            finalFrequencyList.add("Quarterly");
                        }

                        if (s.equalsIgnoreCase("6")) {
                            finalFrequencyList.add("Half - Yearly");
                        }
                        if (s.equalsIgnoreCase("7")) {
                            finalFrequencyList.add("Yearly");
                        }
                    }
                    setFrequencyAdapter(finalFrequencyList);

                    sipStartDates = response.body().getSipStartDates();

                    sipStartDatesList = new ArrayList<>(Arrays.asList(sipStartDates.split("\\|,")));

                    String Day = sipStartDatesList.get(0);

                    finalSipStartDatesList = new ArrayList<>(Arrays.asList(Day.split(",")));

                    setDateAdapter(finalFrequencyList);

                    callAccountDetailAPI();

                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MFOrderValidationResponse> call, Throwable t) {
                util.showProgressDialog(context, false);

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // account name
    private void callAccountDetailAPI() {

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setClientCode(authenticateResponse.getUserCode());

        requestBodyObject.setClientType("H"); //H for client

        requestBodyObject.setIsFundware("false");

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(getContext(), uniqueIdentifier);

        AccountRequestObject.createAccountRequestObject(uniqueIdentifier, Constants.SOURCE, requestBodyObject);


        APIInterface apiInterface = BOBApp.getApi(getContext(), Constants.ACTION_ACCOUNT);

        apiInterface.getAccountResponse(AccountRequestObject.getAccountRequestObject()).enqueue(new Callback<ArrayList<AccountResponseObject>>() {
            @Override
            public void onResponse(Call<ArrayList<AccountResponseObject>> call, Response<ArrayList<AccountResponseObject>> response) {

                // util.showProgressDialog(getContext(), false);

                if (response.isSuccessful()) {
                    accountResponseObjectArrayList = response.body();
                    for (int i = 0; i <= accountResponseObjectArrayList.size(); i++) {
                        if (accountResponseObjectArrayList.get(i).getClientCode().equalsIgnoreCase(l4ClientCode)) {
                            investmentAccountName.setText(accountResponseObjectArrayList.get(i).getClientName());
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }

                getMFValidationResponse();

            }

            @Override
            public void onFailure(Call<ArrayList<AccountResponseObject>> call, Throwable t) {

                util.showProgressDialog(getContext(), false);

                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMFValidationResponse() {

        util.showProgressDialog(context, true);

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_VALIDATION);

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setMWIClientCode(clientHoldingObject.getClientCode());

        requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());

        requestBodyObject.setTransactionType("SWITCH");

        String tillDate = util.getCurrentDate(false);

        requestBodyObject.setTillDate(tillDate);

        globalRequestObject.setRequestBodyObject(requestBodyObject);

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        MFOrderValidationRequest.createGlobalRequestObject(globalRequestObject);

        apiInterface.getOrderValidationData(MFOrderValidationRequest.getGlobalRequestObject()).enqueue(new Callback<MFOrderValidationResponse>() {

            @Override
            public void onResponse(Call<MFOrderValidationResponse> call, Response<MFOrderValidationResponse> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    AwaitingHoldingUnit = Double.parseDouble(response.body().getAwaitingHoldingUnit());
                    DividendOption = response.body().getDividendOption();
                    FundOption = response.body().getFundOption();
                    LatestNAV = response.body().getLatestNAV();
                    ValueResearchRating = response.body().getValueResearchRating();
                    String IsDividends = response.body().getIsDividend();
                    if (IsDividends.equalsIgnoreCase("0")) {
                        IsDividend = "false";
                    } else {
                        IsDividend = "true";
                    }
                    getSealableUnits(AwaitingHoldingUnit);
                } else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MFOrderValidationResponse> call, Throwable t) {
                util.showProgressDialog(context, false);

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addInvCartAPIResponse() {
        util.showProgressDialog(context, true);

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_VALIDATION);

        ArrayList<RequestBodyObject> requestBodyObjectArrayList = new ArrayList<RequestBodyObject>();

        for (int i = 0; i < 1; i++) {

            RequestBodyObject requestBodyObject = new RequestBodyObject();

            requestBodyObject.setCostofInvestment("0");
            requestBodyObject.setCurrentUnits("" + quantity);
            requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());
            requestBodyObject.setParentChannelID("WMSPortal");
            requestBodyObject.setAmountOrUnit(AmountOrUnit);
            requestBodyObject.setAllorPartial(AllorPartial);
            requestBodyObject.setAssetClassName("");
            requestBodyObject.setSipStartDates(finalSipStartDate);
            requestBodyObject.setStartDate(finalSipDate);
            requestBodyObject.setPeriod(noOfInstallments);
            requestBodyObject.setIsDividend(IsDividend);
            requestBodyObject.setLatestNAV(LatestNAV);
            requestBodyObject.setTransactionType("STP");
            requestBodyObject.setDividendOption(DividendOption);
            requestBodyObject.setTargetFundName(targetFundName);
            requestBodyObject.setFolioNo(txtFolioNumber.getText().toString().trim());
            requestBodyObject.setL4ClientCode(clientHoldingObject.getL4Client_Code());
            requestBodyObject.setSchemeName(clientHoldingObject.getSchemeName());
            requestBodyObject.setTargetFundCode(targetFundCode);
            requestBodyObject.setInputmode("2");
            requestBodyObject.setTxnAmountUnit(amount);
            requestBodyObject.setCurrentFundValue(txtCurrentFundValue.getText().toString().trim());
            requestBodyObject.setMWIClientCode(clientHoldingObject.getClientCode());
            requestBodyObject.setValueResearchRating(ValueResearchRating);
            requestBodyObject.setNoofMonth(noOfInstallments);
            requestBodyObject.setSettlementBankCode("0");
            requestBodyObject.setFrequency("0");

            requestBodyObject.setFrequency(selectFrequency);
            requestBodyObject.setFundRiskRating("");
            requestBodyObject.setAssetClassCode("0");

//            requestBodyObject.setChannelID("Transaction");


            requestBodyObjectArrayList.add(requestBodyObject);
        }

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        GlobalRequestObjectArray globalRequestObject = new GlobalRequestObjectArray();

        globalRequestObject.setRequestBodyObjectArrayList(requestBodyObjectArrayList);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        System.out.println("REQUEST :" + new Gson().toJson(globalRequestObject));

        apiInterface.addInvestmentCart(globalRequestObject).enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));

                util.showProgressDialog(context, false);

                if (response.isSuccessful()) {
                    edtAMount.setText("");
                    edtInstallments.setText("");
                    //   edtInstallments.setText("");
                    Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

                util.showProgressDialog(context, false);

                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
