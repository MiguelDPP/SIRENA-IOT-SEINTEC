package com.seintec.sirenaiotseintec.models;

import java.io.Serializable;


public class Device implements Serializable {
    private String name;
    private String key;
    private String mac;

    private String scheduleCurrent;

    private static Device deviceLogin;

    public Device() {
    }

    public Device(String name, String key, String mac, String scheduleCurrent) {
        this.name = name;
        this.key = key;
        this.mac = mac;
        this.scheduleCurrent = scheduleCurrent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public static Device getUserLogin() {
        return Device.deviceLogin;
    }

    public static void setUserLogin(Device deviceLogin) {
        Device.deviceLogin = deviceLogin;
    }

    public String getScheduleCurrent() {
        return scheduleCurrent;
    }

    public void setScheduleCurrent(String scheduleCurrent) {
        this.scheduleCurrent = scheduleCurrent;
    }
}
