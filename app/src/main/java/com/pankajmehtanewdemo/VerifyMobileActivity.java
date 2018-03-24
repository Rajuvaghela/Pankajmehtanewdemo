package com.pankajmehtanewdemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pankajmehtanewdemo.Appcontroler.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class VerifyMobileActivity extends AppCompatActivity implements View.OnClickListener, PopupConformMobile.onSubmitListener {
    EditText et_mobile_no;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    Button btn_veryfy_mobile;
    String otp, usermob;
    JSONArray resultsarray;
    ProgressDialog dialog;
    Typeface font;
    TextView tv_vagadsetu;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mobile);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        if (Build.VERSION.SDK_INT >= 23) {
            checkMultiplePermissions();
        } else {
            initialization();
        }
        initialization();

        FirebaseApp.initializeApp(this);
        token = FirebaseInstanceId.getInstance().getToken();
        // Log and toast
        String msg = getString(R.string.msg_token_fmt, token);
        Log.i("Tag", "Tag :" + msg);

        if (token != null && !token.equalsIgnoreCase("")) {
            Constants.fcmToken = token;

        }


    }

    private void initialization() {
        tv_vagadsetu = (TextView) findViewById(R.id.tv_vagadsetu);
        font = Typeface.createFromAsset(getAssets(), "Lohit-Gujarati.ttf");
        et_mobile_no = (EditText) findViewById(R.id.et_mobile_no);
        btn_veryfy_mobile = (Button) findViewById(R.id.btn_veryfy_mobile);
        btn_veryfy_mobile.setOnClickListener(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_veryfy_mobile:
                String no = et_mobile_no.getText().toString();
                if ((!no.matches("^[7-9][0-9]{9}$"))) {
                    Toast.makeText(getApplicationContext(), "Enter mobile number!", Toast.LENGTH_LONG).show();
                } else {
                    Showcomplaindialogue(no);
                }

                break;
        }
    }


    public void Showcomplaindialogue(final String mobno) {
        LayoutInflater li = LayoutInflater.from(VerifyMobileActivity.this);
        View vi = li.inflate(R.layout.custom_dialog_mobile_verify, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(VerifyMobileActivity.this);
        alertDialogBuilder.setView(vi);
        alertDialogBuilder.setCancelable(false);

        Button btn_ok = (Button) vi.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logincheck(mobno);
            }
        });

        TextView tv_mobile_number_title = (TextView) vi.findViewById(R.id.tv_mobile_number_title);
        tv_mobile_number_title.setText(mobno);
        Button btn_close = (Button) vi.findViewById(R.id.btn_close);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


    public void Logincheck(final String mobno) {
        if (isNetworkAvailable()) {
            dialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(800000);
            RequestParams params = new RequestParams();
            params.put("contact", mobno);
            params.put("fcm_token", Constants.fcmToken);
            client.post(Constants.uri + "regi_ins.php", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    dialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    if (responseString != null) {
                        JSONObject jObj = null;
                        try {
                            jObj = new JSONObject(responseString);
                            String res = jObj.getString("success");
                            if (res.equals("true")) {
                                resultsarray = jObj.getJSONArray("response");
                                for (int i = 0; i < resultsarray.length(); i++) {
                                    otp = resultsarray.getJSONObject(i).getString("otp");
                                    usermob = resultsarray.getJSONObject(i).getString("contact");
                                    getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                                            .edit()
                                            .putString("otp", otp)
                                            .putString("usermob", otp)
                                            .apply();
                                    startActivity(new Intent(VerifyMobileActivity.this, EnterPasswordActivity.class));
                                    finish();
                                    dialog.dismiss();
                                }
                            } else if (res.equals("Allready Registerd")) {
                                resultsarray = jObj.getJSONArray("response");
                                for (int i = 0; i < resultsarray.length(); i++) {
                                    otp = resultsarray.getJSONObject(i).getString("otp");
                                    usermob = resultsarray.getJSONObject(i).getString("contact");
                                    getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                                            .edit()
                                            .putString("otp", otp)
                                            .putString("usermob", otp)
                                            .apply();
                                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                            .edit()
                                            .putBoolean("isLoggedIn", true)
                                            .apply();
                                    startActivity(new Intent(VerifyMobileActivity.this, MenuActivity.class).putExtra("Mobile_no", mobno));
                                    finish();
                                    dialog.dismiss();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        } else {
            dialog.dismiss();
            Toast.makeText(VerifyMobileActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void setOnSubmitListener(String arg) {

    }

    private void checkMultiplePermissions() {

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            List<String> permissionsList = new ArrayList<String>();

            if (!addPermission(permissionsList, Manifest.permission.READ_SMS)) {
                permissionsNeeded.add("Read sms");
            }
            if (!addPermission(permissionsList, Manifest.permission.RECEIVE_SMS)) {
                permissionsNeeded.add("Receive sms");
            }
            if (!addPermission(permissionsList, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECEIVE_SMS, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
