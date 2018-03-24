package com.pankajmehtanewdemo.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pankajmehtanewdemo.Model.HomeFragmentModel;
import com.pankajmehtanewdemo.R;

import java.util.List;

/**
 * Created by hp-pc on 18-03-2016.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;

    public List<HomeFragmentModel> imgArray;

    public ViewPagerAdapter(Context mContext, List<HomeFragmentModel> imgArray) {
        this.mContext = mContext;
        this.imgArray = imgArray;
    }

    @Override
    public int getCount() {
        return imgArray.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item_banner, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
       // imageView.setImageResource(imgArray.get(position));
        Glide.with(mContext).load(imgArray.get(position).getSlider_image()).into(imageView);
        TextView slidenum =(TextView)itemView.findViewById(R.id.slidenum);
        slidenum.setText("("+(position+1)+"/"+imgArray.size()+")");

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}