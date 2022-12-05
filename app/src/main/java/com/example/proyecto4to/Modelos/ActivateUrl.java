package com.example.proyecto4to.Modelos;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class ActivateUrl {
    private final int status;
    private final String message;

    @SerializedName("error")
    private final ArrayList<ErrorRegister> error;

    private final String url;

    public ActivateUrl(int status, String message, ArrayList<ErrorRegister> error, String url) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.url = url;
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

    public String getUrl() {
        return url;
    }
}
