package com.example.proyecto4to.Modelos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UpdateUser {
    private final int status;
    private final String message;

    @SerializedName("error")
    private final ArrayList<ErrorRegister> error;

    public UpdateUser(int status, String message, ArrayList<ErrorRegister> error) {
        this.status = status;
        this.message = message;
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<ErrorRegister> getError() {
        return error;
    }
}
