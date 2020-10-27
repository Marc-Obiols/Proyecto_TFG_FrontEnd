package com.example.proyecto_tfg_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class registro extends AppCompatActivity implements Interfaz {

    private EditText username;
    private EditText password;
    private EditText mail;
    private EditText fecha_nacimiento;
    private Calendar calendario = Calendar.getInstance();
    private EditText peso_actual;
    private EditText peso_deseado;
    private EditText altura;
    private CheckBox hombre;
    private CheckBox mujer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        mail = (EditText) findViewById(R.id.mail);
        fecha_nacimiento = (EditText) findViewById(R.id.etBirthday);
        peso_actual = (EditText) findViewById(R.id.peso_actual);
        peso_deseado = (EditText) findViewById(R.id.peso_deseado);
        altura = (EditText) findViewById(R.id.altura);
        hombre = (CheckBox) findViewById(R.id.hombre);
        mujer = (CheckBox) findViewById(R.id.mujer);

        hombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mujer.setChecked(false);
            }
        });

        mujer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hombre.setChecked(false);
            }
        });


        fecha_nacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(registro.this, date, calendario
                        .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, monthOfYear);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();
        }

    };

    private void actualizarInput() {
        String formatoDeFecha = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        fecha_nacimiento.setText(sdf.format(calendario.getTime()));
    }

    public void Registrarse (View view) {

        String sexo;
        if (hombre.isChecked()) sexo = "hombre";
        else sexo = "mujer";
        String user = username.getText().toString();
        String pass = password.getText().toString();
        String email = mail.getText().toString();
        String aux1 = altura.getText().toString();
        String aux2 = peso_actual.getText().toString();
        String aux3 = peso_deseado.getText().toString();
        String fecha = fecha_nacimiento.getText().toString();
        if (user.length() == 0 || pass.length() == 0 || email.length() == 0 || fecha.length() == 16 || aux1 == "" || aux2 == "" || aux3 == "" || (!hombre.isChecked() && mujer.isChecked())) {
            Toast.makeText(registro.this,"Rellene los parametros de forma correcta", Toast.LENGTH_LONG).show();
        }
        else {
            int alt = Integer.parseInt(aux1);
            int pes_act = Integer.parseInt(aux2);
            int pes_des = Integer.parseInt(aux3);
            if (alt < 80 || alt > 280 || pes_act < 25 || pes_act > 200 || pes_des < 25 || pes_des > 200) {
                Toast.makeText(registro.this,"Rellene los parametros de forma correcta", Toast.LENGTH_LONG).show();
            }
            else {
                JSONObject req = new JSONObject();
                try {
                    req.put("username", user);
                    req.put("password", pass);
                    req.put("peso_actual", pes_act);
                    req.put("peso_deseado", pes_des);
                    req.put("fecha_nacimiento", fecha);
                    req.put("email", email);
                    req.put("altura", alt);
                    req.put("sexo", sexo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Connection con = new Connection(this);
                con.execute("http://169.254.145.10:3000/users/register/", "POST", req.toString());
            }
        }
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
                UsuarioSingleton.getInstance().setSexo(datos.getString("sexo"));
                UsuarioSingleton.getInstance().setPeso_id(datos.getInt("peso_ideal"));

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
                Date fecha_nac = sdf.parse(datos.getString("fecha_nacimiento").substring(0,10));
                UsuarioSingleton.getInstance().setFecha_nacimiento(fecha_nac);

                Toast.makeText(registro.this,"Bienvenido", Toast.LENGTH_LONG).show();
                Intent ii = new Intent(getApplicationContext(), MenuPrincipal.class);
                startActivity(ii);
            } else {
                Toast.makeText(registro.this,"Password o username en uso", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }
}