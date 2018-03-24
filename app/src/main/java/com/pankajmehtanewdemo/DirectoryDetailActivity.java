package com.pankajmehtanewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class DirectoryDetailActivity extends AppCompatActivity {
    Window window;
    TextView tv_person_name, tv_address, tv_d_contact_number, tv_email;
    CircleImageView circleImageView_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_detail);
        circleImageView_image = (CircleImageView) findViewById(R.id.circleImageview_image);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_person_name = (TextView) findViewById(R.id.tv_person_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_d_contact_number = (TextView) findViewById(R.id.tv_d_contact_number);

        Bundle bundle = getIntent().getExtras();
        Glide.with(getApplicationContext()).load(bundle.getString("image")).into(circleImageView_image);
        tv_person_name.setText(bundle.getString("title"));
        tv_email.setText(bundle.getString("email"));
        tv_address.setText(bundle.getString("address"));
        tv_d_contact_number.setText(bundle.getString("contact"));

/*
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.parseColor("#9b27ae"));
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9b27ae")));*/
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
