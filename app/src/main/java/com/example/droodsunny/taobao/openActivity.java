package com.example.droodsunny.taobao;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class openActivity extends AppCompatActivity {
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open);

        handler=new Handler();
        /*保持连接状态*/
        /*在这申请权限*/
        if(ContextCompat.checkSelfPermission(openActivity.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(openActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(openActivity.this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(openActivity.this,new String[]{Manifest.permission.INTERNET,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE},1);
        }else {
            //如果获得了权限
            handler.postDelayed(() -> {
                Intent intent=new Intent(openActivity.this,Main2Activity.class);
                startActivity(intent);
                finish();
            },1000);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
     switch (requestCode){
         case 1:
             boolean cancel=false;
             if(grantResults.length>0){
                 for (int i=0;i<grantResults.length;i++) {

                     if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                         cancel=true;
                         //如果设置了不再询问
                        boolean showRequestPermission=ActivityCompat.shouldShowRequestPermissionRationale(openActivity.this,permissions[i]);
                        if(showRequestPermission){
                            Toast.makeText(openActivity.this,"你拒绝了"+permissions[i]+"请手动开启",Toast.LENGTH_SHORT).show();
                        }
                     }

                 }
                 //请求到了权限
                 if (!cancel){
                     handler.postDelayed(() -> {
                         Intent intent=new Intent(openActivity.this,Main2Activity.class);
                         startActivity(intent);
                         finish();
                     },1000);
                 }
             }
     }
    }
}
