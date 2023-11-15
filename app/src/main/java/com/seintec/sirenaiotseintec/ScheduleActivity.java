package com.seintec.sirenaiotseintec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.seintec.sirenaiotseintec.adapters.ScheduleAdapter;
import com.seintec.sirenaiotseintec.models.Schedule;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {
    LinearLayout viewSelectedSchedule;
    RecyclerView viewSchedule;
    ScheduleAdapter scheduleAdapter;
    ImageButton btnAddSchedule;
    ImageButton btnOptions;

    float y = 0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        viewSelectedSchedule = findViewById(R.id.viewSelectedSchedule);
        viewSelectedSchedule.setOnClickListener((v)->{

        });

        btnAddSchedule = findViewById(R.id.btnAddSchedule);

        //y = btnAddSchedule.getY();

        //Evento para desplazar el boton verticalmente
        btnAddSchedule.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_MOVE){
                //Rectificar que no se pase de los limites

                //Alto de la pantalla
                int height = getResources().getDisplayMetrics().heightPixels - btnAddSchedule.getHeight();

                if(event.getRawY() > height){
                    btnAddSchedule.setY(height);
                }else if(event.getRawY() < 100){
                    btnAddSchedule.setY(100);
                } else {
                    btnAddSchedule.setY(event.getRawY() - btnAddSchedule.getHeight());
                }


            }
            //Si da solo clic
            else if(event.getAction() == MotionEvent.ACTION_UP){
                if (btnAddSchedule.getY() == y || (y == 0 && btnAddSchedule.getY() == event.getRawY())){
                    Dialog dialog = new Dialog(this);
                    LayoutInflater inflater = getLayoutInflater();

                    View view = inflater.inflate(R.layout.dialog_schedule, null);

                    dialog.setContentView(view);

                    //Tamano del dialogo
                    dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    //Border radius
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.trasnparent_bg);
                    dialog.show();

                    ImageButton btnClose = view.findViewById(R.id.btnCloseDialog);
                    btnClose.setOnClickListener((v1)->{
                        dialog.dismiss();
                    });
                }
                y = btnAddSchedule.getY();

            }
            return true;
        });



        viewSchedule = findViewById(R.id.viewSchedules);


        ArrayList<Schedule> schedules = new ArrayList<>();
        schedules.add(new Schedule("Entrada", "10:00 am"));
        schedules.add(new Schedule("Descanso", "10:30 am"));
        schedules.add(new Schedule("Campeonato", "11:00 am"));
        schedules.add(new Schedule("Salida", "12:00 am"));



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        viewSchedule.setLayoutManager(linearLayoutManager);
        scheduleAdapter = new ScheduleAdapter(schedules);
        viewSchedule.setAdapter(scheduleAdapter);


        btnOptions = findViewById(R.id.btnOptions);

        btnOptions.setOnClickListener((v)->{
            Intent intent = new Intent(ScheduleActivity.this, BluetoothActivity.class);
            ScheduleActivity.this.startActivity(intent);

        });

    }

    private void showModal() {

    }
}