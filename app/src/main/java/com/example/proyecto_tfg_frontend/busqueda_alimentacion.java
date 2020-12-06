package com.example.proyecto_tfg_frontend;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class busqueda_alimentacion extends Fragment implements Interfaz{

    private RecyclerView recycler;
    private EditText buscador;
    private CircleImageView buscar;
    private Spinner ing;
    private Dialog pantalla;
    private int llamada;
    private ArrayList<Alimento> resultado;
    private AdaptadorDatosAlimentos adaptador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_busqueda_alimentacion, container, false);
        recycler = (RecyclerView) view.findViewById(R.id.lista_busqueda);
        recycler.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recycler.getContext(), LinearLayoutManager.VERTICAL);
        recycler.addItemDecoration(mDividerItemDecoration);
        pantalla = new Dialog(this.getActivity());
        if (resultado == null)
            resultado = new ArrayList();
        else {
            adaptador = new AdaptadorDatosAlimentos(resultado, this.getActivity());
            recycler.setAdapter(adaptador);
        }
        buscador = (EditText) view.findViewById(R.id.buscador);
        buscar = (CircleImageView) view.findViewById(R.id.buscar);

        ing = (Spinner) view.findViewById(R.id.ingrediente);
        String [] niv = new String[] {"alimentos", "comidas"};
        ArrayAdapter<String> aaDep;
        aaDep = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item, niv);
        ing.setAdapter(aaDep);
        ing.setContentDescription("alimentos");

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("LA QUERY ES: " + buscador.getText().toString());
                String categoria = ing.getSelectedItem().toString();
                if (categoria.equals("alimentos")) categoria = "generic-foods";
                else categoria = "generic-meals";
                System.out.println(categoria);
                String cons = nombreToUrl(buscador.getText().toString());
                llamada = 1;
                Connection con = new Connection(busqueda_alimentacion.this);
                con.execute("https://api.edamam.com/api/food-database/v2/parser?nutrition-type=logging&category="+categoria+"&ingr="+cons+"&app_id=3d46e174&app_key=2d4f7ffde05c02d5c5a122789e44c378","GET",null);
            }
        });

        return view;
    }

    private String nombreToUrl(String nombre) {
        String res = "";
        for (int i=0; i < nombre.length(); i++) {
            char aux = nombre.charAt(i);
            if (aux != ' ') res = res + aux;
            else res = res + "%20";
        }
        return res;
    }

    @Override
    public void Respuesta(JSONObject datos) throws JSONException {
        if (datos.getInt("codigo") == 200) {
            if (llamada == 1) {
                llamada = 5;
                JSONArray hint = datos.getJSONArray("hints");
                if (hint == null) {
                    Toast.makeText(this.getActivity(), "No hay resultados para esta búsqueda", Toast.LENGTH_LONG).show();
                } else {
                    resultado = new ArrayList<>();
                    for (int i = 0; i < hint.length(); i++) {
                        String url_img = "";
                        String contenido_plato = "";
                        JSONObject elemento = hint.getJSONObject(i);
                        JSONObject food = elemento.getJSONObject("food");
                        String id = food.getString("foodId");
                        String nombre_plato_ing = food.getString("label");
                        JSONObject nutrients = food.getJSONObject("nutrients");
                        if (food.has("image")) {
                            url_img = food.getString("image");
                        }
                        double kcal = nutrients.getDouble("ENERC_KCAL");
                        kcal = Math.round(kcal * 100);
                        kcal = kcal / 100;
                        double prot = nutrients.getDouble("PROCNT");
                        prot = Math.round(prot * 100);
                        prot = prot / 100;
                        double gras = nutrients.getDouble("FAT");
                        gras = Math.round(gras * 100);
                        gras = gras / 100;
                        double carbo = nutrients.getDouble("CHOCDF");
                        carbo = Math.round(carbo * 100);
                        carbo = carbo / 100;
                        double fibra = nutrients.getDouble("FIBTG");
                        fibra = Math.round(fibra * 100);
                        fibra = fibra / 100;
                        if (food.has("foodContentsLabel")) {
                            contenido_plato = food.getString("foodContentsLabel");
                        }
                        resultado.add(new Alimento(id, contenido_plato, url_img, nombre_plato_ing, kcal, prot, gras, carbo, fibra));
                        //AHORA LOS DATOS DE LOS NUTRIENTES ESTAN EN 100G
                        adaptador = new AdaptadorDatosAlimentos(resultado, this.getActivity());
                        recycler.setAdapter(adaptador);
                        adaptador.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Button add;
                                TextView kcal, prot, carbo, grasas, fibra, nombre, receta;
                                ImageView imagen;
                                EditText gramos;
                                pantalla.setContentView(R.layout.popup_info_alimento);
                                Alimento alimento_seleccionado = resultado.get(recycler.getChildAdapterPosition(v));

                                nombre = (TextView) pantalla.findViewById(R.id.nom);
                                kcal = (TextView) pantalla.findViewById(R.id.kcal);
                                prot = (TextView) pantalla.findViewById(R.id.proteinas);
                                carbo = (TextView) pantalla.findViewById(R.id.carbo_hidra);
                                grasas = (TextView) pantalla.findViewById(R.id.grasas);
                                fibra = (TextView) pantalla.findViewById(R.id.fibra);
                                receta = (TextView) pantalla.findViewById(R.id.receta);
                                add = (Button) pantalla.findViewById(R.id.add);
                                gramos = (EditText) pantalla.findViewById(R.id.gramos);
                                imagen = (ImageView) pantalla.findViewById(R.id.img);

                                nombre.setText(alimento_seleccionado.getNombre_plato_ing());
                                receta.setText(alimento_seleccionado.getContenido_plato());
                                kcal.setText(alimento_seleccionado.getKcal().toString());
                                prot.setText(alimento_seleccionado.getProt().toString());
                                carbo.setText(alimento_seleccionado.getCarbo().toString());
                                grasas.setText(alimento_seleccionado.getGras().toString());
                                fibra.setText(alimento_seleccionado.getFibra().toString());

                                if (!alimento_seleccionado.getUrl_img().equals(""))
                                    Picasso.get().load(alimento_seleccionado.getUrl_img()).into(imagen);
                                add.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        JSONObject req = new JSONObject();
                                        try {
                                            req.put("cantidad", Integer.parseInt(gramos.getText().toString()));
                                            req.put("id_alimento", alimento_seleccionado.getId());
                                            req.put("kcal",alimento_seleccionado.getKcal());
                                            req.put("fibra", alimento_seleccionado.getFibra());
                                            req.put("proteina", alimento_seleccionado.getProt());
                                            req.put("carbo", alimento_seleccionado.getCarbo());
                                            req.put("grasas", alimento_seleccionado.getGras());
                                            req.put("nombre_al", alimento_seleccionado.getNombre_plato_ing());
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Connection con = new Connection(busqueda_alimentacion.this);
                                        con.execute("http://192.168.0.14:3000/alimentacion/addAlimento/"+UsuarioSingleton.getInstance().getId(),"POST",req.toString());
                                        pantalla.dismiss();
                                    }
                                });
                                pantalla.show();
                            }
                        });
                    }
                }
            }

        }
        else {
            System.out.println("ERROR AL COMUINACARSE");
        }
    }
}