package com.example.droodsunny.taobao;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.droodsunny.taobao.Unit.Goods;
import com.example.droodsunny.taobao.Unit.MyRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyGoodsActivity extends AppCompatActivity {

    private SharedPreferences userInfo;
    private String Email;
    private List<Goods> goodsList;
    private RecyclerView recyclerView;
    private MyRecyclerAdapter myRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goods);
        userInfo=getSharedPreferences("usersInfo",MODE_PRIVATE);
        Email=userInfo.getString("Email",null);
        recyclerView=findViewById(R.id.my_recycler_view);

        GridLayoutManager layoutManager=new GridLayoutManager(MyGoodsActivity.this,2);
        recyclerView.setLayoutManager(layoutManager);
        goodsList=new ArrayList<>();
        BmobQuery<Goods> bmobQuery=new BmobQuery<>();
        bmobQuery.addWhereEqualTo("Email",Email);
        bmobQuery.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> list, BmobException e) {
                if(e==null){
                   goodsList=list;
                    Log.d("exception",goodsList.toString());
                    myRecyclerAdapter=new MyRecyclerAdapter(goodsList);
                    recyclerView.setAdapter(myRecyclerAdapter);
                }else {
                    Log.d("exception",e.toString());
                }
            }
        });



    }
}
