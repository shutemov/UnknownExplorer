package com.example.unknownexplorer.ui.allRoutes;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unknownexplorer.R;
import com.example.unknownexplorer.adapters.allRoutesAdapters.allRoutesAdapter;
import com.example.unknownexplorer.db.DBHelper;
import com.example.unknownexplorer.models.Route;

import java.util.ArrayList;
import java.util.Collection;

public class allRoutesFragment extends Fragment {

    //    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private allRoutesAdapter allRoutesAdapter;

    DBHelper dbHelper;

    private void loadRouters() {

        Collection<Route> routes = getRouters();
        allRoutesAdapter.setItems(routes);
    }

    @org.jetbrains.annotations.NotNull
    private Collection<Route> getRouters() {

        ArrayList<Route> routes = new ArrayList<>();

        Log.d("test", "getRouters  from home fragment");

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //получаем данные из базы данных
        Cursor data = db.query("routes", null, null, null, null, null, null);

        if (data.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = data.getColumnIndex("id");
            int titleColIndex = data.getColumnIndex("title");
            int descriptionColIndex = data.getColumnIndex("description");
            int interestColIndex = data.getColumnIndex("interest");
            int typeColIndex = data.getColumnIndex("type");


            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("out_route",
                        "ID = " + data.getInt(idColIndex) +
                                ", title = " + data.getString(titleColIndex)
                );
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла

                //добавляем в список маршруты из бд
                routes.add(
                        new Route(
                                data.getInt(idColIndex),
                                data.getString(titleColIndex),
                                data.getString(descriptionColIndex),
                                data.getString(interestColIndex),
                                data.getString(typeColIndex),
                                "~2 ч.",
                                "3/5"
                        )
                );

            } while (data.moveToNext());
        } else
            Log.d("out_route", "0 rows");
        data.close();

        return routes;
//        return Arrays.asList(
//                new Route("чил для хикк", "lorem impusmdssdlkf;ldsk fldsk;lfksd;","photo","walk","13:03","3/5"),
//                new Route("чил для хикк", "lorem impusmdssdlkf;ldsk fldsk;lfksd;lorem impusmdssdlkf;ldsk fldsk;lfksd;lorem impusmdssdlkf;ldsk fldsk;lfksd;lorem impusmdssdlkf;ldsk fldsk;lfksd;impusmdssdlkf;ldsk fldsk;lfksd;lorem impusmdssdlkf;ldsk fldsk;lfksd;lorem impusmdssdlkf;ldsk fldsk;lfksd;lorem impusmdssdlkf;ldsk fldsk;lfksd;","photo","walk","13:03","3/5"),
//        );
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.d("test", "onCreateView1 from home fragment");
        Log.d("test", "onCreateView: " + R.layout.all_routes);

        View root = inflater.inflate(R.layout.all_routes, container, false);
        Log.d("test", "onCreateView2 from home fragment");
        Log.d("test", "VIEW ID?: " + root);
        recyclerView = root.findViewById(R.id.routes_recycler_view);


        Log.d("test", "onCreateView: " + recyclerView);
        Log.d("test", "onCreateView3 from home fragment");


        View recycler_item = inflater.inflate(R.layout.recycler_item_all_routes, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(recycler_item.getContext()));




        allRoutesAdapter = new allRoutesAdapter();

        recyclerView.setAdapter(allRoutesAdapter);


        Log.d("test", "onCreateView4 from home fragment");

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(recyclerView.getContext());


        loadRouters();


        return root;
    }

}