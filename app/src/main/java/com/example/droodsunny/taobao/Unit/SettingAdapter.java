package com.example.droodsunny.taobao.Unit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.droodsunny.taobao.LoginActivity;
import com.example.droodsunny.taobao.Main2Activity;
import com.example.droodsunny.taobao.R;

import java.util.List;

public class SettingAdapter extends BaseAdapter {
    private List<String> list;
    SharedPreferences sharedPreferences;
    boolean ifLogin;
    public SettingAdapter(List<String> objects,SharedPreferences sharedPreferences,boolean ifLogin) {
        this.sharedPreferences=sharedPreferences;
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
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine,parent,false);
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
                    case 0:
                        //退出登陆
                        if(ifLogin){
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            Intent intent=new Intent(v.getContext(), Main2Activity.class);
                            v.getContext().startActivity(intent);

                        }else {
                            Intent intent=new Intent(v.getContext(), LoginActivity.class);
                            v.getContext().startActivity(intent);
                        }


                }
            }
        });
        return convertView;
    }
    private static class ViewHolder{
        TextView textView;
    }
}
