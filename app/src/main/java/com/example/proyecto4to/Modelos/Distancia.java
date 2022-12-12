package com.example.proyecto4to.Modelos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Distancia {

    private final int status;
    private final String message;

    @SerializedName("data")
    private final ArrayList<DistanciaData> ListDistanciaData;

    public Distancia(int status, String message, ArrayList<DistanciaData> listDistanciaData) {
        this.status = status;
        this.message = message;
        ListDistanciaData = listDistanciaData;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }


    public ArrayList<DistanciaData> getListDistanciaData() {
        return ListDistanciaData;
    }
}