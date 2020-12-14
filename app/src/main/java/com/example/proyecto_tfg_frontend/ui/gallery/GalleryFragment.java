package com.example.proyecto_tfg_frontend.ui.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.proyecto_tfg_frontend.Connection;
import com.example.proyecto_tfg_frontend.Interfaz;
import com.example.proyecto_tfg_frontend.Log_in;
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
    private static final String url = "http://192.168.0.14:3000/users/delete/";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        imagen_perfil = (CircleImageView) root.findViewById(R.id.foto_perfil);
        username = (EditText) root.findViewById(R.id.username);
        imagen_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargar_imagen();
            }
        });
        email = (EditText) root.findViewById(R.id.mail);
        mod = (Button) root.findViewById(R.id.mod);
        elim = (Button) root.findViewById(R.id.elim);
        logout = (Button) root.findViewById(R.id.logout);
        Picasso.get().load("http://192.168.0.14:3000/users/"+ UsuarioSingleton.getInstance().getId()+"/image").into(imagen_perfil);
        username.setText(UsuarioSingleton.getInstance().getUsername());
        email.setText(UsuarioSingleton.getInstance().getMail());
        elim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection con = new Connection(GalleryFragment.this);
                con.execute(url + UsuarioSingleton.getInstance().getId(), "POST", null);
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

    private void cargar_imagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione la fotografia"), 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            Uri path = data.getData();
            System.out.println(path);
            imagen_perfil.setImageURI(path);
        }
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
}