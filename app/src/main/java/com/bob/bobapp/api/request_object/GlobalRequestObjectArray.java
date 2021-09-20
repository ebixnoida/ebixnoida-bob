package com.bob.bobapp.api.request_object;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GlobalRequestObjectArray {

    @SerializedName("UniqueIdentifier")
    private String uniqueIdentifier;

    @SerializedName("Source")
    private String source;

    @SerializedName("RequestBody")
    private ArrayList<RequestBodyObject> requestBodyObjectArrayList;

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

    public ArrayList<RequestBodyObject> getRequestBodyObjectArrayList() {
        return requestBodyObjectArrayList;
    }

    public void setRequestBodyObjectArrayList(ArrayList<RequestBodyObject> requestBodyObjectArrayList) {
        this.requestBodyObjectArrayList = requestBodyObjectArrayList;
    }
}
