package com.example.unknownexplorer.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unknownexplorer.POJO.User;
import com.example.unknownexplorer.R;
import com.example.unknownexplorer.api.NetworkService;
import com.example.unknownexplorer.db.DBHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityRegistration extends AppCompatActivity implements View.OnClickListener {


    // TODO зарефакторить объявление элементов активити и их id.
    Button btnRegistration;
    EditText inputLogin;
    EditText inputPassword;
    EditText inputRepeatPassword;

    // бд
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnRegistration = findViewById(R.id.btnRegistration);
        inputLogin = findViewById(R.id.inputLoginRegistration);
        inputPassword = findViewById(R.id.inputPasswordRegistration);
        inputRepeatPassword = findViewById(R.id.inputRepeatPasswordRegistration);

        btnRegistration.setOnClickListener(this);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnRegistration:
//                //данные с экрана
//                String login = inputLogin.getText().toString();
//                String password = inputPassword.getText().toString();
//                String repeatPassword = inputRepeatPassword.getText().toString();
//
//
//                // подключаемся к БД
//                SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//                // данные для запроса
//                String selection = "login = ?";
//                String[] selectionArgs = new String[]{login};
//
//                //запрос на валидацию уникальности пользователя.
//                Cursor data = db.query("users", null, selection, selectionArgs, null, null, null);
//
//                //логируем запрос
//                Log.d("registration", "the user exist? "+data.moveToFirst());
//
//                //если пароли равны и такого пользователя нет
//                if ((password.equals(repeatPassword)) && !data.moveToFirst()) {
//
//                    // создаем объект данных пользователя
//                    ContentValues userContent = new ContentValues();
//                    userContent.put("login", login);
//                    userContent.put("password", password);
//
//                    //регистрируем пользователя
//                    db.insert("users", null, userContent);
//                    intent = new Intent(this, ActivityLogin.class);
//                    startActivity(intent);
//                }else {
//                    Toast toast = Toast.makeText(getApplicationContext(),
//                            "Пароли не совпадают или такой пользовател уже существует.",
//                            Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.BOTTOM, 0, 10);
//                    toast.show();
//                }

                //данные с экрана
                String login = inputLogin.getText().toString();
                String password = inputPassword.getText().toString();
                String repeatPassword = inputRepeatPassword.getText().toString();

                if (password.equals(repeatPassword)) {

                    final User PojoUser = new User();
                    PojoUser.setLogin(inputLogin.getText().toString());
                    PojoUser.setPassword(inputPassword.getText().toString());

                    NetworkService.getInstance()
                            .getJSONApi()
                            .getUserExist(PojoUser)
                            .enqueue(new Callback<User>() {

                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    User user = response.body();
                                    if (user.getId() == 0) {

                                        NetworkService.getInstance()
                                                .getJSONApi()
                                                .createNewUser(PojoUser)
                                                .enqueue(new Callback<User>() {
                                                    @Override
                                                    public void onResponse(Call<User> call, Response<User> response) {

                                                    }

                                                    @Override
                                                    public void onFailure(Call<User> call, Throwable t) {

                                                    }
                                                });

                                        Log.d("user exist", "onResponse: this user not exist");
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Вы создали новый аккаунт",
                                                Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.BOTTOM, 0, 10);
                                        toast.show();
                                        Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                                        startActivity(intent);
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


                break;
        }
    }
}
