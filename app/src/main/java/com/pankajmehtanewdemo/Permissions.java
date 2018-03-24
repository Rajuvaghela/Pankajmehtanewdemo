package com.pankajmehtanewdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by raju on 17-07-2017.
 */

public class Permissions extends Activity {
    static final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public void checkMultiplePermissions() {

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            List<String> permissionsList = new ArrayList<String>();

            if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION)) {
                permissionsNeeded.add("GPS");
            }

            if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissionsNeeded.add("Read Storage");
            }

            if (permissionsList.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23)


            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);

                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        return true;
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    return;
                } else {
                    // Permission Denied
                    if (Build.VERSION.SDK_INT >= 23) {
                        Toast.makeText(
                                getApplicationContext(),
                                "App cannot run without  " +
                                        "Permissions.\nRelaunch App or allow permissions" +
                                        " in Applications Settings",
                                Toast.LENGTH_LONG).show();

                        finish();
                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
