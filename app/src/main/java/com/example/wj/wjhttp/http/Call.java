package com.example.wj.wjhttp.http;


import com.example.wj.wjhttp.http.chain.CallServiceInterceptor;
import com.example.wj.wjhttp.http.chain.ConnectionInterceptor;
import com.example.wj.wjhttp.http.chain.HeadersInterceptor;
import com.example.wj.wjhttp.http.chain.Interceptor;
import com.example.wj.wjhttp.http.chain.InterceptorChain;
import com.example.wj.wjhttp.http.chain.RetryInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Call {

    private HttpClient httpClient;
    private Request request;

    //TODO 是否被执行过
    private boolean executed = false;
    //TODO 是否被取消了
    private boolean canceled = false;

    public boolean isCanceled()
    {
        return canceled;
    }
    public Request getRequest()
    {
        return request;
    }
    public HttpClient getHttpClient()
    {
        return httpClient;
    }

    public Call(HttpClient httpClient,Request request)
    {
        this.httpClient = httpClient;
        this.request = request;
    }




    public Response getResponse() throws IOException
    {
        /*Response response = new Response();
        response.setBody("WJHttp测试");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.addAll(httpClient.getInterceptors());
        interceptors.add(new RetryInterceptor());
        interceptors.add(new HeadersInterceptor());
        interceptors.add(new ConnectionInterceptor());
        interceptors.add(new CallServiceInterceptor());

        InterceptorChain interceptorChain = new InterceptorChain(interceptors,0,this,null);
        return interceptorChain.proceed();
    }

    public Call enqueue(Callback callback)
    {
        synchronized (this)
        {
            if(executed)
                throw new IllegalStateException("This Call Already Executed!");
            executed = true;
        }

        httpClient.getDispatcher().enqueue(new AsyncCall(callback));

        return this;
    }

    final class AsyncCall implements  Runnable
    {

        private Callback callback;
        public AsyncCall(Callback callback)
        {
            this.callback = callback;
        }

        @Override
        public void run() {

            boolean signalledCallback = false;

            try {
                Response response = getResponse();
                if(canceled)
                {
                    signalledCallback = true;
                    callback.onFailure(Call.this,new IOException("this task had canceled"));
                }
                else
                {
                    signalledCallback = true;
                    callback.onResponse(Call.this,response);
                }
            }catch (IOException e) {
                if(!signalledCallback)
                    callback.onFailure(Call.this,e);
            }
            finally {
                //将这个任务从调度器移除
                httpClient.getDispatcher().finished(this);
            }
        }

        public String getHost()
        {
            return request.getHttpUrl().getHost();
        }
    }

}
