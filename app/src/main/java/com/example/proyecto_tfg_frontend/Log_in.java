package com.example.proyecto_tfg_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Log_in extends AppCompatActivity implements Interfaz {

    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

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
                UsuarioSingleton.getInstance().setIMC(Integer.parseInt(datos.getString("IMC")));
                UsuarioSingleton.getInstance().setMail(datos.getString("email"));

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
                /*
                for(int j=0;j<aux1.length;j++) {  //length is the property of the array
                    System.out.println(aux1[j]);
                    System.out.println(aux4[j]);
                }*/
                UsuarioSingleton.getInstance().setFechas(aux4);
                UsuarioSingleton.getInstance().setPesos(aux1);

                Toast.makeText(Log_in.this,"Bienvenido", Toast.LENGTH_LONG).show();
                Intent ii = new Intent(getApplicationContext(), MenuPrincipal.class);
                startActivity(ii);
            } else {
                Toast.makeText(Log_in.this,"Password o username incorrecto", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }
}