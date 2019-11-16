package com.example.unknownexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityRegistration extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Button btnRegistration = findViewById(R.id.btnRegistration);

        btnRegistration.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId()){
            case R.id.btnRegistration:
                intent = new Intent(this,ActivityLogin.class);
                startActivity(intent);
                break;
        }
    }
}
