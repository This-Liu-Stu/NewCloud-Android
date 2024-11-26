package com.stefan.airconditioningmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBOpenHelper extends SQLiteOpenHelper {
    final String CREATE_TABLE_SQL =
            "create table record (_id integer primary key autoincrement,temperature VARCHAR2(100),time VARCHAR2(100))";


    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version); //重写构造方法并设置工厂为null
    }

    //创建单词信息表
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("---版本更新-----" + oldVersion + "--->" + newVersion);
    }
}