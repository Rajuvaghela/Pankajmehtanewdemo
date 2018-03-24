package com.pankajmehtanewdemo.Fragmen;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import com.pankajmehtanewdemo.Adapter.MediaAdapter;
import com.pankajmehtanewdemo.Appcontroler.Constants;
import com.pankajmehtanewdemo.Model.MediaModel;
import com.pankajmehtanewdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Yash Ajabiya on 1/23/2017.
 */

public class MediaFragment extends Fragment {
    ImageView iv1, iv2, iv3, iv4, iv5, iv6;

    ProgressDialog dialog;
    List<MediaModel> listuser;
    RecyclerView.Adapter listAdapter;
    RecyclerView recyclerView_media;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_edia, container, false);
        recyclerView_media = (RecyclerView) rootView.findViewById(R.id.recyclerView_media);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        listuser = new ArrayList<>();
        Mediaload();
/*        iv1=(ImageView)rootView.findViewById(R.id.iv1);
        iv2=(ImageView)rootView.findViewById(R.id.iv2);
        iv3=(ImageView)rootView.findViewById(R.id.iv3);
        iv4=(ImageView)rootView.findViewById(R.id.iv4);
        iv5=(ImageView)rootView.findViewById(R.id.iv5);
        iv6=(ImageView)rootView.findViewById(R.id.iv6);*/

     /*   Glide.with(getActivity())
                .load("https://pankajmehtabjp.com/wp-content/uploads/2017/04/banner-1.jpg")
                .into(iv1);

        Glide.with(getActivity())
                .load("https://pankajmehtabjp.com/wp-content/uploads/2017/06/DSC_0227.png")
                .into(iv2);

        Glide.with(getActivity())
                .load("https://pankajmehtabjp.com/wp-content/uploads/2017/06/DSC_9118.png")
                .into(iv3);

        Glide.with(getActivity())
                .load("https://pankajmehtabjp.com/wp-content/uploads/2017/06/IMG_3679.png")
                .into(iv4);
        Glide.with(getActivity())
                .load("https://pankajmehtabjp.com/wp-content/uploads/2017/06/DSC_0143.png")
                .into(iv5);
        Glide.with(getActivity())
                .load("https://pankajmehtabjp.com/wp-content/uploads/2017/04/DSC_6446.jpg")
                .into(iv6);*/

        return rootView;
    }


    public void Mediaload() {
        if (isNetworkAvailable()) {
            dialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(800000);
            RequestParams params = new RequestParams();
            client.post(Constants.uri + "media.php", new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    dialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    if (responseString != null) {
                        listuser.clear();
                        try {
                            Log.e("res", responseString);
                            //Toast.makeText(getContext().getApplicationContext(), res.toString(), Toast.LENGTH_SHORT).show();
                            JSONObject jsonObj = new JSONObject(responseString);
                            JSONArray resultsarray = jsonObj.getJSONArray("response");

                            // looping through All Contacts
                            for (int i = 0; i < resultsarray.length(); i++) {
                                MediaModel temp = new MediaModel();
                                String uri = "http://pankaj.deckoidsolution.com/" + resultsarray.getJSONObject(i).getString("img");
                                Log.e("path", "" + uri);
                                temp.setImageuri("http://pankaj.deckoidsolution.com/" + resultsarray.getJSONObject(i).getString("img"));
                                temp.setTitle(resultsarray.getJSONObject(i).getString("title"));
                                listuser.add(temp);
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                    }

/*                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
                    //  call the constructor of CustomAdapter to send the reference and data to Adapter
                    CustomAdapter customAdapter = new CustomAdapter(YummyFoodsActivity.this, itemNames, itemImages);
                    recyclerView.setAdapter(customAdapter);*/


                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 2);
                    recyclerView_media.setHasFixedSize(true);
                    recyclerView_media.setItemViewCacheSize(20);
                    recyclerView_media.setDrawingCacheEnabled(true);
                    recyclerView_media.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    // mLayoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView_media.setLayoutManager(gridLayoutManager);
                    listAdapter = new MediaAdapter(getContext().getApplicationContext(), listuser);
                    recyclerView_media.setAdapter(listAdapter);


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
