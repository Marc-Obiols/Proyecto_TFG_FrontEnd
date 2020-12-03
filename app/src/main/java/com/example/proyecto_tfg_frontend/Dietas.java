package com.example.proyecto_tfg_frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Dietas extends AppCompatActivity implements Interfaz {

    private ArrayList<Pair<String,String>> list;
    private RecyclerView recycler;
    private AdaptadorDatosDieta adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietas);

        recycler = (RecyclerView) findViewById(R.id.lista_dietas);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        SeparatorDecoration decoration = new SeparatorDecoration(this, R.color.Transparente, 10f);
        recycler.addItemDecoration(decoration);

        Connection con = new Connection(this);
        con.execute("http://192.168.0.14:3000/dietas", "GET", null);
    }

    @Override
    public void Respuesta(JSONObject datos) throws JSONException {
        if(datos.getInt("codigo") == 200) {
            list = new ArrayList<>();
            JSONArray inf_dietas = datos.getJSONArray("array");
            String nombre_die, id_eje;
            for(int i = 0; i < inf_dietas.length(); i++) {
                JSONObject aux = (JSONObject) inf_dietas.get(i);
                nombre_die = aux.getString("nombre");
                id_eje = aux.getString("id");
                list.add(new Pair<>(nombre_die, id_eje));
            }
            adaptador = new AdaptadorDatosDieta(list);
            adaptador.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Dieta.class);
                    i.putExtra("id_dieta", list.get(recycler.getChildAdapterPosition(v)).second);
                    startActivity(i);
                }
            });
            recycler.setAdapter(adaptador);
        }
        else {
            System.out.println(datos.getInt("codigo"));
        }
    }
}