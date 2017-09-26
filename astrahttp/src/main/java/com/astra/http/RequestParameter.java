package com.astra.http;

/**
 * 请求参数
 * Created by Astra on 16/12/1.
 */

public class RequestParameter {
    private String key;
    private Object value;

    protected RequestParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    protected RequestParameter(String key, int value) {
        this.key = key;
        this.value = value;
    }

    protected RequestParameter(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return String.valueOf(value);
    }

    public String getValueOfJson() {
        if (value instanceof String){
            return "\"" + value + "\"";
        }else if (value instanceof Integer){
            return String.valueOf(value);
        }
        return value.toString();
    }
}
