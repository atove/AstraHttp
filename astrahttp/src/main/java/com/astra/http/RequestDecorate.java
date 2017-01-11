package com.astra.http;

import java.util.ArrayList;

/**
 * Created by Astra on 17/1/10.
 */

public abstract class RequestDecorate {
    protected ArrayList<RequestParameter> requestParameters;
    private ArrayList<RequestHeader> requestHeaders;
    public abstract String getMediaType();
    public abstract String getContent();
    public ArrayList<RequestHeader> getRequestHeader(){
        return requestHeaders;
    }
    public void setRequestParameters(ArrayList<RequestParameter> requestParameters){
        this.requestParameters = requestParameters;
    }
    protected void addRequestHeader(String key, String value){
        if (requestHeaders == null){
            requestHeaders = new ArrayList<>();
        }
        requestHeaders.add(new RequestHeader(key, value));
    }

}
