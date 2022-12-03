package com.example.proyecto4to.Modelos;

public class ErrorRegister {
    public final String name;
    public final String email;
    public final String password;
    public final String ap_paterno;
    public final String ap_materno;
    public final String phone_number;

    public ErrorRegister(String name, String email, String password, String ap_paterno, String ap_materno, String phone_number) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.ap_paterno = ap_paterno;
        this.ap_materno = ap_materno;
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAp_paterno() {
        return ap_paterno;
    }

    public String getAp_materno() {
        return ap_materno;
    }

    public String getPhone_number() {
        return phone_number;
    }
}
