package com.example.wj.wjhttp.http.chain;

import com.example.wj.wjhttp.http.Response;

public interface Interceptor {

    public Response intercept(InterceptorChain interceptorChain);

}
