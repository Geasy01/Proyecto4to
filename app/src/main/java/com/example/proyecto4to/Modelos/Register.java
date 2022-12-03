package com.example.proyecto4to.Modelos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Register {
    private final int status;
    private final String message;

    @SerializedName("error")
    private final List<ErrorRegister> error;

    @SerializedName("data")
    private final List<DataUser> dataUser;

    public Register(int status, String message, List<ErrorRegister> error, List<DataUser> dataUser) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.dataUser = dataUser;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<ErrorRegister> getError() {
        return error;
    }

    public List<DataUser> getData() {
        return dataUser;
    }
}
