package com.example.proyecto_tfg_frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Rutinas extends AppCompatActivity implements Interfaz {

    private BottomNavigationView menu;
    private ArrayList<DatosRutina> listDatosRutinas;
    private CircleImageView crear_rutina;
    private Dialog pantalla_crear;
    private int llamada;
    private RecyclerView recycler;
    private AdaptadorDatosRutinas adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutinas);

        listDatosRutinas = new ArrayList<>();
        recycler = (RecyclerView) findViewById(R.id.lista_rutinas);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        llamada = 1;
        Connection con = new Connection(this);
        con.execute("http://169.254.145.10:3000/rutina/"+UsuarioSingleton.getInstance().getId(), "GET", null);

        crear_rutina = (CircleImageView) findViewById(R.id.añadir_rutina);
        pantalla_crear = new Dialog(this);
        crear_rutina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nombre, tiempo_desc;
                Button confirmar, cancelar;
                pantalla_crear.setContentView(R.layout.popup_crear_rutina);
                nombre = (EditText) pantalla_crear.findViewById(R.id.nuev_nombre);
                tiempo_desc = (EditText) pantalla_crear.findViewById(R.id.nuev_tiempo_desc);
                confirmar = (Button) pantalla_crear.findViewById(R.id.confirmar);
                cancelar = (Button) pantalla_crear.findViewById(R.id.cancelar);

                confirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!nombre.getText().toString().equals("") && !tiempo_desc.getText().toString().equals("")) {
                            JSONObject req = new JSONObject();
                            try {
                                req.put("nombre", nombre.getText().toString());
                                req.put("tiempo_descanso", Integer.parseInt(tiempo_desc.getText().toString()));
                                req.put("propietario", UsuarioSingleton.getInstance().getId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            llamada = 2;
                            Connection con = new Connection(Rutinas.this);
                            con.execute("http://169.254.145.10:3000/rutina/register", "POST", req.toString());
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
                    crear_rutina.setVisibility(View.VISIBLE);
                    llamada = 1;
                    Connection con = new Connection(Rutinas.this);
                    con.execute("http://169.254.145.10:3000/rutina/"+UsuarioSingleton.getInstance().getId(), "GET", null);
                }
                else if (item.getItemId() == R.id.predeterminadas) {
                    crear_rutina.setVisibility(View.INVISIBLE);
                    llamada = 3;
                    Connection con = new Connection(Rutinas.this);
                    con.execute("http://169.254.145.10:3000/rutina/predeterminada/datos", "GET", null);
                }
                else if (item.getItemId() == R.id.otras) {
                    crear_rutina.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });
    }

    @Override
    public void Respuesta(JSONObject datos) throws JSONException {
        try {
            if(datos.getInt("codigo") == 200) {
                if (llamada == 1) {
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
                            listDatosRutinas.add(i, new DatosRutina(aux1.getString("nombre"), aux1.getString("id"), aux1.getInt("tiempo_descanso")));
                        }
                        adaptador = new AdaptadorDatosRutinas(listDatosRutinas, this,1);
                    }
                }
                else if (llamada == 2) {
                    listDatosRutinas.add(new DatosRutina(datos.getString("nombre"), datos.getString("_id"), datos.getInt("tiempo_descanso")));
                    adaptador = new AdaptadorDatosRutinas(listDatosRutinas, this, 1);
                    llamada = 5;
                }
                else if (llamada == 3) {
                    JSONArray nombres = datos.getJSONArray("array");
                    listDatosRutinas = new ArrayList<>();
                    for (int i = 0; i < nombres.length(); i++) {
                        JSONObject aux1 = nombres.getJSONObject(i);
                        listDatosRutinas.add(i, new DatosRutina(aux1.getString("nombre"), aux1.getString("id"), aux1.getInt("tiempo_descanso")));
                    }
                    adaptador = new AdaptadorDatosRutinas(listDatosRutinas, this, 2);
                    llamada = 5;
                }
                else if (adaptador.getLlamada() == 3) {
                    JSONArray nombres = datos.getJSONArray("array");
                    listDatosRutinas = new ArrayList<>();
                    if (nombres.length() != 0) {
                        for (int i = 0; i < nombres.length(); i++) {
                            JSONObject aux1 = nombres.getJSONObject(i);
                            listDatosRutinas.add(i, new DatosRutina(aux1.getString("nombre"), aux1.getString("id"), aux1.getInt("tiempo_descanso")));
                        }
                    }
                    adaptador = new AdaptadorDatosRutinas(listDatosRutinas, this,1);
                }
                else if (adaptador.getLlamada() == 4) { //RUTINA MODIFICADA
                    String id_rut = listDatosRutinas.get(adaptador.getPos()).getId();
                    listDatosRutinas.set(adaptador.getPos(), new DatosRutina(datos.getString("nombre"), id_rut, datos.getInt("tiempo_descanso")));
                    adaptador = new AdaptadorDatosRutinas(listDatosRutinas, this,1);
                }
                recycler.setAdapter(adaptador);
            }
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }
}