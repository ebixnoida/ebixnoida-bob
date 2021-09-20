package com.bob.bobapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.R;
import com.bob.bobapp.adapters.FolioAdapter;
import com.bob.bobapp.adapters.FolioSpinnerAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.bean.ClientHoldingObject;
import com.bob.bobapp.api.bean.SpinnerRowItem;
import com.bob.bobapp.api.request_object.AccountRequestObject;
import com.bob.bobapp.api.request_object.GlobalRequestObject;
import com.bob.bobapp.api.request_object.GlobalRequestObjectArray;
import com.bob.bobapp.api.request_object.MFOrderValidationRequest;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.response_object.AccountResponseObject;
import com.bob.bobapp.api.response_object.AddInvCartResponse;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.FolioWisePendingUnitsCollection;
import com.bob.bobapp.api.response_object.MFOrderValidationResponse;
import com.bob.bobapp.fragments.BaseFragment;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.IntentKey;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyActivity extends BaseFragment {
    private AppCompatTextView txtName, investmentAccountName;
    private AppCompatEditText edtAMount;
    private AppCompatButton btnAddToCart;
    private AppCompatSpinner spineerFolio;
    private ClientHoldingObject clientHoldingObject;
    private Context context;
    private Util util;
    private ArrayList<FolioWisePendingUnitsCollection> folioWisePendingUnitsCollectionArrayList = new ArrayList<>();
    private FolioAdapter folioAdapter;
    private String ValueResearchRating = "", FundOption = "", amountUnit = "", DividendOption = "", LatestNAV = "",
            IsDividend, folioNumber = "", l4ClientCode = "", commanScriptCode = "", schemeName = "", l4ClientCodes = "";
    private ArrayList<AccountResponseObject> accountResponseObjectArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        util = new Util(context);


        return inflater.inflate(R.layout.buy_activity, container, false);
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
        txtName = view.findViewById(R.id.txtName);
        spineerFolio = view.findViewById(R.id.spineerFolio);
        btnAddToCart = view.findViewById(R.id.btnAddToCart);
        investmentAccountName = view.findViewById(R.id.investmentAccountName);
        edtAMount = view.findViewById(R.id.edtAMount);
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
            amountUnit = edtAMount.getText().toString().trim();
            if (amountUnit.isEmpty() || amountUnit.equalsIgnoreCase("")) {
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

        requestBodyObject.setTransactionType("BUY");

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

                    } else {
                        folioNumber = "New Folio";
                    }
                    setFolioAdapter(folioWisePendingUnitsCollectionArrayList);
                } else {

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }

                ValueResearchRating = response.body().getValueResearchRating();
                FundOption = response.body().getFundOption();
                DividendOption = response.body().getDividendOption();
                LatestNAV = response.body().getLatestNAV();
                String IsDividends = response.body().getIsDividend();
                if (IsDividends.equalsIgnoreCase("0")) {
                    IsDividend = "false";
                } else {
                    IsDividend = "true";
                }
                callAccountDetailAPI();
            }

            @Override
            public void onFailure(Call<MFOrderValidationResponse> call, Throwable t) {

                util.showProgressDialog(context, false);

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // add cart api
    private void addInvCartAPIResponse() {
        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        util.showProgressDialog(context, true);
        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_VALIDATION);

        ArrayList<RequestBodyObject> requestBodyObjectArrayList = new ArrayList<RequestBodyObject>();

        for (int i = 0; i < 1; i++) {

            RequestBodyObject requestBodyObject = new RequestBodyObject();

            requestBodyObject.setValueResearchRating(ValueResearchRating);

            if (clientHoldingObject != null) {
                requestBodyObject.setSchemeCode(clientHoldingObject.getCommonScripCode());
                requestBodyObject.setSchemeName(clientHoldingObject.getSchemeName());
                requestBodyObject.setL4ClientCode(clientHoldingObject.getL4Client_Code());
                requestBodyObject.setMWIClientCode(clientHoldingObject.getClientCode());
            } else {
                requestBodyObject.setSchemeCode(commanScriptCode);
                requestBodyObject.setSchemeName(schemeName);
                requestBodyObject.setL4ClientCode(l4ClientCodes);
                requestBodyObject.setMWIClientCode(authenticateResponse.getUserCode());
            }

            requestBodyObject.setFundOption(FundOption);

            requestBodyObject.setTxnAmountUnit(amountUnit); //For INR

            requestBodyObject.setFundRiskRating(""); //For base

            requestBodyObject.setDividendOption(DividendOption);

            requestBodyObject.setTransactionType("Buy"); //For client

            requestBodyObject.setInputmode("2");

            requestBodyObject.setLatestNAV(LatestNAV);

            requestBodyObject.setIsDividend(IsDividend);

            requestBodyObject.setFolioNo(folioNumber);

            requestBodyObject.setParentChannelID("WMSPortal");

            requestBodyObject.setChannelID("Transaction");

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


}
