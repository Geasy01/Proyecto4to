package com.example.proyecto4to.Modelos;

public class Bluetooth {
    private final String name;
    private final String address;
    private final String status;

    public Bluetooth(String name, String address, String status) {
        this.name = name;
        this.address = address;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getStatus() {
        return status;
    }
}
