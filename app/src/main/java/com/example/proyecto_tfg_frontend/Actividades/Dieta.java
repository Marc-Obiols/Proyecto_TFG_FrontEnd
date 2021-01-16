package com.example.proyecto_tfg_frontend.Actividades;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.proyecto_tfg_frontend.Connection;
import com.example.proyecto_tfg_frontend.Clases.DatosDia;
import com.example.proyecto_tfg_frontend.Interfaz;
import com.example.proyecto_tfg_frontend.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Dieta extends AppCompatActivity implements Interfaz {

    private TextView des, com, mer, cen, nombre, objetivo, lunes, martes, miercoles, jueves,
            viernes, sabado, domingo;
    private String descripcion;
    private ArrayList<DatosDia> datosDia;
    private Dialog pantalla;
    private final String URL = "https://vidasana.herokuapp.com/";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_informacion, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.info:
                TextView informacion;
                pantalla.setContentView(R.layout.popup_info);
                informacion = pantalla.findViewById(R.id.info_a_presentar);
                informacion.setText(descripcion);
                pantalla.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dieta);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        pantalla = new Dialog(this);

        String id = getIntent().getStringExtra("id_dieta");
        Connection con = new Connection(this);
        con.execute(URL+"dietas/info/" + id, "GET", null);


        des = (TextView) findViewById(R.id.inf_des);
        com = (TextView) findViewById(R.id.inf_comida);
        mer = (TextView) findViewById(R.id.inf_merienda);
        cen = (TextView) findViewById(R.id.inf_cena);
        nombre = (TextView) findViewById(R.id.nombre);
        objetivo  = (TextView) findViewById(R.id.objetivo);

        lunes  = (TextView) findViewById(R.id.lunes);
        martes  = (TextView) findViewById(R.id.martes);
        miercoles  = (TextView) findViewById(R.id.miercoles);
        jueves  = (TextView) findViewById(R.id.jueves);
        viernes  = (TextView) findViewById(R.id.viernes);
        sabado  = (TextView) findViewById(R.id.sabado);
        domingo  = (TextView) findViewById(R.id.domingo);

        lunes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        lunes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modificar_pantalla(0);
            }
        });
        martes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modificar_pantalla(1);
            }
        });
        miercoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modificar_pantalla(2);
            }
        });
        jueves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modificar_pantalla(3);
            }
        });
        viernes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modificar_pantalla(4);
            }
        });
        sabado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modificar_pantalla(5);
            }
        });
        domingo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modificar_pantalla(6);
            }
        });
    }

    @Override
    public void Respuesta(JSONObject datos) throws JSONException {
        if(datos.getInt("codigo") == 200) {

            datosDia = new ArrayList<>();
            nombre.setText(datos.getString("nombre"));
            objetivo.setText(datos.getString("objetivo"));
            descripcion = datos.getString("descripcion");

            JSONArray dias = datos.getJSONArray("dias");
            JSONObject dia;
            for (int i = 0; i < dias.length(); i++) {
                dia = (JSONObject) dias.get(i);
                int a = 0, b = 0, c = 0, d = 0;
                JSONArray aux_d = dia.getJSONArray("desayuno"), aux_co= dia.getJSONArray("comida"), aux_m= dia.getJSONArray("merienda"), aux_ce= dia.getJSONArray("cena");
                boolean com_l = true, des_l = true, cen_l = true, mer_l = true;
                ArrayList <String> aux_s_co = new ArrayList<>(), aux_s_ce = new ArrayList<>(), aux_s_m = new ArrayList<>(), aux_s_d = new ArrayList<>();
                while (com_l || des_l || mer_l || cen_l) {
                    des_l = a < aux_d.length();
                    com_l = b < aux_co.length();
                    mer_l = c < aux_m.length();
                    cen_l = d < aux_ce.length();
                    //System.out.println(des_l+ ":"+com_l+":"+ mer_l+ ":"+cen_l);
                    if (des_l) {
                        aux_s_d.add(aux_d.getString(a));
                        a++;
                    }
                    if (com_l) {
                        aux_s_co.add(aux_co.getString(b));
                        b++;
                    }
                    if (mer_l) {
                        aux_s_m.add(aux_m.getString(c));
                        c++;
                    }
                    if (cen_l) {
                        aux_s_ce.add(aux_ce.getString(d));
                        d++;
                    }
                }
                datosDia.add(new DatosDia(aux_s_d, aux_s_co, aux_s_ce, aux_s_m));
            }
            Modificar_pantalla(0);
        }
        else {
            System.out.println(datos.getInt("codigo"));
        }
    }

    /*public void pintarDiaMenu(int i) {
        System.out.println("DESAYUNO: ");
        for (int j = 0; j < datosDia.get(i).getDesayuno().size(); j++) {
            System.out.println(datosDia.get(i).getDesayuno().get(j));
        }
        System.out.println("COMIDA: ");
        for (int j = 0; j < datosDia.get(i).getComida().size(); j++) {
            System.out.println(datosDia.get(i).getComida().get(j));
        }
        System.out.println("MERIENDA: ");
        for (int j = 0; j < datosDia.get(i).getCena().size(); j++) {
            System.out.println(datosDia.get(i).getCena().get(j));
        }
        System.out.println("CENA: ");
        for (int j = 0; j < datosDia.get(i).getMerienda().size(); j++) {
            System.out.println(datosDia.get(i).getMerienda().get(j));
        }
    }

    public void pintarMenu() {
        System.out.println("SE VA PINTAR EL MENU:");
        for (int i = 0; i < 7; ++i) {
            System.out.println("DIA: " +i);
            System.out.println("DESAYUNO: ");
            for (int j = 0; j < datosDia.get(i).getDesayuno().size(); j++) {
                System.out.println(datosDia.get(i).getDesayuno().get(j));
            }
            System.out.println("COMIDA: ");
            for (int j = 0; j < datosDia.get(i).getComida().size(); j++) {
                System.out.println(datosDia.get(i).getComida().get(j));
            }
            System.out.println("MERIENDA: ");
            for (int j = 0; j < datosDia.get(i).getCena().size(); j++) {
                System.out.println(datosDia.get(i).getCena().get(j));
            }
            System.out.println("CENA: ");
            for (int j = 0; j < datosDia.get(i).getMerienda().size(); j++) {
                System.out.println(datosDia.get(i).getMerienda().get(j));
            }
        }
    }*/

    private void Modificar_pantalla(int dia_selec) {
        String des_query = "", com_query = "", cen_query = "", mer_query = "";
        DatosDia aux = datosDia.get(dia_selec);
        for (int i = 0; i < aux.getDesayuno().size(); i++) {
            des_query += aux.getDesayuno().get(i) +"\n";
            //System.out.println(des_query);
        }
        for (int i = 0; i < aux.getComida().size(); i++) {
            com_query += aux.getComida().get(i) +"\n";
            //System.out.println(com_query);
        }
        for (int i = 0; i < aux.getMerienda().size(); i++) {
            mer_query += aux.getMerienda().get(i) +"\n";
            //System.out.println(mer_query);
        }
        for (int i = 0; i < aux.getCena().size(); i++) {
            cen_query += aux.getCena().get(i) +"\n";
            //System.out.println(cen_query);
        }
        des.setText(des_query);
        com.setText(com_query);
        mer.setText(mer_query);
        cen.setText(cen_query);
    }
}