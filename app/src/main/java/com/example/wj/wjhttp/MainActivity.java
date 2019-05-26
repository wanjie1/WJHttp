package com.example.wj.wjhttp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wj.wjhttp.http.Call;
import com.example.wj.wjhttp.http.Callback;
import com.example.wj.wjhttp.http.HttpClient;
import com.example.wj.wjhttp.http.Request;
import com.example.wj.wjhttp.http.RequestBody;
import com.example.wj.wjhttp.http.Response;

public class MainActivity extends Activity {

    private final String TAG = "WJHttpTest";
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

        HttpClient client;

        client = new HttpClient.Builder()
                .setRetryTimes(3)
                .build();

        RequestBody body = new RequestBody()
                .add("key", "064a7778b8389441e30f91b8a60c9b23")
                .add("city", "深圳");


        Request request = new Request.Builder()
                .setHttpUrl("http://restapi.amap.com/v3/weather/weatherInfo")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.e("响应体", response.getBody());
                //Toast.makeText(MainActivity.this,response.getBody(),Toast.LENGTH_SHORT).show();
            }
        });

       /* HttpClient httpClient = new HttpClient.Builder().setRetryTimes(3).build();

        for(int i = 1;i<=5;i++)
        {
            Request request = new Request.Builder().setHttpUrl("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=15680917815").build();
            Call call = httpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, Throwable throwable) {

                    Log.d(TAG,throwable.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {

                    Log.d(TAG,response.getBody());
                }
            });
        }

        for(int j = 1;j<=5;j++) {
            //Request request = new Request.Builder().setHttpUrl("https://baidu.com/cc/json/mobile_tel_segment.htm?tel=15680917815").build();
            Request request = new Request.Builder().setHttpUrl("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=15680917815").build();
            Call call = httpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, Throwable throwable) {

                    Log.d(TAG, throwable.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) {

                    Log.d(TAG, response.getBody());
                }
            });
        }*/
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
