package com.seintec.sirenaiotseintec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnConectar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConectar = findViewById(R.id.btnConnect);

        btnConectar.setOnClickListener((v)->{
            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
            MainActivity.this.startActivity(intent);
            //MainActivity.this.finish();
        });
    }
}