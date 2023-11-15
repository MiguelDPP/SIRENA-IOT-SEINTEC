package com.seintec.sirenaiotseintec.models;

import android.graphics.drawable.Drawable;

import java.util.UUID;

public class Schedule {
    private String id;
    private String name;
    private String hour;


    //Hacer para que el metodo imageLeft no se guarde en la base de datos
    private Drawable imageLeft;


    public Schedule(String name, String hour) {
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
        this.name = name;
        this.hour = hour;
    }

    public Schedule(String id, String name, String hour) {
        this.id = id;
        this.name = name;
        this.hour = hour;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
