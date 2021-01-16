package com.example.proyecto_tfg_frontend.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_tfg_frontend.R;

import java.util.ArrayList;

public class AdaptadorDatosMedidas extends RecyclerView.Adapter<AdaptadorDatosMedidas.ViewHolderDatosMedidas> implements View.OnClickListener {

    ArrayList<String> listDatos;
    private View.OnClickListener listener;

    public AdaptadorDatosMedidas(ArrayList<String> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public AdaptadorDatosMedidas.ViewHolderDatosMedidas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_alimentos,null,false);
        view.setOnClickListener(this);
        return new AdaptadorDatosMedidas.ViewHolderDatosMedidas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorDatosMedidas.ViewHolderDatosMedidas holder, int position) {
        holder.asignarDatos(listDatos.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        System.out.println("HOLA1");
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        System.out.println("HOLA2");
        if (listener!=null) {
            System.out.println("HOLA3");
            listener.onClick(v);
        }
    }

    public class ViewHolderDatosMedidas extends RecyclerView.ViewHolder {

        TextView info;
        int pos;

        public ViewHolderDatosMedidas(@NonNull View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.info_alimento);
        }

        public void asignarDatos(String dato, int position) {
            info.setText(dato);
            pos = position;
        }
    }
}
