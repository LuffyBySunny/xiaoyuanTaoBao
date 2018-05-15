package com.example.droodsunny.taobao.Unit;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class MyPageAdapter extends PagerAdapter{
    private ViewPager viewPager;
    private List<ImageView> imageViewList;
    public MyPageAdapter(List<ImageView> imageViews,ViewPager viewPager){
        imageViewList=imageViews;
        this.viewPager=viewPager;
    }
    @Override
    public int getCount() {
        return imageViewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView=imageViewList.get(position);
        viewPager.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       viewPager.removeView(imageViewList.get(position));
    }
}
