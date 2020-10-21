package com.example.proyecto_tfg_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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
                System.out.println(datos);
                UsuarioSingleton.getInstance().setId(datos.getString("_id"));
                UsuarioSingleton.getInstance().setUsername(datos.getString("username"));
                Toast.makeText(Log_in.this,"Bienvenido", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(Log_in.this,"Password o username incorrecto", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}