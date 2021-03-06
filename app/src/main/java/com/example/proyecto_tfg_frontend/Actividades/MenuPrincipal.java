package com.example.proyecto_tfg_frontend.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.proyecto_tfg_frontend.Fragments.Perfil;
import com.example.proyecto_tfg_frontend.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuPrincipal extends AppCompatActivity {

    private BottomNavigationView menu;
    private FragmentTransaction transaction;
    private Fragment usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        usuario = new Perfil();

        getSupportFragmentManager().beginTransaction().add(R.id.pantalla, usuario).commit();
        transaction = getSupportFragmentManager().beginTransaction();

        menu = (BottomNavigationView) findViewById(R.id.menu_principal);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menu_perfil) {

                }
                else if (item.getItemId() == R.id.menu_dietas) {

                }
                else if (item.getItemId() == R.id.menu_ejercicios) {
                    Intent i = new Intent(getApplicationContext(), Ejercicios.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(getApplicationContext(), Rutinas.class);
                    startActivity(i);
                }
                return true;
            }
        });
    }
}