package com.example.proyecto_tfg_frontend;
import java.util.ArrayList;
import java.util.List;

public class UsuarioSingleton {

    private String id;
    private String username;
    private String mail;

    private static final UsuarioSingleton ourInstance = new UsuarioSingleton();

    public static UsuarioSingleton getInstance() {
        return ourInstance;
    }

    public boolean setId(String id) {
        this.id = id;
        return true;
    }
    public boolean setMail(String mail) {
        this.mail = mail;
        return true;
    }
    public boolean setUsername(String nom_usuari) {
        this.username = nom_usuari;
        return true;
    }

    public void user_LogOut() {
        id = null;
        username = null;
        mail = null;
    }

    public String getId() {return id;}
    public String getMail() {return mail;}
    public String getUsername() {return username;}
}