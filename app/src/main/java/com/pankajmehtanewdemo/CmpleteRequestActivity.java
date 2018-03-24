package com.pankajmehtanewdemo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CmpleteRequestActivity extends AppCompatActivity {
    TextView tv_codeid;
    String unid;
    private static int SPLASH_TIME_OUT = 3000;//5000

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cmplete);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        tv_codeid = (TextView) findViewById(R.id.tv_codeid);
        unid = getIntent().getStringExtra("unid");
        tv_codeid.setText(unid);
        new Handler().postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                startActivity(new Intent(CmpleteRequestActivity.this,MenuActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);


    }


    @Override
    public void onBackPressed() {

    }
}
