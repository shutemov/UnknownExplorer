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

import com.example.unknownexplorer.POJO.PojoRoute;
import com.example.unknownexplorer.R;
import com.example.unknownexplorer.adapters.AllRoutesAdapter;
import com.example.unknownexplorer.api.NetworkService;
import com.example.unknownexplorer.db.DBHelper;
import com.example.unknownexplorer.models.Route;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllRoutesFragment extends Fragment {

    private RecyclerView recyclerViewAllRoutes;
    private AllRoutesAdapter allRoutesAdapter;

    //элементы модального окна
    TextView routeAutor;
    TextView routeTitle;
    TextView routeDescription;
    TextView routeInterest;
    TextView routeTypeDisplacement;
    RecyclerView recyclerViewAllPointsOfRoute;
    SQLiteDatabase DB;

    DBHelper dbHelper;



    @org.jetbrains.annotations.NotNull
    private void getRouters() throws IOException {

        final ArrayList<Route> routes = new ArrayList<>();

        NetworkService.getInstance()
                .getJSONApi()
                .getAllRoutes()
                .enqueue(new Callback<List<PojoRoute>>() {
                    @Override
                    public void onResponse(Call<List<PojoRoute>> call, Response<List<PojoRoute>> response) {


                        for (int i = 0; i < response.body().size(); i++) {
                            Log.d("load pojoRoutes", "onResponse: " + response.body().get(i).getTitle());
                            routes.add(new Route(
                                    response.body().get(i).getId(),
                                    response.body().get(i).getTitle(),
                                    "description",
                                    "interest",
                                    "type",
                                    "3x",
                                    "3/5"
                            ));
                        }
                        allRoutesAdapter.setItems(routes);
                        recyclerViewAllRoutes.getAdapter().notifyDataSetChanged();
                        Toast toast = Toast.makeText(getContext(),
                                "Все маршруты загружены.",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 10);
                        toast.show();
                    }

                    @Override
                    public void onFailure(Call<List<PojoRoute>> call, Throwable t) {
                        Log.d("load pojoRoutes", "onFailure: " + t.getMessage());
                    }
                });


    }

    ;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // получаем лайаут с ресайкл вью.
        View root = inflater.inflate(R.layout.fragment_all_routes, container, false);
        // находим в нем ресайкл вью.
        recyclerViewAllRoutes = root.findViewById(R.id.routes_recycler_view);

        // получаем лайаут с ресайкл вью элементом.
        View recycler_item = inflater.inflate(R.layout.recycler_item_points_of_my_route, null);
        recyclerViewAllRoutes.setLayoutManager(new LinearLayoutManager(recycler_item.getContext()));

        AllRoutesAdapter.OnAllRoutesClickListener allRoutesListener = new AllRoutesAdapter.OnAllRoutesClickListener() {
            @Override
            public void onRouteClick(Route route) {

                LayoutInflater layoutInflater = LayoutInflater.from(getContext());

                //Получаем вид диалогового окна
                final View infoAboutRoute = layoutInflater.inflate(R.layout.info_about_route, null);


                //если автора маршрута нет, вернет -1.
                int authorOfRouteId = -1;

                //получаем элементы диалогвого окна
                routeAutor = infoAboutRoute.findViewById(R.id.info_route_autor);
                routeTitle = infoAboutRoute.findViewById(R.id.info_route_title);
                routeDescription = infoAboutRoute.findViewById(R.id.info_route_description);
                routeInterest = infoAboutRoute.findViewById(R.id.info_route_interest);
                routeTypeDisplacement = infoAboutRoute.findViewById(R.id.info_route_type_of_displacement);

                // подключаемся к БД
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                //получаем данные из базы данных
                String selection = "id = ?";
                String[] selectionArgs = new String[]{(String.valueOf(route.getId()))};

                // первый запрос на получение данных о маршруте.
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

                //второй запрос на получение имени автора маршрута.
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

                    //Присваеваем значения из диалогового окна
                    for (int i = 0; i < pointsOfROuteData.getCount(); i++) {

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
                    Log.d("out_points", "0 rows");
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

        try {
            getRouters();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;
    }

}