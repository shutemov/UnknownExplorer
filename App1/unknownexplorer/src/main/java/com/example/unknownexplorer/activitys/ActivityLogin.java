package com.example.unknownexplorer.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unknownexplorer.POJO.PojoUser;
import com.example.unknownexplorer.R;
import com.example.unknownexplorer.api.NetworkService;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener {

    TextView inputLogin;
    TextView inputPassword;
    TextView txRegistration;
    Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        txRegistration = findViewById(R.id.txRegistration);
        inputLogin = findViewById(R.id.inputLogin);
        inputPassword = findViewById(R.id.inputPasswordLoginScreen);

        btnLogin.setOnClickListener(this);
        txRegistration.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btnLogin:


                // формируем данные для запроса
                PojoUser pojoUser = new PojoUser();
                pojoUser.setLogin(inputLogin.getText().toString());
                pojoUser.setPassword(inputPassword.getText().toString());


                //делаем запрос.
                NetworkService.getInstance()
                        .getJSONApi()
                        .getUserExist(pojoUser)
                        .enqueue(new Callback<PojoUser>() {


                            @Override
                            public void onResponse(@NotNull Call<PojoUser> call, @NotNull Response<PojoUser> response) {
                                PojoUser user = response.body();
                                if (user.getId() == 0) {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Логин или пароль не совпадают.",
                                            Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.BOTTOM, 0, 10);
                                    toast.show();
                                } else {
                                    Intent intent = new Intent("ActivityMainNavigation");
                                    intent.putExtra("userLogin", Objects.requireNonNull(response.body()).getLogin());
                                    intent.putExtra("userId", response.body().getId());
                                    startActivity(intent);
                                }
                            }


                            @Override
                            public void onFailure(@NotNull Call<PojoUser> call, @NotNull Throwable t) {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        t.getMessage(),
                                        Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.BOTTOM, 0, 10);
                                toast.show();
                            }
                        });
                break;
            case R.id.txRegistration:
                intent = new Intent("ActivityRegistration");
                startActivity(intent);
                break;
        }
    }
}
