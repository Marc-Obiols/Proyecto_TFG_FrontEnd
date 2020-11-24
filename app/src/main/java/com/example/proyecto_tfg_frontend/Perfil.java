package com.example.proyecto_tfg_frontend;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil extends Fragment implements Interfaz{


    private Button buttonModificar;
    private Button ver_progreso;
    private Button modificar;

    private int llamada;
    private Dialog registro_peso;

    private TextView username;
    private TextView est_forma;
    private CircleImageView imagen_perfil;

    private EditText peso;
    private EditText peso_meta;
    private EditText altura;
    private EditText edad;
    private EditText IMC;
    private EditText peso_ideal;
    private EditText peso_max;
    private EditText peso_min;

    //plan
    private TextView prot;
    private TextView azu;
    private TextView gras;
    private TextView cahi;
    private TextView calo;
    private TextView sodi;

    public Perfil() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        registro_peso = new Dialog(this.getActivity());

        username = (EditText) view.findViewById(R.id.editTextTextPersonName2);
        peso = (EditText) view.findViewById(R.id.peso);
        peso_meta = (EditText) view.findViewById(R.id.peso_meta);
        altura = (EditText) view.findViewById(R.id.altura);
        edad = (EditText) view.findViewById(R.id.edad);
        IMC = (EditText) view.findViewById(R.id.IMC);
        peso_ideal = (EditText) view.findViewById(R.id.peso_ideal);
        peso_max = (EditText) view.findViewById(R.id.peso_max);
        peso_min = (EditText) view.findViewById(R.id.peso_min);
        imagen_perfil = (CircleImageView) view.findViewById(R.id.foto_perfil);
        buttonModificar = (Button) view.findViewById(R.id.Modificar);
        ver_progreso = (Button) view.findViewById(R.id.progreso);
        est_forma = (TextView)  view.findViewById(R.id.estado_forma);
        modificar = (Button) view.findViewById(R.id.button2);

        //PLAN
        prot = (TextView)  view.findViewById(R.id.proteinas);
        azu = (TextView)  view.findViewById(R.id.azucar);
        gras = (TextView)  view.findViewById(R.id.grasas);
        cahi = (TextView)  view.findViewById(R.id.carbo_hidra);
        calo = (TextView)  view.findViewById(R.id.calorias);
        sodi = (TextView)  view.findViewById(R.id.sodio);

        IMC.setEnabled(false);
        peso_ideal.setEnabled(false);
        peso_max.setEnabled(false);
        peso_min.setEnabled(false);
        peso.setEnabled(false);
        //peso_meta.setEnabled(false);
        edad.setEnabled(false);
        altura.setEnabled(false);

        username.setText(UsuarioSingleton.getInstance().getUsername());
        peso.setText(String.valueOf(UsuarioSingleton.getInstance().getPeso_act()));
        peso_meta.setText(String.valueOf(UsuarioSingleton.getInstance().getPeso_des()));
        altura.setText(String.valueOf(UsuarioSingleton.getInstance().getAltura()));
        edad.setText(UsuarioSingleton.getInstance().getFecha_nacimiento().toString());
        IMC.setText(String.valueOf(UsuarioSingleton.getInstance().getIMC()));
        System.out.println(UsuarioSingleton.getInstance().getAltura());
        ArrayList<String> valores = calculo_pesos(UsuarioSingleton.getInstance().getSexo(), UsuarioSingleton.getInstance().getAltura());
        peso_ideal.setText(String.valueOf(UsuarioSingleton.getInstance().getPeso_id()));
        peso_max.setText(valores.get(0));
        peso_min.setText(valores.get(1));
        est_forma.setText(estado_forma(UsuarioSingleton.getInstance().getIMC()));
        Picasso.get().load("http://169.254.145.10:3000/users/"+UsuarioSingleton.getInstance().getId()+"/image").into(imagen_perfil);
        llamada = 2;
        Connection con = new Connection(this);
        con.execute("http://169.254.145.10:3000/plan/" + UsuarioSingleton.getInstance().getId(), "GET", null);

        buttonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView ult_registro;
                Button confirmar, cancelar;
                EditText nuev_pes;
                registro_peso.setContentView(R.layout.popup_mod_perfil);
                ult_registro = (TextView) registro_peso.findViewById(R.id.ult_reg);
                confirmar = (Button) registro_peso.findViewById(R.id.actualizar);
                cancelar = (Button) registro_peso.findViewById(R.id.cancelar);
                nuev_pes = (EditText) registro_peso.findViewById(R.id.peso_mod);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date aux = UsuarioSingleton.getInstance().getFechas()[UsuarioSingleton.getInstance().getFechas().length-1];
                String fecha = sdf.format(aux);
                int peso_aux = UsuarioSingleton.getInstance().getPesos()[UsuarioSingleton.getInstance().getPesos().length-1];
                ult_registro.setText("El ultimo registro es " + peso_aux + " del " + fecha);
                confirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject req = new JSONObject();
                        try {
                            req.put("peso_actual", Integer.parseInt(nuev_pes.getText().toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        llamada = 1;
                        System.out.println(req.toString());
                        Connection con = new Connection(Perfil.this);
                        con.execute("http://169.254.145.10:3000/users/registrarPes/" + UsuarioSingleton.getInstance().getId(), "POST", req.toString());
                        registro_peso.dismiss();
                    }
                });
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        registro_peso.dismiss();
                    }
                });
                registro_peso.show();
            }
        });

        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject req = new JSONObject();
                try {
                    req.put("username", UsuarioSingleton.getInstance().getUsername());
                    req.put("email", UsuarioSingleton.getInstance().getMail());
                    req.put("peso_deseado", Integer.parseInt(peso_meta.getText().toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                llamada = 3;
                System.out.println(req.toString());
                Connection con = new Connection(Perfil.this);
                con.execute("http://169.254.145.10:3000/users/modificar/" + UsuarioSingleton.getInstance().getId(), "POST", req.toString());
            }
        });

        ver_progreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Activity_progeso.class);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void Respuesta(JSONObject datos) {
        try {
            if (datos.getInt("codigo") == 200) {
                if (llamada == 1) {
                    JSONArray aux2 = datos.getJSONArray("fechas");
                    JSONArray aux3 = datos.getJSONArray("pesos");
                    int[] aux1 = new int[aux2.length()];
                    Date[] aux4 = new Date[aux2.length()];
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    int i;
                    for (i = 0; i < aux1.length; ++i) {
                        aux1[i] = aux3.getInt(i);
                        aux4[i] = sdf.parse(aux2.getString(i).substring(0, 10));
                    }
                    UsuarioSingleton.getInstance().setPesos(aux1);
                    UsuarioSingleton.getInstance().setFechas(aux4);
                    UsuarioSingleton.getInstance().setPeso_act(datos.getInt("peso_actual"));
                    UsuarioSingleton.getInstance().setIMC(datos.getInt("IMC"));
                    IMC.setText(datos.getString("IMC"));
                    est_forma.setText(estado_forma(UsuarioSingleton.getInstance().getIMC()));
                    peso.setText(String.valueOf(UsuarioSingleton.getInstance().getPeso_act()));
                    Toast.makeText(getActivity(), "Se ha registrado correctamente", Toast.LENGTH_LONG).show();
                }
                else if(llamada == 2) {
                    prot.setText(datos.getString("Proteinas"));
                    azu.setText(datos.getString("Azucar"));
                    gras.setText(datos.getString("Grasas"));
                    cahi.setText(datos.getString("Carbohidratos"));
                    calo.setText(datos.getString("Kcal"));
                    sodi.setText(datos.getString("Sodio"));
                }
                else if (llamada == 3) {
                    peso_meta.setText(datos.getString("peso_deseado"));
                    llamada = 2;
                    Connection con = new Connection(this);
                    con.execute("http://169.254.145.10:3000/plan/" + UsuarioSingleton.getInstance().getId(), "GET", null);
                }
            }
            else {
                System.out.println("algo pasa");
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> calculo_pesos(String sexo, int alt) {
        ArrayList<String> result = new ArrayList<String>();
        double min, max;
        System.out.println(sexo);
        if (sexo.equals("hombre")) {
            System.out.println("SOY HOMBRE");
            min = 21;
            max = 26;
        }
        else {
            System.out.println("SOY MUJER");
            min = 19;
            max = 24;
        }
        double aux = alt;
        aux = aux/100;
        double altu_2 = aux*aux;
        double res_max = altu_2*max;
        double res_min = altu_2*min;
        System.out.println(res_min + " : " + res_max);
        result.add(String.valueOf((int)(res_max)));
        result.add(String.valueOf((int)(res_min)));
        return result;
    }
    private String estado_forma(double IMC) {
        String estado_forma;
        if (IMC < 18.5) estado_forma = "Delgado";
        else if (IMC >= 18.5 && IMC <= 24.9) estado_forma = "Bueno";
        else if (IMC > 24.9 && IMC <= 29.9) estado_forma = "Sobrepeso";
        else estado_forma = "Obesidad";
        return estado_forma;
    }
}