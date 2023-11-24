package com.seintec.sirenaiotseintec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.seintec.sirenaiotseintec.adapters.HoursAdapter;
import com.seintec.sirenaiotseintec.adapters.ScheduleAdapter;
import com.seintec.sirenaiotseintec.models.Device;
import com.seintec.sirenaiotseintec.models.Hour;
import com.seintec.sirenaiotseintec.models.Schedule;
import com.seintec.sirenaiotseintec.utils.Database;

import java.util.ArrayList;

public class EditScheduleActivity extends AppCompatActivity {
    Button btnChangeName;
    ImageButton btnSelectCurrent, btnAddSchedule;
    ArrayAdapter<String> arrayHourAdapter;
    EditText txtNameSchedule;

    RecyclerView viewHours;
    HoursAdapter hoursAdapter;
    ConstraintLayout progressView;

    ConstraintLayout viewNotFound;

    Schedule schedule;
    float y = 0;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);

        btnChangeName = findViewById(R.id.btnChangeName);
        btnSelectCurrent = findViewById(R.id.btnSelectCurrent);
        txtNameSchedule = findViewById(R.id.txtNameSchedule);
        viewNotFound = findViewById(R.id.viewNotFound);
        btnAddSchedule = findViewById(R.id.btnAddSchedule);

        schedule = (Schedule) getIntent().getSerializableExtra("schedule");

        progressView = findViewById(R.id.progressView);
        progressView.setOnClickListener(v -> {
        });
        showProgress();

        if (schedule.isActivated()) {
            btnSelectCurrent.setBackground(getDrawable(R.drawable.bg_primary));
            btnSelectCurrent.setColorFilter(getResources().getColor(R.color.white, null));
        }

        txtNameSchedule.setText(schedule.getName());

        //No permitir caracteres especiales como #, $, [, ], ., , etc
        txtNameSchedule.setFilters(new InputFilter[]{new InputFilter() {
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


        viewHours = findViewById(R.id.viewHours);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        viewHours.setLayoutManager(linearLayoutManager);

        loadHours();

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

        btnChangeName.setOnClickListener(v -> {
            if(txtNameSchedule.getText().toString().equals("")){
                Toast.makeText(this, "El nombre no puede estar vacio", Toast.LENGTH_SHORT).show();
                txtNameSchedule.requestFocus();
            } else if (!txtNameSchedule.getText().toString().trim().equals(schedule.getName())) {
                showProgress();
                Database.getInformationDatabase("devices/"+ Device.getUserLogin().getMac()+"/schedules/"+txtNameSchedule.getText().toString().trim(), Schedule.class)
                        .thenAccept(schedule -> {
                            hideProgress();
                            if (schedule != null) {
                                Toast.makeText(this, "El nombre ya existe", Toast.LENGTH_SHORT).show();
                                txtNameSchedule.requestFocus();
                            } else {
                                Database.deleteFromDatabase("devices/"+ Device.getUserLogin().getMac()+"/schedules/"+this.schedule.getName())
                                        .thenAccept(aVoid -> {
                                            this.schedule.setName(txtNameSchedule.getText().toString().trim());
                                            updateSchedule("Se actualizo correctamente", "No se pudo actualizar");
                                        });
                            }
                        });
            }
        });

        btnSelectCurrent.setOnClickListener(v -> {
            if (!schedule.isActivated()) {
                showProgress();
                Database.findOnDataBaseBool("devices/"+ Device.getUserLogin().getMac()+"/schedules", "activated", true, Schedule.class)
                        .thenAccept(schedule -> {
                            Schedule schedulePrevious = schedule.get(0);
                            schedulePrevious.setActivated(false);
                            Database.saveInformationDatabase("devices/"+ Device.getUserLogin().getMac()+"/schedules/"+schedulePrevious.getName(), false, "activated")
                                    .thenAccept(aVoid -> {
                                        Database.saveInformationDatabase("devices/"+ Device.getUserLogin().getMac(), this.schedule.getName(), "scheduleCurrent")
                                                .thenAccept(aVoid1 -> {
                                                    this.schedule.setActivated(true);
                                                    updateSchedule("Se actualizo correctamente", "No se pudo actualizar");

                                                    hideProgress();
                                                    btnSelectCurrent.setBackground(getDrawable(R.drawable.bg_primary));
                                                    btnSelectCurrent.setColorFilter(getResources().getColor(R.color.white, null));
                                                });
                                    });
                        });
            }
        });

    }
    public void showProgress() {
        progressView.setVisibility(ConstraintLayout.VISIBLE);
    }

    public void hideProgress() {
        progressView.setVisibility(ConstraintLayout.GONE);
    }
    Dialog dialog;
    private void showModal() {
        dialog = new Dialog(this);
        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_hour, null);

        dialog.setContentView(view);

        //Tamano del dialogo
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //Border radius
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.trasnparent_bg);
        dialog.show();

        //Spinner spinnerHour = dialog.findViewById(R.id.spinnerHour);
        Spinner spinnerName = dialog.findViewById(R.id.spinnerName);
        ImageButton btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);

        ArrayList<String> types = new ArrayList<>();
        types.add("Entrada");
        types.add("Salida");
        types.add("CambioHora");
        types.add("Descanso");

        arrayHourAdapter = new ArrayAdapter<>(this, R.layout.list_spiner, types);
        spinnerName.setAdapter(arrayHourAdapter);

        btnCloseDialog.setOnClickListener((v)->{
            dialog.dismiss();
        });

        btnAdd.setOnClickListener(v -> {
            addHourListener(v);
        });
    }

    public void updateSchedule(String Message, String MessageError) {
        Database.saveInformationDatabase("devices/"+ Device.getUserLogin().getMac()+"/schedules", schedule, schedule.getName())
                .thenAccept(aVoid -> {
                    Toast.makeText(this, "Se actualizo correctamente", Toast.LENGTH_SHORT).show();
                    loadHours();
                });
    }

    public void addHourListener(View v) {
        showProgress();
        String type = ((Spinner) v.getRootView().findViewById(R.id.spinnerName)).getSelectedItem().toString();
        TimePicker timePicker = v.getRootView().findViewById(R.id.timeSchedule);

        String hour = (timePicker.getHour() < 10 ? "0"+timePicker.getHour() : timePicker.getHour()) + ":" + (timePicker.getMinute() < 10 ? "0"+timePicker.getMinute() : timePicker.getMinute());

        if (type.equals("Entrada")) {
            schedule.getEntrada().add(hour);
        } else if (type.equals("Salida")) {
            schedule.getSalida().add(hour);
        } else if (type.equals("CambioHora")) {
            schedule.getCambioHora().add(hour);
        } else if (type.equals("Descanso")) {
            schedule.getDescanso().add(hour);
        }

        updateSchedule("Se agrego la hora correctamente", "No se pudo agregar la hora");
        dialog.dismiss();
    }

    private void loadHours() {
        Database.getInformationDatabase("devices/"+ Device.getUserLogin().getMac()+"/schedules/"+schedule.getName(), Schedule.class)
                .thenAccept(schedule -> {

                    ArrayList<Hour> horas = new ArrayList<>();
                    /*
                    horas = ordenarHoras(schedule.getEntrada(), horas, "Entrada");
                    horas = ordenarHoras(schedule.getSalida(), horas, "Salida");
                    horas = ordenarHoras(schedule.getCambioHora(), horas, "CambioHora");
                    horas = ordenarHoras(schedule.getDescanso(), horas, "Descanso");
                     */
                    for (int i = 0; i < schedule.getEntrada().size(); i++) {
                        horas.add(new Hour(schedule.getEntrada().get(i), "Entrada", i));
                    }
                    for (int i = 0; i < schedule.getSalida().size(); i++) {
                        horas.add(new Hour(schedule.getSalida().get(i), "Salida", i));
                    }
                    for (int i = 0; i < schedule.getCambioHora().size(); i++) {
                        horas.add(new Hour(schedule.getCambioHora().get(i), "CambioHora", i));
                    }
                    for (int i = 0; i < schedule.getDescanso().size(); i++) {
                        horas.add(new Hour(schedule.getDescanso().get(i), "Descanso", i));
                    }

                    horas.sort((o1, o2) -> {
                        if (o1.getHour().split(":")[0].equals(o2.getHour().split(":")[0])) {
                            return o1.getHour().split(":")[1].compareTo(o2.getHour().split(":")[1]);
                        } else {
                            return o1.getHour().split(":")[0].compareTo(o2.getHour().split(":")[0]);
                        }
                    });

                    this.schedule = schedule;

                    hoursAdapter = new HoursAdapter(horas);
                    hoursAdapter.setSchedule(this.schedule);
                    hoursAdapter.setProgressView(progressView);
                    hoursAdapter.notifyDataSetChanged();

                    viewHours.setAdapter(hoursAdapter);



                    if (horas.size() > 0) {
                        viewNotFound.setVisibility(ConstraintLayout.GONE);
                    } else {
                        viewNotFound.setVisibility(ConstraintLayout.VISIBLE);
                    }

                    hideProgress();
                });

    }

    public ArrayList<Hour> ordenarHoras(ArrayList<String> horas, ArrayList<Hour> horasActuales, String tipo) {
        ArrayList<Hour> horasOrdenadas = new ArrayList<>();
        if (horas.size() > 0){
            for (int i = 0; i < horas.size(); i++) {
                if (horasActuales.size() > 0) {
                    for (int j = 0; j < horasActuales.size(); j++) {
                        if (Integer.valueOf(horas.get(i).split(":")[0]) < Integer.valueOf(horasActuales.get(j).getHour().split(":")[0]) && Integer.valueOf(horas.get(i).split(":")[1]) < Integer.valueOf(horasActuales.get(j).getHour().split(":")[1])) {
                            horasOrdenadas.add(horasActuales.get(j));
                            break;
                        } else {
                            horasOrdenadas.add(new Hour(horas.get(i), tipo, i));

                        }
                    }
                } else {
                    horasOrdenadas.add(new Hour(horas.get(i), tipo, i));
                }
            }
            return horasOrdenadas;
        } else {
            return horasActuales;
        }

    }
}