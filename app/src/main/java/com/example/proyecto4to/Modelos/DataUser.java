package com.example.proyecto4to.Modelos;

public class DataUser {
    public final String name;
    public final String email;
    public final String password;
    public final String ap_paterno;
    public final String ap_materno;
    public final String phone_number;
    public final String updated_at;
    public final String created_at;
    public final int id;

    public DataUser(String name, String email, String password, String ap_paterno, String ap_materno, String phone_number, String updated_at, String created_at, int id) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.ap_paterno = ap_paterno;
        this.ap_materno = ap_materno;
        this.phone_number = phone_number;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.id = id;
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

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getId() {
        return id;
    }
}
