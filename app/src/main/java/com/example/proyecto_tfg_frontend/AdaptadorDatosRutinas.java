package com.example.proyecto_tfg_frontend;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorDatosRutinas extends RecyclerView.Adapter<AdaptadorDatosRutinas.ViewHolderDatosRutinas> {

    ArrayList<Pair<String, String>> listDatos;
    private Context c;
    private Dialog pantalla_eliminar;
    private int llamada;
    //private Interfaz interfaz;

    public AdaptadorDatosRutinas(ArrayList<Pair<String, String>> listDatos, Context contexto) {
        this.listDatos = listDatos;
        c = contexto;
        llamada = 3;
        pantalla_eliminar = new Dialog(c);
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

    public int getLlamada() {
        return llamada;
    }

    /*@Override
    public void Respuesta(JSONObject datos) throws JSONException {
        try {
            if(datos.getInt("codigo") == 200) {
                System.out.println(datos.toString());
                JSONArray nombres = datos.getJSONArray("array");
                ArrayList<Pair<String,String>> auxiliar = new ArrayList<>();
                if (nombres.length() != 0) {
                    for (int i = 0; i < nombres.length(); i++) {
                        JSONObject aux1 = nombres.getJSONObject(i);
                        auxiliar.add(i, new Pair<>(aux1.getString("nombre"), aux1.getString("id")));
                    }
                }
                listDatos = auxiliar;
            }
            else {
                System.out.println("ERROR AL COMUNICARSE CON SERVER");
            }
        } catch (JSONException err) {
                err.printStackTrace();
            }
    }*/


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

            modificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button confirmar, cancelar;
                    pantalla_eliminar.setContentView(R.layout.popup_eliminar_rutina);
                    confirmar = (Button) pantalla_eliminar.findViewById(R.id.confirmar);
                    cancelar = (Button) pantalla_eliminar.findViewById(R.id.cancelar);

                    confirmar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject req = new JSONObject();
                            try {
                                req.put("id_user", UsuarioSingleton.getInstance().getId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Connection con = new Connection((Interfaz) c);
                            con.execute("http://169.254.145.10:3000/rutina/eliminar/" + listDatos.get(posicion).second, "POST", req.toString());
                            pantalla_eliminar.dismiss();
                        }
                    });

                    cancelar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pantalla_eliminar.dismiss();
                        }
                    });
                    pantalla_eliminar.show();
                }
            });
        }

        public void asignarDatos(Pair<String, String> dato, int position) {
            nombre_rutina.setText(dato.first);
            posicion = position;
        }
    }
}
