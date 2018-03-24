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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    ProgressDialog dialog;
    EditText et_Register_Mobile, et_Register_Name;
    Button bt_Register_Submit;
    int Year, Month, Day;
    String hDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");
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
        et_Register_Mobile = (EditText) findViewById(R.id.et_Register_Mobile);
        et_Register_Name = (EditText) findViewById(R.id.et_Register_Name);
        bt_Register_Submit = (Button) findViewById(R.id.bt_Register_Submit);
        bt_Register_Submit.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        int mnth = Month + 1;
        String date = Day + "-" + mnth + "-" + Year;
        hDate = Year + "-" + mnth + "-" + Day;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_Register_Submit:
                registerSubmitData(et_Register_Mobile.getText().toString(), et_Register_Name.getText().toString());
        }

    }

    public void registerSubmitData(String mob, String name) {
        if (isNetworkAvailable()) {
            dialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(800000);
            RequestParams params = new RequestParams();
            Complainmodel temp1 = new Complainmodel();
            int user = temp1.getUser();
            Log.e("user1", "" + user);
            params.put("flag",user);
            params.put("contact", mob);
            params.put("name", name);
            params.put("date", hDate);
            params.put("flag", user);
            client.post(Constants.uri + "regi_ins.php", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    dialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    if (responseString != null) {
                        if (responseString.equals("success")) {
                            Constants.user_mob = et_Register_Mobile.getText().toString();
                            startActivity(new Intent(RegisterActivity.this, MenuActivity.class));
                            dialog.dismiss();
                            finish();
                        } else if (responseString.equals("Allready Registerd")) {
                            dialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "User Allready Registered", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        } else {
            dialog.dismiss();
            Toast.makeText(RegisterActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
