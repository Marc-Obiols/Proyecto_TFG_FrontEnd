package com.example.proyecto_tfg_frontend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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