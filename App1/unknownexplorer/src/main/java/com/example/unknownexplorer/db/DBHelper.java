package com.example.unknownexplorer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 1);
//        context.deleteDatabase("myDB");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("createRouteDB", "--- onCreate database ---");
        // создаем таблицу с полями

        db.execSQL("create table users ("
                + "id integer primary key autoincrement,"
                + "login text,"
                + "password password" + ");");

        db.execSQL("create table routes ("
                + "id integer primary key autoincrement,"
                + "userId integer,"
                + "title text,"
                + "interest text,"
                + "type text,"
                + "description text" + ");");

        db.execSQL("create table points ("
                + "routeId integer,"
                + "id integer primary key autoincrement,"
//                + "title text,"
//                + "description text,"
                + "FOREIGN KEY(routeId) REFERENCES routes(id)"+");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
