package com.example.proyecto_tfg_frontend.Actividades;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import com.example.proyecto_tfg_frontend.Connection;
import com.example.proyecto_tfg_frontend.Interfaz;
import com.example.proyecto_tfg_frontend.R;
import com.example.proyecto_tfg_frontend.UsuarioSingleton;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class Inicio extends AppCompatActivity implements Interfaz {

    private int llamada;

    private AppBarConfiguration mAppBarConfiguration;
    private CircleImageView img;
    private Uri path;
    private static final int GALLERY_REQUEST = 1889;
    private final String URL = "https://vidasana.herokuapp.com/";
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView gmail = hView.findViewById(R.id.gmail_inicio);
        gmail.setText(UsuarioSingleton.getInstance().getMail());
        TextView username = hView.findViewById(R.id.username_inicio);
        username.setText(UsuarioSingleton.getInstance().getUsername());
        img = hView.findViewById(R.id.foto_perfil_inicio);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargar_imagen();
            }
        });
        System.out.println(URL+"users/"+ UsuarioSingleton.getInstance().getId()+"/image");
        if (UsuarioSingleton.getInstance().getImagen() == null) cargar_img_aux();
        else img.setImageBitmap(UsuarioSingleton.getInstance().getImagen());
    }


    private void cargar_imagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione la fotografia"), GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("EL RESULTCODE:" + resultCode);
        System.out.println("EL requestCode:" + requestCode);
        if(resultCode == RESULT_OK && data != null &&data.getData() != null){
            if (requestCode == GALLERY_REQUEST) {
            path = data.getData();
            UsuarioSingleton.getInstance().setUrl_img(path.toString());
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
                enviar_foto();

                Toast.makeText(Inicio.this,"Reinicia para ver la nueva imagen", Toast.LENGTH_LONG).show();
                img.setImageBitmap(bitmap);
                UsuarioSingleton.getInstance().setImagen(bitmap);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void Respuesta(JSONObject datos) throws JSONException {

    }

    private void enviar_foto() {
        JSONObject req = new JSONObject();
        String imagen = convertitImgString(bitmap);
        try {
            req.put("buffer", imagen);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Connection con = new Connection(this);
        con.execute(URL+ "users/"+UsuarioSingleton.getInstance().getId()+"/Modimage","POST", req.toString());
    }

    private String convertitImgString(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75, array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = null;
        imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);
        return imagenString;

    }

    public void cargar_img_aux() {
        Picasso.get().load(URL+"users/"+ UsuarioSingleton.getInstance().getId()+"/image").into(img);
    }

    @Override
    public void onBackPressed() {

    }
}