package com.example.proyecto4to.Modelos;

public class Polvo {
    private final int status;
    private final String message;

    private String value;

    public Polvo(int status, String message, String value) {
        this.status = status;
        this.message = message;
        this.value = value;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getValue() {
        return value;
    }
}
