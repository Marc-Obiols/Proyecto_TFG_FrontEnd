package com.example.proyecto_tfg_frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Alimentacion_concreta extends AppCompatActivity implements Interfaz{

    private ArrayList<String> list;
    private AdaptadorDatosMedidas adaptador;
    private RecyclerView recycler;
    private TextView kcalorias, proteinas, carbohidratos, fibra, grasas;
    private Dialog pantalla;
    private ImageButton grafico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alimentacion_concreta);

        list = new ArrayList<>();
        int pos = getIntent().getIntExtra("pos",0);
        grafico = (ImageButton) findViewById(R.id.grafico_circular);
        recycler = (RecyclerView) findViewById(R.id.lista_alimentos);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        kcalorias = (TextView) findViewById(R.id.kcal);
        proteinas = (TextView) findViewById(R.id.proteinas);
        carbohidratos = (TextView) findViewById(R.id.carbo_hidra);
        fibra = (TextView) findViewById(R.id.fibra);
        grasas = (TextView) findViewById(R.id.grasas);

        JSONObject req = new JSONObject();
        try {
            req.put("posicion", pos);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Connection con = new Connection(this);
        con.execute("http://192.168.0.14:3000/alimentacion/concreta/" + UsuarioSingleton.getInstance().getId(),"POST", req.toString());

        grafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(Integer.parseInt(proteinas.getText().toString()));
                PieChart graf;
                pantalla.setContentView(R.layout.popup_grafico_circular);
                graf = (PieChart) pantalla.findViewById(R.id.piechart);

                ArrayList<PieEntry> visitors = new ArrayList<>();
                visitors.add(new PieEntry((float) Double.parseDouble(proteinas.getText().toString()), "Proteinas"));
                visitors.add(new PieEntry((float) Double.parseDouble(fibra.getText().toString()), "Fibra"));
                visitors.add(new PieEntry((float) Double.parseDouble(carbohidratos.getText().toString()), "Carbohidratos"));
                visitors.add(new PieEntry((float) Double.parseDouble(grasas.getText().toString()), "Grasas"));

                PieDataSet pieDataSet = new PieDataSet(visitors, "Alimentación");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(16f);

                PieData pieData = new PieData(pieDataSet);
                graf.setData(pieData);
                graf.getDescription().setEnabled(false);
                graf.setCenterText("Alimentación");
                graf.animate();
                pantalla.show();
            }
        });
    }

    @Override
    public void Respuesta(JSONObject datos) throws JSONException {
        if (datos.getInt("codigo") == 200) {
            kcalorias.setText(datos.getString("kcal"));
            proteinas.setText(datos.getString("proteina"));
            carbohidratos.setText(datos.getString("carbo"));
            fibra.setText(datos.getString("fibra"));
            grasas.setText(datos.getString("grasas"));
            JSONArray alimentos = datos.getJSONArray("alimentos");
            for (int i = 0; i < alimentos.length(); i++) {
                JSONObject aux = (JSONObject) alimentos.get(i);
                list.add(i, aux.getString("nombre_al") + " " + aux.getString("cantidad") + "g");
            }
            adaptador = new AdaptadorDatosMedidas(list);
            recycler.setAdapter(adaptador);
        }
        else {
        }
    }
}