package com.pankajmehtanewdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pankajmehtanewdemo.Appcontroler.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class NotificationDetailActivity extends AppCompatActivity {
    ImageView imageView_noti_detail;
    TextView textview_noti_detail,textview_noti_title;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView_noti_detail=(ImageView)findViewById(R.id.imageView_noti_detail);
        textview_noti_detail=(TextView)findViewById(R.id.textview_noti_detail);
        textview_noti_title=(TextView)findViewById(R.id.textview_noti_title);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        loadNotice();
    }



    public void loadNotice() {
        if (isNetworkAvailable()) {
            dialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(800000);
            RequestParams params = new RequestParams();
            params.add("n_id",Constants.noti_id);
            client.post(Constants.uri + "view_notification_detail.php",params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    dialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    if (responseString != null) {
                        Log.e("res", responseString);
                        // listcomplaintype.clear();

                        try {
                            //Toast.makeText(getContext().getApplicationContext(), res.toString(), Toast.LENGTH_SHORT).show();
                            JSONObject jsonObj = new JSONObject(responseString);
                            JSONArray resultsarray = jsonObj.getJSONArray("response");


                           for (int i = 0; i < resultsarray.length(); i++) {
                               String title=resultsarray.getJSONObject(i).getString("title");
                               String notice=resultsarray.getJSONObject(i).getString("notice");
                               textview_noti_title.setText(title);
                               textview_noti_detail.setText(notice);
                               dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                        // Log.d("comty", String.valueOf(comlintypearray.size()));

                    }

                }
            });
        } else {
            Toast.makeText(NotificationDetailActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }




    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
