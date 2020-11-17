package com.example.proyecto_tfg_frontend;

public class DatosRutina {

    private String nombre, id;
    private int tiempo_desc;

    public DatosRutina(String nombre, String id, int tiempo_desc) {
        this.nombre = nombre;
        this.id = id;
        this.tiempo_desc = tiempo_desc;
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public int getTiempo_desc() {
        return tiempo_desc;
    }
}
