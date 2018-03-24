package com.pankajmehtanewdemo.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pankajmehtanewdemo.Model.Complainmodel;
import com.pankajmehtanewdemo.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Gradle on 05-Oct-16.
 */
public class ComplainRegisterAdapter extends RecyclerView.Adapter<ComplainRegisterAdapter.CateImageViewHolder> {

    protected Context mContext;
    List<Complainmodel> listUser;
    protected boolean mView;

    public ComplainRegisterAdapter(Context context, List<Complainmodel> listUser){
        this.mContext = context;
        this.listUser = listUser;

    }

    public class CateImageViewHolder extends RecyclerView.ViewHolder {
        TextView tv_setcode,tv_setdate,compmsg;
        LinearLayout ll_requestpending,ll_underprocess,ll_complete;
        ImageView iv_color_set;

        public CateImageViewHolder(View itemView) {
            super(itemView);
            tv_setcode = (TextView)itemView.findViewById(R.id.tv_setcode);
            tv_setdate = (TextView)itemView.findViewById(R.id.tv_setdate);
            iv_color_set=(ImageView)itemView.findViewById(R.id.iv_color_set);
            compmsg = (TextView)itemView.findViewById(R.id.compmsg);
            ll_requestpending = (LinearLayout)itemView.findViewById(R.id.ll_requestpending);
            ll_underprocess = (LinearLayout)itemView.findViewById(R.id.ll_underprocess);
            ll_complete = (LinearLayout)itemView.findViewById(R.id.ll_complete);
        }
    }

    @Override
    public CateImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView  = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapet_complain, parent, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, //width
                ViewGroup.LayoutParams.WRAP_CONTENT);//height
        itemView.setLayoutParams(lp);
        CateImageViewHolder viewHolder = new CateImageViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CateImageViewHolder holder, int position) {
        position = holder.getAdapterPosition();

        holder.tv_setcode.setText(listUser.get(position).getCode());
        holder.tv_setdate.setText(parseDateToddMMyyyy(listUser.get(position).getDate()));
        if (listUser.get(position).getStatus().equals("0")){
            holder.ll_underprocess.setVisibility(View.GONE);
            holder.iv_color_set.setBackgroundColor(Color.parseColor("#ff0000"));
            holder.ll_requestpending.setVisibility(View.VISIBLE);
            holder.ll_complete.setVisibility(View.GONE);
        }else if (listUser.get(position).getStatus().equals("1")){
            holder.ll_underprocess.setVisibility(View.VISIBLE);
            holder.ll_requestpending.setVisibility(View.GONE);
            holder.ll_complete.setVisibility(View.GONE);
            holder.iv_color_set.setBackgroundColor(Color.parseColor("#ffa500"));
            holder.compmsg.setText("તમારી ફરીયાદ પર કામ ચાલુ છે.");
        }else if (listUser.get(position).getStatus().equals("2")){
            holder.ll_underprocess.setVisibility(View.GONE);
            holder.ll_requestpending.setVisibility(View.GONE);
            holder.ll_complete.setVisibility(View.VISIBLE);
            holder.iv_color_set.setBackgroundColor(Color.parseColor("#00ff00"));
            holder.compmsg.setText("તમારી ફરીયાદનો  ઊકેલ થઈ  ચુક્યો છે.");
        }

 /*       holder.tv_cate_img_list.setText(listUser.get(position).getCategoryname().toUpperCase());


        Glide.with(mContext)
                .load(listUser.get(position).getImageuri())
                .into( holder.iv_cate_img_list);*/
    }

    @Override
    public int getItemCount() {
        return listUser.size();
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
