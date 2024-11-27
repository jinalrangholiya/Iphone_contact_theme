package com.icaller.callscreen.icalldialer.AdsDemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class UtilsAPp {

    public interface FileReaderListener {
        void onFailure(String str);

        void onSuccess(String str);
    }


    @SuppressLint("RestrictedApi")
    public static Boolean isOnline(Context context) {
        NetworkInfo[] allNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean z = true;
        if ((activeNetworkInfo == null || !activeNetworkInfo.isConnectedOrConnecting()) && (activeNetworkInfo == null || !activeNetworkInfo.isConnected() || !connectivityManager.getActiveNetworkInfo().isAvailable())) {
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("http://www.google.com").openConnection();
                    httpURLConnection.setConnectTimeout(PathInterpolatorCompat.MAX_NUM_POINTS);
                    httpURLConnection.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                for (NetworkInfo networkInfo : connectivityManager.getAllNetworkInfo()) {
                    System.out.println("get network type :::" + networkInfo.getTypeName());
                    if ((networkInfo.getTypeName().equalsIgnoreCase("WIFI") || networkInfo.getTypeName().equalsIgnoreCase("MOBILE")) && networkInfo.isConnected() && networkInfo.isAvailable()) {
                        break;
                    }
                }
                z = false;
            }
        }
        return Boolean.valueOf(z);
    }
}
