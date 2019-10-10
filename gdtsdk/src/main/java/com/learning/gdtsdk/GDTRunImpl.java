package com.learning.gdtsdk;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

class GDTRunImpl {
    static Context ctx;
    public static void vendorRun(Context context) {
        ctx=context;
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(ctx.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(ctx.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 如果需要的权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() == 0) {
            // fetchSplashAD(this, container, skipView, Constants.APPID, getPosId(), this, 0);
        } else {
            // 否则，建议请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            ((Activity)ctx).requestPermissions(requestPermissions, 1024);
        }
    }
}
