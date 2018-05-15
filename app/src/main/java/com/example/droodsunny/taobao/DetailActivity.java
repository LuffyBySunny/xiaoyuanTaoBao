package com.example.droodsunny.taobao;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.droodsunny.taobao.Unit.Goods;
import com.example.droodsunny.taobao.Unit.MyPageAdapter;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private Goods mGoods;
    private ViewPager viewPager;
    private TextView mGoodName;
    private TextView mUserName;
    private TextView mPrice;
    private TextView mDescripton;
    private List<ImageView> imageViewList;
    private MyPageAdapter myPageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageViewList=new ArrayList<>();

        viewPager=findViewById(R.id.viewpager);
        mGoodName=findViewById(R.id.good_name);
        mUserName=findViewById(R.id.user_name);
        mPrice=findViewById(R.id.price_text);
        mDescripton=findViewById(R.id.description_text);

        Intent intent=getIntent();
         Bundle bundle=intent.getBundleExtra("good");
         mGoods=bundle.getParcelable("good");
        assert mGoods != null;
        for(String str:mGoods.getImage()){
             ImageView imageView= new ImageView(this);
             imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
             Glide.with(this).load(str).apply(RequestOptions.placeholderOf(R.drawable.loading)).into(imageView);
             imageViewList.add(imageView);
         }
         myPageAdapter=new MyPageAdapter(imageViewList,viewPager);
        viewPager.setAdapter(myPageAdapter);


        // mImageView.setImageBitmap(mGoods.getImage());
         mGoodName.setText(String.format("名称：%s", mGoods.getGoodsName()));
         mUserName.setText(String.format("发布者：%s", mGoods.getEmail()));
         mPrice.setText(String.format("%s￥", String.valueOf(mGoods.getPrice())));
         mDescripton.setText(String.format("商品描述：%s", mGoods.getDescription()));
    }
}
