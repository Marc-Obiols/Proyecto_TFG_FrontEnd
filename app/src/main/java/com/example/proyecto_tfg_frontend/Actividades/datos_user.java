package com.example.proyecto_tfg_frontend.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.proyecto_tfg_frontend.Connection;
import com.example.proyecto_tfg_frontend.Interfaz;
import com.example.proyecto_tfg_frontend.R;
import com.example.proyecto_tfg_frontend.UsuarioSingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class datos_user extends AppCompatActivity implements Interfaz {

    private FloatingActionButton Editar;
    private int llamada;
    private Dialog registro_peso;
    private TextView est_forma;
    private EditText peso, peso_meta, altura, edad, IMC, peso_ideal, peso_max, peso_min;
    private final String URL = "https://vidasana.herokuapp.com/";

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_user);

        registro_peso = new Dialog(this);

        peso = (EditText) findViewById(R.id.peso);
        peso_meta = (EditText) findViewById(R.id.peso_meta);
        altura = (EditText) findViewById(R.id.altura);
        edad = (EditText) findViewById(R.id.edad);
        IMC = (EditText) findViewById(R.id.IMC);
        peso_ideal = (EditText) findViewById(R.id.peso_ideal);
        peso_max = (EditText) findViewById(R.id.peso_max);
        peso_min = (EditText) findViewById(R.id.peso_min);
        Editar = (FloatingActionButton) findViewById(R.id.Editar);
        est_forma = (TextView) findViewById(R.id.estado_forma);

        IMC.setEnabled(false);
        peso_ideal.setEnabled(false);
        peso_max.setEnabled(false);
        peso_min.setEnabled(false);
        peso.setEnabled(false);
        peso_meta.setEnabled(false);
        edad.setEnabled(false);
        altura.setEnabled(false);

        peso.setText(String.valueOf(UsuarioSingleton.getInstance().getPeso_act()));
        peso_meta.setText(String.valueOf(UsuarioSingleton.getInstance().getPeso_des()));
        altura.setText(String.valueOf(UsuarioSingleton.getInstance().getAltura()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Calendar c = Calendar.getInstance();
        int año_actual = c.get(Calendar.YEAR) - 1900;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(UsuarioSingleton.getInstance().getFecha_nacimiento());
        int año_nac = calendar.get(Calendar.YEAR) - 1900;
        System.out.println(año_actual + " : " + año_nac);

        edad.setText(Integer.toString(año_actual-año_nac));
        IMC.setText(String.valueOf(UsuarioSingleton.getInstance().getIMC()));
        ArrayList<String> valores = calculo_pesos(UsuarioSingleton.getInstance().getSexo(), UsuarioSingleton.getInstance().getAltura());
        peso_ideal.setText(String.valueOf(UsuarioSingleton.getInstance().getPeso_id()));
        peso_max.setText(valores.get(0));
        peso_min.setText(valores.get(1));
        est_forma.setText(estado_forma(UsuarioSingleton.getInstance().getIMC()));

        Editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //aqui deberia modificar el datos user de momento solo esta peso_meta
                Button confirmar, cancelar;
                TextView ult_registro;
                EditText nuevo_peso_meta, nueva_altura, nuev_pes;

                registro_peso.setContentView(R.layout.popup_mod_perfil);
                ult_registro = (TextView) registro_peso.findViewById(R.id.ult_reg_pes);
                confirmar = (Button) registro_peso.findViewById(R.id.actualizar);
                cancelar = (Button) registro_peso.findViewById(R.id.cancelar);
                nuevo_peso_meta = (EditText) registro_peso.findViewById(R.id.peso_meta_nuev);
                nueva_altura = (EditText) registro_peso.findViewById(R.id.altura);
                nuev_pes = (EditText) registro_peso.findViewById(R.id.peso_mod);
                nueva_altura.setText(String.valueOf(UsuarioSingleton.getInstance().getAltura()));
                nuev_pes.setText(String.valueOf(UsuarioSingleton.getInstance().getPeso_act()));
                nuevo_peso_meta.setText(String.valueOf(UsuarioSingleton.getInstance().getPeso_des()));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date aux = UsuarioSingleton.getInstance().getFechas()[UsuarioSingleton.getInstance().getFechas().length-1];
                String fecha = sdf.format(aux);
                int peso_aux = UsuarioSingleton.getInstance().getPesos()[UsuarioSingleton.getInstance().getPesos().length-1];
                ult_registro.setText("El ultimo registro es " + peso_aux + " del " + fecha);

                confirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject req = new JSONObject();
                        try {
                            req.put("peso_deseado", Integer.parseInt(nuevo_peso_meta.getText().toString()));
                            req.put("altura", Integer.parseInt(nueva_altura.getText().toString()));
                            req.put("peso_actual", Integer.parseInt(nuev_pes.getText().toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        llamada = 2;
                        Connection con = new Connection(datos_user.this);
                        con.execute(URL+"users/modificar/" + UsuarioSingleton.getInstance().getId(), "POST", req.toString());
                        registro_peso.dismiss();
                    }
                });

                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        registro_peso.dismiss();
                    }
                });
                registro_peso.show();
            }
        });
    }

    private ArrayList<String> calculo_pesos(String sexo, int alt) {
        ArrayList<String> result = new ArrayList<String>();
        double min, max;
        System.out.println(sexo);
        if (sexo.equals("hombre")) {
            min = 21;
            max = 26;
        }
        else {
            min = 19;
            max = 24;
        }
        double aux = alt;
        aux = aux/100;
        double altu_2 = aux*aux;
        double res_max = altu_2*max;
        double res_min = altu_2*min;
        System.out.println(res_min + " : " + res_max);
        result.add(String.valueOf((int)(res_max)));
        result.add(String.valueOf((int)(res_min)));
        return result;
    }

    private String estado_forma(double IMC) {
        String estado_forma;
        if (IMC < 18.5) estado_forma = "Delgado";
        else if (IMC >= 18.5 && IMC <= 24.9) estado_forma = "Bueno";
        else if (IMC > 24.9 && IMC <= 29.9) estado_forma = "Sobrepeso";
        else estado_forma = "Obesidad";
        return estado_forma;
    }

    @Override
    public void Respuesta(JSONObject datos) throws JSONException {
        try {
            if (datos.getInt("codigo") == 200) {
                if (llamada == 2) {
                    JSONArray aux2 = datos.getJSONArray("fechas");
                    JSONArray aux3 = datos.getJSONArray("pesos");
                    int[] aux1 = new int[aux2.length()];
                    Date[] aux4 = new Date[aux2.length()];
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    for (int i = 0; i < aux1.length; ++i) {
                        aux1[i] = aux3.getInt(i);
                        aux4[i] = sdf.parse(aux2.getString(i).substring(0, 10));
                    }
                    UsuarioSingleton.getInstance().setPesos(aux1);
                    UsuarioSingleton.getInstance().setFechas(aux4);
                    UsuarioSingleton.getInstance().setPeso_act(datos.getInt("peso_actual"));

                    peso.setText(String.valueOf(UsuarioSingleton.getInstance().getPeso_act()));
                    peso_meta.setText(datos.getString("peso_deseado"));
                    altura.setText(datos.getString("altura"));
                    IMC.setText(datos.getString("IMC"));
                    peso_ideal.setText(datos.getString("peso_ideal"));
                    est_forma.setText(estado_forma(UsuarioSingleton.getInstance().getIMC()));
                    ArrayList<String> valores = calculo_pesos(UsuarioSingleton.getInstance().getSexo(), datos.getInt("altura"));
                    peso_max.setText(valores.get(0));
                    peso_min.setText(valores.get(1));
                    UsuarioSingleton.getInstance().setPeso_des(datos.getInt("peso_deseado"));
                    UsuarioSingleton.getInstance().setAltura(datos.getInt("altura"));
                    UsuarioSingleton.getInstance().setIMC(datos.getDouble("IMC"));
                    UsuarioSingleton.getInstance().setPeso_id(datos.getInt("peso_ideal"));

                    JSONArray aux_dietas = datos.getJSONArray("dieta_recomendada");
                    List<String> dietas_recom = new ArrayList<>();
                    for (int i=0; i < aux_dietas.length(); i++) {
                        dietas_recom.add(aux_dietas.getString(i));
                    }
                    UsuarioSingleton.getInstance().setDietas(dietas_recom);
                }
            }
            else {
                System.out.println("algo pasa");
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }
}