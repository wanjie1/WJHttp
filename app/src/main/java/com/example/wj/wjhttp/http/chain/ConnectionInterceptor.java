package com.example.wj.wjhttp.http.chain;

import android.util.Log;
import com.example.wj.wjhttp.http.HttpClient;
import com.example.wj.wjhttp.http.HttpConnection;
import com.example.wj.wjhttp.http.HttpUrl;
import com.example.wj.wjhttp.http.Request;
import com.example.wj.wjhttp.http.Response;

import java.io.IOException;

public class ConnectionInterceptor implements Interceptor{

    private final String TAG = "WJHttpTest";

    @Override
    public Response intercept(InterceptorChain interceptorChain) throws IOException {

        Log.d(TAG,"ConnectionInterceptor");

        Request request = interceptorChain.call.getRequest();
        HttpClient httpClient = interceptorChain.call.getHttpClient();
        HttpUrl httpUrl = request.getHttpUrl();

        HttpConnection httpConnection = httpClient.getConnectionPool().getHttpConnection(httpUrl.getHost(),httpUrl.getPort());
        if(null == httpConnection)
        {
            httpConnection = new HttpConnection();
        }

        httpConnection.setRequest(request);

        try {
            Response response = interceptorChain.proceed(httpConnection);
            if (response.isKeepAlive()){
                httpClient.getConnectionPool().putHttpConnection(httpConnection);
            }else{
                httpConnection.close();
            }
            return response;
        }catch (IOException e){
            httpConnection.close();
            throw e;
        }
    }
}
