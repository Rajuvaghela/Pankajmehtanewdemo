package com.pankajmehtanewdemo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 5000;//5000
    boolean isloggedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Makefullscreen();
        setTheme(R.style.NoActionbarTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashnew);
        new Handler().postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                isloggedin=   getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isLoggedIn", false);
                if (isloggedin){
                    startActivity(new Intent(SplashActivity.this,MenuActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashActivity.this,VerifyMobileActivity.class));
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void Makefullscreen()
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}
