package com.pankajmehtanewdemo.Adapter;

/**
 * Created by admin on 23-Aug-17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pankajmehtanewdemo.Model.Newsmodel;
import com.pankajmehtanewdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raju on 23-08-2017.
 */


public class PlayVideoAdapter extends RecyclerView.Adapter<PlayVideoAdapter.MyViewHolder> {
    Context context;
    List<Newsmodel> listuser = new ArrayList<>();

    public PlayVideoAdapter(Context context, List<Newsmodel> listuser) {
        this.context = context;
        this.listuser = listuser;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.play_video_layout_adapter, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        holder.tv_news_title.setText(listuser.get(position).getTitle());
    //    Glide.with(context).load(listuser.get(position).getImageuri()).into(holder.iv_news_image);
//        holder.tv_news_date.setText(parseDateToddMMyyyy(Html.fromHtml(listuser.get(position).getDate()).toString()));



    }


    @Override
    public int getItemCount() {
        return listuser.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
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

}
