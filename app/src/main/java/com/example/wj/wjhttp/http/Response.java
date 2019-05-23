package com.example.wj.wjhttp.http;

import java.util.HashMap;
import java.util.Map;

public class Response {

    private int code;   //状态码
    private int contentLength = -1;
    private Map<String,String> headers = new HashMap<>();   //返回包的头信息
    private String body;   //包的内容
    private boolean isKeepAlive;//是否保持连接


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isKeepAlive() {
        return isKeepAlive;
    }

    public void setKeepAlive(boolean keepAlive) {
        isKeepAlive = keepAlive;
    }
}
