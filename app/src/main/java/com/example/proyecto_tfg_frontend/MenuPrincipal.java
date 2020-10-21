package com.example.proyecto_tfg_frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuPrincipal extends AppCompatActivity {

    private BottomNavigationView menu;
    private FragmentTransaction transaction;
    private Fragment usuario, progreso, menu_perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        usuario = new Perfil();
        progreso = new ProgresoFragment();
        menu_perfil = new menu_perfil();

        getSupportFragmentManager().beginTransaction().add(R.id.menu, menu_perfil).commit();
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

                }
                else {

                }
                return true;
            }
        });
    }
}