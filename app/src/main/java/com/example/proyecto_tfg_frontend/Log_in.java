package com.example.proyecto_tfg_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.text.DateFormat.DEFAULT;

public class Log_in extends AppCompatActivity implements Interfaz {

    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        System.out.println("ESTOY EN LOGIN CREATE");

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
    }

    public void login(View view) throws JSONException {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        JSONObject req = new JSONObject();
        try {
            req.put("username", user);
            req.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Connection con = new Connection(this);
        con.execute("http://169.254.145.10:3000/users/login/", "POST", req.toString());

    }

    public void register(View view) {
        Intent i = new Intent(getApplicationContext(), registro.class);
        startActivity(i);
    }

    @Override
    public void Respuesta(JSONObject datos) {
        try {
            if (datos.getInt("codigo") == 200) {

                UsuarioSingleton.getInstance().setId(datos.getString("_id"));
                UsuarioSingleton.getInstance().setUsername(datos.getString("username"));
                UsuarioSingleton.getInstance().setAltura(Integer.parseInt(datos.getString("altura")));
                UsuarioSingleton.getInstance().setPeso_act(Integer.parseInt(datos.getString("peso_actual")));
                UsuarioSingleton.getInstance().setPeso_des(Integer.parseInt(datos.getString("peso_deseado")));
                UsuarioSingleton.getInstance().setIMC(datos.getDouble("IMC"));
                UsuarioSingleton.getInstance().setMail(datos.getString("email"));
                UsuarioSingleton.getInstance().setSexo(datos.getString("sexo"));
                UsuarioSingleton.getInstance().setPeso_id(datos.getInt("peso_ideal"));

                System.out.println();

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
                JSONObject imagen = datos.getJSONObject("imagen");
                byte [] imageInByteArray = Base64.decode(imagen.getString("data"), Base64.DEFAULT);
                System.out.println("EN BYTES LA IMAGEN");
                System.out.println(imagen.getJSONObject("data").getString("data"));
                System.out.println("SIGUENTE PARAMETRO");
                System.out.println(imagen.getString("contentType"));
                /*
                for(int j=0;j<aux1.length;j++) {  //length is the property of the array
                    System.out.println(aux1[j]);
                    System.out.println(aux4[j]);
                }*/
                UsuarioSingleton.getInstance().setFechas(aux4);
                UsuarioSingleton.getInstance().setPesos(aux1);
                Date fecha_nac = sdf.parse(datos.getString("fecha_nacimiento").substring(0,10));
                UsuarioSingleton.getInstance().setFecha_nacimiento(fecha_nac);
                //UsuarioSingleton.getInstance().setImagen(imageInByteArray);

                Toast.makeText(Log_in.this,"Bienvenido", Toast.LENGTH_LONG).show();
                Intent ii = new Intent(getApplicationContext(), Inicio.class);
                startActivity(ii);
            } else if (datos.getInt("codigo") == 411) {
                Toast.makeText(Log_in.this,"Password o username incorrecto", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Log_in.this,"No esta validado", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }
}