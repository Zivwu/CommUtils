package com.zivwu.commutils;


import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class PermissionHelper {


    public static boolean hasPermission(Context context, String[] permissions) {
        boolean denied = true;
        for (String permission : permissions) {
            denied = denied & hasPermission(context, permission);
        }
        return denied;
    }

    public static boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
