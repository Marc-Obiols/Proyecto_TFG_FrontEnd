package com.example.proyecto_tfg_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Ejecucion_rutina extends AppCompatActivity {

    //private TTSManager ttsManager;
    //private TextToSpeech mTts = null;
    private CountDownTimer cuenta;
    boolean descanso;
    private ProgressBar contador_descanso, contador_ejercicio;
    private TextView cuenta_atras, cuenta_atras_ejercicio;
    private int tiempo_descanso, tiempo_ejercicio, tiempo_total, bucle, tiempo_total_aux;
    private ArrayList<String> listNEjercicios;
    private ArrayList<Integer> listTEjercicios;
    private ImageView imagen_ejercicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejecucion_rutina);

        System.out.println("HOLA EMPIEZO");
        //mTts = new TextToSpeech(this, this);
        //ttsManager = new TTSManager();
        //ttsManager.init(this);

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

        //SpeakOut("let's start");
        empezar();
    }

    private void empezar() {

        bucle = 0;
        tiempo_total_aux = tiempo_total;
        descanso = false;
        long aux = tiempo_total * 1000;
        Picasso.get().load("http://169.254.145.10:3000/ejercicio/image/"+nombreToUrl(listNEjercicios.get(0))).into(imagen_ejercicio);
        cuenta = new CountDownTimer(aux, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                System.out.println("SISUSISU");
                int seg = (int) (millisUntilFinished/1000);
                if (!descanso) {
                    contador_ejercicio.setProgress((tiempo_total_aux-seg)*100/listTEjercicios.get(bucle));
                    cuenta_atras_ejercicio.setText(Integer.toString((tiempo_total_aux-seg-listTEjercicios.get(bucle))*-1));
                    if (tiempo_total_aux-seg == listTEjercicios.get(bucle)) {
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
                }
                else {
                    contador_descanso.setProgress((tiempo_total_aux-seg)*100/tiempo_descanso);
                    cuenta_atras.setText(Integer.toString((tiempo_total_aux-seg-tiempo_descanso)*-1));
                    if (tiempo_total_aux-seg == tiempo_descanso) {
                        contador_descanso.setVisibility(View.INVISIBLE);
                        cuenta_atras.setVisibility(View.INVISIBLE);
                        imagen_ejercicio.setVisibility(View.VISIBLE);
                        cuenta_atras_ejercicio.setVisibility(View.VISIBLE);
                        contador_ejercicio.setVisibility(View.VISIBLE);

                        bucle +=1;
                        Picasso.get().load("http://169.254.145.10:3000/ejercicio/image/"+nombreToUrl(listNEjercicios.get(bucle))).into(imagen_ejercicio);
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
        /*for(int i = 0; i < listNEjercicios.size(); i++) {
            System.out.println("NOMBRE EJERCICIO: ");
            System.out.println(listNEjercicios.get(i));
            aux = tiempo_descanso*1000;
            CountDownTimer cuenta2 = new CountDownTimer(aux, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int seg = (int) (millisUntilFinished/1000);
                    contador_descanso.setProgress((tiempo_descanso-seg)*100/tiempo_descanso);
                    cuenta_atras.setText(Integer.toString((seg)));
                }

                @Override
                public void onFinish() {
                    contador_descanso.setProgress(100);
                    cuenta_atras.setText(Integer.toString((0)));
                    contador_descanso.setVisibility(View.INVISIBLE);
                    cuenta_atras.setVisibility(View.INVISIBLE);

                    imagen_ejercicio.setVisibility(View.VISIBLE);
                    cuenta_atras_ejercicio.setVisibility(View.VISIBLE);
                    contador_ejercicio.setVisibility(View.VISIBLE);
                }
            }.start();

            tiempo_ejercicio = listTEjercicios.get(i);
            aux = listTEjercicios.get(i) * 1000;
            Picasso.get().load("http://169.254.145.10:3000/ejercicio/image/"+nombreToUrl(listNEjercicios.get(i))).into(imagen_ejercicio);
            CountDownTimer cuenta1 = new CountDownTimer(aux, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int seg = (int) (millisUntilFinished/1000);
                    contador_ejercicio.setProgress((tiempo_ejercicio-seg)*100/tiempo_ejercicio);
                    cuenta_atras_ejercicio.setText(Integer.toString((seg)));
                }

                @Override
                public void onFinish() {
                    contador_ejercicio.setProgress(100);
                    cuenta_atras_ejercicio.setText(Integer.toString((0)));

                    contador_descanso.setVisibility(View.VISIBLE);
                    cuenta_atras.setVisibility(View.VISIBLE);

                    imagen_ejercicio.setVisibility(View.INVISIBLE);
                    cuenta_atras_ejercicio.setVisibility(View.INVISIBLE);
                    contador_ejercicio.setVisibility(View.INVISIBLE);
                }
            }.start();
        }*/
    }

    @Override
    protected void onDestroy() {

        /*if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }*/
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
        System.out.println("ESTOY EN PAUSA");
        cuenta.cancel();
        super.onPause();
    }

    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void SpeakOut(String decir) {
        mTts.speak(decir, TextToSpeech.QUEUE_FLUSH, null, "");
    }*/

    /*@Override
    public void onInit(int status) {
        /*if(status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.US);
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The language specified is not supported");
            }
        }
      } */
}