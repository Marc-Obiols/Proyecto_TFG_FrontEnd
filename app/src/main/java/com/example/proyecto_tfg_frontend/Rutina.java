package com.example.proyecto_tfg_frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Rutina extends AppCompatActivity implements Interfaz {

    private int llamada;
    private String nombre_ejercicio_auxiliar;
    private String id_rutina;

    private RecyclerView recycler, recyclerEjercicios;
    private AdaptadorDatosEjercicioRutina adaptador;
    private AdaptadorDatosEjercicios adaptadorEjercicios;
    private CircleImageView añadir_ejercicio;
    private ArrayList<Pair<String,Integer>> listDatosEjercicio;
    private ArrayList<String> listDatosEjercicioSeleccionar;
    private Dialog pantalla_añadir_ejercicio;
    private TextView tiempo, Kcal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina);
        System.out.println(getIntent().getStringExtra("rutina"));
        id_rutina = getIntent().getStringExtra("rutina");

        tiempo = (TextView) findViewById(R.id.tiempo);
        Kcal = (TextView) findViewById(R.id.Kcal);
        pantalla_añadir_ejercicio = new Dialog(this);

        recycler = (RecyclerView) findViewById(R.id.lista_ejercicios_rutina);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        añadir_ejercicio = (CircleImageView) findViewById(R.id.añadir_ejercicio_rutina);
        añadir_ejercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button add;
                EditText tiempo_deporte;
                pantalla_añadir_ejercicio.setContentView(R.layout.popup_seleccionar_ejercicio);
                add = (Button) pantalla_añadir_ejercicio.findViewById(R.id.add);
                tiempo_deporte = (EditText) pantalla_añadir_ejercicio.findViewById(R.id.tiempo_deporte);
                recyclerEjercicios = (RecyclerView) pantalla_añadir_ejercicio.findViewById(R.id.lista_ejercicios);
                recyclerEjercicios.setLayoutManager(new LinearLayoutManager(Rutina.this, LinearLayoutManager.VERTICAL,false));
                llamada = 2;
                Connection con = new Connection(Rutina.this);
                con.execute("http://169.254.145.10:3000/ejercicio", "GET", null);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (nombre_ejercicio_auxiliar == null) {
                            Toast.makeText(Rutina.this, "Selecciona un ejercicio", Toast.LENGTH_LONG).show();
                        }
                        else if (tiempo_deporte.getText().toString().equals("") || tiempo_deporte.getText().toString().equals("0")) {
                            Toast.makeText(Rutina.this, "Rellene correctamente el tiempo de ejecución", Toast.LENGTH_LONG).show();
                        }
                        else {
                            JSONObject req = new JSONObject();
                            try {
                                req.put("nombre_ejercicio", nombre_ejercicio_auxiliar);
                                req.put("tiempo_ejercicio", Integer.parseInt(tiempo_deporte.getText().toString()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            llamada = 3;
                            Connection con = new Connection(Rutina.this);
                            con.execute("http://169.254.145.10:3000/rutina/addEjercicio/" + id_rutina, "POST", req.toString());
                            pantalla_añadir_ejercicio.dismiss();
                        }
                    }
                });

                pantalla_añadir_ejercicio.show();
            }
        });

        llamada = 1;
        Connection con = new Connection(this);
        con.execute("http://169.254.145.10:3000/rutina/datos/" + id_rutina, "GET", null);
    }

    @Override
    public void Respuesta(JSONObject datos) throws JSONException {
        try {
            if(datos.getInt("codigo") == 200) {
                if (llamada == 1) {
                    listDatosEjercicio = new ArrayList<>();
                    tiempo.setText("Tiempo: " + datos.getInt("tiempo_total"));
                    JSONArray aux1 = datos.getJSONArray("ejercicios");
                    JSONArray aux2 = datos.getJSONArray("tiempos");
                    if (aux1.length() == 0) {
                        Toast.makeText(Rutina.this, "No hay ejercicios para esta rutina", Toast.LENGTH_LONG).show();
                    } else {
                        for (int i = 0; i < aux1.length(); i++) {
                            listDatosEjercicio.add(i, new Pair<>(aux1.getString(i), aux2.getInt(i)));
                        }
                        adaptador = new AdaptadorDatosEjercicioRutina(listDatosEjercicio, this);
                        recycler.setAdapter(adaptador);
                    }
                }
                else if (llamada == 2) {
                    JSONArray nombres = datos.getJSONArray("array");
                    if (nombres.length() == 0) {
                        Toast.makeText(Rutina.this, "No se ha encontrado ningun ejercicio", Toast.LENGTH_LONG).show();
                    }
                    else {
                        listDatosEjercicioSeleccionar = new ArrayList<String>();
                        for (int i = 0; i < nombres.length(); i++) {
                            listDatosEjercicioSeleccionar.add(i, nombres.getString(i));
                        }
                        adaptadorEjercicios = new AdaptadorDatosEjercicios(listDatosEjercicioSeleccionar);
                        adaptadorEjercicios.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                nombre_ejercicio_auxiliar = listDatosEjercicioSeleccionar.get(recycler.getChildAdapterPosition(v));
                            }
                        });
                        recyclerEjercicios.setAdapter(adaptadorEjercicios);
                    }
                }
                else if (llamada == 3) {
                    tiempo.setText("Tiempo: " + datos.getString("tiempo_total"));
                    JSONArray aux1 = datos.getJSONArray("ejercicios");
                    JSONArray aux2 = datos.getJSONArray("tiempos");
                    listDatosEjercicio.add(new Pair<>(aux1.getString(aux1.length()-1), aux2.getInt(aux2.length()-1)));
                    adaptador = new AdaptadorDatosEjercicioRutina(listDatosEjercicio, this);
                    recycler.setAdapter(adaptador);
                }

            }
            else {

            }
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }
}