package com.example.unknownexplorer.fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unknownexplorer.R;
import com.example.unknownexplorer.adapters.MyRoutesAdapter;
import com.example.unknownexplorer.adapters.PointsOfRouteAdapter;
import com.example.unknownexplorer.db.DBHelper;
import com.example.unknownexplorer.models.Point;
import com.example.unknownexplorer.models.Route;

import java.util.ArrayList;
import java.util.Collection;


public class MyRoutesFragment extends Fragment {


    private RecyclerView recyclerViewMyRoutes;
    private MyRoutesAdapter myRoutesAdapter;
    private PointsOfRouteAdapter pointsOfRouteAdapter;
    public static Context context;
    private int USER_ID;
    DBHelper dbHelper;


    private Collection<Point> getPoints(long routeId) {

        Log.d("test", "getPoints  from my_routes fragment");

        ArrayList<Point> points = new ArrayList<>();

        // данные для запроса получения точек маршрута.
        String selection = "routeId = ?";
        String[] selectionArgs = new String[]{(String.valueOf(routeId))};

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //получаем данные о точке маршрута из базы данных.
        Cursor dataPoints = db.query("points", null, selection, selectionArgs, null, null, null);

        if (dataPoints.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idColIndex = dataPoints.getColumnIndex("id");
            int titleColIndex = dataPoints.getColumnIndex("title");
            int xCoordColIndex = dataPoints.getColumnIndex("xCoord");
            int yCoordColIndex = dataPoints.getColumnIndex("yCoord");
            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("out_points",
                        "ID = " + dataPoints.getInt(idColIndex)
                );

                //добавляем в список маршруты из бд
                points.add(
                        new Point(
                                dataPoints.getInt(idColIndex),
                                dataPoints.getString(titleColIndex),
                                dataPoints.getString(xCoordColIndex),
                                dataPoints.getString(yCoordColIndex)
                        )
                );
            } while (dataPoints.moveToNext());
        } else
            Log.d("out_route", "0 rows");
        dataPoints.close();

