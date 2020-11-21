package com.example.proyecto_tfg_frontend;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorDatosRutinas extends RecyclerView.Adapter<AdaptadorDatosRutinas.ViewHolderDatosRutinas> {

    ArrayList<DatosRutina> listDatos;
    private Context c;
    private Dialog pantalla_eliminar;
    private int llamada, pos, tipo;

    public AdaptadorDatosRutinas(ArrayList<DatosRutina> listDatos, Context contexto, int tipo) {
        this.listDatos = listDatos;
        c = contexto;
        llamada = 3;
        pantalla_eliminar = new Dialog(c);
        this.tipo = tipo;
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

    public int getPos() {
        return pos;
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
            if (tipo == 2 || tipo == 3) {
                modificar.setVisibility(View.INVISIBLE);
                eliminar.setVisibility(View.INVISIBLE);
            }

            nombre_rutina.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(c.getApplicationContext(), Rutina.class);
                    i.putExtra("tipo", tipo);
                    i.putExtra("rutina", listDatos.get(posicion).getId());
                    c.startActivity(i);
                }
            });

            modificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button confirmar, cancelar;
                    EditText nombre, tiempo;
                    CheckBox publica;
                    Spinner dificultad;
                    pantalla_eliminar.setContentView(R.layout.popup_crear_rutina);
                    confirmar = (Button) pantalla_eliminar.findViewById(R.id.confirmar);
                    cancelar = (Button) pantalla_eliminar.findViewById(R.id.cancelar);
                    nombre = (EditText) pantalla_eliminar.findViewById(R.id.nuev_nombre);
                    tiempo = (EditText) pantalla_eliminar.findViewById(R.id.nuev_tiempo_desc);
                    nombre.setText(listDatos.get(posicion).getNombre());
                    tiempo.setText(Integer.toString(listDatos.get(posicion).getTiempo_desc()));
                    confirmar.setText("Modificar");
                    dificultad = (Spinner) pantalla_eliminar.findViewById(R.id.dificultad);
                    publica = (CheckBox) pantalla_eliminar.findViewById(R.id.Publica);

                    String [] niv = new String[] {"fácil", "normal", "difícil"};
                    ArrayAdapter<String> aaDep;
                    aaDep = new ArrayAdapter<String>(c, R.layout.spinner_item, niv); //activity para mostrar, tipo de spinner, listado de valores
                    aaDep.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    dificultad.setAdapter(aaDep);
                    publica.setChecked(listDatos.get(posicion).isPublica());
                    dificultad.setContentDescription(listDatos.get(posicion).getDificultad());

                    confirmar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject req = new JSONObject();
                            String nom = nombre.getText().toString();
                            if (nom.equals("")){
                                Toast.makeText(c, "Rellene el nombre de la rutina correctamente", Toast.LENGTH_LONG).show();
                            }
                            else if (tiempo.getText().toString().equals("") || tiempo.getText().toString().equals("0")) {
                                Toast.makeText(c, "Rellene el tiempo de descanso de la rutina correctamente", Toast.LENGTH_LONG).show();
                            }
                            else {
                                try {
                                    req.put("nombre", nom);
                                    req.put("tiempo_descanso", Integer.parseInt(tiempo.getText().toString()));
                                    req.put("publica", publica.isChecked());
                                    req.put("dificultad", dificultad.getSelectedItem().toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                llamada = 4;
                                pos = posicion;
                                Connection con = new Connection((Interfaz) c);
                                con.execute("http://169.254.145.10:3000/rutina/modificar/" + listDatos.get(posicion).getId(), "POST", req.toString());
                                pantalla_eliminar.dismiss();
                            }
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

            eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button confirmar, cancelar;
                    TextView contenido;
                    pantalla_eliminar.setContentView(R.layout.popup_eliminar_rutina);
                    confirmar = (Button) pantalla_eliminar.findViewById(R.id.confirmar);
                    cancelar = (Button) pantalla_eliminar.findViewById(R.id.cancelar);
                    contenido = (TextView) pantalla_eliminar.findViewById(R.id.contenido);
                    contenido.setText("¿Seguro que quieres eliminar la rutina?");

                    confirmar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            JSONObject req = new JSONObject();
                            try {
                                req.put("id_user", UsuarioSingleton.getInstance().getId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            llamada = 3;
                            Connection con = new Connection((Interfaz) c);
                            con.execute("http://169.254.145.10:3000/rutina/eliminar/" + listDatos.get(posicion).getId(), "POST", req.toString());
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

        public void asignarDatos(DatosRutina dato, int position) {
            nombre_rutina.setText(dato.getNombre());
            posicion = position;
        }
    }
}
