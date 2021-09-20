package com.bob.bobapp.api.request_object;

import com.google.gson.annotations.SerializedName;

public class DeleterCartRequestBody
{

    @SerializedName("MWIClientCode")
    private String  MWIClientCode;

    @SerializedName("ICDID")
    private String ICDID;

    @SerializedName("inputmode")
    private String inputmode;

    @SerializedName("ParentChannelID")
    private String ParentChannelID;

    public String getMWIClientCode() {
        return MWIClientCode;
    }

    public void setMWIClientCode(String MWIClientCode) {
        this.MWIClientCode = MWIClientCode;
    }

    public String getICDID() {
        return ICDID;
    }

    public void setICDID(String ICDID) {
        this.ICDID = ICDID;
    }

    public String getInputmode() {
        return inputmode;
    }

    public void setInputmode(String inputmode) {
        this.inputmode = inputmode;
    }

    public String getParentChannelID() {
        return ParentChannelID;
    }

    public void setParentChannelID(String parentChannelID) {
        ParentChannelID = parentChannelID;
    }
}
