package com.example.droodsunny.taobao.fragments;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.droodsunny.taobao.R;
import com.example.droodsunny.taobao.Unit.MineAdapter;

import java.util.ArrayList;
import java.util.List;

public class MineFragment extends Fragment {

    private View view;
   private List<String> stringList;
   private MineAdapter mineAdapter;
   SharedPreferences sharedPreferences;
   private boolean ifLogin;
   private ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_mine,container,false);
        sharedPreferences=container.getContext().getSharedPreferences("usersInfo", Context.MODE_PRIVATE);
        ifLogin=sharedPreferences.getBoolean("ifLogin",false);
        listView=view.findViewById(R.id.listView);
        stringList=new ArrayList<>();
        stringList.add("我的发布");
        stringList.add("设置");
        mineAdapter=new MineAdapter(stringList,ifLogin);
        listView.setAdapter(mineAdapter);
        return view;
    }
}
