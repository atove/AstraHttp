package com.astra.http;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Astra on 17/1/10.
 */

public abstract class RequestDecorate {
    protected ArrayList<RequestParameter> requestParameters;
    private HashMap<String, String> requestHeaders;
    public abstract String getMediaType();
    public abstract String getContent();
    public HashMap<String, String> getRequestHeader(){
        return requestHeaders;
    }
    public void setRequestParameters(ArrayList<RequestParameter> requestParameters){
        this.requestParameters = requestParameters;
    }
    protected void addRequestHeader(String key, String value){
        if (requestHeaders == null){
            requestHeaders = new HashMap<>();
        }
        requestHeaders.put(key, value);
    }

}
