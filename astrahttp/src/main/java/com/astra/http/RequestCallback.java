package com.astra.http;

/**
 * 请求回调
 * Created by Astra on 16/12/1.
 */

public abstract class RequestCallback {
    public abstract void onSuccess(String content);
    public abstract void onFail(int code, String errorMessage);

    /**
     * 请求被取消
     */
    public void onCancel(){

    }

}
