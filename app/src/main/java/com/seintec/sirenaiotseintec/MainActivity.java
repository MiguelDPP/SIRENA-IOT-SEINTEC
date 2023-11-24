package com.seintec.sirenaiotseintec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.seintec.sirenaiotseintec.models.Device;
import com.seintec.sirenaiotseintec.utils.Database;

public class MainActivity extends AppCompatActivity {

    Button btnConectar, btnRegistrar;
    EditText txtName, txtKey;

    ConstraintLayout progressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConectar = findViewById(R.id.btnConnect);
        btnRegistrar = findViewById(R.id.btnRegister);
        txtName = findViewById(R.id.txtName);
        txtKey = findViewById(R.id.txtKey);
        progressView = findViewById(R.id.progressView);
        progressView.setOnClickListener((v)->{}); //Para que no se pueda interactuar con el progressView

        btnRegistrar.setOnClickListener((v)->{
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            MainActivity.this.startActivity(intent);
            //MainActivity.this.finish();
        });

        btnConectar.setOnClickListener((v)->{
            if (checkFields()) {
                showProgress();
                Database.findOnDataBase("devices", "name", txtName.getText().toString(), Device.class)
                        .thenAccept(result -> {
                            if (result.size() > 0) {
                                if (result.get(0).getKey().equals(txtKey.getText().toString())) {
                                    Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                                    Device.setUserLogin(result.get(0));
                                    MainActivity.this.startActivity(intent);
                                    MainActivity.this.finish();
                                } else {
                                    Toast.makeText(this, "La llave es incorrecta", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "El nombre no existe", Toast.LENGTH_SHORT).show();
                            }

                            hideProgress();
                        })
                        .exceptionally(throwable -> {
                            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage(throwable.getMessage());
                            builder.show();
                            hideProgress();
                            return null;
                        });
            }
        });
    }

    public boolean checkFields() {
        if (txtName.getText().toString().isEmpty()) {
            txtName.setError("El nombre es requerido");
            return false;
        } else if (txtKey.getText().toString().isEmpty()) {
            txtKey.setError("La llave es requerida");
            return false;
        }
        return true;
    }

    public void showProgress() {
        progressView.setVisibility(ConstraintLayout.VISIBLE);
    }

    public void hideProgress() {
        progressView.setVisibility(ConstraintLayout.GONE);
    }
}