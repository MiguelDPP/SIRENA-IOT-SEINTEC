package com.seintec.sirenaiotseintec.models;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Schedule implements Serializable {
    private String name;
    private boolean isActivated;
    private ArrayList<String> CambioHora;
    private ArrayList<String> Descanso;
    private ArrayList<String> Entrada;
    private ArrayList<String> Salida;

    private boolean isPredetermined;

    public Schedule() {
        this.name = "";
        this.isActivated = false;
        this.CambioHora = new ArrayList<>();
        this.Descanso = new ArrayList<>();
        this.Entrada = new ArrayList<>();
        this.Salida = new ArrayList<>();
    }

    public Schedule(String name, boolean isActivated) {
        this.name = name;
        this.isActivated = isActivated;
        this.CambioHora = new ArrayList<>();
        this.Descanso = new ArrayList<>();
        this.Entrada = new ArrayList<>();
        this.Salida = new ArrayList<>();
    }

    public Schedule(String name) {
        this.name = name;
        this.isActivated = false;
        this.CambioHora = new ArrayList<>();
        this.Descanso = new ArrayList<>();
        this.Entrada = new ArrayList<>();
        this.Salida = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActivated() {
        return this.isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public ArrayList<String> getCambioHora() {
        return CambioHora;
    }

    public void setCambioHora(ArrayList<String> cambioHora) {
        CambioHora = cambioHora;
    }

    public ArrayList<String> getDescanso() {
        return Descanso;
    }

    public void setDescanso(ArrayList<String> descanso) {
        Descanso = descanso;
    }

    public ArrayList<String> getEntrada() {
        return Entrada;
    }

    public void setEntrada(ArrayList<String> entrada) {
        Entrada = entrada;
    }

    public ArrayList<String> getSalida() {
        return Salida;
    }

    public void setSalida(ArrayList<String> salida) {
        Salida = salida;
    }

    public boolean isPredetermined() {
        return isPredetermined;
    }

    public void setPredetermined(boolean predetermined) {
        isPredetermined = predetermined;
    }
}
