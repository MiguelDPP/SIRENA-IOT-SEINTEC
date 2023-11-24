package com.seintec.sirenaiotseintec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seintec.sirenaiotseintec.adapters.ScheduleAdapter;
import com.seintec.sirenaiotseintec.models.Device;
import com.seintec.sirenaiotseintec.models.Schedule;
import com.seintec.sirenaiotseintec.utils.Database;

import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {
    RecyclerView viewSchedule;
    ScheduleAdapter scheduleAdapter;
    ImageButton btnAddSchedule;
    ImageButton btnOptions;
    TextView lbSchedules;
    ConstraintLayout progressView;

    float y = 0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        btnAddSchedule = findViewById(R.id.btnAddSchedule);
        lbSchedules = findViewById(R.id.lbSchedules);
        progressView = findViewById(R.id.progressView);
        progressView.setOnClickListener((v)->{}); //Para que no se pueda interactuar con el progressView

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
                    showModal();
                }
                y = btnAddSchedule.getY();

            }
            return true;
        });



        viewSchedule = findViewById(R.id.viewSchedules);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        viewSchedule.setLayoutManager(linearLayoutManager);
        scheduleAdapter = new ScheduleAdapter();

        loadSchedules();



        btnOptions = findViewById(R.id.btnOptions);

        btnOptions.setOnClickListener((v)->{
            Intent intent = new Intent(ScheduleActivity.this, BluetoothActivity.class);
            ScheduleActivity.this.startActivity(intent);

        });

    }

    private void showModal() {
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
        EditText txtName = view.findViewById(R.id.txtName);
        txtName.requestFocus();
        //Abrir el teclado
        txtName.postDelayed(() -> {
            txtName.dispatchTouchEvent(MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
            txtName.dispatchTouchEvent(MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
        }, 200);

        txtName.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                                       int dend) {
                if (source.equals("")) { // for backspace
                    return source;
                }
                if (source.toString().matches("[a-zA-Z0-9 ]+")) {
                    return source;
                }
                return "";
            }
        }});
        Button btnSave = view.findViewById(R.id.btnAdd);
        CheckBox isActivated = view.findViewById(R.id.checkBoxIsActivated);
        btnClose.setOnClickListener((v1)->{
            dialog.dismiss();
        });

        btnSave.setOnClickListener((v -> {
            if (txtName.getText().toString().isEmpty()) {
                txtName.setError("El nombre es requerido");
            } else {
                showProgress();
                Database.referenceContains("devices/" + Device.getUserLogin().getMac() + "/schedules/" + txtName.getText().toString().trim())
                        .thenAccept(result -> {
                            if (result) {
                                Toast.makeText(this, "El nombre ya existe", Toast.LENGTH_SHORT).show();
                                txtName.setError("El nombre ya existe");
                                hideProgress();
                            } else {
                                Schedule schedule = new Schedule(txtName.getText().toString().trim(), isActivated.isChecked());
                                if (isActivated.isChecked()) {
                                    Database.findOnDataBaseBool("devices/" + Device.getUserLogin().getMac() + "/schedules", "activated", true, Schedule.class)
                                            .thenAccept(resultSchedule -> {
                                                if (resultSchedule.size() > 0) {
                                                    Schedule scheduletemp = resultSchedule.get(0);
                                                    scheduletemp.setActivated(false);
                                                    Database.saveInformationDatabase("devices/" + Device.getUserLogin().getMac() + "/schedules", scheduletemp, scheduletemp.getName())
                                                            .thenAccept(resultSave -> {
                                                                if (resultSave) {
                                                                    Database.saveInformationDatabase("devices/" + Device.getUserLogin().getMac(), schedule.getName(),"scheduleCurrent")
                                                                        .thenAccept(resultSave2 -> {
                                                                            if (resultSave2) {
                                                                                addSchedule(schedule, "devices/" + Device.getUserLogin().getMac() + "/schedules");
                                                                                dialog.dismiss();

                                                                            } else {
                                                                                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                            hideProgress();
                                                                        });
                                                                } else {
                                                                    hideProgress();
                                                                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                } else {

                                                    hideProgress();
                                                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    addSchedule(schedule, "devices/" + Device.getUserLogin().getMac() + "/schedules");
                                    dialog.dismiss();
                                }
                            }
                        });
            }
        }));
    }

    public void loadSchedules() {
        showProgress();
        Database.getInformationDatabaseList("devices/" + Device.getUserLogin().getMac() + "/schedules", Schedule.class)
                .thenAccept(result -> {
                    //Ordenar para que salga primero el predeterminado
                    lbSchedules.setText(result.size() + (result.size() == 1 ? " Horario" : " Horarios"));
                    ArrayList<Schedule> main = new ArrayList<>();
                    ArrayList<Schedule> temp = new ArrayList<>();
                    for (Schedule schedule : result) {
                        if (schedule.isPredetermined() || schedule.isActivated()) {
                            main.add(schedule);
                        } else {
                            temp.add(schedule);
                        }
                    }
                    main.sort((o1, o2) -> {
                        if (o1.isActivated()) {
                            return -1;
                        } else if (o2.isActivated()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    });
                    main.addAll(temp);
                    scheduleAdapter.setLocalDataSet(main);
                    scheduleAdapter.setProgressView(progressView);
                    viewSchedule.setAdapter(scheduleAdapter);

                    hideProgress();
                })
                .exceptionally(throwable -> {
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    hideProgress();
                    return null;
                });
    }

    public void addSchedule(Schedule schedule, String reference) {
        Database.saveInformationDatabase(reference, schedule, schedule.getName())
                .thenAccept(result -> {
                    if (result) {
                        Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
                        loadSchedules();
                    } else {
                        Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSchedules();
    }

    public void showProgress() {
        progressView.setVisibility(ConstraintLayout.VISIBLE);
    }

    public void hideProgress() {
        progressView.setVisibility(ConstraintLayout.GONE);
    }
}