package com.example.proyecto_tfg_frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
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

public class contador_calorias extends AppCompatActivity implements Interfaz{

    private EditText buscador;
    private CircleImageView buscar;
    private Spinner ing;
    private AdaptadorDatosAlimentos adaptador;
    private RecyclerView recycler;
    private ArrayList<Alimento> resultado;
    private Dialog pantalla;
    private int llamada;
    private TextView kcalorias, proteinas, carbohidratos, fibra, grasas, kcalorias_o, proteinas_o, carbohidratos_o, fibra_o, grasas_o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador_calorias);

        pantalla = new Dialog(this);
        resultado = new ArrayList();
        buscador = (EditText) findViewById(R.id.buscador);
        buscar = (CircleImageView) findViewById(R.id.buscar);

        //plan
        kcalorias = (TextView) findViewById(R.id.kcal);
        proteinas = (TextView) findViewById(R.id.proteinas);
        carbohidratos = (TextView) findViewById(R.id.carbo_hidra);
        fibra = (TextView) findViewById(R.id.fibra);
        grasas = (TextView) findViewById(R.id.grasas);
        kcalorias_o = (TextView) findViewById(R.id.kcal_ob);
        proteinas_o = (TextView) findViewById(R.id.proteinas_o);
        carbohidratos_o = (TextView) findViewById(R.id.carbo_hidra_o);
        fibra_o = (TextView) findViewById(R.id.fibra_o);
        grasas_o = (TextView) findViewById(R.id.grasas_o);

        llamada = 3;
        Connection con = new Connection(contador_calorias.this);
        con.execute("http://169.254.145.10:3000/plan/"+UsuarioSingleton.getInstance().getId(),"GET",null);


        ing = (Spinner) findViewById(R.id.ingrediente);
        recycler = (RecyclerView) findViewById(R.id.lista_busqueda);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        String [] niv = new String[] {"alimentos", "comidas"};
        ArrayAdapter<String> aaDep;
        aaDep = new ArrayAdapter<String>(this, R.layout.spinner_item, niv);
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
                Connection con = new Connection(contador_calorias.this);
                con.execute("https://api.edamam.com/api/food-database/v2/parser?nutrition-type=logging&category="+categoria+"&ingr="+cons+"&app_id=3d46e174&app_key=2d4f7ffde05c02d5c5a122789e44c378","GET",null);
            }
        });
    }

    @Override
    public void Respuesta(JSONObject datos) throws JSONException {
        if (datos.getInt("codigo") == 200) {
            if (llamada == 1) {
                JSONArray hint = datos.getJSONArray("hints");
                if (hint == null) {
                    Toast.makeText(contador_calorias.this, "No hay resultados para esta búsqueda", Toast.LENGTH_LONG).show();
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
                        adaptador = new AdaptadorDatosAlimentos(resultado, this);
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
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        llamada = 2;
                                        Connection con = new Connection(contador_calorias.this);
                                        con.execute("http://169.254.145.10:3000/alimentacion/addAlimento/"+UsuarioSingleton.getInstance().getId(),"POST",req.toString());
                                        pantalla.dismiss();
                                    }
                                });
                                pantalla.show();
                            }
                        });
                    }
                }
            }
            else if (llamada == 2) {
                System.out.println(datos);
                kcalorias.setText(datos.getString("kcal"));
                proteinas.setText(datos.getString("proteina"));
                carbohidratos.setText(datos.getString("carbo"));
                fibra.setText(datos.getString("fibra"));
                grasas.setText(datos.getString("grasas"));
            }
            else if (llamada == 3) {
                kcalorias_o.setText(datos.getString("Kcal"));
                proteinas_o.setText(datos.getString("Proteinas"));
                carbohidratos_o.setText(datos.getString("Carbohidratos"));
                fibra_o.setText(datos.getString("Fibra"));
                grasas_o.setText(datos.getString("Grasas"));

                llamada = 2;
                Connection con2 = new Connection(contador_calorias.this);
                con2.execute("http://169.254.145.10:3000/alimentacion/dia/"+UsuarioSingleton.getInstance().getId(),"GET",null);
            }
        }
        else {
            System.out.println("ERROR AL COMUINACARSE");
        }
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
}