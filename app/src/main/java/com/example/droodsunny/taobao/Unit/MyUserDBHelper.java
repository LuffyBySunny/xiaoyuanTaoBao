package com.example.droodsunny.taobao.Unit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyUserDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME="Users.db";
    private static final String USER_TABLE="USER_INFO";
    private static final String CREATE_TABLE="CREATE TABLE IF NOT EXISTS "
            +USER_TABLE
            +"(Email TEXT PRIMARY KEY,"
            +"password TEXT,"
            +"ifreme BOOLEAN,"
            +"ifauto BOOLEAN"
            +")";
    private static MyUserDBHelper mMyUserDBHelper;
    private MyUserDBHelper(Context context, String name,  int version) {
        super(context, name, null, version);
    }

    public synchronized  static SQLiteDatabase getInstance(Context context){
        if(mMyUserDBHelper==null){
            mMyUserDBHelper=new MyUserDBHelper(context,DB_NAME,1);
            return mMyUserDBHelper.getReadableDatabase();
        }else {
            return mMyUserDBHelper.getReadableDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);
        db.execSQL("delete from "+USER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
