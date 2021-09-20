package com.bob.bobapp.api;

import com.bob.bobapp.api.request_object.RequestBodyObject;
import com.bob.bobapp.api.request_object.StopSipBodyObject;
import com.google.gson.annotations.SerializedName;

public class StopSipRequestObject  extends RequestBodyObject {
    @SerializedName("UniqueIdentifier")
    private String uniqueIdentifier;

    @SerializedName("Source")
    private String source;

    @SerializedName("RequestBody")
    private StopSipBodyObject requestBodyObject;



    public StopSipBodyObject getRequestBodyObject() {
        return requestBodyObject;
    }

    public void setRequestBodyObject(StopSipBodyObject requestBodyObject) {
        this.requestBodyObject = requestBodyObject;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


}
