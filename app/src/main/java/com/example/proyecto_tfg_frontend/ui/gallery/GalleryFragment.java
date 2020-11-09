package com.example.proyecto_tfg_frontend.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.proyecto_tfg_frontend.Interfaz;
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
    private Button mod, elim, logout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        imagen_perfil = (CircleImageView) root.findViewById(R.id.foto_perfil);
        username = (EditText) root.findViewById(R.id.username);
        email = (EditText) root.findViewById(R.id.mail);
        mod = (Button) root.findViewById(R.id.mod);
        elim = (Button) root.findViewById(R.id.elim);
        logout = (Button) root.findViewById(R.id.logout);
        Picasso.get().load("http://169.254.145.10:3000/users/"+ UsuarioSingleton.getInstance().getId()+"/image").into(imagen_perfil);
        username.setText(UsuarioSingleton.getInstance().getUsername());
        email.setText(UsuarioSingleton.getInstance().getMail());
        return root;
    }

    @Override
    public void Respuesta(JSONObject datos) throws JSONException {

    }
}