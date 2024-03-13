package com.example.healthkeeper.main;

import android.Manifest;
import android.content.Context;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

public class PermissionUtils {
    public static void checkNotiPermission(Context context, PermissionListener permissionListener) {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("Denied Permission.")
                .setPermissions(Manifest.permission.POST_NOTIFICATIONS)
                .check();
    }

    public static void checkBtPermission(Context context, PermissionListener permissionListener) {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("Denied Permission.")
                .setPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                .check();
    }

    public static void checkCallPermission(Context context, PermissionListener permissionListener) {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("Denied Permission.")
                .setPermissions(
                        Manifest.permission.CALL_PHONE
                )
                .check();
    }
}
