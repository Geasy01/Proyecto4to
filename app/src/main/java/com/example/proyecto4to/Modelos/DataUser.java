package com.example.proyecto4to.Modelos;

public class DataUser {
    public final int id;
    public final String name;
    public final String ap_paterno;
    public final String ap_materno;
    public final String email;
    public final String email_verified_at;
    public final String phone_number;
    public final int rol_id;
    public final String active;
    public final String codigo;
    public final String adafruit_username;
    public final String io_key;
    public final String created_at;
    public final String updated_at;

    public DataUser(int id, String name, String ap_paterno, String ap_materno, String email, String email_verified_at, String phone_number, int rol_id, String active, String codigo, String adafruit_username, String io_key, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.ap_paterno = ap_paterno;
        this.ap_materno = ap_materno;
        this.email = email;
        this.email_verified_at = email_verified_at;
        this.phone_number = phone_number;
        this.rol_id = rol_id;
        this.active = active;
        this.codigo = codigo;
        this.adafruit_username = adafruit_username;
        this.io_key = io_key;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
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

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public int getRol_id() {
        return rol_id;
    }

    public String getActive() {
        return active;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getAdafruit_username() {
        return adafruit_username;
    }

    public String getIo_key() {
        return io_key;
    }
}
