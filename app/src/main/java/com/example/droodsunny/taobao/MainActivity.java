package com.example.droodsunny.taobao;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.GridView;


public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    SharedPreferences mSharedPreferences;
    private Button quitButton;

    private GridView mGridView;

    FloatingActionButton addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView=findViewById(R.id.gridView);

        //准备数据源

        quitButton=findViewById(R.id.quit);
        addButton=findViewById(R.id.add);
        addButton.setOnClickListener(v -> {
            //打开添加商品页面
           Intent intent=new Intent(MainActivity.this,AddGoods.class);
           startActivity(intent);
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

        quitButton.setOnClickListener(v->{
            SharedPreferences.Editor editor=mSharedPreferences.edit();
            editor.clear();
            editor.apply();
            SharedPreferences.Editor editor1=ifRemember.edit();
            editor1.clear();
            editor1.apply();
            SharedPreferences.Editor editor2=ifAutoLogin.edit();
            editor2.clear();
            editor2.apply();

            Intent intent=new Intent(MainActivity.this,LoginActivity.class);

            startActivity(intent);

            finish();
        });



      /*  mToolbar.setOnMenuItemClickListener(item -> {
           switch (item.getItemId()){
               case R.id.add :

                   break;
               case R.id.quit:
                   //清除保存的登录数据


                   break;

           }
           return false;
        });
*/









    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {

       getMenuInflater().inflate(R.menu.main_menu,menu);
       //给menu加图标
       if(menu!=null){
           if (menu.getClass() == MenuBuilder.class) {
               try {
                   @SuppressLint("PrivateApi") Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                   m.setAccessible(true);
                   m.invoke(menu, true);
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       }
       return true;
    }*/
}
