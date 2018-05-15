package com.example.droodsunny.taobao.Unit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.droodsunny.taobao.DetailActivity;
import com.example.droodsunny.taobao.R;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Goods> mGoodsList;
    private Context mContext;
    public RecyclerAdapter(List<Goods> goodsList){
        mGoodsList=goodsList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      if(mContext==null){
          mContext=parent.getContext();
      }

      View view= LayoutInflater.from(mContext).inflate(R.layout.item_gride,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
       //设置数据

        Goods goods=mGoodsList.get(position);
        viewHolder.Email.setText(goods.getEmail());
        viewHolder.price.setText(String.valueOf(goods.getPrice()));
        viewHolder.name.setText(goods.getGoodsName());
        viewHolder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mContext).load(goods.getImage().get(0)).apply(RequestOptions.placeholderOf(R.drawable.loading)).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(10,0, RoundedCornersTransformation.CornerType.ALL))).into(viewHolder.image);
      //   goodsImage(goods,viewHolder.image,mContext);
       // viewHolder.image.setImageBitmap(goods.getImage());
        viewHolder.mCardView.setOnClickListener(v -> {
            Intent intent=new Intent(v.getContext(), DetailActivity.class);
            Bundle bundle=new Bundle();
            bundle.putParcelable("good",goods);
            intent.putExtra("good",bundle);
            v.getContext().startActivity(intent);

        });
        //Glide.with(mContext).load(goods.getImage()).into(viewHolder.image);
    }

    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView Email;
        private TextView name;
        private ImageView image;
        private TextView price;

        private CardView mCardView;

        private ViewHolder(View itemView) {
            super(itemView);
            //实例化控件
            Email=itemView.findViewById(R.id.item_Email);
            name=itemView.findViewById(R.id.item_name);
            price=itemView.findViewById(R.id.item_price);
            image=itemView.findViewById(R.id.item_image);
            mCardView=itemView.findViewById(R.id.cardView);
        }
    }
}
