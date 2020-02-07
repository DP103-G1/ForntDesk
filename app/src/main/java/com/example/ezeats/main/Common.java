package com.example.ezeats.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Common {
    public static final String MEMBER_PREFRENCE = "member";
    public static final String REGEX_EMAIL = "^\\w+((-\\w+)|(.\\w+))*@[A-Za-z0-9]+((\\.|\\-)[A-Za-z0-9]+)*\\.[A-Za-z]+$";
    public static final String REGEX_PHONE = "^09[0-9]{8}$";
    public static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    // check if the device connect to the network
    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager != null ? conManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String getMemId(Context context) {
        SharedPreferences pref = context.getSharedPreferences(MEMBER_PREFRENCE, Context.MODE_PRIVATE);
        return pref.getString("memId", null);
    }

    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