        return points;
    }

    private Collection<Route> getRoutes() {
        Log.d("test", "getRoutes  from my_routes fragment");

        ArrayList<Route> routes = new ArrayList<>();
        // данные для запроса
        String selection = "userId = ?";
        String[] selectionArgs = new String[]{(String.valueOf(USER_ID))};

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //получаем данные из базы данных
        Cursor dataRoutes = db.query("routes", null, selection, selectionArgs, null, null, null);


        if (dataRoutes.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = dataRoutes.getColumnIndex("id");
            int titleColIndex = dataRoutes.getColumnIndex("title");
            int descriptionColIndex = dataRoutes.getColumnIndex("description");
            int interestColIndex = dataRoutes.getColumnIndex("interest");
            int typeColIndex = dataRoutes.getColumnIndex("type");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("out_route",
                        "ID = " + dataRoutes.getInt(idColIndex) +
                                ", title = " + dataRoutes.getString(titleColIndex)
                );

                //добавляем в список маршруты из бд
                routes.add(
                        new Route(
                                dataRoutes.getInt(idColIndex),
                                dataRoutes.getString(titleColIndex),
                                dataRoutes.getString(descriptionColIndex),
                                dataRoutes.getString(interestColIndex),
                                dataRoutes.getString(typeColIndex),
                                "~2 ч.",
                                "3/5"
                        )
                );
            } while (dataRoutes.moveToNext());
        } else
            Log.d("out_route", "0 rows");
        dataRoutes.close();

        return routes;
    }

    private void loadPoints(long routeId) {
        Log.d("test", "loadPoints  from my_routes fragment");
        Collection<Point> points = getPoints(routeId);
        for (int i = 0; i < points.size(); i++) {
            Log.d("point", "loadPoints: " + points);
        }
        pointsOfRouteAdapter.setItems(points);
    }

    ;

    private void loadRouters() {
        Log.d("test", "loadRouters  from my_routes fragment");
        Collection<Route> routes = getRoutes();
        myRoutesAdapter.setItems(routes);
    }

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        // todo ЗАРЕФАКТОРИТЬ ОБЪЯВЛЕНИЕ ЭЛЕМЕНТОВ

        final View root = inflater.inflate(R.layout.my_routes, container, false);

        recyclerViewMyRoutes = root.findViewById(R.id.my_routes_recycler_view);

        //получаем recycle item
        View recycler_item = inflater.inflate(R.layout.recycler_item_my_routes, null);

        //устанавливаем recycle item into recycler view
        recyclerViewMyRoutes.setLayoutManager(new LinearLayoutManager(recycler_item.getContext()));

        // получаем id пользователя
        USER_ID = getActivity().getIntent().getExtras().getInt("userId", -1);


        MyRoutesAdapter.OnMyRoutesClickListener myRoutesListener = new MyRoutesAdapter.OnMyRoutesClickListener() {

            @Override
            public void onRouteClick(Route route) {

            }

            @Override
            public void onEditClick(final Route route) {
                Log.d("onEditClicl", "onEditClick: " + route.getDescription());

                //______________СОЗДАНИЕ ДИАЛОГОВОГО ОКНА РЕДАКТИРОВАНИЯ МАРШРУТА______________

                //Получаем вид с файла dialog_create_point_activity_create_route.xml, который применим для диалогового окна:
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());

                //Получаем вид диалогового окна
                final View editRouteDialogWindow = layoutInflater.inflate(R.layout.dialog_edit_my_route, null);


                //объявляем ресайкл вью точек маршрута
                final RecyclerView recyclerViewPoints;

                //получаем ресайкл вью из вида диалогового окна.
                recyclerViewPoints = editRouteDialogWindow.findViewById(R.id.recycler_view_edit_route);

                View pointRecyclerItem = inflater.inflate(R.layout.recycler_item_points_of_my_route, null);

                recyclerViewPoints.setLayoutManager(new LinearLayoutManager(pointRecyclerItem.getContext()));

                PointsOfRouteAdapter.OnPointClickListener onPointClickListener = new PointsOfRouteAdapter.OnPointClickListener() {
                    @Override
                    public void onDeletePoint(Point point) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        //получаем данные из базы данных
                        db.delete("points", "id = " + point.getId(), null);
                        loadPoints(point.getId());
                    }
                };

                pointsOfRouteAdapter = new PointsOfRouteAdapter(onPointClickListener, getContext());

                recyclerViewPoints.setAdapter(pointsOfRouteAdapter);

                //загружаем точки маршрута.
                loadPoints(route.getId());

                //элементы диалогового окна.
                final Spinner interestSpinner = editRouteDialogWindow.findViewById(R.id.input_spinner_interest);
                final Spinner typeSpinner = editRouteDialogWindow.findViewById(R.id.input_spinner_type);
                final EditText inputNewTitle = editRouteDialogWindow.findViewById(R.id.input_title);
                final EditText inputNewDescription = editRouteDialogWindow.findViewById(R.id.input_description);

                //TODO: устанавливаем значения из маршрута в элементы диалогового окна (решить проблему вставки значения в спиннеры)
                //TODO: ПОСЛЕДНИЙ ЭЛЕМЕНТ НЕ ДОКРУЧИВАЕТСЯ.

                inputNewTitle.setText(route.getTitle());
                inputNewDescription.setText(route.getDescription());

                //END TODO

                //todo
                Button buttonAddPoint = editRouteDialogWindow.findViewById(R.id.add_point_button_edit_route);
                final EditText inputPointName = editRouteDialogWindow.findViewById(R.id.input_point_name);
                final EditText inputPointXCoord = editRouteDialogWindow.findViewById(R.id.input_point_x_coord);
                final EditText inputPointYCoord = editRouteDialogWindow.findViewById(R.id.input_point_y_coord);

                buttonAddPoint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //подключаемся к бд.

                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        //создаем данные для точки маршрута.
                        ContentValues newPointData = new ContentValues();
                        newPointData.put("routeId", route.getId());
                        newPointData.put("title", inputPointName.getText().toString());
                        newPointData.put("xCoord", inputPointXCoord.getText().toString());
                        newPointData.put("yCoord", inputPointYCoord.getText().toString());

                        //добавляем точку маршрут.
                        db.insert("points", "", newPointData);

                        //обновляем ресайкл вью
                        loadPoints(route.getId());

                        //очищаем данные воода.
                        inputPointName.setText("");
                        inputPointXCoord.setText("");
                        inputPointYCoord.setText("");
                        Log.d("add point button", "onClick: " + v);
                    }
                });


                //end todo

                //Создаем AlertDialog
                final AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext());

                //Настраиваем dialog_create_point_activity_create_route.xml для нашего AlertDialog:
                mDialogBuilder.setView(editRouteDialogWindow);
                //Настраиваем сообщение в диалоговом окне:
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Edit",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        final long routeId = route.getId();

                                        //получаем значения полей из диалогового окна.

                                        String newTitle = inputNewTitle.getText().toString();
                                        String newDescription = inputNewDescription.getText().toString();
                                        String inputNewInterest = interestSpinner.getSelectedItem().toString();
                                        String inputNewTypeDisplacement = typeSpinner.getSelectedItem().toString();

                                        // создаем объект для данных
                                        ContentValues newRouteDataContent = new ContentValues();
                                        // подключаемся к БД
                                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                                        //получаем данные из базы данных

                                        newRouteDataContent.put("title", newTitle);
                                        newRouteDataContent.put("description", newDescription);
                                        newRouteDataContent.put("interest", inputNewInterest);
                                        newRouteDataContent.put("type", inputNewTypeDisplacement);

                                        // запрос на обновление route.
                                        int dataRoutes = db.update("routes", newRouteDataContent, "id = ?", new String[]{String.valueOf(routeId)});

                                        //TODO: обновляем данные в ресайкл вью мои маршруты
                                        loadRouters();

                                        //__________________________________________КОД ДЛЯ ПРОВЕРКИ ПРИШЕДШИХ ИЗ БД ДАННЫХ________________________________________________________
