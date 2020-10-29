package com.example.proyecto_tfg_frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Ejercicios extends AppCompatActivity implements Interfaz{

    private ArrayList<String> listDatosEjercicio;
    private RecyclerView recycler;
    private AdaptadorDatosEjercicios adaptador;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filtrar_ejercicios, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filtrar:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicios);

        recycler = (RecyclerView) findViewById(R.id.lista_ejercicios);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        listDatosEjercicio = new ArrayList<String>();
        Connection con = new Connection(this);
        con.execute("http://169.254.145.10:3000/ejercicio", "GET", null);
    }

    @Override
    public void Respuesta(JSONObject datos) {
        try {
            if(datos.getInt("codigo") == 200) {
                JSONArray nombres = datos.getJSONArray("array");
                for (int i = 0; i < nombres.length(); i++) {
                    listDatosEjercicio.add(i,nombres.getString(i));
                }

                //Añadir todos los elementos consultados
                adaptador = new AdaptadorDatosEjercicios(listDatosEjercicio);
                recycler.setAdapter(adaptador);
            }
            else {
                System.out.println("ERROR");
            }
        } catch (JSONException err) {
            err.printStackTrace();
        }

    }
}