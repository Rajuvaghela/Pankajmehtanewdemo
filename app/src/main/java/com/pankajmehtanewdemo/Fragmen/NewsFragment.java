package com.pankajmehtanewdemo.Fragmen;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pankajmehtanewdemo.Adapter.NewsAdapter;
import com.pankajmehtanewdemo.Appcontroler.Constants;
import com.pankajmehtanewdemo.Model.Newsmodel;
import com.pankajmehtanewdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class NewsFragment extends Fragment {
    RecyclerView recyclerView_news;
    RecyclerView.Adapter listAdapter;
    RecyclerView.LayoutManager rv_LayoutManager;
    ProgressDialog dialog;
    List<Newsmodel> listuser;
    ImageView iv_share;
    Bitmap bitmap;
    public static final int RequestPermissionCode = 1;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_news, container, false);
        recyclerView_news = (RecyclerView) rootView.findViewById(R.id.recyclerView_news);

        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        listuser = new ArrayList<>();

        Newsload();
        return rootView;
    }


    public void Newsload() {
        if (isNetworkAvailable()) {
            dialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(800000);
            RequestParams params = new RequestParams();
            client.post(Constants.uri + "news.php", new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    dialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    if (responseString != null) {
                        listuser.clear();
                        try {
                            Log.d("res", responseString);
                            //Toast.makeText(getContext().getApplicationContext(), res.toString(), Toast.LENGTH_SHORT).show();
                            JSONObject jsonObj = new JSONObject(responseString);
                            JSONArray resultsarray = jsonObj.getJSONArray("response");

                            // looping through All Contacts
                            for (int i = 0; i < resultsarray.length(); i++) {
                                Newsmodel temp = new Newsmodel();
                                String uri = "http://pankaj.deckoidsolution.com/" + resultsarray.getJSONObject(i).getString("img");
                                Log.e("path", "" + uri);
                                temp.setImageuri("http://pankaj.deckoidsolution.com/" + resultsarray.getJSONObject(i).getString("img"));
                                temp.setXtdesc(resultsarray.getJSONObject(i).getString("description"));
                                temp.setDate(resultsarray.getJSONObject(i).getString("n_date"));
                                temp.setTitle(resultsarray.getJSONObject(i).getString("title"));
                                listuser.add(temp);
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                    recyclerView_news.setHasFixedSize(true);
                    recyclerView_news.setHasFixedSize(true);
                    recyclerView_news.setItemViewCacheSize(20);
                    recyclerView_news.setDrawingCacheEnabled(true);
                    recyclerView_news.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    rv_LayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView_news.setLayoutManager(rv_LayoutManager);
                    listAdapter = new NewsAdapter(getContext().getApplicationContext(), listuser);
                    recyclerView_news.setAdapter(listAdapter);


                }
            });
        } else {
            Toast.makeText(getContext().getApplicationContext(), "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
