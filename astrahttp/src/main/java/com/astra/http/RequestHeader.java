package com.astra.http;

/**
 * Created by Astra on 17/1/10.
 */

public class RequestHeader {
    private String key;
    private String value;
    protected RequestHeader(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
