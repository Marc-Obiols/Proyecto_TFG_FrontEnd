package com.example.proyecto_tfg_frontend.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.proyecto_tfg_frontend.Fragments.Medidas;
import com.example.proyecto_tfg_frontend.R;
import com.example.proyecto_tfg_frontend.Fragments.historial_alimentacion;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Activity_progeso extends AppCompatActivity {

    private BottomNavigationView menu;
    private FragmentTransaction transaction;
    private Fragment Medidas, hist_al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progeso);

        Medidas = new Medidas();
        hist_al = new historial_alimentacion();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.pantalla, Medidas).commit();

        menu = (BottomNavigationView) findViewById(R.id.menu_hist);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.Pesos) {
                    transaction = getSupportFragmentManager().beginTransaction();
                   transaction.replace(R.id.pantalla, Medidas);
                   transaction.commit();
                }
                else if (item.getItemId() == R.id.Alimentacion) {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.pantalla, hist_al);
                    transaction.commit();
                }
                return true;
            }
        });
    }
}