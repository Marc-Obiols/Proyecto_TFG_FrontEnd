package com.example.proyecto_tfg_frontend;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import android.util.Base64;
import java.util.HashMap;
import java.util.Scanner;

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import de.hdodenhof.circleimageview.CircleImageView;


public class Inicio extends AppCompatActivity implements Interfaz{

    private AppBarConfiguration mAppBarConfiguration;
    private CircleImageView img;
    private Uri path;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int GALLERY_REQUEST = 1889;

    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;
    private StringRequest stringRequest;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView gmail = hView.findViewById(R.id.gmail_inicio);
        gmail.setText(UsuarioSingleton.getInstance().getMail());
        TextView username = hView.findViewById(R.id.username_inicio);
        username.setText(UsuarioSingleton.getInstance().getUsername());
        img = hView.findViewById(R.id.foto_perfil_inicio);
        Picasso.get().load("http://192.168.0.14:3000/users/"+ UsuarioSingleton.getInstance().getId()+"/image").into(img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargar_imagen();
            }
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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
                img.setImageBitmap(bitmap);
            }
            catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            //uploadImage();
                enviar_foto();
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
        System.out.println("HOLA");
    }

    private String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + "=?", new String[]{document_id}, null);
        cursor.moveToFirst();
        String camino = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return camino;
    }

    private void uploadImage() {
        String camino = getPath(path);
        System.out.println(camino);
        try {
            System.out.println("EN TEORIA LO ENVIO");
            String uploadid = UUID.randomUUID().toString();
            new MultipartUploadRequest(this, uploadid, "http://192.168.0.14:3000/users/"+UsuarioSingleton.getInstance().getId()+"/Modimage")
                    .addFileToUpload(camino, "image") //Adding file
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void cargar_webService() {
        request = Volley.newRequestQueue(this);
        String url = "http://192.168.0.14:3000/users/"+UsuarioSingleton.getInstance().getId()+"/Modimage";
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("todo bien");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("MAL");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imagen = convertitImgString(bitmap);

                Map<String, String> parametros = new HashMap<>();
                parametros.put("imagen", imagen);
                return parametros;
            }
        };
        request.add(stringRequest);
    }

    private void enviar_foto() {
        JSONObject req = new JSONObject();
        String imagen = convertitImgString(bitmap);
        try {
            req.put("image", imagen);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Connection con = new Connection(this);
        con.execute("http://192.168.0.14:3000/users/"+UsuarioSingleton.getInstance().getId()+"/Modimage","POST", req.toString());
    }

    private String convertitImgString(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte = array.toByteArray();
        String imagenString = null;
        imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);

        return imagenString;

    }
}