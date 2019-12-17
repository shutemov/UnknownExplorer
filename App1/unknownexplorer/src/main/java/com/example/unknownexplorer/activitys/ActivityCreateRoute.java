package com.example.unknownexplorer.activitys;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unknownexplorer.POJO.PojoPoint;
import com.example.unknownexplorer.POJO.PojoRoute;
import com.example.unknownexplorer.R;
import com.example.unknownexplorer.api.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("deprecation")
public class ActivityCreateRoute extends AppCompatActivity implements View.OnClickListener {

    final Context context = this;

    // данные пользователя
    int USER_ID;

    //  элементы для создания маршрута
    private EditText editTextTitle;
    private EditText editTextDescription;
    private Spinner spinnerInterest;
    private Spinner spinnerTypeOfDisplacement;
    private TableLayout tablePoints;
    private Button deletePoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_route);

        //находим id элементов
        editTextTitle = findViewById(R.id.edit_text_title_activity_create_route);
        editTextDescription = findViewById(R.id.edit_text_description_activity_create_route);
        spinnerInterest = findViewById(R.id.spinner_interest_activity_create_route);
        spinnerTypeOfDisplacement = findViewById(R.id.spinner_type_displacement_activity_create_route);
        tablePoints = findViewById(R.id.table_layout_points_activity_create_route);
        Button buttonCreatePoint = findViewById(R.id.button_add_point_route);
        Button buttonCreateRoute = findViewById(R.id.button_create_route);


        //устанавливаем листенры на кнопки
        buttonCreatePoint.setOnClickListener(this);
        buttonCreateRoute.setOnClickListener(this);


        //получаем данные из ActivityMainNavigation
        USER_ID = getIntent().getIntExtra("userId", -1);
        Log.d("ActivityCreateRoute", "onCreate: " + USER_ID);
    }


    @Override
    public void onClick(View v) {

        //Получаем вид с файла dialog_create_point_activity_create_route.xml, который применим для диалогового окна:
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptsView = layoutInflater.inflate(R.layout.dialog_create_point_activity_create_route, null);


        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);


        //Настраиваем dialog_create_point_activity_create_route.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);


        //Настраиваем отображение полей для ввода текста в открытом диалоге:
        final EditText inputTextPointTitle = promptsView.findViewById(R.id.edit_text_title_dialog_create_point);
        final EditText inputTextPointXCoord = promptsView.findViewById(R.id.edit_x_coord_dialog_create_point);
        final EditText inputTextPointYCoord = promptsView.findViewById(R.id.edit_y_coord_dialog_create_point);


        //Настраиваем сообщение в диалоговом окне:
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Create",
                        new DialogInterface.OnClickListener() {
                            @SuppressLint("ResourceType")
                            public void onClick(DialogInterface dialog, int id) {

                                String pointTitle = inputTextPointTitle.getText().toString();
                                String pointXCoord = inputTextPointXCoord.getText().toString();
                                String pointYCoord = inputTextPointYCoord.getText().toString();

                                if (!pointTitle.equals("") && !pointXCoord.equals("") && !pointYCoord.equals("")) {


                                    // Добавляем tableRow в TableLayout на основном экране
                                    TableRow tableRow = new TableRow(context);

                                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    tablePoints.setStretchAllColumns(true);


                                    //Создаем элементы tableRow
                                    TextView title = new TextView(context);
                                    TextView X = new TextView(context);
                                    TextView Y = new TextView(context);
                                    deletePoint = new Button(context);


                                    //Присваеваем значения из диалогового окна
                                    title.setText(inputTextPointTitle.getText());
                                    X.setText(inputTextPointXCoord.getText());
                                    Y.setText(inputTextPointYCoord.getText());
                                    deletePoint.setText("X");


                                    //задаем LayoutParams
                                    title.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    title.setId(4);
                                    X.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    X.setId(3);
                                    Y.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    Y.setId(2);
                                    deletePoint.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    deletePoint.setId(1);


                                    //добавляем элементы в tableRow
                                    tableRow.addView(title);
                                    tableRow.addView(X);
                                    tableRow.addView(Y);
                                    tableRow.addView(deletePoint);
                                    tablePoints.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));


                                    //удаление точки маршрута из таблицы
                                    deletePoint.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final View colTableRow = v.findViewById(1);
                                            tablePoints.removeView((View) colTableRow.getParent());
                                        }
                                    });
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Введите все данные точки маршрута.",
                                            Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


        switch (v.getId()) {
            //кнопка создания маршрута.
            case R.id.button_create_route:
                String routeTitle = editTextTitle.getText().toString();
                String routeDescription = editTextDescription.getText().toString();
                String routeInterest = spinnerInterest.getSelectedItem().toString();
                String routeTypeDisplacement = spinnerTypeOfDisplacement.getSelectedItem().toString();

                if (!routeTitle.equals("") && !routeDescription.equals("") && !routeInterest.equals("") && !routeTypeDisplacement.equals("")) {

                    //формируем данные для запроса.
                    PojoRoute createRoute = new PojoRoute();
                    createRoute.setTitle(routeTitle);
                    createRoute.setDescription(routeDescription);
                    createRoute.setInterest(routeInterest);
                    createRoute.setType(routeTypeDisplacement);

                    //делаем запрос.
                    NetworkService.getInstance()
                            .getJSONApi()
                            .createNewRoute(USER_ID, createRoute)
                            .enqueue(new Callback<PojoRoute>() {
                                @Override
                                public void onResponse(Call<PojoRoute> call, Response<PojoRoute> response) {

                                    if (tablePoints.getChildCount() != 0) {

                                        //для каждой точки запускаем запрос.
                                        for (int i = 0; i < tablePoints.getChildCount(); i++) {

                                            @SuppressLint("ResourceType") TextView titleColInTablePoints = tablePoints.getChildAt(i).findViewById(4);
                                            @SuppressLint("ResourceType") TextView xCoordColInTablePoints = tablePoints.getChildAt(i).findViewById(3);
                                            @SuppressLint("ResourceType") TextView yCoordColInTablePoints = tablePoints.getChildAt(i).findViewById(2);


                                            PojoPoint createPoint = new PojoPoint();

                                            createPoint.setTitle(titleColInTablePoints.getText().toString());
                                            createPoint.setXCoord(xCoordColInTablePoints.getText().toString());
                                            createPoint.setYCoord(yCoordColInTablePoints.getText().toString());

                                            final int finalI = i;
                                            NetworkService.getInstance()
                                                    .getJSONApi()
                                                    .createNewPoint(response.body().getId(), createPoint)
                                                    .enqueue(new Callback<PojoPoint>() {

                                                        @Override
                                                        public void onResponse(Call<PojoPoint> call, Response<PojoPoint> response) {
                                                            //если последний запрос выполнился, то маршрут создан.
                                                            if (finalI == (tablePoints.getChildCount() - 1) && call.isExecuted()) {

                                                                Toast toast = Toast.makeText(getApplicationContext(),
                                                                        "Марщрут создан.",
                                                                        Toast.LENGTH_SHORT);
                                                                toast.setGravity(Gravity.CENTER, 0, 0);
                                                                toast.show();
                                                                finish();
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<PojoPoint> call, Throwable t) {
                                                            Toast toast = Toast.makeText(getApplicationContext(),
                                                                    t.getMessage(),
                                                                    Toast.LENGTH_SHORT);
                                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                                            toast.show();
                                                            finish();
                                                        }
                                                    });
                                        }
                                    } else {
                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                "Введите все данные и не менее 1 точки маршрута.",
                                                Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<PojoRoute> call, Throwable t) {
                                    Toast toast = Toast.makeText(getApplicationContext(),
                                            "Проблемы соединения с сервером",
                                            Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            });
                } else {
                    //создаём и отображаем текстовое уведомление
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Введите все данные и не менее 1 точки маршрута.",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
            case R.id.button_add_point_route:

                //Создаем и отображаем AlertDialog:
                AlertDialog alertDialog = mDialogBuilder.create();
                alertDialog.show();
                break;
        }
    }
}
