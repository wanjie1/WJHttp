package com.example.wj.wjhttp.http;

import java.util.Map;

public class Request {

    private Map<String,String> headers;  //用于设置http头部
    private String method; //请求方法，post、get
    private RequestBody requestBody;    //用于存储方法为post的参数
    private HttpUrl httpUrl;  //http的url信息
    private Request(Builder builder)
    {
        this.headers = builder.headers;
        this.method = builder.method;
        this.requestBody = builder.requestBody;
        this.httpUrl = builder.httpUrl;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getMethod() {
        return method;
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public HttpUrl getHttpUrl() {
        return httpUrl;

    }

    public static final class Builder
    {
        private Map<String,String> headers;  //用于设置http头部
        private String method = null; //请求方法，post、get
        private RequestBody requestBody;    //用于存储方法为post的参数
        private HttpUrl httpUrl = null;  //http的url信息

        public Builder addHeaders(String key,String value)
        {
            headers.put(key,value);
            return this;
        }

        public Builder removeHeaders(String key)
        {
            headers.remove(key);
            return this;
        }

        public Builder post(RequestBody requestBody)
        {
            this.requestBody = requestBody;
            this.method = "post";
            return this;
        }

        public Builder get()
        {
            this.method = "get";
            return this;
        }

        public Builder setHttpUrl(HttpUrl httpUrl)
        {
            this.httpUrl = httpUrl;
            return this;
        }

        public Request build()
        {
            if(httpUrl == null)
                throw new IllegalArgumentException("url不能为空\n");
            if(method == null)
                this.method = "get";

            return new Request(this);
        }

    }
}
