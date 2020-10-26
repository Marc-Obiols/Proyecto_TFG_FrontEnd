package com.example.proyecto_tfg_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

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

public class Activity_progeso extends AppCompatActivity {

    private LineChart grafico;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progeso);

        grafico = (LineChart) findViewById(R.id.grafico);

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
        LineDataSet lineadata1 = new LineDataSet(data, "Historial");
        lineadata1.setColor(Color.parseColor("#041DF4"));
        LineDataSet lineadata2 = new LineDataSet(pes_id, "Peso deseado");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineadata1);
        dataSets.add(lineadata2);
        LineData result = new LineData(dataSets);

        YAxis yaxisleft = grafico.getAxisLeft();
        YAxis yaxisright = grafico.getAxisRight();

        XAxis xAxis = grafico.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xLabel.get((int)value);
            }
        });

        //xAxis.setLabelCount(3);
        xAxis.setTextSize(8);
        grafico.setData(result);
        grafico.invalidate();
        grafico.setDrawBorders(true);
        grafico.setBorderColor(Color.parseColor("#03A9F4"));
        grafico.setContentDescription("");
    }
}