package com.example.proyecto_tfg_frontend.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.proyecto_tfg_frontend.Connection;
import com.example.proyecto_tfg_frontend.Interfaz;
import com.example.proyecto_tfg_frontend.Actividades.Log_in;
import com.example.proyecto_tfg_frontend.R;
import com.example.proyecto_tfg_frontend.UsuarioSingleton;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import org.json.JSONObject;
import de.hdodenhof.circleimageview.CircleImageView;

public class GalleryFragment extends Fragment implements Interfaz {

    private EditText email;
    private EditText username;
    private CircleImageView imagen_perfil;
    private Button  elim, logout;
    private final String URL = "https://vidasana.herokuapp.com/";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        imagen_perfil = (CircleImageView) root.findViewById(R.id.foto_perfil);
        username = (EditText) root.findViewById(R.id.username);
        email = (EditText) root.findViewById(R.id.mail);
        elim = (Button) root.findViewById(R.id.elim);
        logout = (Button) root.findViewById(R.id.logout);

        System.out.println(URL+"users/"+ UsuarioSingleton.getInstance().getId()+"/image");
        if (UsuarioSingleton.getInstance().getImagen() == null) cargar_img_aux();
        else imagen_perfil.setImageBitmap(UsuarioSingleton.getInstance().getImagen());

        username.setText(UsuarioSingleton.getInstance().getUsername());
        email.setText(UsuarioSingleton.getInstance().getMail());
        elim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection con = new Connection(GalleryFragment.this);
                con.execute(URL+"users/delete/" + UsuarioSingleton.getInstance().getId(), "POST", null);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Log_in.class);
                startActivity(i);
            }
        });
        return root;
    }

    @Override
    public void Respuesta(JSONObject datos) throws JSONException {
        if (datos.getInt("codigo") == 200) {
            Intent i = new Intent(getActivity(), Log_in.class);
            startActivity(i);
        }
        else {

        }
    }

    public void cargar_img_aux() {
        Picasso.get().load(URL+"users/"+ UsuarioSingleton.getInstance().getId()+"/image").into(imagen_perfil);
    }
}