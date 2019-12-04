package com.example.unknownexplorer.activitys;

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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unknownexplorer.R;
import com.example.unknownexplorer.db.DBHelper;

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
    private Button buttonCreatePoint;
    private Button buttonCreateRoute;
    private Button deletePoint;

    // бд
    DBHelper dbHelper;


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
        buttonCreatePoint = findViewById(R.id.button_add_point_route);
        buttonCreateRoute = findViewById(R.id.button_create_route);

        //устанавливаем листенры на кнопки
        buttonCreatePoint.setOnClickListener(this);
        buttonCreateRoute.setOnClickListener(this);

        //получаем данные из ActivityMainNavigation
        USER_ID = getIntent().getIntExtra("userId",-1);
        Log.d("ActivityCreateRoute", "onCreate: "+USER_ID);

        tablePoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d("ClickTableRow", "onClick: !!!");

                tablePoints.removeViewInLayout((ViewGroup)v.getParent());
            }
        });

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
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
                            public void onClick(DialogInterface dialog, int id) {
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
                                X.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                Y.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                deletePoint.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


                                //добавляем элементы в tableRow
                                tableRow.addView(title);
                                tableRow.addView(X);
                                tableRow.addView(Y);
                                tableRow.addView(deletePoint);

                                tablePoints.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


        // создаем объект для данных
        ContentValues routeContent = new ContentValues();

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        switch (v.getId()) {
            case R.id.button_create_route:
                Log.d("insertRoute", "--- Insert in mytable: ---");
                String routeTitle = editTextTitle.getText().toString();
                String routeDescription = editTextDescription.getText().toString();
                String interest = spinnerInterest.getSelectedItem().toString();
                String typeDisplacement = spinnerTypeOfDisplacement.getSelectedItem().toString();


                // подготовим данные для вставки в виде пар: наименование столбца - значение
                routeContent.put("userId", USER_ID);
                routeContent.put("title", routeTitle);
                routeContent.put("description", routeDescription);
                routeContent.put("interest", interest);
                routeContent.put("type", typeDisplacement);



                // вставляем запись и получаем ее ID
                long routeID = db.insert("routes", null, routeContent);
                Log.d("insertRoute", "row inserted, ID = " + routeID);

                for (int i = 0; i < tablePoints.getChildCount(); i++) {
                    ContentValues pointContent = new ContentValues();
                    pointContent.put("routeId", routeID);
                    long pointID = db.insert("points", null, pointContent);
                    Log.d("insertPoint", "row inserted, ID = " + pointID + " into route, ID = " + routeID);
                }

                //делаем вывод данных в логе
                Cursor c = db.query("routes", null, null, null, null, null, null);

                if (c.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex("id");
                    int idUserColIndex = c.getColumnIndex("userId");
                    int titleColIndex = c.getColumnIndex("title");
//                    int emailColIndex = c.getColumnIndex("email");

                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d("out_route",
                                "ID = " + c.getInt(idColIndex) +
                                        "userId = " + c.getInt(idUserColIndex) +
                                        ", name = " + c.getString(titleColIndex)
                                       );
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                } else
                    Log.d("out_route", "0 rows");
                c.close();


                break;
            case R.id.button_add_point_route:

                //Создаем AlertDialog:
                AlertDialog alertDialog = mDialogBuilder.create();
                //и отображаем его:
                alertDialog.show();
        }

    }

}
