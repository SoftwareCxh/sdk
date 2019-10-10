package com.learning.bdsdk;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.baidu.mobads.AdView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BDEntryRunImpl {

    public static void vendorRun(Context context, String appId) {
        AdView.setAppSid(context, appId);
        checkAndRequestPermission(context);
    }

    //  <uses-permission android:name="android.permission.INTERNET" />
//    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
//    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
//    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
//    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
//    <uses-permission android:name="android.permissio.WRITE_EXTERNAL_STORAGE" />
//    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
    @TargetApi(Build.VERSION_CODES.M)
    private static void checkAndRequestPermission(Context context) {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE))) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!(checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION))) {
            lackedPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!(checkSelfPermission(context, Manifest.permission.INTERNET))) {
            lackedPermission.add(Manifest.permission.INTERNET);
        }


        if (!(checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE))) {
            lackedPermission.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }

        if (!(checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE))) {
            lackedPermission.add(Manifest.permission.ACCESS_WIFI_STATE);
        }

        if (!(checkSelfPermission(context, Manifest.permission.REQUEST_INSTALL_PACKAGES))) {
            lackedPermission.add(Manifest.permission.REQUEST_INSTALL_PACKAGES);
        }


        if (lackedPermission.size() == 0) {
            // 权限都已经有了，那么直接调用SDK
            //   fetchSplashAD();
        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            ((Activity) context).requestPermissions(requestPermissions, 1000);
        }
    }

    public static boolean checkSelfPermission(Context context, String permission) {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                Method method = Context.class.getMethod("checkSelfPermission",
                        String.class);
                return (Integer) method.invoke(context, permission) == PackageManager.PERMISSION_GRANTED;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


}

