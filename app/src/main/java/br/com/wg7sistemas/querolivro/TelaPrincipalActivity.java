package br.com.wg7sistemas.querolivro;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wg7sistemas.querolivro.Models.Livro;
import br.com.wg7sistemas.querolivro.Models.Usuario;

public class TelaPrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private RecyclerView lista;
    private List<Livro> listadelivros = new ArrayList<Livro>();
    private AdapterTelaInicial adpter;
    private TextView nomedousuario;
    int contador = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lista = (RecyclerView) findViewById(R.id.listatelainicial);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent doarlivro = new Intent(TelaPrincipalActivity.this, AdicionarLivro.class);
                startActivity(doarlivro);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lista.addItemDecoration(new DividerItemDecorationRecyclerView(this, LinearLayoutManager.VERTICAL));
        adpter = new AdapterTelaInicial(listadelivros, this);
        lista.setAdapter(adpter);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        lista.setLayoutManager(layout);



        popularlivros();

//        nomedousuario = (TextView) navigationView.findViewById(R.id.txtnomesuario);
//        nomedousuario.setText(Singleton.getInstance().usuariologado.getNome());

        //FirebaseInstanceId.getInstance().getToken()
        //Captura o "Id desde dispositivo" no firebase

        enviarDadosCelularServidor(FirebaseInstanceId.getInstance().getToken(), -1.0, -1.0);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getBaseContext(),"Não existe permissão para localização",
                    Toast.LENGTH_LONG).show();
            return;
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }


    }

    //Método que envia para o servidor dados relacionados a notificação, latitude e longitude
    //Usa-se -1 na latitude e longitude quando não queiras que o servidor a trate

    public void enviarDadosCelularServidor(final  String tokenNotification, final double latitude, final double longitude){
        StringRequest postRequest = new StringRequest(Request.Method.POST,SingletonNetwork.URLserver + "updatebasicData",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            switch(response.statusCode) {
                                default:
                                    String value = null;
                                    try {

                                        value = new String(response.data, "UTF-8");
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    json = trimMessage(value, "message");
                                    break;
                            }
                        }
                        error.printStackTrace();
                    }
                    public String trimMessage(String json, String key){
                        String trimmedString = null;
                        try{
                            JSONObject obj = new JSONObject(json);
                            trimmedString = obj.getString(key);
                        } catch(JSONException e){
                            e.printStackTrace();
                            return null;
                        }
                        return trimmedString;
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //Aqui se passa os parametros para o servidor
                params.put("latitude", Double.toString(latitude));
                params.put("longitude", Double.toString(longitude));
                params.put("token", tokenNotification);
                params.put("user", Integer.toString(Singleton.getInstance().usuariologado.getId()));
                return params;
            }
        };

        // This adds the request to the request queue
        SingletonNetwork.getInstance(TelaPrincipalActivity.this)
                .addToRequestQueue(postRequest);


    }

    public void popularlivros(){
        StringRequest postRequest = new StringRequest(Request.Method.POST,SingletonNetwork.URLserver + "getAllBooksAvaliable",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONArray allboks = new JSONArray(response);
                            for(int i = 0; i < allboks.length(); i++){
                                    JSONObject temp = allboks.getJSONObject(i);
                                    Usuario user = new Usuario(temp.getString("UserName"), temp.getString("WhatsApp"), temp.getInt("UserCode"));
                                    Livro l = new Livro(temp.getString("Name"), temp.getString("Author"), temp.getString("Photo"), temp.getInt("Year"), temp.getDouble("Price"), 0.00, user);
                                        l.setId(temp.getInt("IdBook"));
                                    listadelivros.add(l);
                            }



                        } catch (JSONException e) {

                        }

                      adpter.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            switch(response.statusCode) {
                                default:
                                    String value = null;
                                    try {

                                        value = new String(response.data, "UTF-8");
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    json = trimMessage(value, "message");
                                    break;
                            }
                        }
                        error.printStackTrace();
                    }
                    public String trimMessage(String json, String key){
                        String trimmedString = null;
                        try{
                            JSONObject obj = new JSONObject(json);
                            trimmedString = obj.getString(key);
                        } catch(JSONException e){
                            e.printStackTrace();
                            return null;
                        }
                        return trimmedString;
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                //Aqui se passa os parametros para o servidor
//                params.put("latitude", Double.toString(latitude));
//                params.put("longitude", Double.toString(longitude));

                return params;
            }
        };

        // This adds the request to the request queue
        SingletonNetwork.getInstance(TelaPrincipalActivity.this)
                .addToRequestQueue(postRequest);





    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tela_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent config = new Intent(this, ConfiguracoesActivity.class);
            startActivity(config);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        // Cada if é para quando clica em um dos botoes do menu lateral
        if (id == R.id.nav_doarlivros) {
                Intent doarlivro = new Intent(this, AdicionarLivro.class);
                startActivity(doarlivro);
        }else if(id == R.id.nav_meuslivros){
            Intent meuslivros = new Intent(this, MeusLivrosActivity.class);
            startActivity(meuslivros);
        }else if(id == R.id.nav_solicitacoes){
            Intent solicitacoes = new Intent(this, SolicitacoesAcitivity.class);
            startActivity(solicitacoes);
        }else if(id == R.id.nav_config){
            Intent config = new Intent(this, ConfiguracoesActivity.class);
            startActivity(config);
        }else if(id == R.id.nav_exit){
            try{
                LoginManager.getInstance().logOut();
            }
            catch (Exception e){

            }

            SharedPreferences sharedPref = TelaPrincipalActivity.this.getSharedPreferences("br.com.wg7sistemas.querolivro.lg", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("conteudo", "");
            editor.commit();

            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude =  (location.getLatitude());
        double longitude = (location.getLongitude());
        //Envia somente uma vez
        // Quando o app é aberto

        if(contador == 0){
            enviarDadosCelularServidor(FirebaseInstanceId.getInstance().getToken(),latitude, longitude);
            contador++;

        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
