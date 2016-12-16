package com.astra.http;

/**
 * 请求回调
 * Created by Astra on 16/12/1.
 */

public interface RequestCallback {
    void onSuccess(String content);
    void onFail(String errorMessage);
}
