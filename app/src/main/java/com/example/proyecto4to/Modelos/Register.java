package com.example.proyecto4to.Modelos;

import java.util.List;

public class Register {
    private final int status;
    private final String message;
    private  final String error;
    private final List<data> data;

    public Register(int status, String message, String error, List<com.example.proyecto4to.Modelos.data> data) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public List<com.example.proyecto4to.Modelos.data> getData() {
        return data;
    }
}
