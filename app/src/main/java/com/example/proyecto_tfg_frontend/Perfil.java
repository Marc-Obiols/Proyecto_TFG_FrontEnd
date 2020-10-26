package com.example.proyecto_tfg_frontend;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Perfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Perfil extends Fragment implements Interfaz{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button buttonModificar;
    private Button ver_progreso;

    private Dialog registro_peso;

    private TableLayout info_user;
    private TextView username;
    private CircleImageView imagen_perfil;

    private EditText email;
    private EditText peso;
    private EditText peso_meta;
    private EditText altura;
    private EditText edad;
    private EditText IMC;
    private EditText peso_ideal;
    private EditText peso_max;
    private EditText peso_min;

    public Perfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Perfil.
     */
    // TODO: Rename and change types and number of parameters
    public static Perfil newInstance(String param1, String param2) {
        Perfil fragment = new Perfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        registro_peso = new Dialog(this.getActivity());

        info_user = (TableLayout) view.findViewById(R.id.info_user);
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
        email = (EditText) view.findViewById(R.id.editTextTextPersonName);
        ver_progreso = (Button) view.findViewById(R.id.progreso);

        IMC.setEnabled(false);
        peso_ideal.setEnabled(false);
        peso_max.setEnabled(false);
        peso_min.setEnabled(false);
        peso.setEnabled(false);
        peso_meta.setEnabled(false);
        edad.setEnabled(false);
        altura.setEnabled(false);

        username.setText(UsuarioSingleton.getInstance().getUsername());
        peso.setText(String.valueOf(UsuarioSingleton.getInstance().getPeso_act()));
        peso_meta.setText(String.valueOf(UsuarioSingleton.getInstance().getPeso_des()));
        altura.setText(String.valueOf(UsuarioSingleton.getInstance().getAltura()));
        edad.setText("12");
        IMC.setText(String.valueOf(UsuarioSingleton.getInstance().getIMC()));
        int pes_id = 40;
        int pes_max = 60;
        int pes_min = 20;
        peso_ideal.setText(String.valueOf(pes_id));
        peso_max.setText(String.valueOf(pes_max));
        peso_min.setText(String.valueOf(pes_min));
        Picasso.get().load("http://169.254.145.10:3000/users/"+UsuarioSingleton.getInstance().getId()+"/image").into(imagen_perfil);

        buttonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("HOLA");
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

                JSONArray aux2 = datos.getJSONArray("fechas");
                JSONArray aux3 = datos.getJSONArray("pesos");
                int [] aux1 = new int[aux2.length()];
                Date[] aux4 = new Date[aux2.length()];
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                int i;
                for (i=0;i<aux1.length;++i) {
                    aux1[i] = aux3.getInt(i);
                    aux4[i] = sdf.parse(aux2.getString(i).substring(0,10));
                }
                UsuarioSingleton.getInstance().setPesos(aux1);
                UsuarioSingleton.getInstance().setFechas(aux4);
                UsuarioSingleton.getInstance().setPeso_act(datos.getInt("peso_actual"));
                System.out.println(datos.getInt("peso_actual"));
                peso.setText(String.valueOf(UsuarioSingleton.getInstance().getPeso_act()));
                Toast.makeText(getActivity(),"Se ha registrado correctamente", Toast.LENGTH_LONG).show();
            }
            else {
                System.out.println("algo pasa");
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }
}