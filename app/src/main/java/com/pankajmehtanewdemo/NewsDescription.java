package com.pankajmehtanewdemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsDescription extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView_new_detail;
    TextView textview_news_detail, textview_news_title, textview_news_date;

    Bitmap bitmap;
    public static final int RequestPermissionCode = 1;
    ImageView iv_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_description);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView_new_detail = (ImageView) findViewById(R.id.imageView_new_detail); // init a ImageView
        textview_news_detail = (TextView) findViewById(R.id.textview_news_detail);
        textview_news_title = (TextView) findViewById(R.id.textview_news_title);
        textview_news_date = (TextView) findViewById(R.id.textview_news_date);



        iv_share=(ImageView)findViewById(R.id.iv_share);
        iv_share.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        String image = bundle.getString("image");
        textview_news_detail.setText(bundle.getString("des"));
        textview_news_title.setText(bundle.getString("title"));
        textview_news_date.setText(bundle.getString("date"));

        Glide.with(getApplicationContext()).load(image).into(imageView_new_detail);
        // selectedImage.setImageResource(intent.getIntExtra("image", 0));
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



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_share:
                if (checkPermission()) {
                    bitmap = getBitmapFromView(imageView_new_detail);
                    //saveImage(bitmap);
                    String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", null);
                    Uri bmpUri = Uri.parse(pathofBmp);
                    final Intent emailIntent1 = new Intent(Intent.ACTION_SEND);
                    emailIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    emailIntent1.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    emailIntent1.setType("image/png");
                    String upToNCharacters = textview_news_detail.getText().toString().substring(0, Math.min(textview_news_detail.getText().toString().length(), 160));
                    //  fulldisplay_description.getText().toString() +"\n"+fulldisplay_address.getText().toString()
                    emailIntent1.putExtra(Intent.EXTRA_TEXT,upToNCharacters+"\n"+ "Get more Description:\nhttp://rajkotcityguide.com");
                  /*  emailIntent1.putExtra(Intent.EXTRA_TEXT,
                            "Get more stickers on Dayro app:\nhttps://play.google.com/store/apps/details?id=com.gradlesol.dayro");*/
                    startActivity(Intent.createChooser(emailIntent1, "Share via"));
                }else {
                    requestPermission();
                }
                break;
        }
    }



    public Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }


    private void    requestPermission() {
        ActivityCompat.requestPermissions(NewsDescription.this, new
                String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestPermissionCode);
    }

    public boolean checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = ContextCompat.checkSelfPermission(NewsDescription.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }else {
            return true;
        }
    }



}
