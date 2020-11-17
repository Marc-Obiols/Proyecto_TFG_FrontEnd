package com.example.proyecto_tfg_frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
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

    private int llamada, tiempo_descanso, tiempo_total, tipo;
    private String nombre_ejercicio_auxiliar;
    private String id_rutina;

    private RecyclerView recycler, recyclerEjercicios;
    private AdaptadorDatosEjercicioRutina adaptador;
    private AdaptadorDatosEjercicios adaptadorEjercicios;
    private CircleImageView añadir_ejercicio, empezar_rutina;
    private ArrayList<Pair<String,Integer>> listDatosEjercicio;
    private ArrayList<String> listDatosEjercicioSeleccionar;
    private Dialog pantalla_añadir_ejercicio;
    private TextView tiempo, Kcal, tiempo_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina);
        id_rutina = getIntent().getStringExtra("rutina");
        tipo = getIntent().getIntExtra("tipo", 1);

        tiempo = (TextView) findViewById(R.id.tiempo);
        tiempo_desc = (TextView) findViewById(R.id.tiempo_descanso);
        Kcal = (TextView) findViewById(R.id.Kcal);
        pantalla_añadir_ejercicio = new Dialog(this);

        recycler = (RecyclerView) findViewById(R.id.lista_ejercicios_rutina);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        empezar_rutina = (CircleImageView) findViewById(R.id.empezar_rutina);
        empezar_rutina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Ejecucion_rutina.class);
                ArrayList<String> nombres_ejercicios = new ArrayList<>();
                ArrayList<Integer> tiempos_ejercicios = new ArrayList<>();
                for (int j = 0; j < listDatosEjercicio.size(); j++) {
                    nombres_ejercicios.add(listDatosEjercicio.get(j).first);
                    tiempos_ejercicios.add(listDatosEjercicio.get(j).second);
                }
                i.putExtra("ejercicios_nombres", nombres_ejercicios);
                i.putExtra("ejercicios_tiempos", tiempos_ejercicios);
                i.putExtra("tiempo_descanso", tiempo_descanso);
                i.putExtra("tiempo_total", tiempo_total);
                System.out.println("quiero empezar la ejecucion de la rutina");
                startActivity(i);
            }
        });

        añadir_ejercicio = (CircleImageView) findViewById(R.id.añadir_ejercicio_rutina);
        if (tipo == 2) añadir_ejercicio.setVisibility(View.INVISIBLE);
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
                if (llamada == 1) { //GET LOS DATOS DE UNA RUTINA
                    listDatosEjercicio = new ArrayList<>();
                    double d = datos.getInt("tiempo_total");
                    d = d/60;
                    String min_s = String.valueOf(d);
                    tiempo.setText("Tiempo: " + min_s + " min");
                    tiempo_descanso = datos.getInt("tiempo_descanso");
                    tiempo_total = datos.getInt("tiempo_total");
                    tiempo_desc.setText("Tiempo Descanso: " + datos.getString("tiempo_descanso"));
                    JSONArray aux1 = datos.getJSONArray("ejercicios");
                    JSONArray aux2 = datos.getJSONArray("tiempos");
                    if (aux1.length() == 0) {
                        Kcal.setText("Gasto calórico: 0");
                        Toast.makeText(Rutina.this, "No hay ejercicios para esta rutina", Toast.LENGTH_LONG).show();
                    } else {
                        int tiempo = 0;
                        for (int i = 0; i < aux1.length(); i++) {
                            listDatosEjercicio.add(i, new Pair<>(aux1.getString(i), aux2.getInt(i)));
                            tiempo += aux2.getInt(i);
                        }
                        double gasto = gasto_kcal(tiempo);
                        String s=String.valueOf(gasto);
                        Kcal.setText("Gasto calórico: " + s);
                        adaptador = new AdaptadorDatosEjercicioRutina(listDatosEjercicio, this, id_rutina, tipo);
                        recycler.setAdapter(adaptador);
                    }
                }
                else if (llamada == 2) { // PEDIR TODOS LOS EJERCICIOS DEL SISTEMA
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
                else if (llamada == 3) { //AÑADIR EJERCICIO A LA RUTINA
                    double d = datos.getInt("tiempo_total");
                    d = d/60;
                    String min_s = String.valueOf(d);
                    tiempo.setText("Tiempo: " + min_s + " min");
                    tiempo_total = datos.getInt("tiempo_total");
                    JSONArray aux1 = datos.getJSONArray("ejercicios");
                    JSONArray aux2 = datos.getJSONArray("tiempos");
                    int tiempo = 0;
                    for (int i = 0; i < aux1.length(); i++) {
                        listDatosEjercicio.add(i, new Pair<>(aux1.getString(i), aux2.getInt(i)));
                        tiempo += aux2.getInt(i);
                    }
                    double gasto = gasto_kcal(tiempo);
                    String s = String.valueOf(gasto);
                    Kcal.setText("Gasto calórico: " + s);
                    listDatosEjercicio.add(new Pair<>(aux1.getString(aux1.length()-1), aux2.getInt(aux2.length()-1)));
                    adaptador = new AdaptadorDatosEjercicioRutina(listDatosEjercicio, this, id_rutina, tipo);
                    recycler.setAdapter(adaptador);
                }
                else if (adaptador.getLlamada() == 3) {
                    listDatosEjercicio = new ArrayList<>();
                    double d = datos.getInt("tiempo_total");
                    d = d/60;
                    String min_s = String.valueOf(d);
                    tiempo.setText("Tiempo: " + min_s + " min");
                    tiempo_descanso = datos.getInt("tiempo_descanso");
                    tiempo_total = datos.getInt("tiempo_total");
                    tiempo_desc.setText("Tiempo Descanso: " + datos.getString("tiempo_descanso"));
                    JSONArray aux1 = datos.getJSONArray("ejercicios");
                    JSONArray aux2 = datos.getJSONArray("tiempos");
                    if (aux1.length() == 0) {
                        Kcal.setText("Gasto calórico: 0");
                        Toast.makeText(Rutina.this, "No hay ejercicios para esta rutina", Toast.LENGTH_LONG).show();
                    } else {
                        int tiempo = 0;
                        for (int i = 0; i < aux1.length(); i++) {
                            listDatosEjercicio.add(i, new Pair<>(aux1.getString(i), aux2.getInt(i)));
                            tiempo += aux2.getInt(i);
                        }
                        double gasto = gasto_kcal(tiempo);
                        String s=String.valueOf(gasto);
                        Kcal.setText("Gasto calórico: " + s);
                    }
                    adaptador = new AdaptadorDatosEjercicioRutina(listDatosEjercicio, this, id_rutina, tipo);
                    recycler.setAdapter(adaptador);
                }
                else if (adaptador.getLlamada() == 4) {
                    listDatosEjercicio = new ArrayList<>();
                    double d = datos.getInt("tiempo_total");
                    d = d/60;
                    String min_s = String.valueOf(d);
                    tiempo.setText("Tiempo: " + min_s + " min");
                    tiempo_descanso = datos.getInt("tiempo_descanso");
                    tiempo_total = datos.getInt("tiempo_total");
                    tiempo_desc.setText("Tiempo Descanso: " + datos.getString("tiempo_descanso"));
                    JSONArray aux1 = datos.getJSONArray("ejercicios");
                    JSONArray aux2 = datos.getJSONArray("tiempos");
                    if (aux1.length() == 0) {
                        Kcal.setText("Gasto calórico: 0");
                        Toast.makeText(Rutina.this, "No hay ejercicios para esta rutina", Toast.LENGTH_LONG).show();
                    } else {
                        int tiempo = 0;
                        for (int i = 0; i < aux1.length(); i++) {
                            listDatosEjercicio.add(i, new Pair<>(aux1.getString(i), aux2.getInt(i)));
                            tiempo += aux2.getInt(i);
                        }
                        double gasto = gasto_kcal(tiempo);
                        String s=String.valueOf(gasto);
                        Kcal.setText("Gasto calórico: " + s);
                    }
                    adaptador = new AdaptadorDatosEjercicioRutina(listDatosEjercicio, this, id_rutina, tipo);
                    recycler.setAdapter(adaptador);
                }
                llamada = -1;
            }
            else {

            }
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }

    private double gasto_kcal(int tiempo) {
        double segundos = tiempo;
        System.out.println(tiempo);
        double minutos = segundos/60;
        double value = 0.081*UsuarioSingleton.getInstance().getPeso_act()*minutos;
        System.out.println(UsuarioSingleton.getInstance().getPeso_act());
        System.out.println(value);
        return  Math.floor(value * 100) / 100;
    }
}