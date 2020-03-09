package com.example.ezeats.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.ezeats.socket.EZeatsWebSocketClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class Common {
    public static final String MEMBER_PREFRENCE = "member";
    public static final String NUMBER = "discount";
    public static final String REGEX_EMAIL = "^\\w+((-\\w+)|(.\\w+))*@[A-Za-z0-9]+((\\.|\\-)[A-Za-z0-9]+)*\\.[A-Za-z]+$";
    public static final String REGEX_PHONE = "^09[0-9]{8}$";
    public static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    public static EZeatsWebSocketClient eZeatsWebSocketClient;

    // check if the device connect to the network
    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager != null ? conManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void connectSocketServer(Context context, String employeeType) {
        URI uri = null;
        try {
            uri = new URI(Url.SOCKET_URI + employeeType);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (eZeatsWebSocketClient == null) {
            eZeatsWebSocketClient = new EZeatsWebSocketClient(uri, context);
            eZeatsWebSocketClient.connect();
        }
    }

    public static void disconnectSocketServer() {
        if (eZeatsWebSocketClient != null) {
            eZeatsWebSocketClient.close();
            eZeatsWebSocketClient = null;
        }
    }

    public static int getMemId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(MEMBER_PREFRENCE, Context.MODE_PRIVATE);
        return pref.getInt("memId",0);
    }

    public static boolean isServiceOn(Context context) {
        SharedPreferences pref = context.getSharedPreferences(MEMBER_PREFRENCE, Context.MODE_PRIVATE);
        return pref.getBoolean("serviceOn",false);
    }

    public static String getDis(Context context){
        SharedPreferences pref = context.getSharedPreferences(NUMBER, Context.MODE_PRIVATE);
        return pref.getString("number",null);
    }

    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
