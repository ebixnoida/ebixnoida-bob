package com.bob.bobapp.api.request_object;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StopSipBodyObject {
    private static StopSipBodyObject requestBodyObject = null;

    @SerializedName("FundCode")
    private String FundCode;

    @SerializedName("ClientCode")
    private String ClientCode;

    @SerializedName("Orders")
    private ArrayList<OrdersObject> requestBodyObjectArrayList;

    public String getFundCode() {
        return FundCode;
    }

    public void setFundCode(String fundCode) {
        FundCode = fundCode;
    }

    public String getClientCode() {
        return ClientCode;
    }

    public void setClientCode(String clientCode) {
        ClientCode = clientCode;
    }

    public ArrayList<OrdersObject> getRequestBodyObjectArrayList() {
        return requestBodyObjectArrayList;
    }

    public void setRequestBodyObjectArrayList(ArrayList<OrdersObject> requestBodyObjectArrayList) {
        this.requestBodyObjectArrayList = requestBodyObjectArrayList;
    }




}
