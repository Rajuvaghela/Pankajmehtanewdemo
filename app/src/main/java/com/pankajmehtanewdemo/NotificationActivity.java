package com.pankajmehtanewdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pankajmehtanewdemo.Adapter.NotificationAdapter;
import com.pankajmehtanewdemo.Appcontroler.Constants;
import com.pankajmehtanewdemo.Model.NotificationModel;
import com.pankajmehtanewdemo.util.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView rv_notification;

    ProgressDialog dialog;
    List<NotificationModel> listuser;


    RecyclerView.Adapter listAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv_notification = (RecyclerView) findViewById(R.id.rv_notification);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        listuser = new ArrayList<>();
        loadNotice();
    }

    public void loadNotice() {
        if (isNetworkAvailable()) {
            dialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(800000);
            RequestParams params = new RequestParams();
            client.post(Constants.uri + "view_notification.php", new TextHttpResponseHandler() {
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
                            Log.e("res", responseString);
                            //Toast.makeText(getContext().getApplicationContext(), res.toString(), Toast.LENGTH_SHORT).show();
                            JSONObject jsonObj = new JSONObject(responseString);
                            JSONArray resultsarray = jsonObj.getJSONArray("response");
                            listuser.clear();
                            // looping through All Contacts
                            for (int i = 0; i < resultsarray.length(); i++) {
                                NotificationModel temp = new NotificationModel();
                                temp.setTitle(resultsarray.getJSONObject(i).getString("title"));
                                temp.setNotice(resultsarray.getJSONObject(i).getString("notice"));
                                temp.setNid(resultsarray.getJSONObject(i).getString("n_id"));
                                listuser.add(temp);

                                //  listuser.add(resultsarray.getJSONObject(i).getString("tt_name"));

                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                        // Log.d("comty", String.valueOf(comlintypearray.size()));

                    }
                    rv_notification.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    rv_notification.setLayoutManager(layoutManager);
                    listAdapter = new NotificationAdapter(NotificationActivity.this, listuser);
                    rv_notification.setAdapter(listAdapter);
                    rv_notification.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Constants.noti_id=listuser.get(position).getNid();
                            startActivity(new Intent(getApplicationContext(), NotificationDetailActivity.class));
                        }
                    }));
                }
            });
        } else {
            Toast.makeText(NotificationActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadNotice();
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
