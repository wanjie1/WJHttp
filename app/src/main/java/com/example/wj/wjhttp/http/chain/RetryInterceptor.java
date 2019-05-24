package com.example.wj.wjhttp.http.chain;

import android.util.Log;
import com.example.wj.wjhttp.http.Call;
import com.example.wj.wjhttp.http.Response;
import java.io.IOException;

public class RetryInterceptor implements Interceptor{

    private final String TAG = "WJHttpTest";

    @Override
    public Response intercept(InterceptorChain interceptorChain) throws IOException {
        Log.d(TAG,"RetryInterceptor");

        Call call = interceptorChain.call;
        IOException ioException = null;
        for(int i = 0;i<call.getHttpClient().getRetryTimes();i++)
        {
            if(call.isCanceled())
            {
                throw new IOException("this task had canceled");
            }
            try {
                Response response = interceptorChain.proceed();
                return response;
            } catch (IOException e) {
                ioException = e;
            }
        }
        throw ioException;
    }
}
