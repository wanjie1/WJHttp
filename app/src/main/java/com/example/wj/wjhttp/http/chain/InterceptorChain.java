package com.example.wj.wjhttp.http.chain;

import com.example.wj.wjhttp.http.Call;
import com.example.wj.wjhttp.http.HttpConnection;
import com.example.wj.wjhttp.http.Response;

import java.util.List;

public class InterceptorChain {

    private final List<Interceptor> interceptors ;
    private final int index;
    private final Call call;
    private HttpConnection httpConnection = null;

    public InterceptorChain(List<Interceptor> interceptors,int index,Call call,HttpConnection httpConnection)
    {
        this.interceptors = interceptors;
        this.index = index;
        this.call = call;
        this.httpConnection = httpConnection;
    }


}
