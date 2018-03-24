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
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pankajmehtanewdemo.Adapter.DirectoryAdapter;
import com.pankajmehtanewdemo.Appcontroler.Constants;
import com.pankajmehtanewdemo.Model.DirectoryModel;
import com.pankajmehtanewdemo.util.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class DirectroyActivity extends AppCompatActivity {
    RecyclerView recyclerView_directory;
    RecyclerView.Adapter listAdapter;
    RecyclerView.LayoutManager layoutManager;


    ProgressDialog dialog;
    List<DirectoryModel> directoryModels_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directroy);
        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM); //bellow setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.title_bar);

        recyclerView_directory = (RecyclerView) findViewById(R.id.recyclerView_directory);
/*        recyclerView_directory.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView_directory.setLayoutManager(layoutManager);
        listAdapter = new DirectoryAdapter(DirectroyActivity.this);
        recyclerView_directory.setAdapter(listAdapter);*/
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        directoryModels_list = new ArrayList<>();
        loadNotice();
    }

    public void loadNotice() {
        if (isNetworkAvailable()) {
            dialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(800000);
            RequestParams params = new RequestParams();
            client.post(Constants.uri + "sel_directory.php", new TextHttpResponseHandler() {
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
                            directoryModels_list.clear();
                            // looping through All Contacts
                            for (int i = 0; i < resultsarray.length(); i++) {
                                DirectoryModel temp = new DirectoryModel();
                                temp.setEmail(resultsarray.getJSONObject(i).getString("d_email"));
                                temp.setTitle(resultsarray.getJSONObject(i).getString("d_name"));
                                temp.setContact(resultsarray.getJSONObject(i).getString("d_contact"));
                                temp.setD_id(resultsarray.getJSONObject(i).getString("d_id"));
                                temp.setImage("http://pankaj.deckoidsolution.com/" + resultsarray.getJSONObject(i).getString("d_img"));
                                temp.setAddress(resultsarray.getJSONObject(i).getString("d_address"));
                                temp.setDate(resultsarray.getJSONObject(i).getString("d_date"));
                                temp.setPincode(resultsarray.getJSONObject(i).getString("d_pincode"));
                                temp.setArea(resultsarray.getJSONObject(i).getString("d_area"));
                                temp.setCity(resultsarray.getJSONObject(i).getString("d_city"));
                                directoryModels_list.add(temp);

                                //  listuser.add(resultsarray.getJSONObject(i).getString("tt_name"));

                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                        // Log.d("comty", String.valueOf(comlintypearray.size()));

                    }

                    recyclerView_directory.setHasFixedSize(true);
                    recyclerView_directory.setItemViewCacheSize(20);
                    recyclerView_directory.setDrawingCacheEnabled(true);
                    recyclerView_directory.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


                    layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView_directory.setLayoutManager(layoutManager);
                    listAdapter = new DirectoryAdapter(DirectroyActivity.this, directoryModels_list);
                    recyclerView_directory.setAdapter(listAdapter);
                    recyclerView_directory.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Constants.noti_id = directoryModels_list.get(position).getD_id();
                            startActivity(new Intent(getApplicationContext(), NotificationDetailActivity.class));
                        }
                    }));
                }
            });
        } else {
            Toast.makeText(DirectroyActivity.this, "No Internet connection", Toast.LENGTH_SHORT).show();
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
}
