package com.example.proyecto4to.Modelos;

public class Bluetooth {
    private final String name;
    private final String address;

    public Bluetooth(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
}
