package com.example.wj.wjhttp.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RequestBody {

    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";

    private static final String CHARSET = "UTF-8";

    Map<String,String> encodeBodys = new HashMap<>();

    public String getContentType()
    {
        return CONTENT_TYPE;
    }

    public String getCharset()
    {
        return CHARSET;
    }



    public String getBody()
    {
        StringBuffer stringBuffer = new StringBuffer();

        for(Map.Entry<String,String> entry : encodeBodys.entrySet())
        {
            stringBuffer.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }

        if(stringBuffer.length() != 0)
            stringBuffer.deleteCharAt(stringBuffer.length()-1);
        return stringBuffer.toString();
    }

    public RequestBody add(String key,String value)
    {
        try {
            encodeBodys.put(URLEncoder.encode(CHARSET,key),URLEncoder.encode(CHARSET,value));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }


}
