package com.example.droodsunny.taobao;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.droodsunny.taobao.Unit.SettingAdapter;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> stringList;
    private SettingAdapter settingAdapter;
    private SharedPreferences sharedPreferences;
    private boolean ifLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        stringList=new ArrayList<>();

        sharedPreferences=getSharedPreferences("usersInfo",MODE_PRIVATE);
        ifLogin=sharedPreferences.getBoolean("ifLogin",false);
        if(!ifLogin){
            stringList.add("登录");
        }else {
            stringList.add("退出登录");
        }
        settingAdapter=new SettingAdapter(stringList,sharedPreferences,ifLogin);
        listView=findViewById(R.id.settinglist);
        listView.setAdapter(settingAdapter);
    }
}
