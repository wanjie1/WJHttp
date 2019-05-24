package com.example.wj.wjhttp.http.chain;

import com.example.wj.wjhttp.http.Call;
import com.example.wj.wjhttp.http.HttpConnection;
import com.example.wj.wjhttp.http.Response;

import java.io.IOException;
import java.util.List;

public class InterceptorChain {

    private final List<Interceptor> interceptors ;
    private final int index;
    final Call call;
    private HttpConnection httpConnection = null;

    public InterceptorChain(List<Interceptor> interceptors,int index,Call call,HttpConnection httpConnection)
    {
        this.interceptors = interceptors;
        this.index = index;
        this.call = call;
        this.httpConnection = httpConnection;
    }

    public Response proceed(HttpConnection httpConnection) throws IOException {
        this.httpConnection = httpConnection;
        return proceed();
    }

    public Response proceed() throws IOException {
        if(index > interceptors.size()) {
            throw new IOException("Interceptor Chain Error");
        }

        Interceptor interceptor = interceptors.get(index);
        InterceptorChain next = new InterceptorChain(interceptors,index + 1,call,httpConnection);

        Response response = interceptor.intercept(next);
        return response;
    }




}
