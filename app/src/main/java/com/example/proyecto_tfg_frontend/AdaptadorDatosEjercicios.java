package com.example.proyecto_tfg_frontend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorDatosEjercicios extends RecyclerView.Adapter<AdaptadorDatosEjercicios.ViewHolderDatosEjercicio> implements View.OnClickListener {

    ArrayList <String> listDatos;
    private View.OnClickListener listener;

    public AdaptadorDatosEjercicios(ArrayList<String> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public ViewHolderDatosEjercicio onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ejercicios,null,false);
        view.setOnClickListener(this);
        return new ViewHolderDatosEjercicio(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatosEjercicio holder, int position) {
        holder.asignarDatos(listDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null) {
            listener.onClick(v);
        }
    }

    public class ViewHolderDatosEjercicio extends RecyclerView.ViewHolder {

        TextView nombre_ejercicio;
        CircleImageView foto_ejercicio;

        private String nombreToUrl(String nombre) {
            String res = "";
            for (int i=0; i < nombre.length(); i++) {
                char aux = nombre.charAt(i);
                if (aux != ' ') res = res + aux;
                else res = res + "%20";
            }
            System.out.println(res);
            return res;
        }

        public ViewHolderDatosEjercicio(@NonNull View itemView) {
            super(itemView);
            nombre_ejercicio = itemView.findViewById(R.id.nombre_ejercicio);
            foto_ejercicio = itemView.findViewById(R.id.foto_ejercicio);
        }

        public void asignarDatos(String dato) {
            nombre_ejercicio.setText(dato);
            Picasso.get().load("http://169.254.145.10:3000/ejercicio/image/"+nombreToUrl(dato)).into(foto_ejercicio);
        }
    }
}
