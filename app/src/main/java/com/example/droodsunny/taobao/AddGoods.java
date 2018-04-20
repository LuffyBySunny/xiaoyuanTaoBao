package com.example.droodsunny.taobao;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.droodsunny.taobao.Unit.MyDBHelper;

import org.angmarch.views.NiceSpinner;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;

public class AddGoods extends AppCompatActivity {

    NiceSpinner mNiceSpinner;
    private EditText nameEdit;
    private EditText desEdit;
    private EditText priceEdit;
    private ImageView mImageView1;
    private ImageView mImageView2;
    private ImageView mImageView3;
    private SQLiteDatabase db;
    private static final String TABLE_NAME="GoodsInfo";
   private String Email;
    private static final int IMAGE1=1;
    private static final int IMAGE2=2;
    private static final int IMAGE3=3;

    boolean addimage=false;
    ArrayList<String> item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);

        db=MyDBHelper.getInstance(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mNiceSpinner = findViewById(R.id.nice_spinner);

        nameEdit=findViewById(R.id.name);
        desEdit=findViewById(R.id.description);
        priceEdit=findViewById(R.id.price);
        mImageView1=findViewById(R.id.image1);
        mImageView2=findViewById(R.id.image2);
        mImageView3=findViewById(R.id.image3);
        mImageView2.setVisibility(View.INVISIBLE);
        mImageView3.setVisibility(View.INVISIBLE);

        Intent intent=getIntent();
        Email=intent.getStringExtra("username");

       item =new ArrayList<>();
        item.add("服饰");
        item.add("文具");
        item.add("食品");
        item.add("生活用品");
        item.add("电子产品");
        mNiceSpinner.attachDataSource(item);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mImageView1.setImageDrawable(getDrawable(R.drawable.addgoods));
            mImageView2.setImageDrawable(getDrawable(R.drawable.addgoods));
            mImageView3.setImageDrawable(getDrawable(R.drawable.addgoods));
        }else {
            mImageView1.setImageResource(R.drawable.addgoods);
            mImageView2.setImageResource(R.drawable.addgoods);
            mImageView3.setImageResource(R.drawable.addgoods);
        }
        mImageView1.setOnClickListener(v ->{
              /*打开照片选择界面*/
              //请求权限
             if(ContextCompat.checkSelfPermission(AddGoods.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                 ActivityCompat.requestPermissions(AddGoods.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
             }else {
                 //选择照片
                 openAlbum(IMAGE1);
                 addimage=true;
                 Handler handler=new Handler();
                 handler.postDelayed(() -> mImageView2.setVisibility(View.VISIBLE),1000);
             }
                } );
        mImageView2.setOnClickListener(v-> {
            openAlbum(IMAGE2);
                    Handler handler=new Handler();
                    handler.postDelayed(() -> mImageView3.setVisibility(View.VISIBLE),1000);

        }
        );
        mImageView3.setOnClickListener(v-> openAlbum(IMAGE3));

        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setTitle(" ");
            actionBar.setDisplayHomeAsUpEnabled(true);
            //设置按钮
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->{
            //用户
            View focusview=null;
            boolean cancle=false;
            nameEdit.setError(null);
            desEdit.setError(null);
            priceEdit.setError(null);
            String name= nameEdit.getText().toString();
            int index=mNiceSpinner.getSelectedIndex();
            //类型
            String type=item.get(index);
            //描述
            String description=desEdit.getText().toString();
            //价格

            //对image进行转码
            if(TextUtils.isEmpty(name)){
                nameEdit.setError("请输入名称");
                cancle=true;
                nameEdit.requestFocus();
            }else if(TextUtils.isEmpty(description)){
                desEdit.setError("请输入描述");
                cancle=true;
                desEdit.requestFocus();
            }else if(TextUtils.isEmpty(priceEdit.getText().toString())){
                priceEdit.setError("请输入价格");
                cancle=true;
                priceEdit.requestFocus();
             }else if(!addimage){
                Toast.makeText(AddGoods.this,"请上传图片",Toast.LENGTH_SHORT).show();
                cancle=true;
             }
             if(!cancle) {
                 float price=Float.parseFloat(priceEdit.getText().toString());
                 mImageView1.setDrawingCacheEnabled(true);
                 Bitmap bitmap = mImageView1.getDrawingCache();
                 //图片

                 byte[] bytes = bitmapToBytes(bitmap);
                 bitmap.recycle();
                 addGoods(db, Email, name, type, price, bytes, description);
                 Toast.makeText(AddGoods.this,"添加成功",Toast.LENGTH_SHORT).show();
                 finish();
             }
        });
    }

    private void addGoods(SQLiteDatabase db,String Email,String name,String type,float price,byte[] imageByte,String description){
        ContentValues values = new ContentValues();
        values.put("Email",Email);
        values.put("name",name);
        values.put("type",type);
        values.put("price",price);
        values.put("image",imageByte);
        values.put("description",description);
        db.insert(TABLE_NAME,null,values);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            for(int i:grantResults){
                if(i==PackageManager.PERMISSION_GRANTED){
                    //打开相册
                   openAlbum(IMAGE1);
                    addimage=true;
                }else {
                    Toast.makeText(AddGoods.this,"用户拒绝打开相册",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //打开相册

    public void openAlbum(int requestCode){
        //打开相册
        Intent intent =new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==IMAGE1){
            if(resultCode==RESULT_OK){
                handleimage(mImageView1,data);
            }else {
                //如果添加失败
                mImageView1.setImageResource(R.drawable.addgoods);
                addimage=true;
            }
        }else if(requestCode==IMAGE2){
            if(resultCode==RESULT_OK){
                handleimage(mImageView2,data);
            }else {
                mImageView2.setImageResource(R.drawable.addgoods);
            }
        }else if(requestCode==IMAGE3){
            if(resultCode==RESULT_OK){
                handleimage(mImageView3,data);
            }else {
                mImageView3.setImageResource(R.drawable.addgoods);
            }
        }
    }

    public void handleimage(ImageView imageView,Intent data){
        ContentResolver contentResolver=getContentResolver();
        Bitmap bitmap2 = null;
        try {
           //找到正确的缩放
            BitmapFactory.Options options1=new BitmapFactory.Options();
            options1.inSampleSize=8;
            bitmap2=BitmapFactory.decodeStream(contentResolver.openInputStream(Objects.requireNonNull(data.getData())),null,options1);
            imageView.setImageBitmap(bitmap2);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        }

    public byte[] bitmapToBytes(Bitmap bitmap){
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        ByteArrayOutputStream baos= new ByteArrayOutputStream(size);
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }
}
