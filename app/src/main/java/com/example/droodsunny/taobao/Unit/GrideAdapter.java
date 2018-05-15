package com.example.droodsunny.taobao.Unit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.droodsunny.taobao.R;

import java.util.List;

public class GrideAdapter extends BaseAdapter {
   private List<Goods> mGoodsList;

    public GrideAdapter( List<Goods> goodsList){
        mGoodsList=goodsList;
    }
    @Override
    public int getCount() {
        return mGoodsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mGoodsList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Goods goods=mGoodsList.get(position);
        if(convertView==null){
            //加载布局
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gride,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.Email=convertView.findViewById(R.id.item_Email);
            viewHolder.name=convertView.findViewById(R.id.item_name);
            viewHolder.price=convertView.findViewById(R.id.item_price);
            viewHolder.image=convertView.findViewById(R.id.item_image);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.Email.setText(goods.getEmail());
        viewHolder.name.setText(goods.getGoodsName());
        viewHolder.price.setText(String.format("%s", goods.getPrice()));
       // viewHolder.image.setImageBitmap(goods.getImage());

        return convertView;
    }


    private class ViewHolder{
        public TextView getEmail() {
            return Email;
        }
        public void setEmail(TextView email) {
            Email = email;
        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public ImageView getImage() {
            return image;
        }

        public void setImage(ImageView image) {
            this.image = image;
        }

        public TextView getPrice() {
            return price;
        }

        public void setPrice(TextView price) {
            this.price = price;
        }

        private TextView Email;
        private TextView name;
        private ImageView image;
        private TextView price;
    }
}
