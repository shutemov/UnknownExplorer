package com.example.unknownexplorer.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.unknownexplorer.adapters.AllRoutesAdapter;
import com.example.unknownexplorer.db.DBHelper;
import com.example.unknownexplorer.models.Route;

import java.util.ArrayList;
import java.util.Collection;

public class AllRoutesFragment extends Fragment {

    //    private HomeViewModel homeViewModel;
    private RecyclerView recyclerViewAllRoutes;
    private AllRoutesAdapter allRoutesAdapter;

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
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.all_routes, container, false);
        recyclerViewAllRoutes = root.findViewById(R.id.routes_recycler_view);
        View recycler_item = inflater.inflate(R.layout.recycler_item_points_of_route, null);
        recyclerViewAllRoutes.setLayoutManager(new LinearLayoutManager(recycler_item.getContext()));

        AllRoutesAdapter.OnAllRoutesClickListener allRoutesListener = new AllRoutesAdapter.OnAllRoutesClickListener() {
            @Override
            public void onRouteClick(Route route) {
                Log.d("click", "onRouteClick: !!! " + route.getId());
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());

                //Получаем вид диалогового окна
                final View showInfoAboutRoute = layoutInflater.inflate(R.layout.info_about_route, null);

                //Создаем AlertDialog
                final AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext());

                //Настраиваем dialog_create_point_activity_create_route.xml для нашего AlertDialog:
                mDialogBuilder.setView(showInfoAboutRoute);
                //Настраиваем сообщение в диалоговом окне:
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Start",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                //Создаем AlertDialog:
                AlertDialog alertDialog = mDialogBuilder.create();
                //и отображаем его:
                alertDialog.show();
            }
        };

        allRoutesAdapter = new AllRoutesAdapter(allRoutesListener,this);
        recyclerViewAllRoutes.setAdapter(allRoutesAdapter);
        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(recyclerViewAllRoutes.getContext());
        loadRouters();
        return root;
    }

}