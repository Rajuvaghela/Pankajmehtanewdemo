package com.pankajmehtanewdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pankajmehtanewdemo.Appcontroler.Constants;
import com.pankajmehtanewdemo.smsverycatcher.OnSmsCatchListener;
import com.pankajmehtanewdemo.smsverycatcher.SmsVerifyCatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnterPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_forgot_password;
    Button btn_veryfy_password;
    String string_mobile_number;


    SmsVerifyCatcher smsVerifyCatcher;
    EditText et_password;
    String  otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        tv_forgot_password.setOnClickListener(this);
        btn_veryfy_password = (Button) findViewById(R.id.btn_veryfy_password);
        btn_veryfy_password.setOnClickListener(this);
        otp=getIntent().getStringExtra("otp");
        et_password=(EditText)findViewById(R.id.et_password);

        Constants.user_otp=getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).getString("otp","");

        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                et_password.setText(message);//set code in edit text
            }
        });

        smsVerifyCatcher.setPhoneNumberFilter("IM-DEKOID");
        smsVerifyCatcher.setPhoneNumberFilter("HP-DEKOID");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forgot_password:
                break;
            case R.id.btn_veryfy_password:
                if (Constants.user_otp != null){
                    if (et_password.getText().toString().equals(Constants.user_otp)){
                        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                .edit()
                                .putBoolean("isLoggedIn", true)
                                .apply();
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                    }else{
                        Toast.makeText(EnterPasswordActivity.this, "Wrong otp Please resend otp", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }


    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
