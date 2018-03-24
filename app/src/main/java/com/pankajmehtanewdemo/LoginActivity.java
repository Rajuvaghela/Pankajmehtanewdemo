package com.pankajmehtanewdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pankajmehtanewdemo.Appcontroler.Constants;
import com.pankajmehtanewdemo.Model.Complainmodel;


import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.category;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button bt_Register, bt_Login;
    EditText et_login_no;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("user");
        categories.add("committee");
        categories.add("admin");
        categories.add("super admin");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        bt_Register = (Button) findViewById(R.id.bt_Register);
        et_login_no = (EditText) findViewById(R.id.et_login_no);
        bt_Register.setOnClickListener(this);
        bt_Login = (Button) findViewById(R.id.bt_Login);
        bt_Login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
                break;
            case R.id.bt_Login:
                //  Logincheck(et_login_no.getText().toString());
                Logincheck(et_login_no.getText().toString());
                break;
        }
    }


    public void Logincheck(String mobno) {
        if (isNetworkAvailable()) {
            dialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(800000);
            RequestParams params = new RequestParams();
            Complainmodel temp1 = new Complainmodel();
            int user = temp1.getUser();
            Log.e("user1", "" + user);
            params.put("flag",user);


            params.put("contact", mobno);
            client.post(Constants.uri + "login_chk.php", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    dialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    if (responseString != null) {
                        if (responseString.equals("success")) {
                            Constants.user_mob = et_login_no.getText().toString();
                            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                            finish();
                            dialog.dismiss();
                            // Toast.makeText(getApplicationContext(), "Successfully Login", Toast.LENGTH_SHORT).show();
                        } else if (responseString.equals("Invalid User")) {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Mobile Number Not Available", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } else {
            dialog.dismiss();
            Toast.makeText(LoginActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e("user", "" + position);
        Complainmodel temp = new Complainmodel();
        temp.setuser(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
