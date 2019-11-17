package com.example.unknownexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {
    TextView inputLogin;
    TextView inputPassword;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLogin);
        TextView txRegistration = findViewById(R.id.txRegistration);
        inputLogin = findViewById(R.id.inputLogin);
        inputPassword = findViewById(R.id.inputPasswordLoginScreen);

        btnLogin.setOnClickListener(this);
        txRegistration.setOnClickListener(this);
        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent ;
        ContentValues cv = new ContentValues();
        String login = inputLogin.getText().toString();
        String password = inputPassword.getText().toString();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (v.getId()){
            case R.id.btnLogin:
                cv.put("login",login);
                cv.put("password",password);
                // вставляем запись и получаем ее ID
                long rowID = db.insert("users", null, cv);


                Log.d("test","HELLO from Login");
                intent = new Intent("ActivityMainNavigation");
                startActivity(intent);
                break;
            case R.id.txRegistration:
                intent = new Intent("ActivityRegistration");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("State", "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table users ("
                    + "id integer primary key autoincrement,"
                    + "login text,"
                    + "password password" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
