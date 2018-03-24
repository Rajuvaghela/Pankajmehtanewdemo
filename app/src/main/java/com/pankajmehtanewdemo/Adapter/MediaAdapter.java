package com.pankajmehtanewdemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pankajmehtanewdemo.Model.MediaModel;
import com.pankajmehtanewdemo.NewsDescription;
import com.pankajmehtanewdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raju on 26-07-2017.
 */

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MyViewHolder> {

    Context context;
    List<MediaModel> listuser=new ArrayList<>();

    public MediaAdapter(Context context, List<MediaModel> listuser) {
        this.context = context;
        this.listuser=listuser;


    }

    @Override
    public MediaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_adapter, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MediaAdapter.MyViewHolder vh = new MediaAdapter.MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MediaAdapter.MyViewHolder holder, final int position) {
        holder.tv_media_des.setText(listuser.get(position).getTitle(), TextView.BufferType.SPANNABLE);
        Glide.with(context).load(listuser.get(position).getImageuri()).into(holder.iv_media_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("path",""+listuser.get(position).getImageuri());
                // open another activity on item click
                Intent intent = new Intent(context, NewsDescription.class);
                String string=listuser.get(position).getImageuri();
                String string_title=listuser.get(position).getTitle();
                intent.putExtra("image",string);
                intent.putExtra("title",string_title);
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

        ImageView iv_media_image;
        TextView tv_media_des;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_media_image=(ImageView)itemView.findViewById(R.id.iv_media_image);
            tv_media_des=(TextView)itemView.findViewById(R.id.tv_media_des);

            // tv = (TextView) itemView.findViewById(R.id.tv_item_name);



        }
    }
}
