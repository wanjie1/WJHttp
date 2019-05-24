package com.example.wj.wjhttp.http;

/**
 * 回调接口，用户最终回调的接口
 */
public interface Callback {

    public void onFailure(Call call,Throwable throwable);
    public void onResponse(Call call,Response response);
}
