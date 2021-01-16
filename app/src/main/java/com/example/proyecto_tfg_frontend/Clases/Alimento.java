package com.example.proyecto_tfg_frontend.Clases;

public class Alimento {

    private String id, contenido_plato, url_img, nombre_plato_ing;
    private Double kcal, prot, gras, carbo, fibra;

    public Alimento(String id, String contenido_plato, String url_img, String nombre_plato_ing, Double kcal, Double prot, Double gras, Double carbo, Double fibra) {
        this.id = id;
        this.contenido_plato = contenido_plato;
        this.url_img = url_img;
        this.nombre_plato_ing = nombre_plato_ing;
        this.kcal = kcal;
        this.prot = prot;
        this.gras = gras;
        this.carbo = carbo;
        this.fibra = fibra;
    }

    public String getId() {
        return id;
    }

    public String getContenido_plato() {
        return contenido_plato;
    }

    public String getUrl_img() {
        return url_img;
    }

    public String getNombre_plato_ing() {
        return nombre_plato_ing;
    }

    public Double getKcal() {
        return kcal;
    }

    public Double getProt() {
        return prot;
    }

    public Double getGras() {
        return gras;
    }

    public Double getCarbo() {
        return carbo;
    }

    public Double getFibra() {
        return fibra;
    }
}
