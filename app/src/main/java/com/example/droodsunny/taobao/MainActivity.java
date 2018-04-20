package com.example.droodsunny.taobao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.droodsunny.taobao.Unit.Goods;
import com.example.droodsunny.taobao.Unit.GrideAdapter;
import com.example.droodsunny.taobao.Unit.MyDBHelper;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    SharedPreferences mSharedPreferences;

    SQLiteDatabase db;

    private GridView mGridView;
    String mEmail;
    private   GrideAdapter grideAdapter;
    private List<Goods> goodsList;

    private NavigationView mNavigationView;


    FloatingActionButton addButton;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridView=findViewById(R.id.gridView);

        mSwipeRefreshLayout =findViewById(R.id.swiperefresh);

        mNavigationView=findViewById(R.id.nav_view);

        View headView=mNavigationView.getHeaderView(0);
        TextView textView=headView.findViewById(R.id.username);
        //得到用户名
         Intent intent=getIntent();
         mEmail=intent.getStringExtra("username");
         textView.setText(mEmail);


         db= MyDBHelper.getInstance(getApplicationContext());

         DisplayMetrics dm=new DisplayMetrics();
         getWindowManager().getDefaultDisplay().getMetrics(dm);
         ViewGroup.LayoutParams p=mGridView.getLayoutParams();
         p.width=dm.widthPixels;
         p.height=dm.heightPixels;
         mGridView.setLayoutParams(p);
        //准备数据源
         goodsList=new ArrayList<>();

        addButton=findViewById(R.id.add);
        addButton.setOnClickListener(v -> {
            //打开添加商品页
           Intent intent1=new Intent(MainActivity.this,AddGoods.class);
           intent1.putExtra("username",mEmail);
           startActivity(intent1);
        });
        mSharedPreferences=getSharedPreferences("messageUser", MODE_PRIVATE);
        SharedPreferences ifRemember = getSharedPreferences("rememberPassword", MODE_PRIVATE);
        SharedPreferences ifAutoLogin = getSharedPreferences("autoLogin", MODE_PRIVATE);
        mToolbar=findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(" ");
        }

        mNavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.my:
                    break;
                case R.id.quit:
                    //清除全部缓存
                    SharedPreferences.Editor editor1=ifRemember.edit();
                    editor1.clear();
                    editor1.apply();
                    SharedPreferences.Editor editor2=ifAutoLogin.edit();
                    editor2.clear();
                    editor2.apply();
                    SharedPreferences.Editor editor=mSharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    finish();
                    break;
                case R.id.add:
                    Intent intent1=new Intent(MainActivity.this,AddGoods.class);
                    intent1.putExtra("username",mEmail);
                    startActivity(intent1);
                    break;
                    default:
                        return false;
            }
            return true;
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
                    //回调方法
            goodsList.clear();
            query(db, new SQLCallback() {
                @Override
                public void onFinish() {
                   runOnUiThread(()->{
                       grideAdapter.notifyDataSetChanged();
                       mSwipeRefreshLayout.setRefreshing(false);
                   });
                }
                @Override
                public void onFailure() {
                     runOnUiThread(() -> {
                         Toast.makeText(MainActivity.this,"刷新失败",Toast.LENGTH_SHORT).show();
                         mSwipeRefreshLayout.setRefreshing(false);
                     });
                }
            },goodsList);
                });
        /*退出登录*/


    }
    @Override
    public void onBackPressed() {
        LoginActivity.sLoginActivity.finish();
        super.onBackPressed();
    }
    private interface SQLCallback{
       //接口中的变量都是public static final
         void onFinish();
         void onFailure();
    }
    private void query(SQLiteDatabase sqLiteDatabase, SQLCallback sqlCallback,List<Goods> goodsList){
        //开启子线程进行查询
        new Thread(() -> {
            Cursor cursor=null;
            try {
                cursor=sqLiteDatabase.query("GoodsInfo",null,null,null,null,null,null);
                while (cursor.moveToNext()){
                    Goods goods=new Goods();
                    String Email=cursor.getString(1);
                    String name=cursor.getString(2);
                    String type=cursor.getString(3);
                    float price=cursor.getFloat(4);
                    byte[] image=cursor.getBlob(5);
                    String description=cursor.getString(6);
                    Bitmap bitmap= BitmapFactory.decodeByteArray(image,0,image.length);
                    goods.setEmail(Email);
                    goods.setGoodsName(name);
                    goods.setCategory(type);
                    goods.setPrice(price);
                    goods.setImage(bitmap);
                    goods.setDescription(description);
                    goodsList.add(goods);
                    sqlCallback.onFinish();
                }
            }catch (Exception e){
                e.printStackTrace();
                sqlCallback.onFailure();
            }finally {
                if(cursor!=null){
                    cursor.close();
                }
            }
        }).start();
    }
    @Override
    protected void onResume() {
        goodsList.clear();
        //回调方法
        Handler handler=new Handler(msg -> {
            switch (msg.what){
                //查询成功，加载数据
                case 1:
                    grideAdapter=new GrideAdapter(goodsList);
                    mGridView.setAdapter(grideAdapter);
            }
            return true;
        });
        //回调方法
        query(db, new SQLCallback() {
            @Override
            public void onFinish() {
                Message message=new Message();
                message.what=1;
                handler.sendMessage(message);
            }
            @Override
            public void onFailure() {
                runOnUiThread(() -> Toast.makeText(MainActivity.this,"加载失败,请刷新",Toast.LENGTH_SHORT).show());
            }
        },goodsList);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
