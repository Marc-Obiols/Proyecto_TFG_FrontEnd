package com.example.proyecto_tfg_frontend;

import java.util.ArrayList;

public class DatosDia {

    ArrayList<String> desayuno, comida, cena, merienda;

    public DatosDia(ArrayList<String> desayuno, ArrayList<String> comida, ArrayList<String> cena, ArrayList<String> merienda) {
        this.desayuno = desayuno;
        this.comida = comida;
        this.cena = cena;
        this.merienda = merienda;
    }

    public ArrayList<String> getDesayuno() {
        return desayuno;
    }

    public ArrayList<String> getComida() {
        return comida;
    }

    public ArrayList<String> getCena() {
        return cena;
    }

    public ArrayList<String> getMerienda() {
        return merienda;
    }
}
