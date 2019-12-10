package com.example.unknownexplorer.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.unknownexplorer.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class ActivityMainNavigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    //данные пользователя
    int USER_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("ActivityCreateRoute");

                //получаем id пользователя
                int USER_ID = getIntent().getIntExtra("userId", -1);
                intent.putExtra("userId", USER_ID);
                startActivity(intent);
                //всплывающая сноска
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_all_routes, R.id.nav_my_routes, R.id.nav_exit)
                .setDrawerLayout(drawer)
                .build();

//        NavigationView exit = findViewById(R.id.nav_exit);
//        exit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent("ActivityRegistration"));
//            }
//        });
        //отвечает за бергер кнопку и описание вбранного элемента меню.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Принимаем Id
        USER_ID = getIntent().getIntExtra("userId", -1);

        // Принимаем Login
        String USER_LOGIN = getIntent().getStringExtra("userLogin");

        // выводим принятое имя
        Log.d("intent data", "ID: " + USER_ID + " LOGIN: " + USER_LOGIN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.d("test", "111");
        switch (menuItem.getItemId()) {
            case R.id.nav_my_routes: {
                Intent intent = new Intent(this, ActivityLogin.class);
                startActivity(intent);
                break;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Log.d("test", "on back Pressedddd");
    }

}
