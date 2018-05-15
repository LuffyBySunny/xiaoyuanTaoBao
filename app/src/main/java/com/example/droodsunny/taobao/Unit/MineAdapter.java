package com.example.droodsunny.taobao.Unit;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.droodsunny.taobao.LoginActivity;
import com.example.droodsunny.taobao.MyGoodsActivity;
import com.example.droodsunny.taobao.R;
import com.example.droodsunny.taobao.SettingActivity;

import java.util.List;

public class MineAdapter extends BaseAdapter{

   private List<String> list;
   private boolean ifLogin;
    public MineAdapter( List<String> objects,boolean ifLogin) {
        this.ifLogin=ifLogin;
        this.list=objects;
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Nullable
    @Override
    public String getItem(int position) {
        return list.get(position);
}

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        ViewHolder viewHolder;
       if(convertView==null){
            viewHolder=new ViewHolder();
           convertView=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine,parent,false);
           viewHolder.textView=convertView.findViewById(R.id.text);
           convertView.setTag(viewHolder);
       }else {
           viewHolder= (ViewHolder) convertView.getTag();
       }
       String string=list.get(position);
       viewHolder.textView.setText(string);
       convertView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               switch (position){
                   //如果点击了我的发布
                   case 0:
                       if(!ifLogin){
                           Snackbar.make(v,"请先登录",Snackbar.LENGTH_SHORT).setAction("登录", new View.OnClickListener() {
                               @Override
                               public void onClick(View v) {
                                   ifLogin=true;
                                   Intent intent=new Intent(v.getContext(),LoginActivity.class);
                                   v.getContext().startActivity(intent);
                               }
                           }).show();
                       }else {
                           Intent intent=new Intent(v.getContext(), MyGoodsActivity.class);
                           v.getContext().startActivity(intent);
                       }
                       break;
                       //如果点击了设置
                   case 1:
                       Intent intent1=new Intent(v.getContext(), SettingActivity.class);
                       v.getContext().startActivity(intent1);
                       break;
               }
           }
       });
       return convertView;
    }
    private static class ViewHolder{
        TextView textView;
    }
}
