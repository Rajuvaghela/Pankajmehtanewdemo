package com.pankajmehtanewdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pankajmehtanewdemo.Adapter.PlayVideoAdapter;
import com.pankajmehtanewdemo.Appcontroler.Constants;
import com.pankajmehtanewdemo.Model.Newsmodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class WatchVideoActivity extends AppCompatActivity {
    RecyclerView recyclerView_play_video;
    RecyclerView.Adapter listAdapter;
    RecyclerView.LayoutManager rv_LayoutManager;
    ProgressDialog dialog;
    List<Newsmodel> listuser;
    ImageView iv_share;
    Bitmap bitmap;
    VideoView videoView;
    public static final int RequestPermissionCode = 1;
    Uri uri;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);

        recyclerView_play_video = (RecyclerView) findViewById(R.id.recyclerView_play_video);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        listuser = new ArrayList<>();
        videoView = (VideoView) findViewById(R.id.youtube_view);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);


        loadvideo();
    }

    public void loadvideo() {
        if (isNetworkAvailable()) {
            dialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(800000);
            RequestParams params = new RequestParams();
            client.post(Constants.uri + "video.php", new TextHttpResponseHandler() {
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
                            JSONObject jsonObj = new JSONObject(responseString);
                            JSONArray resultsarray = jsonObj.getJSONArray("response");

                            for (int i = 0; i < resultsarray.length(); i++) {
                                Newsmodel temp = new Newsmodel();
                                temp.setTitle(resultsarray.getJSONObject(i).getString("v_link"));
                                listuser.add(temp);
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                    recyclerView_play_video.setHasFixedSize(true);
                    recyclerView_play_video.setHasFixedSize(true);
                    recyclerView_play_video.setItemViewCacheSize(20);
                    recyclerView_play_video.setDrawingCacheEnabled(true);
                    recyclerView_play_video.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    rv_LayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView_play_video.setLayoutManager(rv_LayoutManager);
                    listAdapter = new PlayVideoAdapter(getApplicationContext(), listuser);
                    recyclerView_play_video.setAdapter(listAdapter);
                    recyclerView_play_video.addOnItemTouchListener(new RecyclerItemClickListener(WatchVideoActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(getApplicationContext(), YoutubeVideoPlayActivity.class);
                            intent.putExtra("id", listuser.get(position).getTitle());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent); // start Intent


                        }
                    }));


                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
