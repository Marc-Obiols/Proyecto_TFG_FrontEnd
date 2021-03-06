package com.example.proyecto_tfg_frontend.Adaptadores;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_tfg_frontend.Clases.Alimento;
import com.example.proyecto_tfg_frontend.Connection;
import com.example.proyecto_tfg_frontend.Interfaz;
import com.example.proyecto_tfg_frontend.R;
import com.example.proyecto_tfg_frontend.UsuarioSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorDatosAlimentosSeleccionados extends RecyclerView.Adapter<AdaptadorDatosAlimentosSeleccionados.ViewHolderDatosAlimentosSeleccionados> {

    private ArrayList <Alimento> list;
    private Context c;
    private Interfaz encargado;
    private Dialog pantalla;
    private int llamada;
    private final String URL = "https://vidasana.herokuapp.com/";

    public void setLlamada(int llamada) {
        this.llamada = llamada;
    }

    public AdaptadorDatosAlimentosSeleccionados(ArrayList<Alimento> listDatos, Context contexto, Interfaz encargado) {
        this.list = listDatos;
        c = contexto;
        this.encargado = encargado;
        pantalla = new Dialog(c);
    }

    @NonNull
    @Override
    public ViewHolderDatosAlimentosSeleccionados onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_alimento_seleccionado,null,false);
        return new AdaptadorDatosAlimentosSeleccionados.ViewHolderDatosAlimentosSeleccionados(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatosAlimentosSeleccionados holder, int position) {
        holder.asignarDatos(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public int getLlamada() {
        return llamada;
    }


    public class ViewHolderDatosAlimentosSeleccionados extends RecyclerView.ViewHolder {

        TextView info, kcal_intro;
        CircleImageView delete;
        int posicion;

        public ViewHolderDatosAlimentosSeleccionados(@NonNull View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.info_alimento);
            kcal_intro = itemView.findViewById(R.id.kcal_intro);
            delete = itemView.findViewById(R.id.delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button confirmar, cancelar;
                    TextView contenido;
                    pantalla.setContentView(R.layout.popup_eliminar_rutina);
                    confirmar = (Button) pantalla.findViewById(R.id.confirmar);
                    cancelar = (Button) pantalla.findViewById(R.id.cancelar);
                    contenido = (TextView) pantalla.findViewById(R.id.contenido);
                    contenido.setText("¿Seguro que quieres eliminar el alimento?");

                    confirmar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject req = new JSONObject();
                            try {
                                req.put("id_user", UsuarioSingleton.getInstance().getId());
                                req.put("posicion", posicion);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            llamada = 2;
                            Connection con = new Connection(encargado);
                            con.execute(URL+"alimentacion/removeAlimento", "POST", req.toString());
                            pantalla.dismiss();
                        }
                    });

                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pantalla.dismiss();
                        }
                    });
                    pantalla.show();
                }
            });
        }

        public void asignarDatos(Alimento dato, int position) {
            info.setText(dato.getNombre_plato_ing());
            kcal_intro.setText( "Kcal consumidas: " + dato.getKcal().toString());
            posicion = position;
        }
    }
}
