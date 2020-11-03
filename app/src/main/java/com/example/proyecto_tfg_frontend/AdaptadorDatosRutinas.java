package com.example.proyecto_tfg_frontend;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorDatosRutinas extends RecyclerView.Adapter<AdaptadorDatosRutinas.ViewHolderDatosRutinas> {

    ArrayList<Pair<String, String>> listDatos;
    private Context c;

    public AdaptadorDatosRutinas(ArrayList<Pair<String, String>> listDatos, Context contexto) {
        this.listDatos = listDatos;
        c = contexto;
    }

    @NonNull
    @Override
    public ViewHolderDatosRutinas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_rutina,null,false);
        return new ViewHolderDatosRutinas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatosRutinas holder, int position) {
        holder.asignarDatos(listDatos.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class ViewHolderDatosRutinas extends RecyclerView.ViewHolder {
        TextView nombre_rutina;
        CircleImageView modificar;
        CircleImageView eliminar;
        int posicion;

        public ViewHolderDatosRutinas(@NonNull View itemView) {
            super(itemView);
            nombre_rutina = itemView.findViewById(R.id.nombre_rutina);
            modificar = itemView.findViewById(R.id.modficar_rut);
            eliminar = itemView.findViewById(R.id.eliminar_rut);

            nombre_rutina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(c.getApplicationContext(), Rutina.class);
                    i.putExtra("rutina", listDatos.get(posicion).second);
                    c.startActivity(i);
                }
            });
        }

        public void asignarDatos(Pair<String, String> dato, int position) {
            nombre_rutina.setText(dato.first);
            posicion = position;
        }
    }
}
