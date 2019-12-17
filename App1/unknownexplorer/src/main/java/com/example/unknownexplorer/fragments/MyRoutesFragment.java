package com.example.unknownexplorer.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unknownexplorer.POJO.PojoPoint;
import com.example.unknownexplorer.POJO.PojoRoute;
import com.example.unknownexplorer.R;
import com.example.unknownexplorer.adapters.MyRoutesAdapter;
import com.example.unknownexplorer.adapters.PointsOfRouteAdapter;
import com.example.unknownexplorer.api.NetworkService;
import com.example.unknownexplorer.models.Point;
import com.example.unknownexplorer.models.Route;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyRoutesFragment extends Fragment {


    private RecyclerView recyclerViewPoints;

    private MyRoutesAdapter myRoutesAdapter;
    private PointsOfRouteAdapter pointsOfRouteAdapter;

    private int USER_ID;

    private void getPoints(long routeId) {

        final ArrayList<Point> points = new ArrayList<>();

        NetworkService.getInstance()
                .getJSONApi()
                .getRoutePoints((int) routeId)
                .enqueue(new Callback<List<PojoPoint>>() {
                    @Override
                    public void onResponse(Call<List<PojoPoint>> call, Response<List<PojoPoint>> response) {

                        if (response.body() != null) {

                            for (int i = 0; i < response.body().size(); i++) {

                                int routeId = response.body().get(i).getId();
                                String title = response.body().get(i).getTitle();
                                String xCoord = response.body().get(i).getXCoord();
                                String yCoord = response.body().get(i).getYCoord();

                                points.add(new Point(routeId, title, xCoord, yCoord));
                            }

                        } else {
                            Toast toast = Toast.makeText(getContext(),
                                    "У маршрута нет точек. ",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        pointsOfRouteAdapter.setItems(points);
                    }

                    @Override
                    public void onFailure(Call<List<PojoPoint>> call, Throwable t) {
                        Toast toast = Toast.makeText(getContext(),
                                t.getMessage(),
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
    }

    private void getRoutes() {

        final ArrayList<Route> routes = new ArrayList<>();

        NetworkService.getInstance()
                .getJSONApi()
                .getMyRoutes(USER_ID)
                .enqueue(new Callback<List<PojoRoute>>() {
                    @Override
                    public void onResponse(Call<List<PojoRoute>> call, Response<List<PojoRoute>> response) {

                        if (response.body().size() == 0) {
                            Toast toast = Toast.makeText(getContext(),
                                    "Вы еще не создали ни одного маршрута.",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            for (int i = 0; i < response.body().size(); i++) {

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


                            Toast toast = Toast.makeText(getContext(),
                                    "Ваши маршруты загружены.",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }


                        myRoutesAdapter.setItems(routes);
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
        getRoutes();
    }

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        USER_ID = getActivity().getIntent().getExtras().getInt("userId", -1);

        final View root = inflater.inflate(R.layout.my_routes, container, false);

        RecyclerView recyclerViewMyRoutes = root.findViewById(R.id.my_routes_recycler_view);

        //получаем recycle item
        View recycler_item = inflater.inflate(R.layout.recycler_item_my_routes, null);

        //устанавливаем recycle item в recycler view
        recyclerViewMyRoutes.setLayoutManager(new LinearLayoutManager(recycler_item.getContext()));


        MyRoutesAdapter.OnMyRoutesClickListener myRoutesListener = new MyRoutesAdapter.OnMyRoutesClickListener() {

            @Override
            public void onEditClick(final Route route) {

                //______________СОЗДАНИЕ ДИАЛОГОВОГО ОКНА РЕДАКТИРОВАНИЯ МАРШРУТА______________

                //Получаем вид с файла dialog_create_point_activity_create_route.xml, который применим для диалогового окна:
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());

                //Получаем вид диалогового окна
                final View editRouteDialogWindow = layoutInflater.inflate(R.layout.dialog_edit_my_route, null);


                //получаем ресайкл вью из вида диалогового окна.
                recyclerViewPoints = editRouteDialogWindow.findViewById(R.id.recycler_view_edit_route);
                final View pointRecyclerItem = inflater.inflate(R.layout.recycler_item_points_of_my_route, null);

                recyclerViewPoints.setLayoutManager(new LinearLayoutManager(pointRecyclerItem.getContext()));


                //TODO исправить баг удаления точки по 2 клику.
                //создаем слушатель точек маршрута.
                PointsOfRouteAdapter.OnPointClickListener onPointClickListener = new PointsOfRouteAdapter.OnPointClickListener() {
                    @Override
                    public void onDeletePoint(final Point point) {

                        NetworkService.getInstance()
                                .getJSONApi()
                                .deletePoint(point.getId())
                                .enqueue(new Callback<PojoPoint>() {

                                    @Override
                                    public void onResponse(Call<PojoPoint> call, Response<PojoPoint> response) {

                                        getPoints(route.getId());

                                        Toast toast = Toast.makeText(getContext(),
                                                "Ваша точка удалена.",
                                                Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }

                                    @Override
                                    public void onFailure(Call<PojoPoint> call, Throwable t) {
                                        Log.d("123", "onFailure: "+t.getMessage());
                                        Toast toast = Toast.makeText(getContext(),
                                                t.getMessage(),
                                                Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }
                                });
                    }
                };


                // создаем и устанавливаем адаптер точек маршрута.
                pointsOfRouteAdapter = new PointsOfRouteAdapter(onPointClickListener, getContext());
                recyclerViewPoints.setAdapter(pointsOfRouteAdapter);


                //загружаем точки маршрута.
                getPoints(route.getId());


                //элементы диалогового окна.
                final Spinner interestSpinner = editRouteDialogWindow.findViewById(R.id.input_spinner_interest);
                final Spinner typeSpinner = editRouteDialogWindow.findViewById(R.id.input_spinner_type);
                final EditText inputNewTitle = editRouteDialogWindow.findViewById(R.id.input_title);
                final EditText inputNewDescription = editRouteDialogWindow.findViewById(R.id.input_description);


                //TODO: устанавливаем значения из маршрута в элементы диалогового окна (решить проблему вставки значения в спиннеры)
                inputNewTitle.setText(route.getTitle());
                inputNewDescription.setText(route.getDescription());

                Button buttonAddPoint = editRouteDialogWindow.findViewById(R.id.add_point_button_edit_route);
                final EditText inputPointName = editRouteDialogWindow.findViewById(R.id.input_point_name);
                final EditText inputPointXCoord = editRouteDialogWindow.findViewById(R.id.input_point_x_coord);
                final EditText inputPointYCoord = editRouteDialogWindow.findViewById(R.id.input_point_y_coord);

                buttonAddPoint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //получаем значения элементов из диалогового окна редактирования маршрута для создания новой точки.
                        String pointName = inputPointName.getText().toString();
                        String pointXCoord = inputPointXCoord.getText().toString();
                        String pointYCoord = inputPointYCoord.getText().toString();

                        if (!pointName.equals("") && !pointXCoord.equals("") && !pointYCoord.equals("")) {


                            //готовим данные для запроса.
                            PojoPoint newPoint = new PojoPoint();
                            newPoint.setTitle(pointName);
                            newPoint.setXCoord(pointXCoord);
                            newPoint.setYCoord(pointYCoord);


                            //делаем запрос
                            int routeid = (int) route.getId();

                            NetworkService.getInstance()
                                    .getJSONApi()
                                    .createNewPoint(routeid, newPoint)
                                    .enqueue(new Callback<PojoPoint>() {
                                        @Override
                                        public void onResponse(Call<PojoPoint> call, Response<PojoPoint> response) {

                                            //обновляем точки в маршруте.
                                            getPoints(route.getId());


                                            Toast toast = Toast.makeText(getContext(),
                                                    "Новая точка создана.",
                                                    Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                        }

                                        @Override
                                        public void onFailure(Call<PojoPoint> call, Throwable t) {
                                            Log.d("load pojoRoutes", "onFailure: " + t.getMessage());
                                        }
                                    });


                            //очищаем данные воода.
                            inputPointName.setText("");
                            inputPointXCoord.setText("");
                            inputPointYCoord.setText("");
                        } else {
                            Toast toast = Toast.makeText(getContext(),
                                    "Введите все данные точки маршрута.",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }
                });


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

                                        final int routeId = (int) route.getId();

                                        //получаем значения полей из диалогового окна редактирования маршрута.
                                        String newTitle = inputNewTitle.getText().toString();
                                        String newDescription = inputNewDescription.getText().toString();
                                        String inputNewInterest = interestSpinner.getSelectedItem().toString();
                                        String inputNewTypeDisplacement = typeSpinner.getSelectedItem().toString();


                                        //готовим данные для запроса.
                                        PojoRoute updateRoute = new PojoRoute();

                                        updateRoute.setTitle(newTitle);
                                        updateRoute.setDescription(newDescription);
                                        updateRoute.setInterest(inputNewInterest);
                                        updateRoute.setType(inputNewTypeDisplacement);


                                        //делаем запрос.
//                                        int routeId =(int) route.getId();
                                        NetworkService.getInstance()
                                                .getJSONApi()
                                                .updateRoute(routeId, updateRoute)
                                                .enqueue(new Callback<PojoRoute>() {
                                                    @Override
                                                    public void onResponse(Call<PojoRoute> call, Response<PojoRoute> response) {

                                                        getRoutes();

                                                        Toast toast = Toast.makeText(getContext(),
                                                                "Данные маршрута обновлены.",
                                                                Toast.LENGTH_SHORT);
                                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                                        toast.show();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<PojoRoute> call, Throwable t) {
                                                        Toast toast = Toast.makeText(getContext(),
                                                                t.getMessage(),
                                                                Toast.LENGTH_SHORT);
                                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                                        toast.show();
                                                    }
                                                });
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                //Создаем AlertDialog и отображаем его
                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.show();
            }

            @Override
            public void onDeleteClick(final Route route) {
                Log.d("onEditClicl", "onEditClick: " + route.getDescription());

                final int routeId = (int) route.getId();

                //Создаем AlertDialog
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getContext());

                //Настраиваем сообщение и кнопки в диалоговом окне:
                mDialogBuilder
                        .setMessage("Are you sure you want to delete the route?")
                        .setCancelable(false)
                        .setPositiveButton("Delete",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        PojoRoute deleteRoute = new PojoRoute();
                                        deleteRoute.setTitle(route.getTitle());
                                        NetworkService.getInstance()
                                                .getJSONApi()
                                                .deleteRoute(routeId, deleteRoute)
                                                .enqueue(new Callback<PojoRoute>() {
                                                    @Override
                                                    public void onResponse(Call<PojoRoute> call, Response<PojoRoute> response) {

                                                        getRoutes();

                                                        Toast toast = Toast.makeText(getContext(),
                                                                "Маршрут удален.",
                                                                Toast.LENGTH_SHORT);
                                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                                        toast.show();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<PojoRoute> call, Throwable t) {
                                                        Toast toast = Toast.makeText(getContext(),
                                                                t.getMessage(),
                                                                Toast.LENGTH_SHORT);
                                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                                        toast.show();
                                                    }
                                                });
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                //Создаем AlertDialog и отображаем его:
                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.show();
            }
        };


        // создаем и устанавливаем адаптер
        myRoutesAdapter = new MyRoutesAdapter(myRoutesListener, this);
        recyclerViewMyRoutes.setAdapter(myRoutesAdapter);

        //обновляем данные ресайкл вью при создании фрагмента.
        getRoutes();

        //возвращаем отображение из фрагмента.
        return root;
    }
}