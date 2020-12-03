package com.example.proyecto_tfg_frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Ejercicio extends AppCompatActivity implements Interfaz{

    private TextView nom, musc, est;
    private ImageView imagen_ej;
    private YouTubePlayerView video;

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        nom = (TextView) findViewById(R.id.nombre_ejercicio);
        musc = (TextView) findViewById(R.id.musculos);
        est = (TextView) findViewById(R.id.estiramiento);
        imagen_ej = (ImageView) findViewById(R.id.imagen_ejercicio);

        //YOUTUBE
        video = (YouTubePlayerView) findViewById(R.id.video);

        //***********************

        String nombre = getIntent().getStringExtra("ejercicio");
        Connection con = new Connection(this);
        con.execute("http://192.168.0.14:3000/ejercicio/" + nombre, "GET", null);

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
                Picasso.get().load("http://192.168.0.14:3000/ejercicio/image/"+nombreToUrl(datos.getString("nombre"))).into(imagen_ej);
                JSONArray array = datos.getJSONArray("musculos");
                String nombres_musc = "";
                for (int i = 0; i < array.length(); i++) {
                    nombres_musc += " " + array.get(i);
                }
                musc.setText("Musculos:" + nombres_musc);
                getLifecycle().addObserver(video);
                video.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        String videoId = null;
                        try {
                            videoId = datos.getString("id_youtube");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        youTubePlayer.loadVideo(videoId, 0);
                    }
                });
            }
            else {
                System.out.println("CODIGO DE ERROR");
            }
        } catch (JSONException err) {
            err.printStackTrace();
        }
    }
}