package com.example.newsdemo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @version $Rev$
 * @anthor Administrator
 * @dsc ${TOOD}
 * @updateAuthor $Author
 * @updateDsc ${TOOD}
 */
public class HttpUtil {

    public static void sendHttpRequest(final String address, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    StringBuilder sbd = new StringBuilder();
                    while ((line = reader.readLine()) != null){
                        sbd.append(line);
                    }
                    if (listener != null){
                        listener.onFinish(sbd.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null)
                    listener.onError(e);
                }finally {
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }


    public static void sendRequestWithOkhttp(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
