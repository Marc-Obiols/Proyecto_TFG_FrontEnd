package com.example.proyecto_tfg_frontend;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class alimentacion_actual extends Fragment implements Interfaz {

    private RecyclerView recycler;
    private ImageButton grafico;
    private Dialog pantalla;
    private int llamada;
    private TextView kcalorias, proteinas, carbohidratos, fibra, grasas, kcalorias_o, proteinas_o, carbohidratos_o, fibra_o, grasas_o;
    private ArrayList<Alimento> alimentos_añadidos;
    private AdaptadorDatosAlimentosSeleccionados adaptadorDatosAlimentosSeleccionados;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alimentacion_actual, container, false);
        alimentos_añadidos = new ArrayList<>();
        recycler = (RecyclerView) view.findViewById(R.id.lista_busqueda);
        recycler.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recycler.getContext(), LinearLayoutManager.VERTICAL);
        recycler.addItemDecoration(mDividerItemDecoration);
        pantalla = new Dialog(this.getActivity());
        grafico = (ImageButton) view.findViewById(R.id.grafico_circular);

        grafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PieChart graf;
                pantalla.setContentView(R.layout.popup_grafico_circular);
                graf = (PieChart) pantalla.findViewById(R.id.piechart);

                ArrayList<PieEntry> visitors = new ArrayList<>();
                System.out.println(Double.parseDouble(proteinas.getText().toString()));
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

        //plan
        kcalorias = (TextView) view.findViewById(R.id.kcal);
        proteinas = (TextView) view.findViewById(R.id.proteinas);
        carbohidratos = (TextView) view.findViewById(R.id.carbo_hidra);
        fibra = (TextView) view.findViewById(R.id.fibra);
        grasas = (TextView) view.findViewById(R.id.grasas);
        kcalorias_o = (TextView) view.findViewById(R.id.kcal_ob);
        proteinas_o = (TextView) view.findViewById(R.id.proteinas_o);
        carbohidratos_o = (TextView) view.findViewById(R.id.carbo_hidra_o);
        fibra_o = (TextView) view.findViewById(R.id.fibra_o);
        grasas_o = (TextView) view.findViewById(R.id.grasas_o);

        llamada = 3;
        Connection con = new Connection(this);
        con.execute("http://192.168.0.14:3000/plan/"+ UsuarioSingleton.getInstance().getId() ,"GET",null);
        return view;
    }

    @Override
    public void Respuesta(JSONObject datos) throws JSONException {
        if (datos.getInt("codigo") == 200) {
            if (llamada == 2) {//Get alimentacion dia

                llamada = 5;
                kcalorias.setText(datos.getString("kcal"));
                proteinas.setText(datos.getString("proteina"));
                carbohidratos.setText(datos.getString("carbo"));
                fibra.setText(datos.getString("fibra"));
                grasas.setText(datos.getString("grasas"));

                JSONArray aliment = datos.getJSONArray("alimentos");
                alimentos_añadidos = new ArrayList<>();
                for (int i = 0; i < aliment.length(); i++) {
                    //prot, gras, carbo, fibra
                    System.out.println(aliment.toString());
                    JSONObject obj = (JSONObject) aliment.get(i);
                    Alimento aux = new Alimento(obj.getString("id_alimento"), "", "", obj.getString("nombre_al"),
                            obj.getDouble("kcal_al") * obj.getInt("cantidad") / 100,
                            obj.getDouble("proteina_al") * obj.getInt("cantidad") / 100,
                            obj.getDouble("grasas_al") * obj.getInt("cantidad") / 100,
                            obj.getDouble("carbo_al") * obj.getInt("cantidad") / 100,
                            obj.getDouble("fibra_al") * obj.getInt("cantidad") / 100);
                    alimentos_añadidos.add(aux);
                }
                adaptadorDatosAlimentosSeleccionados = new AdaptadorDatosAlimentosSeleccionados(alimentos_añadidos, this.getActivity(), this);
            } else if (llamada == 3) { //Get plan user

                kcalorias_o.setText(datos.getString("Kcal"));
                proteinas_o.setText(datos.getString("Proteinas"));
                carbohidratos_o.setText(datos.getString("Carbohidratos"));
                fibra_o.setText(datos.getString("Fibra"));
                grasas_o.setText(datos.getString("Grasas"));

                llamada = 2;
                Connection con2 = new Connection(this);
                con2.execute("http://192.168.0.14:3000/alimentacion/dia/" + UsuarioSingleton.getInstance().getId(), "GET", null);
            } else if (adaptadorDatosAlimentosSeleccionados.getLlamada() == 2) {

                System.out.println("LO HE ELIMINADO");
                kcalorias.setText(datos.getString("kcal"));
                proteinas.setText(datos.getString("proteina"));
                carbohidratos.setText(datos.getString("carbo"));
                fibra.setText(datos.getString("fibra"));
                grasas.setText(datos.getString("grasas"));

                JSONArray aliment = datos.getJSONArray("alimentos");
                alimentos_añadidos = new ArrayList<>();
                for (int i = 0; i < aliment.length(); i++) {
                    //prot, gras, carbo, fibra
                    System.out.println(aliment.toString());
                    JSONObject obj = (JSONObject) aliment.get(i);
                    Alimento aux = new Alimento(obj.getString("id_alimento"), "", "", obj.getString("nombre_al"),
                            obj.getDouble("kcal_al") * obj.getInt("cantidad") / 100,
                            obj.getDouble("proteina_al") * obj.getInt("cantidad") / 100,
                            obj.getDouble("grasas_al") * obj.getInt("cantidad") / 100,
                            obj.getDouble("carbo_al") * obj.getInt("cantidad") / 100,
                            obj.getDouble("fibra_al") * obj.getInt("cantidad") / 100);
                    alimentos_añadidos.add(aux);
                }
                adaptadorDatosAlimentosSeleccionados = new AdaptadorDatosAlimentosSeleccionados(alimentos_añadidos, this.getActivity(), this);
                recycler.setAdapter(adaptadorDatosAlimentosSeleccionados);
            }
            recycler.setAdapter(adaptadorDatosAlimentosSeleccionados);
        }
        else {
            System.out.println("ERROR AL COMUINACARSE");
        }
    }
}