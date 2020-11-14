package com.example.proyecto_tfg_frontend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AdaptadorDatosAlimentos extends RecyclerView.Adapter<com.example.proyecto_tfg_frontend.AdaptadorDatosAlimentos.ViewHolderDatosAlimentos> implements View.OnClickListener {

    private ArrayList <Alimento> list;
    private Context c;
    private View.OnClickListener listener;

    public AdaptadorDatosAlimentos(ArrayList<Alimento> listDatos, Context contexto) {
        this.list = listDatos;
        c = contexto;
    }

    @NonNull
    @Override
    public ViewHolderDatosAlimentos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_alimentos,null,false);
        view.setOnClickListener(this);
        return new AdaptadorDatosAlimentos.ViewHolderDatosAlimentos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatosAlimentos holder, int position) {
        holder.asignarDatos(list.get(position).getNombre_plato_ing());
    }

    @Override
    public int getItemCount() {
        return list.size();
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

    public class ViewHolderDatosAlimentos extends RecyclerView.ViewHolder {

        TextView info;

        public ViewHolderDatosAlimentos(@NonNull View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.info_alimento);
        }

        public void asignarDatos(String dato) {
            info.setText(dato);
        }
    }
}
