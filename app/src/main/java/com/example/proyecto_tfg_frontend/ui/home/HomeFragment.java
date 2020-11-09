package com.example.proyecto_tfg_frontend.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.proyecto_tfg_frontend.Activity_progeso;
import com.example.proyecto_tfg_frontend.Ejercicios;
import com.example.proyecto_tfg_frontend.R;
import com.example.proyecto_tfg_frontend.Rutinas;
import com.example.proyecto_tfg_frontend.datos_user;

public class HomeFragment extends Fragment {

    private CardView rutinas, info, progeso, contador, dietas, ejercicios;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        rutinas = (CardView) root.findViewById(R.id.Rutinas);
        info = (CardView) root.findViewById(R.id.datos_user);
        progeso = (CardView) root.findViewById(R.id.progreso);
        contador = (CardView) root.findViewById(R.id.Contador_cal);
        dietas = (CardView) root.findViewById(R.id.Dietas);
        ejercicios = (CardView) root.findViewById(R.id.Ejericios);

        rutinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Rutinas.class);
                getActivity().startActivity(intent);
            }
        });

        progeso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_progeso.class);
                getActivity().startActivity(intent);
            }
        });

        ejercicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Ejercicios.class);
                getActivity().startActivity(intent);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), datos_user.class);
                getActivity().startActivity(intent);
            }
        });
        return root;
    }
}