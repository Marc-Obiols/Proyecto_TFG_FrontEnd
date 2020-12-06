package com.example.proyecto_tfg_frontend;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class historial_alimentacion extends Fragment implements Interfaz{

    private RecyclerView recycler;
    private ArrayList<String> list;
    private AdaptadorDatosMedidas adaptador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historial_alimentacion, container, false);

        recycler = (RecyclerView) view.findViewById(R.id.historial);
        recycler.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL,false));
        SeparatorDecoration decoration = new SeparatorDecoration(getActivity(), R.color.Transparente, 5f);
        recycler.addItemDecoration(decoration);

        list = new ArrayList<>();
        Connection con = new Connection(this);
        con.execute("http://192.168.0.14:3000/alimentacion/historial/" + UsuarioSingleton.getInstance().getId(), "GET", null);
        return view;
    }

    @Override
    public void Respuesta(JSONObject datos) {
        try {
            if(datos.getInt("codigo") == 200) {
                SimpleDateFormat sdfr = new SimpleDateFormat("dd-MM-yyyy");
                JSONArray resp = datos.getJSONArray("array");
                for (int i=0; i<resp.length(); i++) {
                    list.add(i, resp.getString(i).substring(0,10));
                }
                adaptador = new AdaptadorDatosMedidas(list);
                recycler.setAdapter(adaptador);
                adaptador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), Alimentacion_concreta.class);
                        i.putExtra("pos", recycler.getChildAdapterPosition(v));
                        startActivity(i);
                    }
                });
            }
            else {
                System.out.println("ERROR");
            }
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }
}