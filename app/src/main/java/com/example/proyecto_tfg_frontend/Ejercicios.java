package com.example.proyecto_tfg_frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Ejercicios extends AppCompatActivity implements Interfaz{

    private ArrayList<String> listDatosEjercicio;
    private RecyclerView recycler;
    private AdaptadorDatosEjercicios adaptador;
    private Dialog pantalla_filtrar;
    private int llamada;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filtrar_ejercicios, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filtrar:
                EditText busqueda_nombre;
                Button filtrar, cancelar;
                CheckBox piernas, gluteo, biceps, triceps, espalda, abdomen, hombros, pecho;
                Spinner dificultad, tipo_ejercicio;
                pantalla_filtrar.setContentView(R.layout.popup_filtrar_ejercicios);

                busqueda_nombre = (EditText) pantalla_filtrar.findViewById(R.id.busqueda_ejercicio);
                filtrar = (Button) pantalla_filtrar.findViewById(R.id.confirmar);
                cancelar = (Button) pantalla_filtrar.findViewById(R.id.cancelar);
                piernas = (CheckBox) pantalla_filtrar.findViewById(R.id.piernas);
                gluteo = (CheckBox) pantalla_filtrar.findViewById(R.id.gluteo);
                biceps = (CheckBox) pantalla_filtrar.findViewById(R.id.biceps);
                triceps = (CheckBox) pantalla_filtrar.findViewById(R.id.triceps);
                espalda = (CheckBox) pantalla_filtrar.findViewById(R.id.espalda);
                abdomen = (CheckBox) pantalla_filtrar.findViewById(R.id.abdomen);
                hombros = (CheckBox) pantalla_filtrar.findViewById(R.id.hombros);
                pecho = (CheckBox) pantalla_filtrar.findViewById(R.id.pecho);
                dificultad = (Spinner) pantalla_filtrar.findViewById(R.id.dificultad);
                tipo_ejercicio = (Spinner) pantalla_filtrar.findViewById(R.id.tipo_ejercicio);

                String [] niv = new String[] {"todas", "facil", "normal", "dificil"};
                ArrayAdapter<String> aaDep;
                aaDep = new ArrayAdapter<String>(this, R.layout.spinner_item, niv); //activity para mostrar, tipo de spinner, listado de valores
                aaDep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dificultad.setAdapter(aaDep);
                dificultad.setContentDescription("todas");

                String [] tip = new String[] {"todos", "aerobico", "estiramiento"};
                ArrayAdapter<String> aaDep2;
                aaDep2 = new ArrayAdapter<String >(this, R.layout.spinner_item, tip); //activity para mostrar, tipo de spinner, listado de valores
                aaDep2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tipo_ejercicio.setAdapter(aaDep2);

                filtrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nombre = busqueda_nombre.getText().toString();
                        System.out.println("NOMBRE: " + nombre);
                        String dif = dificultad.getSelectedItem().toString();
                        String est = tipo_ejercicio.getSelectedItem().toString();
                        int est_int = 2;
                        if (dif.equals("todas")) dif = "";
                        if (est.equals("estiramiento")) est_int = 1;
                        else if (est.equals("aerobico")) est_int = 0;
                        System.out.println("Dificultad: " + dif);
                        System.out.println("Tipo ejercicio: " + est_int);
                        JSONArray  musc = new JSONArray();
                        if (piernas.isChecked()) musc.put("piernas");
                        if (gluteo.isChecked()) musc.put("gluteo");
                        if (biceps.isChecked()) musc.put("biceps");
                        if (triceps.isChecked()) musc.put("triceps");
                        if (espalda.isChecked()) musc.put("espalda");
                        if (abdomen.isChecked()) musc.put("abdomen");
                        if (hombros.isChecked()) musc.put("hombros");
                        if (pecho.isChecked()) musc.put("pecho");
                        System.out.println(musc);

                        JSONObject req = new JSONObject();
                        try {
                            req.put("dificultad", dif);
                            req.put("musculos", musc);
                            req.put("nombre", nombre);
                            req.put("estiramiento", est_int);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        llamada = 2;
                        System.out.println(req.toString());
                        Connection con = new Connection(Ejercicios.this);
                        con.execute("http://192.168.0.14:3000/ejercicio/filtrados", "POST", req.toString());
                        pantalla_filtrar.dismiss();
                    }
                });

                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pantalla_filtrar.dismiss();
                    }
                });
                pantalla_filtrar.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicios);

        pantalla_filtrar = new Dialog(this);

        recycler = (RecyclerView) findViewById(R.id.lista_ejercicios);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        listDatosEjercicio = new ArrayList<String>();
        llamada = 1;
        Connection con = new Connection(this);
        con.execute("http://192.168.0.14:3000/ejercicio", "GET", null);
    }

    @Override
    protected void onDestroy() {
        System.out.println("ME DESTRUYO");
        super.onDestroy();
    }

    @Override
    public void Respuesta(JSONObject datos) {
        try {
            if(datos.getInt("codigo") == 200) {
                if (llamada == 1) {
                    JSONArray nombres = datos.getJSONArray("array");
                    for (int i = 0; i < nombres.length(); i++) {
                        listDatosEjercicio.add(i, nombres.getString(i));
                    }
                    //Añadir todos los elementos consultados
                    adaptador = new AdaptadorDatosEjercicios(listDatosEjercicio);
                    adaptador.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("VOY CAMBIAR DE PANTALLA");
                            Intent i = new Intent(getApplicationContext(), Ejercicio.class);
                            i.putExtra("ejercicio", listDatosEjercicio.get(recycler.getChildAdapterPosition(v)));
                            startActivity(i);
                        }
                    });
                    recycler.setAdapter(adaptador);
                }
                else if (llamada == 2) {
                    JSONArray nombres = datos.getJSONArray("array");
                    if (nombres.length() == 0) {
                        Toast.makeText(Ejercicios.this, "No se ha encontrado ningun ejercicio", Toast.LENGTH_LONG).show();
                    }
                    else {
                        listDatosEjercicio = new ArrayList<String>();
                        for (int i = 0; i < nombres.length(); i++) {
                            listDatosEjercicio.add(i, nombres.getString(i));
                        }

                        adaptador = new AdaptadorDatosEjercicios(listDatosEjercicio);
                        adaptador.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println("VOY CAMBIAR DE PANTALLA");
                                Intent i = new Intent(getApplicationContext(), Ejercicio.class);
                                i.putExtra("ejercicio", listDatosEjercicio.get(recycler.getChildAdapterPosition(v)));
                                startActivity(i);
                            }
                        });
                        recycler.setAdapter(adaptador);
                    }
                }
            }
            else {
                System.out.println("ERROR");
            }
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }
}