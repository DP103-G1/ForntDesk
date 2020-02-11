package com.example.ezeats.task;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ezeats.order.OrderWebSocketClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class CommonTask extends AsyncTask<String, Integer, String> {
    private final static String TAG = "TAG_CommonTask";
    public static final String SERVER_URI = "ws://10.0.2.2:8080/Thematic_G1/WSChatBasic/";
    public static OrderWebSocketClient orderWebSocketClient;
    private String url, outStr;

    public static void connectServer(Context context, String userName) {
        URI uri = null;
        try {
            uri = new URI(SERVER_URI + userName);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (orderWebSocketClient == null) {
            orderWebSocketClient = new OrderWebSocketClient(uri, context);
            orderWebSocketClient.connect();
        }
    }

    public static void disconnectServer() {
        if (orderWebSocketClient != null) {
            orderWebSocketClient.close();
            orderWebSocketClient = null;
        }
    }

    public CommonTask(String url, String outStr) {
        this.url = url;
        this.outStr = outStr;
    }

    @Override
    protected String doInBackground(String... params) {
        return getRemoteData();
    }

    private String getRemoteData() {
        HttpURLConnection connection = null;
        StringBuilder inStr = new StringBuilder();
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true); // allow inputs
            connection.setDoOutput(true); // allow outputs
            // 不知道請求內容大小時可以呼叫此方法將請求內容分段傳輸，設定0代表使用預設大小
            // 參考HttpURLConnection API的Posting Content部分
            connection.setChunkedStreamingMode(0);
            connection.setUseCaches(false); // do not use a cached copy
            connection.setRequestMethod("POST");
            connection.setRequestProperty("charset", "UTF-8");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bw.write(outStr);
            Log.d(TAG, "output: " + outStr);
            bw.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    inStr.append(line);
                }
            } else {
                Log.d(TAG, "response code: " + responseCode);
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        Log.d(TAG, "input: " + inStr);
        return inStr.toString();
    }
}