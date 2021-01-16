package com.example.proyecto_tfg_frontend.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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

import com.example.proyecto_tfg_frontend.Adaptadores.AdaptadorDatosRutinas;
import com.example.proyecto_tfg_frontend.Connection;
import com.example.proyecto_tfg_frontend.Clases.DatosRutina;
import com.example.proyecto_tfg_frontend.Interfaz;
import com.example.proyecto_tfg_frontend.R;
import com.example.proyecto_tfg_frontend.SeparatorDecoration;
import com.example.proyecto_tfg_frontend.UsuarioSingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Rutinas extends AppCompatActivity implements Interfaz {

    private BottomNavigationView menu;
    private ArrayList<DatosRutina> listDatosRutinas;
    private FloatingActionButton crear_rutina;
    private Dialog pantalla_crear;
    private int llamada;
    private RecyclerView recycler;
    private AdaptadorDatosRutinas adaptador;
    private boolean otras_rutinas;
    private final String URL = "https://vidasana.herokuapp.com/";

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filtrar_ejercicios, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filtrar:
                if(otras_rutinas) {
                    EditText busqueda_nombre;
                    CircleImageView buscar;
                    CheckBox ordenar;
                    Spinner dificultad;
                    pantalla_crear.setContentView(R.layout.popup_filtrar_rutinas);

                    busqueda_nombre = (EditText) pantalla_crear.findViewById(R.id.buscador);
                    buscar = (CircleImageView) pantalla_crear.findViewById(R.id.buscar);
                    ordenar = (CheckBox) pantalla_crear.findViewById(R.id.Publica);
                    dificultad = (Spinner) pantalla_crear.findViewById(R.id.dificultad);

                    String[] niv = new String[]{"todas", "fácil", "normal", "difícil"};
                    ArrayAdapter<String> aaDep;
                    aaDep = new ArrayAdapter<String>(this, R.layout.spinner_item, niv); //activity para mostrar, tipo de spinner, listado de valores
                    aaDep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dificultad.setAdapter(aaDep);
                    dificultad.setContentDescription("todas");

                    buscar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            JSONObject req = new JSONObject();
                            try {
                                req.put("id", UsuarioSingleton.getInstance().getId());
                                req.put("usuario_busc", busqueda_nombre.getText().toString());
                                req.put("dificultad", dificultad.getSelectedItem().toString());
                                req.put("filtrar", ordenar.isChecked());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            llamada = 4;
                            Connection con = new Connection(Rutinas.this);
                            con.execute(URL+"rutina/usuarios/datos", "POST", req.toString());
                            pantalla_crear.dismiss();
                        }
                    });
                    pantalla_crear.show();
                }
                else Toast.makeText(Rutinas.this, "NO SIRVE EN ESTA PANTALLA", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutinas);
        otras_rutinas = false;
        listDatosRutinas = new ArrayList<>();
        recycler = (RecyclerView) findViewById(R.id.lista_rutinas);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        SeparatorDecoration decoration = new SeparatorDecoration(this, R.color.Transparente, 5f);
        recycler.addItemDecoration(decoration);

        llamada = 1;
        Connection con = new Connection(this);
        con.execute(URL+"rutina/"+UsuarioSingleton.getInstance().getId(), "GET", null);

        crear_rutina = (FloatingActionButton) findViewById(R.id.añadir_rutina);
        pantalla_crear = new Dialog(this);
        crear_rutina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nombre, tiempo_desc;
                Button confirmar, cancelar;
                CheckBox publica;
                Spinner dificultad;
                pantalla_crear.setContentView(R.layout.popup_crear_rutina);
                nombre = (EditText) pantalla_crear.findViewById(R.id.nuev_nombre);
                tiempo_desc = (EditText) pantalla_crear.findViewById(R.id.nuev_tiempo_desc);
                confirmar = (Button) pantalla_crear.findViewById(R.id.confirmar);
                cancelar = (Button) pantalla_crear.findViewById(R.id.cancelar);
                dificultad = (Spinner) pantalla_crear.findViewById(R.id.dificultad);
                publica = (CheckBox) pantalla_crear.findViewById(R.id.Publica);

                String [] niv = new String[] {"fácil", "normal", "difícil"};
                ArrayAdapter<String> aaDep;
                aaDep = new ArrayAdapter<String>(Rutinas.this, R.layout.spinner_item, niv); //activity para mostrar, tipo de spinner, listado de valores
                aaDep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dificultad.setAdapter(aaDep);
                dificultad.setContentDescription("fácil");

                confirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!nombre.getText().toString().equals("") && !tiempo_desc.getText().toString().equals("")) {
                            JSONObject req = new JSONObject();
                            try {
                                req.put("nombre", nombre.getText().toString());
                                req.put("tiempo_descanso", Integer.parseInt(tiempo_desc.getText().toString()));
                                req.put("propietario", UsuarioSingleton.getInstance().getId());
                                req.put("publica", publica.isChecked());
                                req.put("dificultad", dificultad.getSelectedItem().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            llamada = 2;
                            Connection con = new Connection(Rutinas.this);
                            con.execute(URL+"rutina/register", "POST", req.toString());
                            pantalla_crear.dismiss();
                        }
                        else {
                            Toast.makeText(Rutinas.this, "Rellene los parametros correctamente", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pantalla_crear.dismiss();
                    }
                });

                pantalla_crear.show();
            }
        });
        menu = (BottomNavigationView) findViewById(R.id.menu_rutinas);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.mis_rutinas) {
                    otras_rutinas = false;
                    crear_rutina.setVisibility(View.VISIBLE);
                    llamada = 1;
                    Connection con = new Connection(Rutinas.this);
                    con.execute(URL+"rutina/"+UsuarioSingleton.getInstance().getId(), "GET", null);
                }
                else if (item.getItemId() == R.id.predeterminadas) {
                    otras_rutinas = false;
                    crear_rutina.setVisibility(View.INVISIBLE);
                    llamada = 3;
                    Connection con = new Connection(Rutinas.this);
                    con.execute(URL+"rutina/predeterminada/datos", "GET", null);
                }
                else if (item.getItemId() == R.id.otras) {
                    otras_rutinas = true;
                    crear_rutina.setVisibility(View.INVISIBLE);
                    JSONObject req = new JSONObject();
                    try {
                        req.put("id", UsuarioSingleton.getInstance().getId());
                        req.put("usuario_busc", "");
                        req.put("dificultad", "todas");
                        req.put("filtrar", true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    llamada = 4;
                    Connection con = new Connection(Rutinas.this);
                    con.execute(URL+"rutina/usuarios/datos", "POST", req.toString());
                }
                return true;
            }
        });
    }

    @Override
    public void Respuesta(JSONObject datos) throws JSONException {
        try {
            if(datos.getInt("codigo") == 200) {
                if (llamada == 1) { //get rutinas creadas por mi
                    llamada = 5;
                    JSONArray nombres = datos.getJSONArray("array");
                    if (nombres.length() == 0) {
                        listDatosRutinas = new ArrayList<>();
                        adaptador = new AdaptadorDatosRutinas(listDatosRutinas, this,1);
                        Toast.makeText(Rutinas.this, "No hay rutinas creadas", Toast.LENGTH_LONG).show();
                    }
                    else {
                        listDatosRutinas = new ArrayList<>();
                        for (int i = 0; i < nombres.length(); i++) {
                            JSONObject aux1 = nombres.getJSONObject(i);
                            int copias = aux1.getInt("copias");
                            boolean publica = true;
                            if (copias<0) publica = false;
                            listDatosRutinas.add(i, new DatosRutina(aux1.getString("nombre"), aux1.getString("id"), aux1.getString("dificultad"), aux1.getInt("tiempo_descanso"), publica));
                        }
                        adaptador = new AdaptadorDatosRutinas(listDatosRutinas, this,1);
                    }
                }
                else if (llamada == 2) { //al crear una rutina nueva
                    int copias = datos.getInt("copias");
                    boolean publica = true;
                    if (copias<0) publica = false;
                    listDatosRutinas.add(new DatosRutina(datos.getString("nombre"), datos.getString("_id"), datos.getString("dificultad"), datos.getInt("tiempo_descanso"), publica));
                    adaptador = new AdaptadorDatosRutinas(listDatosRutinas, this, 1);
                    llamada = 5;
                }
                else if (llamada == 3) {//al recibir las rutinas predetermindas
                    JSONArray nombres = datos.getJSONArray("array");
                    listDatosRutinas = new ArrayList<>();
                    for (int i = 0; i < nombres.length(); i++) {
                        JSONObject aux1 = nombres.getJSONObject(i);
                        System.out.println(aux1.toString());
                        listDatosRutinas.add(i, new DatosRutina(aux1.getString("nombre"), aux1.getString("id"), aux1.getString("dificultad"), aux1.getInt("tiempo_descanso"),true));
                    }
                    adaptador = new AdaptadorDatosRutinas(listDatosRutinas, this, 2);
                    llamada = 5;
                }
                else if (llamada == 4) { //Todas las rutinas de otros users
                    JSONArray nombres = datos.getJSONArray("array");
                    listDatosRutinas = new ArrayList<>();
                    for (int i = 0; i < nombres.length(); i++) {
                        JSONObject aux1 = nombres.getJSONObject(i);
                        DatosRutina dr = new DatosRutina(aux1.getString("nombre"), aux1.getString("id"), aux1.getString("dificultad"), aux1.getInt("tiempo_descanso"), true);
                        dr.setProp(aux1.getString("propietario"));
                        listDatosRutinas.add(i, dr);
                    }
                    adaptador = new AdaptadorDatosRutinas(listDatosRutinas, this, 3);
                    llamada = 5;
                }
                else if (adaptador.getLlamada() == 3) { //rutina eliminada
                    JSONArray nombres = datos.getJSONArray("array");
                    listDatosRutinas = new ArrayList<>();
                    if (nombres.length() != 0) {
                        for (int i = 0; i < nombres.length(); i++) {
                            JSONObject aux1 = nombres.getJSONObject(i);
                            int copias = aux1.getInt("copias");
                            boolean publica = true;
                            if (copias<0) publica = false;
                            listDatosRutinas.add(i, new DatosRutina(aux1.getString("nombre"), aux1.getString("id"),aux1.getString("dificultad"), aux1.getInt("tiempo_descanso"),publica));
                        }
                    }
                    adaptador = new AdaptadorDatosRutinas(listDatosRutinas, this,1);
                }
                else if (adaptador.getLlamada() == 4) { //RUTINA MODIFICADA
                    String id_rut = listDatosRutinas.get(adaptador.getPos()).getId();
                    int copias = datos.getInt("copias");
                    boolean publica = true;
                    if (copias<0) publica = false;
                    listDatosRutinas.set(adaptador.getPos(), new DatosRutina(datos.getString("nombre"), id_rut, datos.getString("dificultad"), datos.getInt("tiempo_descanso"), publica));
                    adaptador = new AdaptadorDatosRutinas(listDatosRutinas, this,1);
                }
                recycler.setAdapter(adaptador);
            }
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }
}