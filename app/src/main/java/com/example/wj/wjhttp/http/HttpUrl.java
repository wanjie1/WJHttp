package com.example.wj.wjhttp.http;

import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class HttpUrl {

    private String host;   //服务器地址
    private String file;   //请求服务器路径
    private String protocol;//协议，为http、https

    private int port = 80;    //默认地址为80

    public HttpUrl(String url) throws MalformedURLException {

        URL localUrl = new URL(url);  //url格式化
        host = localUrl.getHost();
        file = localUrl.getFile();
        protocol = localUrl.getProtocol();
        port = localUrl.getPort();
        if(-1 == port)
        {
            //url中没有端口信息，使用默认端口，http:80,https:443
            port = localUrl.getDefaultPort();
        }

        if(TextUtils.isEmpty(file)) {
            //没有设置服务器路径
            file = "/";
        }
    }


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
