package com.example.proyecto_tfg_frontend.Actividades;

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

import com.example.proyecto_tfg_frontend.Adaptadores.AdaptadorDatosEjercicioRutina;
import com.example.proyecto_tfg_frontend.Adaptadores.AdaptadorDatosEjercicios;
import com.example.proyecto_tfg_frontend.Connection;
import com.example.proyecto_tfg_frontend.Interfaz;
import com.example.proyecto_tfg_frontend.R;
import com.example.proyecto_tfg_frontend.SeparatorDecoration;
import com.example.proyecto_tfg_frontend.UsuarioSingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Rutina extends AppCompatActivity implements Interfaz {

    private int llamada, tiempo_descanso, tiempo_total, tipo;
    private String nombre_ejercicio_auxiliar;
    private String id_rutina;

    private RecyclerView recycler, recyclerEjercicios;
    private AdaptadorDatosEjercicioRutina adaptador;
    private AdaptadorDatosEjercicios adaptadorEjercicios;
    private FloatingActionButton añadir_ejercicio, empezar_rutina;
    private ArrayList<Pair<String,Integer>> listDatosEjercicio;
    private ArrayList<String> listDatosEjercicioSeleccionar;
    private Dialog pantalla_añadir_ejercicio;
    private TextView tiempo, Kcal, tiempo_desc;
    private Button copiar;
    private final String URL = "https://vidasana.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina);
        System.out.println("ME ESTAS INICIANDO");
        id_rutina = getIntent().getStringExtra("rutina");
        tipo = getIntent().getIntExtra("tipo", 1);

        copiar = (Button) findViewById(R.id.copiar);

        tiempo = (TextView) findViewById(R.id.tiempo);
        tiempo_desc = (TextView) findViewById(R.id.tiempo_descanso);
        Kcal = (TextView) findViewById(R.id.Kcal);
        pantalla_añadir_ejercicio = new Dialog(this);

        recycler = (RecyclerView) findViewById(R.id.lista_ejercicios_rutina);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        SeparatorDecoration decoration = new SeparatorDecoration(this, R.color.Transparente, 5f);
        recycler.addItemDecoration(decoration);

        empezar_rutina = (FloatingActionButton) findViewById(R.id.empezar_rutina);
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

        añadir_ejercicio = (FloatingActionButton) findViewById(R.id.añadir_ejercicio_rutina);
        if (tipo == 1) copiar.setVisibility(View.INVISIBLE);
        else if (tipo == 2) {
            copiar.setVisibility(View.INVISIBLE);
            añadir_ejercicio.setVisibility(View.INVISIBLE);
        }
        else {
            empezar_rutina.setVisibility(View.INVISIBLE);
            añadir_ejercicio.setVisibility(View.INVISIBLE);
            copiar.setVisibility(View.VISIBLE);
            copiar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject req = new JSONObject();
                    try {
                        req.put("id_user", UsuarioSingleton.getInstance().getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    llamada = 1;
                    Connection con = new Connection(Rutina.this);
                    con.execute(URL+"rutina/copiar/"+id_rutina, "POST", req.toString());
                    Toast.makeText(Rutina.this, "Se ha copiado la rutina", Toast.LENGTH_LONG).show();
                }
            });
        }

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
                con.execute(URL+"ejercicio", "GET", null);

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
                            con.execute(URL+"rutina/addEjercicio/" + id_rutina, "POST", req.toString());
                            pantalla_añadir_ejercicio.dismiss();
                        }
                    }
                });

                pantalla_añadir_ejercicio.show();
            }
        });

        llamada = 1;
        Connection con = new Connection(this);
        con.execute(URL+"rutina/datos/" + id_rutina, "GET", null);
    }

    @Override
    public void Respuesta(JSONObject datos) throws JSONException {
        try {
            if(datos.getInt("codigo") == 200) {
                if (llamada == 1) { //GET LOS DATOS DE UNA RUTINA
                    listDatosEjercicio = new ArrayList<>();
                    double d = datos.getInt("tiempo_total");
                    d = d/60;
                    d = Math.floor(d * 100) / 100;
                    String min_s = String.valueOf(d);
                    tiempo.setText(min_s + " min");
                    tiempo_descanso = datos.getInt("tiempo_descanso");
                    tiempo_total = datos.getInt("tiempo_total");
                    tiempo_desc.setText(datos.getString("tiempo_descanso") + " seg");
                    JSONArray aux1 = datos.getJSONArray("ejercicios");
                    JSONArray aux2 = datos.getJSONArray("tiempos");
                    if (aux1.length() == 0) {
                        Kcal.setText("0");
                        Toast.makeText(Rutina.this, "No hay ejercicios para esta rutina", Toast.LENGTH_LONG).show();
                    } else {
                        int tiempo = 0;
                        for (int i = 0; i < aux1.length(); i++) {
                            listDatosEjercicio.add(i, new Pair<>(aux1.getString(i), aux2.getInt(i)));
                            tiempo += aux2.getInt(i);
                        }
                        double gasto = gasto_kcal(tiempo);
                        String s=String.valueOf(gasto);
                        Kcal.setText(s);
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
                                Toast.makeText(Rutina.this, nombre_ejercicio_auxiliar, Toast.LENGTH_LONG).show();
                            }
                        });
                        recyclerEjercicios.setAdapter(adaptadorEjercicios);
                    }
                }
                else if (llamada == 3) { //AÑADIR EJERCICIO A LA RUTINA
                    System.out.println("AÑADIENDO EJERCICIO");
                    double d = datos.getInt("tiempo_total");
                    d = d/60;
                    d = Math.floor(d * 100) / 100;
                    String min_s = String.valueOf(d);
                    tiempo.setText(min_s + " min");
                    tiempo_total = datos.getInt("tiempo_total");
                    JSONArray aux1 = datos.getJSONArray("ejercicios");
                    JSONArray aux2 = datos.getJSONArray("tiempos");
                    int tiempo = 0;
                    for (int i = 0; i < aux1.length(); i++) {
                        System.out.println(aux1.getString(i));
                        tiempo += aux2.getInt(i);
                    }
                    double gasto = gasto_kcal(tiempo);
                    String s = String.valueOf(gasto);
                    Kcal.setText(s);
                    listDatosEjercicio.add(new Pair<>(aux1.getString(aux1.length()-1), aux2.getInt(aux2.length()-1)));
                    adaptador = new AdaptadorDatosEjercicioRutina(listDatosEjercicio, this, id_rutina, tipo);
                    recycler.setAdapter(adaptador);
                }
                else if (adaptador.getLlamada() == 3) {
                    listDatosEjercicio = new ArrayList<>();
                    double d = datos.getInt("tiempo_total");
                    d = d/60;
                    d = Math.floor(d * 100) / 100;
                    String min_s = String.valueOf(d);
                    tiempo.setText( min_s + " min");
                    tiempo_descanso = datos.getInt("tiempo_descanso");
                    tiempo_total = datos.getInt("tiempo_total");
                    tiempo_desc.setText(datos.getString("tiempo_descanso") + " seg");
                    JSONArray aux1 = datos.getJSONArray("ejercicios");
                    JSONArray aux2 = datos.getJSONArray("tiempos");
                    if (aux1.length() == 0) {
                        Kcal.setText("0");
                        Toast.makeText(Rutina.this, "No hay ejercicios para esta rutina", Toast.LENGTH_LONG).show();
                    } else {
                        int tiempo = 0;
                        for (int i = 0; i < aux1.length(); i++) {
                            listDatosEjercicio.add(i, new Pair<>(aux1.getString(i), aux2.getInt(i)));
                            tiempo += aux2.getInt(i);
                        }
                        double gasto = gasto_kcal(tiempo);
                        String s=String.valueOf(gasto);
                        Kcal.setText(s);
                    }
                    adaptador = new AdaptadorDatosEjercicioRutina(listDatosEjercicio, this, id_rutina, tipo);
                    recycler.setAdapter(adaptador);
                }
                else if (adaptador.getLlamada() == 4) {
                    listDatosEjercicio = new ArrayList<>();
                    double d = datos.getInt("tiempo_total");
                    d = d/60;
                    d = Math.floor(d * 100) / 100;
                    String min_s = String.valueOf(d);
                    tiempo.setText(min_s + " min");
                    tiempo_descanso = datos.getInt("tiempo_descanso");
                    tiempo_total = datos.getInt("tiempo_total");
                    tiempo_desc.setText(datos.getString("tiempo_descanso") + " seg");
                    JSONArray aux1 = datos.getJSONArray("ejercicios");
                    JSONArray aux2 = datos.getJSONArray("tiempos");
                    if (aux1.length() == 0) {
                        Kcal.setText("0");
                        Toast.makeText(Rutina.this, "No hay ejercicios para esta rutina", Toast.LENGTH_LONG).show();
                    } else {
                        int tiempo = 0;
                        for (int i = 0; i < aux1.length(); i++) {
                            listDatosEjercicio.add(i, new Pair<>(aux1.getString(i), aux2.getInt(i)));
                            tiempo += aux2.getInt(i);
                        }
                        double gasto = gasto_kcal(tiempo);
                        String s=String.valueOf(gasto);
                        Kcal.setText(s);
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

    @Override
    protected void onPause() {
        System.out.println("EN PAUSA");
        super.onPause();
    }

    @Override
    protected void onStop() {
        System.out.println("EN STOP");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        System.out.println("ME RECUPERO");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        System.out.println("ME DESTRUYO");
        super.onDestroy();
    }

    private double gasto_kcal(int tiempo) {
        double segundos = tiempo;
        double minutos = segundos/60;
        double value = 0.081*UsuarioSingleton.getInstance().getPeso_act()*minutos;
        return  Math.floor(value * 100) / 100;
    }
}