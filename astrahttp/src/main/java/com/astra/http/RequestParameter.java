package com.astra.http;

/**
 * 请求参数
 * Created by Astra on 16/12/1.
 */

class RequestParameter {
    private String key;
    private String value;

    protected RequestParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    protected RequestParameter(String key, int value) {
        this.key = key;
        this.value = String.valueOf(value);
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
