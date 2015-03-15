package com.baxterpad.jengaapp;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Sean on 3/15/2015.
 */
public class Network {

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =  getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
