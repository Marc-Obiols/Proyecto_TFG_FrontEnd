package com.example.proyecto_tfg_frontend.Adaptadores;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_tfg_frontend.Connection;
import com.example.proyecto_tfg_frontend.Actividades.Ejercicio;
import com.example.proyecto_tfg_frontend.Interfaz;
import com.example.proyecto_tfg_frontend.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorDatosEjercicioRutina extends RecyclerView.Adapter <AdaptadorDatosEjercicioRutina.ViewHolderDatosEjercicioRutina>{

    ArrayList<Pair<String, Integer>> listDatos;
    private Context c;
    private Dialog pantalla;
    private int llamada, posicion, tipo;
    private String id_rutina;
    private final String URL = "https://vidasana.herokuapp.com/";

    public AdaptadorDatosEjercicioRutina(ArrayList<Pair<String, Integer>> listDatos, Context contexto, String id_rutina, int tipo) {
        this.listDatos = listDatos;
        c = contexto;
        pantalla = new Dialog(c);
        this.id_rutina = id_rutina;
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public ViewHolderDatosEjercicioRutina onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_ejercicios_rutina,null,false);
        return new ViewHolderDatosEjercicioRutina(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatosEjercicioRutina holder, int position) {
        holder.asignarDatos(listDatos.get(position).first, listDatos.get(position).second, position);
    }


    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public int getLlamada() {
        return llamada;
    }

    public int getPosicion() {
        return posicion;
    }

    public class ViewHolderDatosEjercicioRutina extends RecyclerView.ViewHolder {

        TextView nombre_ejercicio;
        TextView tiempo_ejercicio;
        CircleImageView foto_ejercicio;
        CircleImageView foto_mod;
        CircleImageView foto_eliminar;
        int val;
        int pos;

        public ViewHolderDatosEjercicioRutina(@NonNull View itemView) {
            super(itemView);
            nombre_ejercicio = itemView.findViewById(R.id.nombre_ejercicio);
            foto_ejercicio = itemView.findViewById(R.id.foto_ejercicio);
            foto_mod = itemView.findViewById(R.id.modficar_ejer);
            foto_eliminar = itemView.findViewById(R.id.eliminar_ejer);
            tiempo_ejercicio = itemView.findViewById(R.id.tiempo_ejercicio);

            if (tipo == 2 || tipo == 3) {
                foto_mod.setVisibility(View.INVISIBLE);
                foto_eliminar.setVisibility(View.INVISIBLE);
            }

            nombre_ejercicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(c.getApplicationContext(), Ejercicio.class);
                    System.out.println(nombre_ejercicio.getText().toString());
                    i.putExtra("ejercicio", nombre_ejercicio.getText().toString());
                    c.startActivity(i);
                }
            });

            foto_mod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    val = pos;
                    System.out.println(pos);
                    Button confirmar, cancelar;
                    EditText segundos;
                    ImageButton up, down;
                    TextView nuev_pos;
                    pantalla.setContentView(R.layout.popup_mod_ejercicio_rutina);
                    confirmar = (Button) pantalla.findViewById(R.id.confirmar);
                    cancelar = (Button) pantalla.findViewById(R.id.cancelar);
                    segundos = (EditText) pantalla.findViewById(R.id.nuevo_tiempo);
                    up = (ImageButton) pantalla.findViewById(R.id.sum);
                    down = (ImageButton) pantalla.findViewById(R.id.rest);
                    nuev_pos = (TextView) pantalla.findViewById(R.id.pos);
                    String aux = Integer.toString(pos);
                    nuev_pos.setText(aux);
                    segundos.setText(Integer.toString(listDatos.get(pos).second));

                    up.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(val != listDatos.size()-1) {
                                val+=1;
                                nuev_pos.setText(Integer.toString(val));
                            }
                            System.out.println(val);
                        }
                    });

                    down.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(val != 0)  {
                                val-=1;
                                nuev_pos.setText(Integer.toString(val));
                            }
                            System.out.println(val);
                        }
                    });

                    confirmar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println(val);
                            JSONObject req = new JSONObject();
                            try {
                                req.put("posicion", pos);
                                req.put("tiempo_nuev", Integer.parseInt(segundos.getText().toString()));
                                req.put("nueva_posicion", val);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            llamada = 4;
                            Connection con = new Connection((Interfaz) c);
                            con.execute(URL+"rutina/modEjercicio/" + id_rutina, "POST", req.toString());
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

            foto_eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button confirmar, cancelar;
                    TextView contenido;
                    pantalla.setContentView(R.layout.popup_eliminar_rutina);
                    confirmar = (Button) pantalla.findViewById(R.id.confirmar);
                    cancelar = (Button) pantalla.findViewById(R.id.cancelar);
                    contenido = (TextView) pantalla.findViewById(R.id.contenido);
                    contenido.setText("¿Seguro que quieres eliminar el ejercicio?");

                    confirmar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject req = new JSONObject();
                            try {
                                req.put("posicion", pos);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            llamada = 3;
                            Connection con = new Connection((Interfaz) c);
                            con.execute(URL+"rutina/eliminarEjercicio/" + id_rutina, "POST", req.toString());
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

        public void asignarDatos(String dato , Integer tiempo, int pos) {
            this.pos = pos;
            System.out.println(pos);
            nombre_ejercicio.setText(dato);
            tiempo_ejercicio.setText(tiempo.toString() + " seg");
            Picasso.get().load(URL+"ejercicio/image/" + nombreToUrl(dato)).into(foto_ejercicio);
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