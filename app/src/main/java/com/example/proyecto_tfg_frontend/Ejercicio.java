package com.example.proyecto_tfg_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Ejercicio extends AppCompatActivity implements Interfaz{

    private TextView nom, musc, est;
    private ImageView imagen_ej;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio);

        nom = (TextView) findViewById(R.id.nombre_ejercicio);
        musc = (TextView) findViewById(R.id.musculos);
        est = (TextView) findViewById(R.id.estiramiento);
        imagen_ej = (ImageView) findViewById(R.id.imagen_ejercicio);

        String nombre = getIntent().getStringExtra("ejercicio");
        Connection con = new Connection(this);
        con.execute("http://169.254.145.10:3000/ejercicio/" + nombre, "GET", null);

    }

    private String nombreToUrl(String nombre) {
        String res = "";
        for (int i=0; i < nombre.length(); i++) {
            char aux = nombre.charAt(i);
            if (aux != ' ') res = res + aux;
            else res = res + "%20";
        }
        return res;
    }

    @Override
    public void Respuesta(JSONObject datos) throws JSONException {
        try {
            if(datos.getInt("codigo") == 200) {
                nom.setText("Nombre: " + datos.getString("nombre"));
                Boolean aux = datos.getBoolean("estiramiento");
                if (aux) est.setText("Tipo ejercicio: Estiramiento");
                else est.setText("Tipo ejercicio: Aerobico");
                Picasso.get().load("http://169.254.145.10:3000/ejercicio/image/"+nombreToUrl(datos.getString("nombre"))).into(imagen_ej);
                JSONArray array = datos.getJSONArray("musculos");
                String nombres_musc = "";
                for (int i = 0; i < array.length(); i++) {
                    nombres_musc += " " + array.get(i);
                }
                musc.setText("Musculos:" + nombres_musc);
            }
            else {
                System.out.println("CODIGO DE ERROR");
            }
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }
}