//                                        int idColIndex = dataRoutes.getColumnIndex("id");
//                                        int titleColIndex = dataRoutes.getColumnIndex("title");
//                                        int descriptionColIndex = dataRoutes.getColumnIndex("description");
//                                        int interestColIndex = dataRoutes.getColumnIndex("interest");
//                                        int typeColIndex = dataRoutes.getColumnIndex("type");
//                                        if (dataRoutes.moveToFirst()) {
//                                            do {
//                                                // получаем значения по номерам столбцов и пишем все в лог
//                                                Log.d("out_route_TESST",
//                                                        "ID = " + dataRoutes.getInt(idColIndex) +
//                                                                ", title = " + dataRoutes.getString(titleColIndex)
//                                                );
//
//                                            } while (dataRoutes.moveToNext());
//                                        } else
//                                            dataRoutes.close();
//______________________________________________________________________________________________________________________________________________________________________________________________________________________________recyclerView.getAdapter().notifyDataSetChanged();

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

            @Override
            public void onDeleteClick(final Route route) {
                Log.d("onEditClicl", "onEditClick: " + route.getDescription());

                //Создаем AlertDialog
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext());

                //Настраиваем сообщение и кнопки в диалоговом окне:
                mDialogBuilder
                        .setMessage("Are you sure you want to delete the route?")
                        .setCancelable(false)
                        .setPositiveButton("Delete",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // создаем объект для данных
                                        ContentValues newRouteDataContent = new ContentValues();
                                        // подключаемся к БД
                                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                                        //получаем данные из базы данных
                                        int delCount = db.delete("routes", "id = " + route.getId(), null);
                                        loadRouters();
                                        Log.d("delete", "onClick: route was deleted " + delCount);
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

        // устанавливаем адаптер
        myRoutesAdapter = new MyRoutesAdapter(myRoutesListener, this);

        recyclerViewMyRoutes.setAdapter(myRoutesAdapter);

        dbHelper = new DBHelper(recyclerViewMyRoutes.getContext());

        loadRouters();

        //возвращаем отображение из фрагмента.
        return root;
    }


}