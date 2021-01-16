package com.example.proyecto_tfg_frontend.Actividades;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.proyecto_tfg_frontend.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class Ejecucion_rutina extends AppCompatActivity {


    private TextToSpeech mTts = null;
    private long aux; //representa lo que falta
    private FloatingActionButton play;
    private CountDownTimer cuenta;
    private boolean descanso, pausa;
    private ProgressBar contador_descanso, contador_ejercicio;
    private TextView cuenta_atras, cuenta_atras_ejercicio;
    private int tiempo_descanso, tiempo_total, bucle, tiempo_total_aux;
    private ArrayList<String> listNEjercicios;
    private ArrayList<Integer> listTEjercicios;
    private ImageView imagen_ejercicio;
    private final String URL = "https://vidasana.herokuapp.com/";


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
        setContentView(R.layout.activity_ejecucion_rutina);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        pausa = false;

        mTts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    System.out.println("Tengo idioma");
                    mTts.setLanguage(Locale.US);
                    mTts.setPitch(1);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        System.out.println("Voy a hablar");
                        mTts.speak("Lets start the work out", TextToSpeech.QUEUE_FLUSH, null, null);
                    }
                }
            }
        });

        play = (FloatingActionButton) findViewById(R.id.fab);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pausa) { // ME PONGO EN PAUSA

                    pausa = true;
                    cuenta.cancel();
                    play.setImageResource(R.drawable.ic_pause);
                }
                else { //VUELVO A HACER EJERCICIO
                    play.setImageResource(R.drawable.ic_media_play);
                    pausa = false;
                    empezar();
                }
            }
        });
        imagen_ejercicio = (ImageView) findViewById(R.id.imagen_ejercicio);
        cuenta_atras_ejercicio = (TextView) findViewById(R.id.cuenta_atras_ejercicio);
        contador_ejercicio = (ProgressBar) findViewById(R.id.contador_ejercicio);

        contador_descanso = (ProgressBar) findViewById(R.id.contador_descanso);
        cuenta_atras = (TextView) findViewById(R.id.cuenta_atras);
        contador_descanso.setVisibility(View.INVISIBLE);
        cuenta_atras.setVisibility(View.INVISIBLE);

        tiempo_descanso = getIntent().getIntExtra("tiempo_descanso", 0);
        listNEjercicios = getIntent().getStringArrayListExtra("ejercicios_nombres");
        listTEjercicios = getIntent().getIntegerArrayListExtra("ejercicios_tiempos");
        tiempo_total = getIntent().getIntExtra("tiempo_total", 0);

        bucle = 0;
        tiempo_total_aux = tiempo_total;
        descanso = false;
        aux = tiempo_total * 1000;
        Picasso.get().load(URL+"ejercicio/image/"+nombreToUrl(listNEjercicios.get(0))).into(imagen_ejercicio);
        empezar();
    }

    private void empezar() {

        cuenta = new CountDownTimer(aux, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                aux = millisUntilFinished;
                int seg = (int) (millisUntilFinished / 1000);
                if (!descanso) {
                    contador_ejercicio.setProgress((tiempo_total_aux - seg) * 100 / listTEjercicios.get(bucle));
                    cuenta_atras_ejercicio.setText(Integer.toString((tiempo_total_aux - seg - listTEjercicios.get(bucle)) * -1));
                    if (tiempo_total_aux - seg == listTEjercicios.get(bucle)) {
                        contador_descanso.setVisibility(View.VISIBLE);
                        cuenta_atras.setVisibility(View.VISIBLE);
                        imagen_ejercicio.setVisibility(View.INVISIBLE);
                        cuenta_atras_ejercicio.setVisibility(View.INVISIBLE);
                        contador_ejercicio.setVisibility(View.INVISIBLE);

                        contador_descanso.setProgress(0);
                        cuenta_atras.setText(Integer.toString(tiempo_descanso));
                        tiempo_total_aux -= listTEjercicios.get(bucle);
                        descanso = true;
                    }
                } else {
                    contador_descanso.setProgress((tiempo_total_aux - seg) * 100 / tiempo_descanso);
                    cuenta_atras.setText(Integer.toString((tiempo_total_aux - seg - tiempo_descanso) * -1));
                    if (tiempo_total_aux - seg == tiempo_descanso) {
                        contador_descanso.setVisibility(View.INVISIBLE);
                        cuenta_atras.setVisibility(View.INVISIBLE);
                        imagen_ejercicio.setVisibility(View.VISIBLE);
                        cuenta_atras_ejercicio.setVisibility(View.VISIBLE);
                        contador_ejercicio.setVisibility(View.VISIBLE);

                        bucle += 1;
                        Picasso.get().load(URL+"ejercicio/image/" + nombreToUrl(listNEjercicios.get(bucle))).into(imagen_ejercicio);
                        contador_ejercicio.setProgress(0);
                        cuenta_atras_ejercicio.setText(Integer.toString(listTEjercicios.get(bucle)));
                        tiempo_total_aux -= tiempo_descanso;
                        descanso = false;
                    }
                }
                //contador_descanso.setProgress((tiempo_descanso-seg)*100/tiempo_descanso);
                //cuenta_atras.setText(Integer.toString((seg)));
            }

            @Override
            public void onFinish() {
                Toast.makeText(Ejecucion_rutina.this, "Buen Trabajo", Toast.LENGTH_LONG).show();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
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
    protected void onPause() {
        if (cuenta != null)
            cuenta.cancel();
        if(mTts !=null){
            mTts.stop();
            mTts.shutdown();
        }
        super.onPause();
    }

}