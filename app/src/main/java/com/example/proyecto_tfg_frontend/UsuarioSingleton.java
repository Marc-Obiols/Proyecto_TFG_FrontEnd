package com.example.proyecto_tfg_frontend;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsuarioSingleton {

    private String id;
    private String username;
    private String mail;
    private String sexo;
    private int IMC;
    private int altura;
    private int peso_act;
    private int peso_des;
    private int[] pesos;
    private Date[] fechas;

    private static final UsuarioSingleton ourInstance = new UsuarioSingleton();

    public UsuarioSingleton() {
    }

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

    public int getPeso_des() {
        return peso_des;
    }

    public void setPeso_des(int peso_des) {
        this.peso_des = peso_des;
    }

    public int getPeso_act() {
        return peso_act;
    }

    public void setPeso_act(int peso_act) {
        this.peso_act = peso_act;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getIMC() {
        return IMC;
    }

    public void setIMC(int IMC) {
        this.IMC = IMC;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int[] getPesos() {
        return pesos;
    }

    public void setPesos(int[] pesos) {
        this.pesos = pesos;
    }

    public Date[] getFechas() {
        return fechas;
    }

    public void setFechas(Date[] fechas) {
        this.fechas = fechas;
    }
}