package com.example.proyecto_tfg_frontend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

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

    private TableLayout info_user;
    private TextView username;
    private CircleImageView imagen_perfil;

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

        info_user = (TableLayout) view.findViewById(R.id.info_user);
        username = (TextView) view.findViewById(R.id.username);
        peso = (EditText) view.findViewById(R.id.peso);
        peso_meta = (EditText) view.findViewById(R.id.peso_meta);
        altura = (EditText) view.findViewById(R.id.altura);
        edad = (EditText) view.findViewById(R.id.edad);
        IMC = (EditText) view.findViewById(R.id.IMC);
        peso_ideal = (EditText) view.findViewById(R.id.peso_ideal);
        peso_max = (EditText) view.findViewById(R.id.peso_max);
        peso_min = (EditText) view.findViewById(R.id.peso_min);
        imagen_perfil = (CircleImageView) view.findViewById(R.id.foto_perfil);

        IMC.setEnabled(false);
        peso_ideal.setEnabled(false);
        peso_max.setEnabled(false);
        peso_min.setEnabled(false);

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
        System.out.println("QUE ESTA PASANDO");
        //System.out.println(UsuarioSingleton.getInstance().getImagen().toString());
        //System.out.println(UsuarioSingleton.getInstance().getImagen());
        //Bitmap bitmap = BitmapFactory.decodeByteArray(UsuarioSingleton.getInstance().getImagen(), 0, UsuarioSingleton.getInstance().getImagen().length);
        //System.out.println(bitmap);
        Picasso.get().load("http://169.254.145.10:3000/users/"+UsuarioSingleton.getInstance().getId()+"/image").into(imagen_perfil);
        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imagen_perfil);
        //Connection con = new Connection(this);
        //con.execute("http://169.254.145.10:3000/users/"+UsuarioSingleton.getInstance().getId()+"/image", "GET", null);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void Respuesta(JSONObject datos) {
        try {
            if (datos.getInt("codigo") == 200) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}