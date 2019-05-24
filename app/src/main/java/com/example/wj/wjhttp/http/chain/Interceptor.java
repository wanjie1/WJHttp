package com.example.wj.wjhttp.http.chain;

import com.example.wj.wjhttp.http.Response;

import java.io.IOException;

public interface Interceptor {

    public Response intercept(InterceptorChain interceptorChain) throws IOException;

}
