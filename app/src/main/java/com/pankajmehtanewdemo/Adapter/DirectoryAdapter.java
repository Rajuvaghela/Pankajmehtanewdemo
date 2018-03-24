package com.pankajmehtanewdemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pankajmehtanewdemo.DirectoryDetailActivity;
import com.pankajmehtanewdemo.Model.DirectoryModel;
import com.pankajmehtanewdemo.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.MyViewHolder> {


    Context context;
    List<DirectoryModel> directoryModel_list = new ArrayList<>();
    ;

    public DirectoryAdapter(Context context, List<DirectoryModel> directoryModel_list) {
        this.context = context;
        this.directoryModel_list = directoryModel_list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.directory_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.textView_d_persion_name.setText(Html.fromHtml(directoryModel_list.get(position).getTitle()), TextView.BufferType.SPANNABLE);
        holder.tv_d_email.setText(Html.fromHtml(directoryModel_list.get(position).getEmail()), TextView.BufferType.SPANNABLE);
        holder.tv_d_contact_number.setText(Html.fromHtml(directoryModel_list.get(position).getContact()), TextView.BufferType.SPANNABLE);
        Glide.with(context).load(directoryModel_list.get(position).getImage()).into(holder.iv_dir_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DirectoryDetailActivity.class);
                String string_title = Html.fromHtml(directoryModel_list.get(position).getTitle()).toString();
                String string_email = Html.fromHtml(directoryModel_list.get(position).getEmail()).toString();
                String string_address = Html.fromHtml(directoryModel_list.get(position).getAddress()).toString();
                String string_contact = Html.fromHtml(directoryModel_list.get(position).getContact()).toString();
                intent.putExtra("image", directoryModel_list.get(position).getImage());
                intent.putExtra("title", string_title);
                intent.putExtra("email", string_email);
                intent.putExtra("address", string_address);
                intent.putExtra("contact", string_contact);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return directoryModel_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        LinearLayout ll_directory;
        CircleImageView iv_dir_image;
        TextView tv_d_email, tv_d_contact_number, textView_d_persion_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            ll_directory = (LinearLayout) itemView.findViewById(R.id.ll_directory);
            tv_d_email = (TextView) itemView.findViewById(R.id.tv_d_email);
            iv_dir_image = (CircleImageView) itemView.findViewById(R.id.iv_dir_image);
            tv_d_contact_number = (TextView) itemView.findViewById(R.id.tv_d_contact_number);
            textView_d_persion_name = (TextView) itemView.findViewById(R.id.textView_d_persion_name);


        }
    }
}