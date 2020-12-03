package com.example.proyecto_tfg_frontend;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsuarioSingleton {

    private String id;
    private String username;
    private String mail;
    private String sexo;
    private double IMC;
    private int altura;
    private int peso_act;
    private int peso_des;
    private int peso_id;
    private int[] pesos;
    private Date[] fechas;
    private Date fecha_nacimiento;
    private Bitmap imagen;
    private String dato;
    private String url_img;



    private static final UsuarioSingleton ourInstance = new UsuarioSingleton();

    public UsuarioSingleton() {
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
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

    public double getIMC() {
        return IMC;
    }

    public void setIMC(double IMC) {
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

    public Bitmap getImagen() {
        return imagen;
    }

    public void setImagen(Bitmap imagen) {
        this.imagen = imagen;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public int getPeso_id() {
        return peso_id;
    }

    public void setPeso_id(int peso_id) {
        this.peso_id = peso_id;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }
}