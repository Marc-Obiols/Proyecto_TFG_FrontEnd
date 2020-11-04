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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorDatosEjercicioRutina extends RecyclerView.Adapter <AdaptadorDatosEjercicioRutina.ViewHolderDatosEjercicioRutina>{

    ArrayList<Pair<String, Integer>> listDatos;
    private Context c;

    public AdaptadorDatosEjercicioRutina(ArrayList<Pair<String, Integer>> listDatos, Context contexto) {
        this.listDatos = listDatos;
        c = contexto;
    }

    @NonNull
    @Override
    public ViewHolderDatosEjercicioRutina onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ejercicios_rutina,null,false);
        return new ViewHolderDatosEjercicioRutina(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatosEjercicioRutina holder, int position) {
        holder.asignarDatos(listDatos.get(position).first, listDatos.get(position).second);
    }


    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class ViewHolderDatosEjercicioRutina extends RecyclerView.ViewHolder {

        TextView nombre_ejercicio;
        TextView tiempo_ejercicio;
        CircleImageView foto_ejercicio;
        CircleImageView foto_mod;
        CircleImageView foto_eliminar;

        public ViewHolderDatosEjercicioRutina(@NonNull View itemView) {
            super(itemView);
            nombre_ejercicio = itemView.findViewById(R.id.nombre_ejercicio);
            foto_ejercicio = itemView.findViewById(R.id.foto_ejercicio);
            foto_mod = itemView.findViewById(R.id.modficar_ejer);
            foto_eliminar = itemView.findViewById(R.id.eliminar_ejer);
            tiempo_ejercicio = itemView.findViewById(R.id.tiempo_ejercicio);

            nombre_ejercicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(c.getApplicationContext(), Ejercicio.class);
                    System.out.println(nombre_ejercicio.getText().toString());
                    i.putExtra("ejercicio", nombre_ejercicio.getText().toString());
                    c.startActivity(i);
                }
            });
        }

        public void asignarDatos(String dato , Integer tiempo) {
            nombre_ejercicio.setText(dato);
            tiempo_ejercicio.setText(tiempo.toString() + " seg");
            Picasso.get().load("http://169.254.145.10:3000/ejercicio/image/" + nombreToUrl(dato)).into(foto_ejercicio);
        }

        private String nombreToUrl(String nombre) {
            String res = "";
            for (int i=0; i < nombre.length(); i++) {
                char aux = nombre.charAt(i);
                if (aux != ' ') res = res + aux;
                else res = res + "%20";
            }
            return res;
        }
    }
}
