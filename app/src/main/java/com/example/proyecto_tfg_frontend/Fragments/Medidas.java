package com.example.proyecto_tfg_frontend.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyecto_tfg_frontend.Adaptadores.AdaptadorDatosMedidas;
import com.example.proyecto_tfg_frontend.R;
import com.example.proyecto_tfg_frontend.SeparatorDecoration;
import com.example.proyecto_tfg_frontend.UsuarioSingleton;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Medidas extends Fragment {

    private LineChart grafico;
    private RecyclerView recycler;
    private ArrayList<String> list;
    private AdaptadorDatosMedidas adaptador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_medidas, container, false);
        grafico = (LineChart) view.findViewById(R.id.grafico);
        recycler = (RecyclerView) view.findViewById(R.id.historial);
        recycler.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL,false));
        SeparatorDecoration decoration = new SeparatorDecoration(getActivity(), R.color.Transparente, 5f);
        recycler.addItemDecoration(decoration);

        Date[] aux = UsuarioSingleton.getInstance().getFechas();
        String[] fechas = new String[aux.length];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(int i = 0; i < aux.length; i++) {
            fechas[i] = sdf.format(aux[i]);
        }
        for(int j=0;j<fechas.length;j++) {  //length is the property of the array
            System.out.println(fechas[j]);
        }
        int[] pesos = UsuarioSingleton.getInstance().getPesos();

        ArrayList<Entry> data = new ArrayList<Entry>();
        ArrayList<Entry> pes_id = new ArrayList<Entry>();
        if (aux.length == 1) pes_id.add(new Entry(0, UsuarioSingleton.getInstance().getPeso_des()));
        else {
            pes_id.add(new Entry(0, UsuarioSingleton.getInstance().getPeso_des()));
            pes_id.add(new Entry(aux.length-1, UsuarioSingleton.getInstance().getPeso_des()));
        }
        final ArrayList<String> xLabel = new ArrayList<>();
        for(int i = 0; i < aux.length; i++) {
            data.add(new Entry(i, pesos[i]));
            xLabel.add(fechas[i]);
        }
        for(int j=0;j<fechas.length;j++) {  //length is the property of the array
            System.out.println(xLabel.get(j));
        }
        LineDataSet lineadata1 = new LineDataSet(data, "Historial");
        lineadata1.setColor(Color.parseColor("#041DF4"));
        lineadata1.setCircleColor(Color.parseColor("#041DF4"));
        lineadata1.setCircleColorHole(Color.parseColor("#041DF4"));

        LineDataSet lineadata2 = new LineDataSet(pes_id, "Peso deseado");
        lineadata2.setColor(Color.parseColor("#FF0000"));
        lineadata2.setCircleColor(Color.parseColor("#FF0000"));
        lineadata2.setCircleColorHole(Color.parseColor("#FF0000"));

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineadata1);
        dataSets.add(lineadata2);
        LineData result = new LineData(dataSets);

        YAxis yaxisleft = grafico.getAxisLeft();
        YAxis yaxisright = grafico.getAxisRight();
        yaxisright.setEnabled(false);

        XAxis xAxis = grafico.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                System.out.println(value);
                if (value < 0) return "";
                if (value >= xLabel.size()) return "";
                return xLabel.get((int)value);
            }
        });
        xAxis.setLabelCount(2);
        xAxis.setTextSize(8);
        grafico.setData(result);
        grafico.invalidate();
        grafico.setDrawBorders(true);
        grafico.setBorderColor(Color.parseColor("#03A9F4"));
        grafico.setContentDescription("");
        llenar_recycler();
        return view;
    }

    private void llenar_recycler(){
        list = new ArrayList<>();
        SimpleDateFormat sdfr = new SimpleDateFormat("dd-MMM-yyyy");
        Date [] fechas_aux = UsuarioSingleton.getInstance().getFechas();
        int [] pes_aux = UsuarioSingleton.getInstance().getPesos();
        int j = 0;
        for (int i = pes_aux.length-1; i >= 0; i--) {
            String f = sdfr.format(fechas_aux[i]);
            String p = Integer.toString(pes_aux[i]);
            list.add(j, f + " " + p + " Kg");
            j++;
        }
        adaptador = new AdaptadorDatosMedidas(list);
        recycler.setAdapter(adaptador);
    }

}