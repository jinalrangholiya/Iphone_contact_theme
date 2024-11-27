package com.icaller.callscreen.icalldialer.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

public class Permission {
    public static boolean isStoragePermissionGranted(Context context) {
        return Build.VERSION.SDK_INT >= 33 ? context.checkSelfPermission("android.permission.READ_MEDIA_IMAGES") == PackageManager.PERMISSION_GRANTED : context.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0 && context.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0;
    }

    public static void getStoragePermission(Activity activity, int i) {
        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.READ_MEDIA_IMAGES"}, i);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, i);
        }
    }

    public static boolean isPhonenGranted(Context context) {
        return Build.VERSION.SDK_INT >= 33 ? context.checkSelfPermission("android.permission.READ_CONTACTS") == 0 && context.checkSelfPermission("android.permission.WRITE_CONTACTS") == 0 && context.checkSelfPermission("android.permission.READ_PHONE_STATE") == 0 && context.checkSelfPermission("android.permission.WRITE_CALL_LOG") == 0 && context.checkSelfPermission("android.permission.READ_CALL_LOG") == 0 : context.checkSelfPermission("android.permission.READ_CONTACTS") == 0 && context.checkSelfPermission("android.permission.WRITE_CONTACTS") == 0 && context.checkSelfPermission("android.permission.READ_PHONE_STATE") == 0 && context.checkSelfPermission("android.permission.WRITE_CALL_LOG") == 0 && context.checkSelfPermission("android.permission.READ_CALL_LOG") == 0;
    }

    public static void getPhonePermission(Activity activity, int i) {
        if (Build.VERSION.SDK_INT >= 33) {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.READ_PHONE_STATE", "android.permission.WRITE_CALL_LOG", "android.permission.READ_CALL_LOG"}, i);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.READ_PHONE_STATE", "android.permission.WRITE_CALL_LOG", "android.permission.READ_CALL_LOG"}, i);
        }
    }
}
