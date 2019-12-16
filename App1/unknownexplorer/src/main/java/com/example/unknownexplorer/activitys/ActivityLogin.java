package com.example.unknownexplorer.activitys;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unknownexplorer.POJO.User;
import com.example.unknownexplorer.R;
import com.example.unknownexplorer.api.NetworkService;
import com.example.unknownexplorer.db.DBHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

//        inputLogin.setText("j");
//        inputPassword.setText("1");

        btnLogin.setOnClickListener(this);
        txRegistration.setOnClickListener(this);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        ContentValues contentValues = new ContentValues();


        switch (v.getId()) {
//            case R.id.btnLogin:
//                String login = inputLogin.getText().toString();
//                String password = inputPassword.getText().toString();
//                Log.d("login", "onClick: " + login + " " + password);
//                // данные для запроса
//                String selection = "login = ? and password = ?";
//                String[] selectionArgs = new String[]{login, password};
//
//                // подключаемся к БД
//                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                //запрос на валидацию уникальности пользователя.
//                Cursor data = db.query("users", null, selection, selectionArgs, null, null, null);
//                Log.d("login", "onClick: " + data.getCount() + " " + data.moveToFirst());
//
//
//                if (data.moveToFirst() && (data.getCount() == 1)) {
//                    intent = new Intent("ActivityMainNavigation");
//
//                    int idCol = data.getColumnIndex("id");
//                    int loginColIndex = data.getColumnIndex("login");
//                    Log.d("TEST", "onClick: " + idCol + " " + data.getInt(idCol) + " " + data.getString(idCol));
//                    intent.putExtra("userId", data.getInt(idCol));
//                    intent.putExtra("userLogin", data.getString(loginColIndex));
//                    startActivity(intent);
//                } else {
//                    Toast toast = Toast.makeText(getApplicationContext(),
//                            "Логин или пароль не совпадают.",
//                            Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.BOTTOM, 0, 10);
//                    toast.show();
//                }
//                break;

            case R.id.btnLogin:
                User user = new User();

                user.setLogin(inputLogin.getText().toString());
                user.setPassword(inputPassword.getText().toString());

                NetworkService.getInstance()
                        .getJSONApi()
                        .getUserExist(user)
                        .enqueue(new Callback<User>() {

                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                User user = response.body();
                                if (user.getId() == 0) {
                                    Log.d("user exist", "onResponse: this user not exist");
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Логин или пароль не совпадают.",
                                            Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.BOTTOM, 0, 10);
                                    toast.show();
                                } else {
                                    Log.d("ELSE", "onResponse: "+user.getId()+" "+user.getLogin());
                                    Intent intent = new Intent("ActivityMainNavigation");
                                    intent.putExtra("userLogin", user.getLogin());
                                    intent.putExtra("userId", user.getId());
                                    startActivity(intent);
                                    Log.d("test-api", "onResponse: " + user.getId() + " " + user.getLogin());
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Log.d("test-api", "onFailure: " + t.getMessage());
                            }
                        });
                break;
            case R.id.txRegistration:
                intent = new Intent("ActivityRegistration");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {

        User user = new User();
        user.setLogin("login 12");
        user.setPassword("321");

        NetworkService.getInstance()
                .getJSONApi()
                .getUserExist(user)
                .enqueue(new Callback<User>() {

                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        User user = response.body();
                        if (user.getId() == 0) {
                            Log.d("user exist", "onResponse: this user not exist");
                        } else {
                            Log.d("test-api", "onResponse: " + user.getId() + " " + user.getLogin());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("test-api", "onFailure: " + t.getMessage());
                    }
                });
    }
}
