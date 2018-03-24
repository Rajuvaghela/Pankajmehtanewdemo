package com.pankajmehtanewdemo.Adapter;

/**
 * Created by admin on 18-Aug-17.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pankajmehtanewdemo.Model.SocialUpdatesModel;
import com.pankajmehtanewdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raju on 26-07-2017.
 */


public class SocialUpdatesAdapter extends RecyclerView.Adapter<SocialUpdatesAdapter.MyViewHolder> {
    Context context;
    List<SocialUpdatesModel> listuser = new ArrayList<>();

    public SocialUpdatesAdapter(Context context, List<SocialUpdatesModel> listuser) {
        this.context = context;
        this.listuser = listuser;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_social_updates, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
      //  holder.tv_news_title.setText(Html.fromHtml(listuser.get(position).getTitle()), TextView.BufferType.SPANNABLE);
        Glide.with(context).load(listuser.get(position).getImage_url()).into(holder.iv_social_image);
        holder.tv_social_title.setText(Html.fromHtml(listuser.get(position).getImage_description()).toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Intent intent = new Intent(context, NewsDescription.class);
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                facebookIntent.setData(Uri.parse(listuser.get(position).getImage_link()));
                facebookIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(facebookIntent); // start Intent
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

        ImageView iv_social_image;
        TextView tv_social_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_social_image = (ImageView) itemView.findViewById(R.id.iv_social_image);
            tv_social_title = (TextView) itemView.findViewById(R.id.tv_social_title);



        }
    }
}
