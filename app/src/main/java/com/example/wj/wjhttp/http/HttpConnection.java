package com.example.wj.wjhttp.http;

import android.text.TextUtils;

import com.example.wj.wjhttp.HttpCodec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

public class HttpConnection {

    Socket socket;
    long lastUseTime;

    private Request request;
    private InputStream inputStream;
    private OutputStream outputStream;

    public void setRequest(Request request)
    {
        this.request = request;
    }

    public void updateLastUseTime()
    {
        lastUseTime = System.currentTimeMillis();
    }

    public boolean isSameAddress(String host,int port)
    {
        if(null == socket)
            return false;

        return TextUtils.equals(request.getHttpUrl().getHost(),host) && request.getHttpUrl().getPort() == port;
    }

    /**
     * 创建socket连接
     * @throws IOException
     */
    private void createSocket() throws IOException
    {
        if(null == socket || socket.isClosed())
        {
            HttpUrl httpUrl = request.getHttpUrl();
            if(httpUrl.getProtocol().equalsIgnoreCase(HttpCodec.PROTOCOL_HTTPS)){
                //如果是https，就需要使用jdk默认的SSLSocketFactory来创建socket
                socket = SSLSocketFactory.getDefault().createSocket();
            }else{
                socket = new Socket();
            }

            socket.connect(new InetSocketAddress(httpUrl.getHost(),httpUrl.getPort()));
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        }
    }


    /**
     *关闭socketed连接
     */
    public void closed()
    {
        if(null != socket)
        {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 向服务器写入数据
     * @param httpCodec
     * @return
     * @throws IOException
     */
    public InputStream call(HttpCodec httpCodec) throws IOException {

        //创建socket连接
        createSocket();
        //发送请求
        httpCodec.writeRequest(outputStream,request);
        //返回服务器响应
        return inputStream;
    }

}
