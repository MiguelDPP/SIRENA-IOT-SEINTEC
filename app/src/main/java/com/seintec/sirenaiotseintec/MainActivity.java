package com.seintec.sirenaiotseintec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Codigo para schedule
    LinearLayout filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        filter = findViewById(R.id.viewSelectedSchedule);

        //Cambiar el color mientras tenga seleccionado un filtro
        filter.setOnClickListener(v -> {
            Toast.makeText(this, "Filtro", Toast.LENGTH_SHORT).show();
        });
    }
}