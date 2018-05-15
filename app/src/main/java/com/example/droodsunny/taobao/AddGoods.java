package com.example.droodsunny.taobao;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.droodsunny.taobao.Unit.Goods;
import com.example.droodsunny.taobao.Unit.PathGetter;

import org.angmarch.views.NiceSpinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class AddGoods extends AppCompatActivity {

    NiceSpinner mNiceSpinner;
    private EditText nameEdit;
    private EditText desEdit;
    private EditText priceEdit;
    private ImageView mImageView1;
    private ImageView mImageView2;
    private ImageView mImageView3;
   private String Email;
    private static final int IMAGE1=1;
    private static final int IMAGE2=2;
    private static final int IMAGE3=3;

    boolean addimage=false;
    ArrayList<String> item;

    private ArrayList<BmobFile> imagesArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);

        imagesArray=new ArrayList<>();


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


        mImageView1.setImageResource(R.drawable.addgoods);


        mImageView1.setOnClickListener(v ->{
              /*打开照片选择界面*/
              //请求权限
                 //选择照片
                 openAlbum(IMAGE1);
                 addimage=true;
                 Handler handler=new Handler();
                 handler.postDelayed(() -> mImageView2.setVisibility(View.VISIBLE),1000);
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
                 //将商品添加到Bmob
                 addtoBmob(Email,name,type,price,imagesArray,description);
                 finish();
             }
        });
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
                imagesArray.add(new BmobFile(new File(Objects.requireNonNull(PathGetter.getPath(AddGoods.this, data.getData())))));

                mImageView2.setImageResource(R.drawable.addgoods);
            }else {
                //如果添加失败
                mImageView1.setImageResource(R.drawable.addgoods);
                addimage=true;
            }
        }else if(requestCode==IMAGE2){
            if(resultCode==RESULT_OK){
                handleimage(mImageView2,data);
                imagesArray.add(new BmobFile(new File(Objects.requireNonNull(PathGetter.getPath(AddGoods.this, data.getData())))));

                mImageView3.setImageResource(R.drawable.addgoods);
            }
        }else if(requestCode==IMAGE3){
            if(resultCode==RESULT_OK){
                handleimage(mImageView3,data);
                imagesArray.add(new BmobFile(new File(Objects.requireNonNull(PathGetter.getPath(AddGoods.this, data.getData())))));

            }
        }
    }
    public void handleimage(ImageView imageView,Intent data){
        ContentResolver contentResolver=getContentResolver();
        Bitmap bitmap2 = null;
        try {
            //得到图像的宽高
           BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            BitmapFactory.decodeStream(contentResolver.openInputStream(Objects.requireNonNull(data.getData())),null,options);
            int imageWidth=options.outWidth;
            int imageHeight=options.outHeight;
            //将图像的显示设置为centerCrop型
            BitmapFactory.Options options1=new BitmapFactory.Options();
            bitmap2=BitmapFactory.decodeStream(contentResolver.openInputStream(Objects.requireNonNull(data.getData())),null,options1);
            imageView.setImageBitmap(bitmap2);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        }

   /* private WeakReference<Bitmap> getBitmapFromBlum(Uri data) throws FileNotFoundException {
        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(data),null,null);
        return new WeakReference<>(bitmap);
    }*/


    private boolean addtoBmob(String Email,String goodsName,String type,float price,ArrayList<BmobFile> images,String description){


        Goods goods=new Goods();
        ArrayList<String> imagesUrl=new ArrayList<>();
        goods.setEmail(Email);
        goods.setGoodsName(goodsName);
        goods.setCategory(type);
        goods.setPrice(price);
        goods.setDescription(description);

        Handler handler=new Handler(msg -> {
            //如果已经全部上传成功
            if(msg.what==1){
                goods.setImage(imagesUrl);
                goods.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e!=null){
                            Log.d("goodsException",e.toString());
                        }
                    }
                });
            }
            return true;
        });
        for(int i=0;i<images.size();i++) {
            BmobFile bmobFile=images.get(i);
            bmobFile.upload(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null) {
                        imagesUrl.add(bmobFile.getFileUrl());
                        //说明已经全部上传成功
                        if(images.size()==imagesUrl.size()){
                            handler.sendEmptyMessage(1);
                        }
                    }else {
                        Toast.makeText(AddGoods.this,"上传失败",Toast.LENGTH_SHORT).show();
                        Log.d("goodsException",e.toString());

                    }
                }
            });

        }

        return true;
    }




    public byte[] bitmapToBytes(Bitmap bitmap){
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        ByteArrayOutputStream baos= new ByteArrayOutputStream(size);
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }

   /* private String saveImage(String goodsName) throws FileNotFoundException {
        StringBuilder stringBuilder=new StringBuilder();
        Bitmap bitmap=null;
        File file=null;

        for(int j=0;j<imageUriList.size();j++){
            bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUriList.get(j)),null,null);
            file=new File(dirPath+"/"+goodsName+j+".png");

           if(j!=imageUriList.size()-1){
               stringBuilder.append(dirPath).append("/").append(goodsName).append(j).append(".png").append(",");
           }
           FileOutputStream fileOutputStream=new FileOutputStream(file);
            //将bitmap转成png格式并存入文件
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
        }

        return stringBuilder.toString();
    }
*/

    /*private void addGoods(SQLiteDatabase db,String Email,String name,String type,float price,String imagesPath,String description){
        ContentValues values = new ContentValues();
        values.put("Email",Email);
        values.put("name",name);
        values.put("type",type);
        values.put("price",price);
        values.put("image",imagesPath);
        values.put("description",description);

        db.insert(TABLE_NAME,null,values);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
