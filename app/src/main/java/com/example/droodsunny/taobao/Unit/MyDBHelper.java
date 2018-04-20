package com.example.droodsunny.taobao.Unit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String myDBname="Goods.db";
   private static MyDBHelper mMyDBHelper;
    private static final String TABLE_NAME="GoodsInfo";
    private static final String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+
            "(_id INTEGER PRIMARY KEY ,"+
            "Email TEXT,"+
            "name TEXT,"+
            "type TEXT,"+
            "price REAL,"+
            "image BLOB,"+
            "description TEXT)";
    private MyDBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }
    public synchronized static SQLiteDatabase getInstance(Context context){
        if(mMyDBHelper==null){
            mMyDBHelper=new MyDBHelper(context,myDBname,1);
            return mMyDBHelper.getReadableDatabase();
        }else {

            return mMyDBHelper.getReadableDatabase();
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
             //每次创建都删除全部数据
         //   db.execSQL("delete from "+TABLE_NAME);
            db.execSQL(CREATE_TABLE);
        db.execSQL("delete from "+TABLE_NAME);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.needUpgrade(newVersion);
    }
}
