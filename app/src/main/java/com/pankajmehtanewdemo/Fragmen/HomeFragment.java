package com.pankajmehtanewdemo.Fragmen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pankajmehtanewdemo.Adapter.SocialUpdatesAdapter;
import com.pankajmehtanewdemo.Adapter.ViewPagerAdapter;
import com.pankajmehtanewdemo.Appcontroler.Constants;
import com.pankajmehtanewdemo.Model.HomeFragmentModel;
import com.pankajmehtanewdemo.Model.SocialUpdatesModel;
import com.pankajmehtanewdemo.R;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Yash Ajabiya on 1/23/2017.
 */

public class HomeFragment extends Fragment implements ViewPager.OnPageChangeListener{
    ViewPager intro_images;
    public ArrayList<Integer> imgArray = new ArrayList<Integer>();
    public Runnable mRun;
    private ViewPagerAdapter mAdapter;
    public int pos = 0;
    private final Handler handler = new Handler();
    LinearLayout ll1, ll2, ll3;
    ProgressDialog dialog;
    Intent facebookIntent;
    CirclePageIndicator indicator;
    List<HomeFragmentModel> listuser;
    RecyclerView recyclerView_news;
    RecyclerView.Adapter listAdapter;
    RecyclerView.LayoutManager rv_LayoutManager;
    List<SocialUpdatesModel> listuser2;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_omemenu, container, false);
        // pager_introduction=(ViewPager)rootView.findViewById(R.id.pager_introduction);

        recyclerView_news = (RecyclerView) rootView.findViewById(R.id.recyclerView_news);
        intro_images = (ViewPager) rootView.findViewById(R.id.intro_images);
        indicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);


        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        listuser = new ArrayList<>();
        listuser2 = new ArrayList<>();
        loadSlider();
        social_updates_load();
        return rootView;
    }


    private void setTopSlider() {
        mAdapter = new ViewPagerAdapter(getContext().getApplicationContext(), listuser);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);
        intro_images.setCurrentItem(pos, true);
        indicator.setViewPager(intro_images);


        mRun = new Runnable() {
            @Override
            public void run() {
                if (pos < mAdapter.getCount()) {
                    intro_images.setCurrentItem(pos, true);
                    // second_slider.setCurrentItem(pos, true);
                    handler.postDelayed(this, 10000);
                    pos++;
                } else {
                    pos = 0;
                    intro_images.setCurrentItem(pos, true);
                    //second_slider.setCurrentItem(pos, true);
                    handler.postDelayed(this, 10000);
                    indicator.setViewPager(intro_images);
                    pos++;
                }
            }
        };
        handler.post(mRun);


    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



    public void loadSlider() {
        if (isNetworkAvailable()) {
            dialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(800000);
            RequestParams params = new RequestParams();
            client.post(Constants.uri + "slider.php", new TextHttpResponseHandler() {
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
                                HomeFragmentModel temp = new HomeFragmentModel();
                                temp.setSlider_image("http://pankaj.deckoidsolution.com/" + resultsarray.getJSONObject(i).getString("img"));

                                listuser.add(temp);
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                        }
                    }
                    setTopSlider();
                }
            });
        } else {
            Toast.makeText(getContext().getApplicationContext(), "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    public void social_updates_load() {
        if (isNetworkAvailable()) {
            dialog.show();
            AsyncHttpClient client = new AsyncHttpClient();
            client.setTimeout(800000);
            RequestParams params = new RequestParams();
            client.post(Constants.uri + "social_upd.php", new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    dialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    if (responseString != null) {
                        listuser2.clear();
                        try {
                            Log.e("res", responseString);
                            //Toast.makeText(getContext().getApplicationContext(), res.toString(), Toast.LENGTH_SHORT).show();
                            JSONObject jsonObj = new JSONObject(responseString);
                            JSONArray resultsarray = jsonObj.getJSONArray("response");

                            // looping through All Contacts
                            for (int i = 0; i < resultsarray.length(); i++) {
                                SocialUpdatesModel temp = new SocialUpdatesModel();

                                temp.setImage_url("http://pankaj.deckoidsolution.com/" + resultsarray.getJSONObject(i).getString("img"));
                                temp.setImage_description(resultsarray.getJSONObject(i).getString("description"));
                                temp.setImage_link(resultsarray.getJSONObject(i).getString("link"));
                                listuser2.add(temp);
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
                    listAdapter = new SocialUpdatesAdapter(getContext().getApplicationContext(), listuser2);
                    recyclerView_news.setAdapter(listAdapter);


                }
            });
        } else {
            Toast.makeText(getContext().getApplicationContext(), "No Internet connection", Toast.LENGTH_SHORT).show();
        }
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
