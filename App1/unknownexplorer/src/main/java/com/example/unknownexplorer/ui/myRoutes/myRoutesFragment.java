package com.example.unknownexplorer.ui.myRoutes;

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
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unknownexplorer.R;
import com.example.unknownexplorer.adapters.myRoutesAdapter.MyRoutesAdapter;
import com.example.unknownexplorer.db.DBHelper;
import com.example.unknownexplorer.models.Route;

import java.util.ArrayList;
import java.util.Collection;


public class myRoutesFragment extends Fragment {


    private RecyclerView recyclerView;
    private MyRoutesAdapter myRoutesAdapter;
    public static Context context;
    private int USER_ID;
    DBHelper dbHelper;

    private void loadRouters() {
        Log.d("test", "loadRouters  from my_routes fragment");
        Collection<Route> routes = getRouters();
        myRoutesAdapter.setItems(routes);
    }

    @org.jetbrains.annotations.NotNull
    private Collection<Route> getRouters() {
        Log.d("test", "getRouters  from my_routes fragment");

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

//        return Arrays.asList(
//                new Route(1,"чил для хикк", "lorem impusmdssdlkf;ldsk fldsk;lfksd;","photo","walk","13:03","3/5"),
//                new Route(2,"чил для хикк", "lorem impusmdssdlkf;ldsk fldsk;lfksd;","photo","walk","13:03","3/5"),
//                new Route(3,"чил для хикк", "lorem impusmdssdlkf;ldsk fldsk;lfksd;","photo","walk","13:03","3/5")
//        );
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.my_routes, container, false);

        recyclerView = root.findViewById(R.id.my_routes_recycler_view);
        //получаем recycle item
        View recycler_item = inflater.inflate(R.layout.recycler_item_my_routes, null);
        //устанавливаем recycle item into recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(recycler_item.getContext()));


        // проверяем можем ли получить id user
        USER_ID = getActivity().getIntent().getExtras().getInt("userId", -1);
        Log.d("FROM MY ROUTES FRAGMENT", "onCreateView: " + USER_ID);

//


        MyRoutesAdapter.OnMyRoutesClickListener myRoutesListener = new MyRoutesAdapter.OnMyRoutesClickListener() {
            @Override
            public void onRouteClick(Route route) {
                Log.d("TESST", "onRouteClick: " + route.getDescription());
                Log.d("TESST", "onRouteClick: " + route.getId());
            }

            @Override
            public void onEditClick(final Route route) {
                Log.d("onEditClicl", "onEditClick: " + route.getDescription());
                //Получаем вид с файла dialog_create_point_activity_create_route.xml, который применим для диалогового окна:
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View promptsView = layoutInflater.inflate(R.layout.dialog_edit_my_route, null);

                //элементы диалогового окна.
                final Spinner interestSpinner = promptsView.findViewById(R.id.input_spinner_interest);
                final Spinner typeSpinner = promptsView.findViewById(R.id.input_spinner_type);
                final EditText inputNewTitle = promptsView.findViewById(R.id.input_title);
                final EditText inputNewDescription = promptsView.findViewById(R.id.input_description);

                //TODO: устанавливаем значения из маршрута в элементы диалогового окна
                inputNewTitle.setText(route.getTitle());
                inputNewDescription.setText(route.getDescription());

                //Создаем AlertDialog
                final AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext());

                //Настраиваем dialog_create_point_activity_create_route.xml для нашего AlertDialog:
                mDialogBuilder.setView(promptsView);
                //Настраиваем сообщение в диалоговом окне:
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Edit",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        //получаем значения полей из диалогового окна.
                                        long routeId = route.getId();
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
            public void onDeleteClick(Route route) {
                Log.d("onEditClicl", "onEditClick: " + route.getDescription());
                //Получаем вид с файла dialog_create_point_activity_create_route.xml, который применим для диалогового окна:
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View dialogEditMyRouteView = layoutInflater.inflate(R.layout.dialog_edit_my_route, null);

                //Создаем AlertDialog
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext());

                //Настраиваем dialog_create_point_activity_create_route.xml для нашего AlertDialog:
                mDialogBuilder.setView(dialogEditMyRouteView);
                //Настраиваем сообщение в диалоговом окне:
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Delete",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Добавляем tableRow в TableLayout на основном экране

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


        recyclerView.setAdapter(myRoutesAdapter);

        dbHelper = new DBHelper(recyclerView.getContext());
        loadRouters();

        return root;
    }

}