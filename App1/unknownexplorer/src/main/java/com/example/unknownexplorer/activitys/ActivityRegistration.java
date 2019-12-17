package com.example.unknownexplorer.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ActivityRegistration extends AppCompatActivity implements View.OnClickListener {

    Button btnRegistration;
    EditText inputLogin;
    EditText inputPassword;
    EditText inputRepeatPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnRegistration = findViewById(R.id.btnRegistration);
        inputLogin = findViewById(R.id.inputLoginRegistration);
        inputPassword = findViewById(R.id.inputPasswordRegistration);
        inputRepeatPassword = findViewById(R.id.inputRepeatPasswordRegistration);

        btnRegistration.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnRegistration:

                //данные с экрана
                String login = inputLogin.getText().toString();
                String password = inputPassword.getText().toString();
                String repeatPassword = inputRepeatPassword.getText().toString();

                if (password.equals(repeatPassword)) {

                    //формируем данные для запроса.
                    final PojoUser User = new PojoUser();
                    User.setLogin(inputLogin.getText().toString());
                    User.setPassword(inputPassword.getText().toString());

                    //выполняем запрос.
                    NetworkService.getInstance()
                            .getJSONApi()
                            .getUserExist(User)
                            .enqueue(new Callback<PojoUser>() {
                                @Override
                                public void onResponse(@NotNull Call<PojoUser> call, @NotNull Response<PojoUser> response) {
                                    PojoUser user = response.body();
                                    if (Objects.requireNonNull(user).getId() == 0) {
                                        NetworkService.getInstance()
                                                .getJSONApi()
                                                .createNewUser(User)
                                                .enqueue(new Callback<PojoUser>() {
                                                    @Override
                                                    public void onResponse(@NotNull Call<PojoUser> call, @NotNull Response<PojoUser> response) {

                                                    }

                                                    @Override
                                                    public void onFailure(Call<PojoUser> call, Throwable t) {
                                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                                t.getMessage(),
                                                                Toast.LENGTH_SHORT);
                                                        toast.setGravity(Gravity.BOTTOM, 0, 10);
                                                        toast.show();
                                                    }
                                                });

                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Вы создали новый аккаунт",
                                                Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.BOTTOM, 0, 10);
                                        toast.show();


                                        //возвращемся в окно авторизации
                                        Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                                        startActivity(intent);
                                    } else {

                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Придумайте другой логин или проверьте пароли",
                                                Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.BOTTOM, 0, 10);
                                        toast.show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<PojoUser> call, Throwable t) {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            t.getMessage(),
                                            Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.BOTTOM, 0, 10);
                                    toast.show();
                                }
                            });
                }
                break;
        }
    }
}
