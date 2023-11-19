package com.seintec.sirenaiotseintec.models;

public class Hour {
    private String hour;
    private String type;
    private int index;

    public Hour () {
        this.hour = "";
        this.type = "";
        this.index = 0;
    }
    public Hour(String hour, String type, int index) {
        this.hour = hour;
        this.type = type;
        this.index = index;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
