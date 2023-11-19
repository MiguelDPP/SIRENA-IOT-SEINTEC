package com.seintec.sirenaiotseintec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.seintec.sirenaiotseintec.models.Schedule;

public class EditScheduleActivity extends AppCompatActivity {
    Button btnChangeName;
    ImageButton btnSelectCurrent;
    EditText txtNameSchedule;

    Schedule schedule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);

        btnChangeName = findViewById(R.id.btnChangeName);
        btnSelectCurrent = findViewById(R.id.btnSelectCurrent);
        txtNameSchedule = findViewById(R.id.txtNameSchedule);

        schedule = (Schedule) getIntent().getSerializableExtra("schedule");

        txtNameSchedule.setText(schedule.getName());

    }
}