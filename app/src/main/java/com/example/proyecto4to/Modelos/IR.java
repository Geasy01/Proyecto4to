package com.example.proyecto4to.Modelos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class IR {
    private final int status;
    private final String message;

    @SerializedName("data")
    private final ArrayList<IRData> ListIRData;

    public IR(int status, String message, ArrayList<IRData> listIRData) {
        this.status = status;
        this.message = message;
        ListIRData = listIRData;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<IRData> getListIRData() {
        return ListIRData;
    }
}
