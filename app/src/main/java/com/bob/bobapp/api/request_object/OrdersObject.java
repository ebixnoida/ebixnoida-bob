package com.bob.bobapp.api.request_object;

import com.google.gson.annotations.SerializedName;

public class OrdersObject {

    @SerializedName("TransactionType")
    private int TransactionType;

    public int getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(int transactionType) {
        TransactionType = transactionType;
    }

    public int getRequestId() {
        return RequestId;
    }

    public void setRequestId(int requestId) {
        RequestId = requestId;
    }

    public int getFundCode() {
        return FundCode;
    }

    public void setFundCode(int fundCode) {
        FundCode = fundCode;
    }

    public String getNextInstallmentDate() {
        return NextInstallmentDate;
    }

    public void setNextInstallmentDate(String nextInstallmentDate) {
        NextInstallmentDate = nextInstallmentDate;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public int getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        OrderNumber = orderNumber;
    }

    @SerializedName("RequestId")
    private int RequestId;

    @SerializedName("FundCode")
    private int FundCode;

    @SerializedName("NextInstallmentDate")
    private String NextInstallmentDate;

    @SerializedName("Code")
    private String Code;

    @SerializedName("OrderNumber")
    private int OrderNumber;
}
