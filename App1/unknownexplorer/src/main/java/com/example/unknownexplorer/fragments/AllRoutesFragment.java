package com.example.unknownexplorer.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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

    //элементы модального окна
    TextView routeAutor;
    TextView routeTitle;
    TextView routeDescription;
    TextView routeInterest;
    TextView routeTypeDisplacement;
    RecyclerView recyclerViewAllPointsOfRoute;

    DBHelper dbHelper;

    private void loadRouters() {

        Collection<Route> routes = getRouters();
        allRoutesAdapter.setItems(routes);

        Toast toast = Toast.makeText(getContext(),
                "Все маршруты из системы загружены.",
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 10);
        toast.show();
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
        View recycler_item = inflater.inflate(R.layout.recycler_item_points_of_my_route, null);
        recyclerViewAllRoutes.setLayoutManager(new LinearLayoutManager(recycler_item.getContext()));

        AllRoutesAdapter.OnAllRoutesClickListener allRoutesListener = new AllRoutesAdapter.OnAllRoutesClickListener() {
            @Override
            public void onRouteClick(Route route) {
                Log.d("click", "onRouteClick: !!! " + route.getId());
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                //Получаем вид диалогового окна
                final View infoAboutRoute = layoutInflater.inflate(R.layout.info_about_route, null);

                //получаем элементы диалогвого окна.
                int authorOfRouteId = -1;
                routeAutor = infoAboutRoute.findViewById(R.id.info_route_autor);
                routeTitle = infoAboutRoute.findViewById(R.id.info_route_title);
                routeDescription = infoAboutRoute.findViewById(R.id.info_route_description);
                routeInterest = infoAboutRoute.findViewById(R.id.info_route_interest);
                routeTypeDisplacement = infoAboutRoute.findViewById(R.id.info_route_type_of_displacement);

                // подключаемся к БД
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                //получаем данные из базы данных
                Log.d("click", "onRouteClickkkk: " + route.getId());
                String selection = "id = ?";
                String[] selectionArgs = new String[]{(String.valueOf(route.getId()))};

                // первый запрос на получение данных о маршруте
                Cursor routeData = db.query("routes", null, selection, selectionArgs, null, null, null);
                if (routeData.moveToFirst()) {
                    int authorIdCol = routeData.getColumnIndex("userId");
                    int titleCol = routeData.getColumnIndex("title");
                    int descriptionCol = routeData.getColumnIndex("description");
                    int interestCol = routeData.getColumnIndex("interest");
                    int typeCol = routeData.getColumnIndex("type");

                    authorOfRouteId = routeData.getInt(authorIdCol);
                    routeAutor.setText(String.valueOf(routeData.getInt(authorIdCol)));
                    routeTitle.setText(routeData.getString(titleCol));
                    routeDescription.setText(routeData.getString(descriptionCol));
                    routeInterest.setText(routeData.getString(interestCol));
                    routeTypeDisplacement.setText(routeData.getString(typeCol));

                } else
                    Log.d("out_route", "0 rows");
                routeData.close();

                //второй запрос на получение имени автора маршрута
                selection = "id = ?";
                selectionArgs = new String[]{(String.valueOf(authorOfRouteId))};
                Cursor authorData = db.query("users", null, selection, selectionArgs, null, null, null);
                if (authorData.moveToFirst()) {
                    int loginCol = authorData.getColumnIndex("login");
                    routeAutor.setText(String.valueOf(authorData.getString(loginCol)));
                } else
                    Log.d("out_route", "0 rows");
                authorData.close();


                //третий запрос на получение точек маршрута.

                //таблица показа точек маршрута.
                TableLayout tablePoints;
                tablePoints = infoAboutRoute.findViewById(R.id.table_points_Info_route);
                tablePoints.setStretchAllColumns(true);

                selection = "routeId = ?";
                selectionArgs = new String[]{(String.valueOf(route.getId()))};

                Cursor pointsOfROuteData = db.query("points", null, selection, selectionArgs, null, null, null);
                if (pointsOfROuteData.moveToFirst()) {
                    int loginCol = pointsOfROuteData.getColumnIndex("login");
//                    routeAutor.setText(String.valueOf(pointsOfROuteData.getString(loginCol)));
                    //Присваеваем значения из диалогового окна
                    for (int i=0 ; i< pointsOfROuteData.getCount();i++){
                        // Добавляем tableRow в TableLayout на основном экране
                        TableRow tableRow = new TableRow(getContext());

                        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tablePoints.setStretchAllColumns(true);

                        //Создаем элементы tableRow
                        TextView title = new TextView(getContext());
                        TextView X = new TextView(getContext());
                        TextView Y = new TextView(getContext());

                        //задаем LayoutParams
                        title.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        X.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        Y.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                        title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        X.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        Y.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                        int titlePointCol = pointsOfROuteData.getColumnIndex("title");
                        int xCoordPointCol = pointsOfROuteData.getColumnIndex("xCoord");
                        int yCoordPointCol = pointsOfROuteData.getColumnIndex("yCoord");

                        title.setText(pointsOfROuteData.getString(titlePointCol));
                        X.setText(pointsOfROuteData.getString(xCoordPointCol));
                        Y.setText(pointsOfROuteData.getString(yCoordPointCol));

                        //добавляем элементы в tableRow
                        tableRow.addView(title);
                        tableRow.addView(X);
                        tableRow.addView(Y);
                        tablePoints.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                        pointsOfROuteData.moveToNext();
                    }
                } else
                    Log.d("out_route", "0 rows");
                authorData.close();


                //Создаем AlertDialog
                final AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext());

                //Настраиваем dialog_create_point_activity_create_route.xml для нашего AlertDialog:
                mDialogBuilder.setView(infoAboutRoute);
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

        allRoutesAdapter = new AllRoutesAdapter(allRoutesListener, this);
        recyclerViewAllRoutes.setAdapter(allRoutesAdapter);
        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(recyclerViewAllRoutes.getContext());
        loadRouters();
        return root;
    }

}