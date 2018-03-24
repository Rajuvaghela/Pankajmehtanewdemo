package com.pankajmehtanewdemo.Adapter;

/**
 * Created by raju on 26-07-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pankajmehtanewdemo.Model.Newsmodel;
import com.pankajmehtanewdemo.NewsDescription;
import com.pankajmehtanewdemo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    Context context;
    List<Newsmodel> listuser = new ArrayList<>();

    public NewsAdapter(Context context, List<Newsmodel> listuser) {
        this.context = context;
        this.listuser = listuser;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_adapter_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tv_news_title.setText(Html.fromHtml(listuser.get(position).getTitle()), TextView.BufferType.SPANNABLE);
        Glide.with(context).load(listuser.get(position).getImageuri()).into(holder.iv_news_image);
        holder.tv_news_date.setText(parseDateToddMMyyyy(Html.fromHtml(listuser.get(position).getDate()).toString()));



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("path", "" + listuser.get(position).getImageuri());
                // open another activity on item click
                Intent intent = new Intent(context, NewsDescription.class);
                String string = listuser.get(position).getImageuri();
                String string_des = Html.fromHtml(listuser.get(position).getXtdesc()).toString();
                String string_date = parseDateToddMMyyyy(Html.fromHtml(listuser.get(position).getDate()).toString());
                String string_title = Html.fromHtml(listuser.get(position).getTitle()).toString();
                intent.putExtra("image", string);
                intent.putExtra("date", string_date);
                intent.putExtra("title", string_title);
                intent.putExtra("des", string_des);
                // put image data in Intent

                // Log.e("itemImages.get",""+ itemImages.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent); // start Intent
            }
        });
    }


    @Override
    public int getItemCount() {
        return listuser.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        //   TextView tv;

        ImageView iv_news_image;
        TextView tv_news_title,tv_news_date;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_news_image = (ImageView) itemView.findViewById(R.id.iv_news_image);
            tv_news_title = (TextView) itemView.findViewById(R.id.tv_news_title);
            tv_news_date=(TextView)itemView.findViewById(R.id.tv_news_date);
            // tv = (TextView) itemView.findViewById(R.id.tv_item_name);


        }
    }


    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
