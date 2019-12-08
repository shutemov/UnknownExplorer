package com.example.unknownexplorer.activitys;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unknownexplorer.R;
import com.example.unknownexplorer.db.DBHelper;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    // !!!! зарефакторить объявление элементов активи и их id.

    TextView inputLogin;
    TextView inputPassword;
    TextView txRegistration;
    Button btnLogin;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        txRegistration = findViewById(R.id.txRegistration);
        inputLogin = findViewById(R.id.inputLogin);
        inputPassword = findViewById(R.id.inputPasswordLoginScreen);

        inputLogin.setText("j");
        inputPassword.setText("1");

        btnLogin.setOnClickListener(this);
        txRegistration.setOnClickListener(this);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        ContentValues contentValues = new ContentValues();


        switch (v.getId()) {
            case R.id.btnLogin:
                String login = inputLogin.getText().toString();
                String password = inputPassword.getText().toString();
                Log.d("login", "onClick: " + login + " " + password);
                // данные для запроса
                String selection = "login = ? and password = ?";
                String[] selectionArgs = new String[]{login, password};

                // подключаемся к БД
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //запрос на валидацию уникальности пользователя.
                Cursor data = db.query("users", null, selection, selectionArgs, null, null, null);
                Log.d("login", "onClick: " + data.getCount() + " " + data.moveToFirst());


                // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ DELETE THIS $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

                Cursor allUsers = db.query("users", null, null, null, null, null, null);
                Log.d("login", "onClick: " + data.getCount() + " " + data.moveToFirst());
                if (allUsers.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    int idColIndex = allUsers.getColumnIndex("login");
                    int idColIndex1 = allUsers.getColumnIndex("id");
                    int titleColIndex = allUsers.getColumnIndex("password");
//                    int emailColIndex = c.getColumnIndex("email");

                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d("all users in system",
                                "login = " + allUsers.getString(idColIndex) +
                                        ", password = " + allUsers.getString(titleColIndex)+
                                        ", ID = " + allUsers.getString(idColIndex1)
                        );
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (allUsers.moveToNext());
                } else
                    Log.d("all users in system", "0 rows");
                allUsers.close();
// $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ DELETE THIS $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

                if (data.moveToFirst() && (data.getCount() == 1)) {
                    intent = new Intent("ActivityMainNavigation");

                    int idCol = data.getColumnIndex("id");
                    int loginColIndex = data.getColumnIndex("login");
                    Log.d("TEST", "onClick: "+ idCol +" "+data.getInt(idCol)+" "+data.getString(idCol));
                    intent.putExtra("userId", data.getInt(idCol));
                    intent.putExtra("userLogin",data.getString(loginColIndex));
                    startActivity(intent);
                }
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

}
