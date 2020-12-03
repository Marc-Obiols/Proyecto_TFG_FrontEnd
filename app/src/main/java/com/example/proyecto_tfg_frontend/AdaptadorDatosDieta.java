package com.example.proyecto_tfg_frontend;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class AdaptadorDatosDieta extends RecyclerView.Adapter<AdaptadorDatosDieta.ViewHolderDatosDieta> implements View.OnClickListener{

    ArrayList<Pair<String, String>> listDatos;
    private View.OnClickListener listener;


    public AdaptadorDatosDieta(ArrayList<Pair<String, String>> listDatos) {
        this.listDatos = listDatos;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null) {
            listener.onClick(v);
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderDatosDieta onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_dietas,null,false);
        view.setOnClickListener(this);
        return new AdaptadorDatosDieta.ViewHolderDatosDieta(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatosDieta holder, int position) {
        holder.asignarDatos(listDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class ViewHolderDatosDieta extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView nombre;

        public ViewHolderDatosDieta(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre_dieta);
            img = itemView.findViewById(R.id.imagen_dieta);
        }

        public void asignarDatos(Pair<String, String> dato) {
            nombre.setText(dato.first);
            img.setImageResource(R.drawable.dieta_1);
        }
    }
}
