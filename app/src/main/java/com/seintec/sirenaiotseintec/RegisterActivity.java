package com.seintec.sirenaiotseintec;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.seintec.sirenaiotseintec.models.Device;
import com.seintec.sirenaiotseintec.models.Schedule;
import com.seintec.sirenaiotseintec.utils.Database;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister;
    EditText txtName, txtKey, txtMac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.btnRegister);
        txtName = findViewById(R.id.txtName);
        txtKey = findViewById(R.id.txtKey);
        txtMac = findViewById(R.id.txtMac);

        btnRegister.setOnClickListener((v)-> {
            if (checkFields()) {
                Database.referenceContains("devices/"+txtMac.getText().toString())
                        .thenAccept(result -> {
                            if (result) {
                                Toast.makeText(this, "Ya existe un dispositivo con esa MAC", Toast.LENGTH_SHORT).show();
                            } else {
                                Database.findOnDataBase("/devices", "name", txtName.getText().toString(), Device.class)
                                        .thenAccept(result2 -> {
                                            if (result2.size() > 0) {
                                                Toast.makeText(RegisterActivity.this, "El nombre ya existe", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Device device = new Device(txtName.getText().toString(), txtKey.getText().toString(), txtMac.getText().toString().toUpperCase(), "Predeterminado");
                                                Database.saveInformationDatabase("devices", device, txtMac.getText().toString())
                                                        .thenAccept(resultSave -> {
                                                            if (resultSave) {
                                                                txtName.setText("");
                                                                txtKey.setText("");
                                                                txtMac.setText("");
                                                                txtName.requestFocus();
                                                                Schedule schedule = new Schedule("Predeterminado", true);
                                                                schedule.setPredetermined(true);
                                                                Database.saveInformationDatabase("devices/" + device.getMac() + "/schedules", schedule, schedule.getName().toString())
                                                                        .thenAccept(resultSaveSchedule -> {
                                                                            if (resultSaveSchedule) {
                                                                                Toast.makeText(RegisterActivity.this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
                                                                            } else {
                                                                                Toast.makeText(RegisterActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                            finish();
                                                                        });

                                                            } else {
                                                                Toast.makeText(RegisterActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });


                                            }
                                        });
                            }
                        });
            }

        });
    }

    public boolean checkFields () {
        if (txtName.getText().toString().isEmpty()) {
            txtName.setError("El nombre es requerido");
            return false;
        }
        if (txtKey.getText().toString().isEmpty()) {
            txtKey.setError("La clave es requerida");
            return false;
        }

        return checkMac();
    }

    public boolean checkMac () {
        if (txtMac.getText().toString().isEmpty()) {
            txtMac.setError("La MAC es requerida");
            return false;
        } else if (txtMac.getText().toString().length() != 17) {
            txtMac.setError("La MAC debe tener 17 caracteres");
            return false;
        } else if (!txtMac.getText().toString().contains(":")) {
            txtMac.setError("La MAC debe tener el formato AA:BB:CC:DD:EE:FF");
            return false;
        } else if (txtMac.getText().toString().split(":").length != 6) {
            txtMac.setError("La MAC debe tener el formato AA:BB:CC:DD:EE:FF");
            return false;
        } else {
            String[] mac = txtMac.getText().toString().split(":");
            for (String m : mac) {
                if (m.length() != 2) {
                    txtMac.setError("La MAC debe tener el formato AA:BB:CC:DD:EE:FF");
                    return false;
                } else if (!m.matches("[0-9A-Fa-f]+")) {
                    txtMac.setError("La MAC debe tener el formato AA:BB:CC:DD:EE:FF");
                    return false;
                }
            }
        }
        return true;
    }
}