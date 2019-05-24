package com.example.wj.wjhttp.http;

import com.example.wj.wjhttp.http.chain.Interceptor;
import java.util.List;

public class HttpClient {

    private Dispatcher dispatcher;
    private List<Interceptor> interceptors;
    private ConnectionPool connectionPool;
    private int retryTimes;
    //还需要添加连接池

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public ConnectionPool getConnectionPool()
    {
        return connectionPool;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    //生成一个网络请求Call对象实例
    public Call newCall(Request request)
    {
        return new Call(this,request);
    }

    private HttpClient(Builder builder)
    {
        this.dispatcher = builder.dispatcher;
        this.connectionPool = builder.connectionPool;
        this.interceptors = builder.interceptors;
        this.retryTimes = builder.retryTimes;
    }

    public static final class Builder
    {
        private Dispatcher dispatcher;
        private List<Interceptor> interceptors;
        private ConnectionPool connectionPool;
        private int retryTimes;

        public Builder addInterceptors(Interceptor interceptor){
            interceptors.add(interceptor);
            return this;
        }

        public Builder setDispather(Dispatcher dispather){
            this.dispatcher = dispather;
            return this;
        }

        public Builder setRetryTimes(int retryTimes){
            this.retryTimes = retryTimes;
            return this;
        }

        public Builder setConnectionPool(ConnectionPool connectionPool){
            this.connectionPool = connectionPool;
            return this;
        }


        public HttpClient build(){

            if(null == dispatcher){
                dispatcher = new Dispatcher();
            }

            if(null == connectionPool){
                connectionPool = new ConnectionPool();
            }
            return new HttpClient(this);
        }
    }

}
