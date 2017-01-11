package com.astra.http;

/**
 * 请求信息实体
 * Created by Astra on 16/12/1.
 */

class UrlData {
    private String key;
    private long expires;
    private String netType;
    private String host;
    private String url;
    private String DecorateType;

    public String getDecorateType() {
        return DecorateType;
    }

    public void setDecorateType(String decorateType) {
        DecorateType = decorateType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
