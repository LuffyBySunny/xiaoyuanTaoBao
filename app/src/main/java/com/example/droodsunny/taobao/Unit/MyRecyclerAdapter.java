package com.example.droodsunny.taobao.Unit;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.droodsunny.taobao.R;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    private List<Goods> list;
    private Context mContext;
    public MyRecyclerAdapter(List<Goods> list){
        this.list=list;
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

        Goods goods=list.get(position);
        viewHolder.Email.setText(goods.getEmail());
        viewHolder.price.setText(String.valueOf(goods.getPrice()));
        viewHolder.name.setText(goods.getGoodsName());
        viewHolder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mContext).load(goods.getImage().get(0)).apply(RequestOptions.placeholderOf(R.drawable.loading)).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(10,0, RoundedCornersTransformation.CornerType.ALL))).into(viewHolder.image);
        //   goodsImage(goods,viewHolder.image,mContext);
        // viewHolder.image.setImageBitmap(goods.getImage());
       viewHolder.mCardView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
               View view=View.inflate(v.getContext(),R.layout.alertdialogview,null);
               builder.setView(view);
               builder.setCancelable(true);
               TextView edit=view.findViewById(R.id.edit);
               TextView delete=view.findViewById(R.id.delete);
               AlertDialog alertDialog=builder.create();
               alertDialog.show();
               edit.setOnClickListener(v12 -> alertDialog.hide());
               delete.setOnClickListener(v1 -> {
                   alertDialog.hide();
                   Goods goods1=list.get(position);
                   list.remove(position);
                   notifyDataSetChanged();
                   goods1.setObjectId(goods1.getObjectId());
                   goods1.delete(new UpdateListener() {
                       @Override
                       public void done(BmobException e) {
                           if(e==null){
                               //删除图片
                               BmobFile bmobFile=new BmobFile();
                               for(String url:goods1.getImage()){
                                   bmobFile.setUrl(url);
                                   bmobFile.delete(new UpdateListener() {
                                       @Override
                                       public void done(BmobException e) {
                                           if(e!=null){
                                               Toast.makeText(mContext,"删除失败",Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   });
                               }
                           }
                       }
                   });

               });
               return true;
           }
       });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView Email;
        private TextView name;
        private ImageView image;
        private TextView price;

        private CardView mCardView;
        public ViewHolder(View itemView) {
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
