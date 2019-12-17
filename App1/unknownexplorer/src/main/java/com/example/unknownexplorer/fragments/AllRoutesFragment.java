package com.example.unknownexplorer.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.unknownexplorer.POJO.PojoPoint;
import com.example.unknownexplorer.POJO.PojoRoute;
import com.example.unknownexplorer.POJO.PojoUser;
import com.example.unknownexplorer.R;
import com.example.unknownexplorer.adapters.AllRoutesAdapter;
import com.example.unknownexplorer.api.NetworkService;
import com.example.unknownexplorer.models.Route;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllRoutesFragment extends Fragment {

    private RecyclerView recyclerViewAllRoutes;
    private AllRoutesAdapter allRoutesAdapter;

    private View infoAboutRoute;

    //элементы диалогово окна информации о маршруте
    private TextView routeAutor;
    private TextView routeTitle;
    private TextView routeDescription;
    private TextView routeInterest;
    private TextView routeTypeDisplacement;


    private void getRouters() throws IOException {

        final ArrayList<Route> routes = new ArrayList<>();

        NetworkService.getInstance()
                .getJSONApi()
                .getAllRoutes()
                .enqueue(new Callback<List<PojoRoute>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<PojoRoute>> call, @NotNull Response<List<PojoRoute>> response) {

                        if (response.body() != null) {
                            for (int i = 0; i < response.body().size(); i++) {
                                Log.d("load pojoRoutes", "onResponse: " + response.body().get(i).getTitle());
                                routes.add(new Route(
                                        response.body().get(i).getId(),
                                        response.body().get(i).getTitle(),
                                        response.body().get(i).getDescription(),
                                        response.body().get(i).getInterest(),
                                        response.body().get(i).getType(),
                                        "~1.5 ч.",
                                        "3/5"
                                ));
                            }
                        } else {
                            Toast toast = Toast.makeText(getContext(),
                                    "В системе пока нет ни одного маршрута.",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 10);
                            toast.show();
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


    @Override
    public void onResume() {
        super.onResume();
        try {
            getRouters();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        // получаем лайаут с ресайкл вью.
        View root = inflater.inflate(R.layout.fragment_all_routes, container, false);


        // находим в нем ресайкл вью.
        recyclerViewAllRoutes = root.findViewById(R.id.routes_recycler_view);


        // получаем лайаут с ресайкл вью элементом.
        View recyclerItem = inflater.inflate(R.layout.recycler_item_points_of_my_route, null);
        recyclerViewAllRoutes.setLayoutManager(new LinearLayoutManager(recyclerItem.getContext()));


        // слушатель клика по маршруту из адаптера всех маршрутов.
        AllRoutesAdapter.OnAllRoutesClickListener allRoutesListener = new AllRoutesAdapter.OnAllRoutesClickListener() {
            @Override
            public void onRouteClick(Route route) {


                //Получаем вид диалогового окна
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                infoAboutRoute = layoutInflater.inflate(R.layout.info_about_route, null);


                //получаем элементы диалогвого окна при клике на маршрут.
                routeAutor = infoAboutRoute.findViewById(R.id.info_route_autor);
                routeTitle = infoAboutRoute.findViewById(R.id.info_route_title);
                routeDescription = infoAboutRoute.findViewById(R.id.info_route_description);
                routeInterest = infoAboutRoute.findViewById(R.id.info_route_interest);
                routeTypeDisplacement = infoAboutRoute.findViewById(R.id.info_route_type_of_displacement);


                //Выполняем запросы на получение информации о маршруте.
                PojoRoute pojoRoute = new PojoRoute();
                pojoRoute.setId((int) route.getId());

                NetworkService.getInstance()
                        .getJSONApi()
                        .getRouteById((int) route.getId(), pojoRoute)
                        .enqueue(new Callback<List<PojoRoute>>() {
                            @Override
                            public void onResponse(Call<List<PojoRoute>> call, Response<List<PojoRoute>> response) {

                                //получаем user_id маршрута.
                                int userId = response.body().get(0).getUserId();

                                NetworkService.getInstance()
                                        .getJSONApi()
                                        .getUserById(userId)
                                        .enqueue(new Callback<PojoUser>() {
                                            @Override
                                            public void onResponse(Call<PojoUser> call, Response<PojoUser> response) {
                                                String userLogin = response.body().getLogin();
                                                routeAutor.setText(userLogin);
                                            }

                                            @Override
                                            public void onFailure(Call<PojoUser> call, Throwable t) {
                                                Toast toast = Toast.makeText(getContext(),
                                                        "Не могу получить информацию об имени автора.",
                                                        Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.BOTTOM, 0, 10);
                                                toast.show();
                                            }
                                        });

                                //получаем данные, пришедщие из запроса в элементы диалогового окна.
                                String routeTitleValue = response.body().get(0).getTitle();
                                String routeDescriptionValue = response.body().get(0).getDescription();
                                String routeInterestValue = response.body().get(0).getInterest();
                                String routeTypeValue = response.body().get(0).getType();


                                //устанавливаем данные, пришедщие из запроса в элементы диалогового окна.
                                routeTitle.setText(routeTitleValue);
                                routeDescription.setText(routeDescriptionValue);
                                routeInterest.setText(routeInterestValue);
                                routeTypeDisplacement.setText(routeTypeValue);


                                // создаем таблица показа точек маршрута.
                                TableLayout tablePoints;
                                tablePoints = infoAboutRoute.findViewById(R.id.table_points_Info_route);
                                tablePoints.setStretchAllColumns(true);


                                // получаем точки маршрута.
                                ArrayList<PojoPoint> pojoPoints = new ArrayList<PojoPoint>(response.body().get(0).getPoints());


                                //отображем их в таблице точек марщрута tablePoints.
                                for (int i = 0; i < pojoPoints.size(); i++) {


                                    // создаем tableRow в TableLayout на основном экране
                                    TableRow tableRow = new TableRow(getContext());
                                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


                                    //равномерно по горизонтали располагаем элементы в таблице.
                                    tablePoints.setStretchAllColumns(true);


                                    //Создаем элементы (col's) tableRow.
                                    TextView title = new TextView(getContext());
                                    TextView X = new TextView(getContext());
                                    TextView Y = new TextView(getContext());


                                    //задаем параметры для элементов.
                                    title.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    X.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    Y.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                                    title.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    X.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    Y.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


                                    // устанавливаем данные точек из запроса в элементы.
                                    title.setText(pojoPoints.get(i).getTitle());
                                    X.setText(pojoPoints.get(i).getXCoord());
                                    Y.setText(pojoPoints.get(i).getYCoord());


                                    //добавляем элементы в tableRow
                                    tableRow.addView(title);
                                    tableRow.addView(X);
                                    tableRow.addView(Y);
                                    tablePoints.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                                }

                                Toast toast = Toast.makeText(getContext(),
                                        "Информация о маршруте загружена.",
                                        Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.BOTTOM, 0, 10);
                                toast.show();
                            }

                            @Override
                            public void onFailure(Call<List<PojoRoute>> call, Throwable t) {
                                Toast toast = Toast.makeText(getContext(),
                                        t.getMessage(),
                                        Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.BOTTOM, 0, 10);
                                toast.show();
                            }
                        });


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
                                        Toast toast = Toast.makeText(getContext(),
                                                "Маршрут 'начат'",
                                                Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.BOTTOM, 0, 10);
                                        toast.show();
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


        //создаем и устанавливаем адаптер для отображения всех маршрутов.
        allRoutesAdapter = new AllRoutesAdapter(allRoutesListener, this);
        recyclerViewAllRoutes.setAdapter(allRoutesAdapter);


        //обновляем ресайкл вью при создании фрагмента.
        try {
            getRouters();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return root;
    }

}