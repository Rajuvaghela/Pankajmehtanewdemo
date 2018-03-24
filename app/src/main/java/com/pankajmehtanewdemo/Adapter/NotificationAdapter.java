package com.pankajmehtanewdemo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pankajmehtanewdemo.Model.NotificationModel;
import com.pankajmehtanewdemo.R;

import java.util.List;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    protected Context mContext;
    List<NotificationModel> listUser;

    Context context;

    public NotificationAdapter(Context context, List<NotificationModel> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_adapter, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.textView_notification_title.setText(listUser.get(position).getTitle());

        holder.tv_notice_discription.setText(listUser.get(position).getNotice());

/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open another activity on item click
                Intent intent = new Intent(context, DirectoryDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.putExtra("image", itemImages.get(position)); // put image data in Intent

                // Log.e("itemImages.get",""+ itemImages.get(position));
                context.startActivity(intent); // start Intent
            }
        });*/


/*        // set the data in items
      //  holder.name.setText(itemNames.get(position));
       // holder.image.setImageResource(itemImages.get(position));
        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open another activity on item click
*//*                Intent intent = new Intent(context, SecondActivity.class);
                intent.putExtra("image", itemImages.get(position)); // put image data in Intent
                context.startActivity(intent); // start Intent*//*
            }
        });*/

    }


    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView_notification_title, tv_notice_discription;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_notice_discription = (TextView) itemView.findViewById(R.id.tv_notice_discription);
            textView_notification_title = (TextView) itemView.findViewById(R.id.textView_notification_title);


        }
    }
}