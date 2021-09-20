package com.bob.bobapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bob.bobapp.BOBApp;
import com.bob.bobapp.Home.BaseContainerFragment;
import com.bob.bobapp.R;
import com.bob.bobapp.activities.BOBActivity;
import com.bob.bobapp.adapters.AccountListAdapter;
import com.bob.bobapp.api.APIInterface;
import com.bob.bobapp.api.bean.PortfolioPerformanceDetailCollection;
import com.bob.bobapp.api.request_object.AccountRequestObject;
import com.bob.bobapp.api.request_object.GlobalRequestObject;
import com.bob.bobapp.api.request_object.PortfolioPerformanceRequestObject;
import com.bob.bobapp.api.request_object.RMDetailRequestObject;
import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.response_object.AccountResponseObject;
import com.bob.bobapp.api.response_object.AuthenticateResponse;
import com.bob.bobapp.api.response_object.PortfolioPerformanceResponseObject;
import com.bob.bobapp.api.response_object.RMDetailResponseObject;
import com.bob.bobapp.listener.OnItemDeleteListener;
import com.bob.bobapp.utility.Constants;
import com.bob.bobapp.utility.SettingPreferences;
import com.bob.bobapp.utility.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends BaseFragment implements OnItemDeleteListener {

    private TextView tv_rm_username_name, tv_rm_name, tv_rm_email, tv_rm_mobile_number,
            txtAccountEmail, txtAccountPhone, btn_submit;

    private RecyclerView accountDetailsRecyclerView;

    private APIInterface apiInterface;

    private Util util;

    private ArrayList<RMDetailResponseObject> rmDetailResponseObjectArrayList = new ArrayList<>();

    private ArrayList<AccountResponseObject> accountResponseObjectArrayList = new ArrayList<>();

    private AccountResponseObject accountResponseObject;

    private int selectedPosition;

    private Context context;
    private String clientCodes = "", clinetTypes = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        apiInterface = BOBApp.getApi(getContext(), Constants.ACTION_RM_DETAIL);

        context = getActivity();

        util = new Util(context);

        callRMDetailAPI();
    }

    @Override
    protected void getIds(View view) {

        tv_rm_username_name = view.findViewById(R.id.tv_rm_username_name);

        tv_rm_name = view.findViewById(R.id.tv_rm_name);

        tv_rm_email = view.findViewById(R.id.tv_rm_email);

        tv_rm_mobile_number = view.findViewById(R.id.tv_rm_mobile_number);

        tv_rm_username_name = view.findViewById(R.id.tv_rm_username_name);

        accountDetailsRecyclerView = view.findViewById(R.id.rvAccounts);

        txtAccountEmail = view.findViewById(R.id.txtAccountEmail);
        txtAccountPhone = view.findViewById(R.id.txtAccountPhone);
        btn_submit = view.findViewById(R.id.btn_submit);
    }

    @Override
    protected void handleListener() {

        BOBActivity.imgBack.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    protected void initializations() {

        BOBActivity.llMenu.setVisibility(View.GONE);

        BOBActivity.title.setText("Profile");
    }

    @Override
    protected void setIcon(Util util) {

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.imgBack) {

            getActivity().onBackPressed();
        }
        if (id == R.id.btn_submit) {
            if (clientCodes.isEmpty() || clientCodes.equalsIgnoreCase("")) {
                Toast.makeText(getContext(), "Please select account", Toast.LENGTH_SHORT).show();
            } else {
                getPortfolioPerformanceAPIResponse(clientCodes, clinetTypes);
            }
        }
    }

    private void callRMDetailAPI() {

        util.showProgressDialog(getContext(), true);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setUserId(authenticateResponse.getUserID());

        requestBodyObject.setUserType(authenticateResponse.getUserType());

        requestBodyObject.setUserCode(authenticateResponse.getUserCode());

        requestBodyObject.setLastBusinessDate(authenticateResponse.getBusinessDate());

        requestBodyObject.setCurrencyCode("1"); //For INR

        requestBodyObject.setAmountDenomination("0"); //For base

        requestBodyObject.setAccountLevel("0"); //For client

        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(getContext(), uniqueIdentifier);

        globalRequestObject.setRequestBodyObject(requestBodyObject);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        RMDetailRequestObject.createGlobalRequestObject(globalRequestObject);

        apiInterface.getRMDetailResponse(RMDetailRequestObject.getGlobalRequestObject()).enqueue(new Callback<ArrayList<RMDetailResponseObject>>() {

            @Override
            public void onResponse(Call<ArrayList<RMDetailResponseObject>> call, Response<ArrayList<RMDetailResponseObject>> response) {


                if (response.isSuccessful()) {

                    rmDetailResponseObjectArrayList = response.body();

                    tv_rm_username_name.setText(rmDetailResponseObjectArrayList.get(0).getClientName());

                    tv_rm_name.setText(rmDetailResponseObjectArrayList.get(0).getPrimaryRMName());

                    tv_rm_email.setText(rmDetailResponseObjectArrayList.get(0).getPrimaryRMEmail());

                    tv_rm_mobile_number.setText(rmDetailResponseObjectArrayList.get(0).getPrimaryRMContactNo());

                    txtAccountEmail.setText(rmDetailResponseObjectArrayList.get(0).getEmailAddress());

                    txtAccountPhone.setText(rmDetailResponseObjectArrayList.get(0).getContactNo());

                    callAccountDetailAPI();

                } else {

                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RMDetailResponseObject>> call, Throwable t) {

                util.showProgressDialog(getContext(), false);

                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

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

                System.out.println("Account RESPONSE: " + new Gson().toJson(response.body()));


                if (response.isSuccessful()) {

                    accountResponseObjectArrayList = response.body();

                    setPopupData(accountResponseObjectArrayList);

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


    private void setPopupData(ArrayList<AccountResponseObject> accountResponseObjectArrayList) {

        AccountListAdapter adapter = new AccountListAdapter(getContext(), accountResponseObjectArrayList, this) {

            @Override
            protected void onAccountSelect(int position) {

                accountResponseObject = accountResponseObjectArrayList.get(position);

                selectedPosition = position;
            }
        };

        accountDetailsRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemDeleteListener(String clientCode, int position, String ClientType) {
        clientCodes = clientCode;
        clinetTypes = ClientType;

    }


    private void getPortfolioPerformanceAPIResponse(String clientCode, String ClientTypes) {
        util.showProgressDialog(context, true);
        APIInterface apiInterface = BOBApp.getApi(context, Constants.ACTION_PORTFOLIO_PERFORMANCE);

        AuthenticateResponse authenticateResponse = BOBActivity.authResponse;

        RequestBodyObject requestBodyObject = new RequestBodyObject();

        requestBodyObject.setUserId(authenticateResponse.getUserID());
        requestBodyObject.setUserType("1");
        requestBodyObject.setLastBusinessDate(authenticateResponse.getBusinessDate());
        requestBodyObject.setClientCode(clientCode);
        requestBodyObject.setCurrencyCode("1"); //For INR
        requestBodyObject.setAmountDenomination("0"); //For base

        requestBodyObject.setAccountLevel("0"); //For client

        requestBodyObject.setIsFundware("false");

        requestBodyObject.setIndexType("0");
        requestBodyObject.setClientType(ClientTypes);


        UUID uuid = UUID.randomUUID();

        String uniqueIdentifier = String.valueOf(uuid);

        SettingPreferences.setRequestUniqueIdentifier(context, uniqueIdentifier);

        GlobalRequestObject globalRequestObject = new GlobalRequestObject();

        globalRequestObject.setRequestBodyObject(requestBodyObject);

        globalRequestObject.setUniqueIdentifier(uniqueIdentifier);

        globalRequestObject.setSource(Constants.SOURCE);

        PortfolioPerformanceRequestObject.createGlobalRequestObject(globalRequestObject);

        apiInterface.getPortfolioPerformanceAPIResponse(PortfolioPerformanceRequestObject.getGlobalRequestObject()).enqueue(new Callback<PortfolioPerformanceResponseObject>() {

            @Override
            public void onResponse(@NonNull Call<PortfolioPerformanceResponseObject> call, @NonNull Response<PortfolioPerformanceResponseObject> response) {

                util.showProgressDialog(context, false);
                System.out.println("VALIDATION RESPONSE: " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PortfolioPerformanceResponseObject> call, @NonNull Throwable t) {

                util.showProgressDialog(context, false);

                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}