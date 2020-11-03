package com.example.proyecto_tfg_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Rutina extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutina);
        System.out.println(getIntent().getStringExtra("rutina"));
    }
}