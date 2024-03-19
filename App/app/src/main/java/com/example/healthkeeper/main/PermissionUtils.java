package com.example.healthkeeper.main;

import android.Manifest;
import android.content.Context;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

public class PermissionUtils {
    public void checkPermission(PermissionListener permissionListener) {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("Denied Permission.")
                .setPermissions(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.SEND_SMS)
                .check();
    }

    public void checkBtPermission(PermissionListener permissionListener) {
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

    public void checkCallPermission(PermissionListener permissionListener) {
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("Denied Permission.")
                .setPermissions(
                        Manifest.permission.CALL_PHONE
                )
                .check();
    }
}
