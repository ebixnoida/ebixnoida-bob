package com.bob.bobapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.FolioAdapter;
import com.bob.bobapp.adapters.SipDateAdapter;
import com.bob.bobapp.adapters.SipFrequencyAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.request_object.AccountRequestObject;
import com.bob.bobapp.api.request_object.GlobalRequestObject;
import com.bob.bobapp.api.request_object.GlobalRequestObjectArray;
import com.bob.bobapp.api.request_object.MFOrderValidationRequest;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.response_object.AccountResponseObject;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.FolioWisePendingUnitsCollection;
import com.bob.bobapp.api.response_object.MFOrderValidationResponse;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.IntentKey;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SipActivity extends BaseFragment {
    private AppCompatTextView txtName, investmentAccountName, txtDate;
    private AppCompatSpinner spineerFolio, spineerFrequency, spineerDay;
    private AppCompatEditText edtAMount, edtInstallments;
    private CheckBox checkSelect;
    private LinearLayoutCompat linearInstallment;
    private AppCompatButton btnAddToCart;
    private ClientHoldingObject clientHoldingObject;
    private Context context;
    private Util util;
    private ArrayList<FolioWisePendingUnitsCollection> folioWisePendingUnitsCollectionArrayList = new ArrayList<>();
    private FolioAdapter folioAdapter;
    private SipFrequencyAdapter SipFrequencyAdapter;
    private SipDateAdapter sipDateAdapter;
    private String frequency = "", sipStartDates = "", l4ClientCode = "";
    private ArrayList<String> frequencyList;
    private ArrayList<String> sipStartDatesList;
    private ArrayList<String> finalFrequencyList = new ArrayList<>();
    private ArrayList<String> finalSipStartDatesList;
    private ArrayList<AccountResponseObject> accountResponseObjectArrayList = new ArrayList<>();
    private String amount = "", ValueResearchRating = "", LatestNAV = "", FundOption = "", folioNumber,
            IsDividend = "", selectFrequency = "", DividendOption = "", finalSipStartDate, installmentNumber = "", IsPerpetual = "false",
            dateTime, year, month, SipDate, finalSipDate, nextInstallment, schemeName, commanScriptCode, l4ClientCodes = "";
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);

        return inflater.inflate(R.layout.sip_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        getExtraDataFromIntent();

        setData();

        getValidationResponse();

    }

    // set data here...
    private void setData() {
        if (clientHoldingObject != null) {
            txtName.setText(clientHoldingObject.getSchemeName());
            l4ClientCode = clientHoldingObject.getL4Client_Code();
        } else {
            txtName.setText(schemeName);
        }
    }

    private void getExtraDataFromIntent() {

        if (getArguments() != null) {

            String response = getArguments().getString(IntentKey.RESPONSE_KEY);

            clientHoldingObject = new Gson().fromJson(response, ClientHoldingObject.class);
        }
    }

    @Override
    protected void getIds(View view) {
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateTime = simpleDateFormat.format(calendar.getTime());
        String s = dateTime;
        String[] arrayString = s.split("-");

        year = arrayString[0];
        month = arrayString[1];

        txtName = view.findViewById(R.id.txtName);
        spineerFolio = view.findViewById(R.id.spineerFolio);
        spineerFrequency = view.findViewById(R.id.spineerFrequency);
        spineerDay = view.findViewById(R.id.spineerDay);
        investmentAccountName = view.findViewById(R.id.investmentAccountName);
        btnAddToCart = view.findViewById(R.id.btnAddToCart);
        edtAMount = view.findViewById(R.id.edtAMount);
        edtInstallments = view.findViewById(R.id.edtInstallments);
        checkSelect = view.findViewById(R.id.checkSelect);
        linearInstallment = view.findViewById(R.id.linearInstallment);
        txtDate = view.findViewById(R.id.txtDate);

        checkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    IsPerpetual = "true";
                    linearInstallment.setVisibility(View.GONE);
                } else {
                    IsPerpetual = "false";
                    linearInstallment.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void handleListener() {
        btnAddToCart.setOnClickListener(this);
    }

    @Override
    protected void initializations() {
        if (getArguments() != null) {

            commanScriptCode = getArguments().getString("commanScriptCode");
            schemeName = getArguments().getString("schemeName");

//            model = new Gson().fromJson(response, ClientHoldingObject.class);
        }
    }

    @Override
    protected void setIcon(Util util) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnAddToCart) {
            amount = edtAMount.getText().toString().trim();
            installmentNumber = edtInstallments.getText().toString().trim();
            SipDate = txtDate.getText().toString();

            String[] arrayString = SipDate.split("-");

            String year = arrayString[0];
            String month = arrayString[1];
            String date = arrayString[2];

            finalSipDate = year + month + date;
            nextInstallment = (year + 1) + month + date;

            if (amount.isEmpty() || amount.equalsIgnoreCase("")) {
                edtAMount.setFocusable(true);
                edtAMount.requestFocus();
                Toast.makeText(getContext(), "please enter amount", Toast.LENGTH_SHORT).show();
            } else {
                addInvCartAPIResponse();
            }
        }
    }

    // adapter
    private void setFolioAdapter(ArrayList<FolioWisePendingUnitsCollection> arrayList) {
        folioAdapter = new FolioAdapter(getContext(), arrayList);
        spineerFolio.setAdapter(folioAdapter);
        spineerFolio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (arrayList.size() > 0) {
                    folioNumber = arrayList.get(position).getFolioNo();
                } else {
                    folioNumber = "New Folio";
                }
                //   Toast.makeText(getContext(), folioNumber, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

    // sip frequency adapter
    private void setFrequencyAdapter(ArrayList<String> arrayList) {
        SipFrequencyAdapter = new SipFrequencyAdapter(getContext(), arrayList);
        spineerFrequency.setAdapter(SipFrequencyAdapter);

        spineerFrequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectFrequency = arrayList.get(position);

                //  Toast.makeText(getContext(),selectFrequency,Toast.LENGTH_SHORT).show();

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


    // day frequency adapter
    private void setDateAdapter(ArrayList<String> arrayList) {
        sipDateAdapter = new SipDateAdapter(getContext(), arrayList);
        spineerDay.setAdapter(sipDateAdapter);
        spineerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectDate = arrayList.get(position);
                txtDate.setText(year + "-" + month + "-" + selectDate);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }


    // api
    private void getValidationResponse() {

        util.showProgressDialog(context, true);
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_VALIDATION);

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        if (clientHoldingObject != null) {
            requestBodyObject.setMWIClientCode(clientHoldingObject.getClientCode());
            requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());
        } else {
            requestBodyObject.setMWIClientCode(authenticateResponse.getUserCode());
            requestBodyObject.setSchemeCode(commanScriptCode);
        }

        requestBodyObject.setTransactionType("SIP");

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
                    folioWisePendingUnitsCollectionArrayList = response.body().getFolioWisePendingUnitsCollection();
                    if (folioWisePendingUnitsCollectionArrayList.size() > 0) {
                        folioNumber=folioWisePendingUnitsCollectionArrayList.get(0).getFolioNo();
                    } else {
                        folioNumber = "New Folio";
                    }

                    setFolioAdapter(folioWisePendingUnitsCollectionArrayList);

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

                    ValueResearchRating = response.body().getValueResearchRating();
                    LatestNAV = response.body().getLatestNAV();
                    FundOption = response.body().getFundOption();
                    DividendOption = response.body().getDividendOption();

                    String IsDividends = response.body().getIsDividend();
                    if (IsDividends.equalsIgnoreCase("0")) {
                        IsDividend = "false";
                    } else {
                        IsDividend = "true";
                    }


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


    // for showing name
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

                util.showProgressDialog(getContext(), false);

                if (response.isSuccessful()) {
                    accountResponseObjectArrayList = response.body();
                    l4ClientCodes = accountResponseObjectArrayList.get(1).getClientCode();
                    if (l4ClientCode.equalsIgnoreCase("")) {
                        investmentAccountName.setText(accountResponseObjectArrayList.get(1).getClientName());
                    } else {
                        for (int i = 0; i < accountResponseObjectArrayList.size(); i++) {
                            if (accountResponseObjectArrayList.get(i).getClientCode().equalsIgnoreCase(l4ClientCode)) {
                                investmentAccountName.setText(accountResponseObjectArrayList.get(i).getClientName());
                                break;
                            }
                        }
                    }
                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<AccountResponseObject>> call, Throwable t) {

                util.showProgressDialog(getContext(), false);

                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // add cart api
    private void addInvCartAPIResponse() {

        util.showProgressDialog(context, true);
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;
        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_VALIDATION);

        ArrayList<RequestBodyObject> requestBodyObjectArrayList = new ArrayList<RequestBodyObject>();

        for (int i = 0; i < 1; i++) {

            RequestBodyObject requestBodyObject = new RequestBodyObject();

            if (clientHoldingObject != null) {
                requestBodyObject.setL4ClientCode(clientHoldingObject.getL4Client_Code());
                requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());
                requestBodyObject.setSchemeName(clientHoldingObject.getSchemeName());

                requestBodyObject.setMWIClientCode(clientHoldingObject.getClientCode());

            } else {
                requestBodyObject.setL4ClientCode(l4ClientCodes);
                requestBodyObject.setSchemeCode(commanScriptCode);
                requestBodyObject.setSchemeName(schemeName);
                requestBodyObject.setMWIClientCode(authenticateResponse.getUserCode());

            }

            requestBodyObject.setValueResearchRating(ValueResearchRating);
            requestBodyObject.setLatestNAV(LatestNAV);
            requestBodyObject.setFundOption(FundOption);


            requestBodyObject.setParentChannelID("WMSPortal");
            requestBodyObject.setIsPerpetual(IsPerpetual);
            requestBodyObject.setTxnAmountUnit(amount);
            requestBodyObject.setFolioNo(folioNumber);

            requestBodyObject.setFundRiskRating(""); //For base
            requestBodyObject.setIsDividend(IsDividend);
            requestBodyObject.setCostofInvestment("0");
            requestBodyObject.setFrequency(selectFrequency);
            requestBodyObject.setDividendOption(DividendOption);
            requestBodyObject.setInputmode("2");
            if (IsPerpetual.equalsIgnoreCase("true")) {
                requestBodyObject.setPeriod("");
                requestBodyObject.setNoofMonth("");
            } else {
                requestBodyObject.setPeriod(installmentNumber);
                requestBodyObject.setNoofMonth(installmentNumber);
            }

            requestBodyObject.setChannelID("Transaction");
            requestBodyObject.setSipStartDates(finalSipStartDate);
            requestBodyObject.setStartDate(finalSipDate);
            requestBodyObject.setNextInstallmentDate(nextInstallment);
            requestBodyObject.setTransactionType("SIP"); //For client
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
