package com.example.droodsunny.taobao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.droodsunny.taobao.Unit.Goods;
import com.example.droodsunny.taobao.fragments.HomeFragment;
import com.example.droodsunny.taobao.fragments.MineFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    private int selected=-1;

    private Button mHomeButton;
    private Button mMineButton;
   private Fragment currentFragment;

    private FloatingActionButton mAddButton;
    private FrameLayout mFrameLayout;
    private SharedPreferences userInfo;
    boolean ifLogin=false;
    private String Email;
    private List<Goods> goodsList;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Bmob.initialize(this, "1ac735ea998d3f3e71b3c082f96eb4fd");

        goodsList=new ArrayList<>();

        userInfo=getSharedPreferences("usersInfo",MODE_PRIVATE);
        ifLogin=userInfo.getBoolean("ifLogin",false);
        Email=userInfo.getString("Email",null);

        mHomeButton=findViewById(R.id.home);
        mHomeButton.setBackgroundColor(0);

        mMineButton=findViewById(R.id.my);
        mMineButton.setBackgroundColor(0);
        mAddButton=findViewById(R.id.add);
        mFrameLayout=findViewById(R.id.fragment);
        mHomeButton.setOnClickListener(this);
        mMineButton.setOnClickListener(this);
        mAddButton.setOnClickListener(this);
        textView=findViewById(R.id.toolbarText);

        currentFragment=getSupportFragmentManager().findFragmentById(R.id.fragment);

       //一次性加载完毕，太不好了
        BmobQuery<Goods> bmobQuery=new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<Goods>() {
            @Override
            public void done(List<Goods> list, BmobException e) {
                if(e==null){
                    if(list!=null) {
                        goodsList = list;
                        HomeFragment homeFragment = HomeFragment.newInstance((ArrayList<Goods>) goodsList);
                        replaceFragment(homeFragment);
                        textView.setText("校园淘宝");
                        selected = R.id.home;
                    }
                }else {
                    Log.d("bmobfile",e.toString());
                    Toast.makeText(Main2Activity.this,"没有数据",Toast.LENGTH_SHORT).show();
                }
            }
        });

     /*MineFragment mineFragment=new MineFragment();
     replaceFragment(mineFragment);*/
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home:
                if(selected!=R.id.home) {
                    HomeFragment homeFragment = HomeFragment.newInstance((ArrayList<Goods>) goodsList);
                    selected=R.id.home;
                    textView.setText("校园淘宝");
                    replaceFragment(homeFragment);
                }
                break;
            case R.id.my:
                if(selected!=R.id.my){
                    MineFragment mineFragment=new MineFragment();
                    selected=R.id.my;
                    textView.setText("我的");
                    replaceFragment(mineFragment);
                }
                break;
            case R.id.add:
                if(!ifLogin){
                    //snackBar
                    Snackbar.make(v,"请先登录",Snackbar.LENGTH_SHORT).setAction("登录", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ifLogin=true;
                            Intent intent=new Intent(Main2Activity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                    }).show();

                }else {
                    Intent intent = new Intent(Main2Activity.this, AddGoods.class);
                    intent.putExtra("username",Email);
                    startActivity(intent);
                }
                default:
                    break;
        }
    }
    private void replaceFragment(android.support.v4.app.Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
       FragmentTransaction   fragmentTransaction=fragmentManager.beginTransaction();
       fragmentTransaction.replace(R.id.fragment,fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        userInfo=getSharedPreferences("usersInfo",MODE_PRIVATE);
        ifLogin=userInfo.getBoolean("ifLogin",false);
        Email=userInfo.getString("Email",null);

    }
}
