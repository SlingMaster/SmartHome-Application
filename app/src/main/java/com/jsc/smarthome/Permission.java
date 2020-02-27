/*
 * Copyright (c) 2020. Jeneral Samopal Company
 * Design and Programming by Alex Dovby
 */

package com.jsc.smarthome;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

// ===================================================
// ===================================================
public class Permission {
    static final int PERMISSION_REQUEST_CODE = 99;

    // ===================================================
    public static void requestMultiplePermissions(Activity main, int request_code) {
        ActivityCompat.requestPermissions(main,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION
                },
                request_code);
    }

    // ===================================================
    public static void comparePermissions(Activity main, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Permission.PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                     Toast.makeText(main, "Permission has already been granted READ_EXTERNAL_STORAGE",
//                          Toast.LENGTH_SHORT).show();
                    System.out.println("trace | PermissionsResult " + grantResults[0] + " == " + PackageManager.PERMISSION_GRANTED);
                }
            }
            if (grantResults.length > 1) {
                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // Toast.makeText(getBaseContext(), "Permission has already been granted WRITE_EXTERNAL_STORAGE",
                    //        Toast.LENGTH_SHORT).show();
                    System.out.println("trace | PermissionsResult " + grantResults[1] + " == " + PackageManager.PERMISSION_GRANTED);
                }
            }
            if (grantResults.length > 2) {
                if (grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(getBaseContext(), "Permission has already been granted ACCESS_FINE_LOCATION",
                    //        Toast.LENGTH_SHORT).show();
                    System.out.println("trace | PermissionsResult " + grantResults[2] + " == " + PackageManager.PERMISSION_GRANTED);
                }
            }
        }
    }
}
