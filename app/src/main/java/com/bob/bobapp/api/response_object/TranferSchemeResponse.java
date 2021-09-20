package com.bob.bobapp.api.response_object;

public class TranferSchemeResponse {
    private String Name;
    private String Code;
    private String IsDividend;
    private String DividendOption;
    private String FundRiskRating;
    private String ValueRiskRating;
    private String LatestNAV;
    private String AdditionalPurchaseMinimumAmount;
    private String MinimumAmount;

    public String getName() {
        return Name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getIsDividend() {
        return IsDividend;
    }

    public void setIsDividend(String isDividend) {
        IsDividend = isDividend;
    }

    public String getDividendOption() {
        return DividendOption;
    }

    public void setDividendOption(String dividendOption) {
        DividendOption = dividendOption;
    }

    public String getFundRiskRating() {
        return FundRiskRating;
    }

    public void setFundRiskRating(String fundRiskRating) {
        FundRiskRating = fundRiskRating;
    }

    public String getValueRiskRating() {
        return ValueRiskRating;
    }

    public void setValueRiskRating(String valueRiskRating) {
        ValueRiskRating = valueRiskRating;
    }

    public String getLatestNAV() {
        return LatestNAV;
    }

    public void setLatestNAV(String latestNAV) {
        LatestNAV = latestNAV;
    }

    public String getAdditionalPurchaseMinimumAmount() {
        return AdditionalPurchaseMinimumAmount;
    }

    public void setAdditionalPurchaseMinimumAmount(String additionalPurchaseMinimumAmount) {
        AdditionalPurchaseMinimumAmount = additionalPurchaseMinimumAmount;
    }

    public String getMinimumAmount() {
        return MinimumAmount;
    }

    public void setMinimumAmount(String minimumAmount) {
        MinimumAmount = minimumAmount;
    }

    public void setName(String name) {
        Name = name;
    }
}
