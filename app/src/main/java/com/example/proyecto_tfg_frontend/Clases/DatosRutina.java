package com.example.proyecto_tfg_frontend.Clases;

public class DatosRutina {

    private String nombre;
    private String id;
    private String dificultad;
    private String prop;
    private int tiempo_desc;
    private boolean publica;

    public DatosRutina(String nombre, String id, String dificultad, int tiempo_desc, boolean publica) {
        this.nombre = nombre;
        this.id = id;
        this.dificultad = dificultad;
        this.tiempo_desc = tiempo_desc;
        this.publica = publica;
        this.prop = "";
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public String getDificultad() {
        return dificultad;
    }

    public int getTiempo_desc() {
        return tiempo_desc;
    }

    public boolean isPublica() {
        return publica;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }
}
