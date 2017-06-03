package br.com.wg7sistemas.querolivro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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

public class MeusLivrosActivity extends AppCompatActivity {
    private RecyclerView listameuslivros;
    private List<Livro> listadelivros = new ArrayList<>();
    private AdpterMeusLivros adpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_livros);
        listameuslivros = (RecyclerView) findViewById(R.id.listameuslivros);
        adpter = new AdpterMeusLivros(listadelivros, this, this);
        listameuslivros.setAdapter(adpter);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listameuslivros.setLayoutManager(layout);
        this.carregaLivros();
    }

    public void carregaLivros(){
        listadelivros.clear();
        StringRequest postRequest = new StringRequest(Request.Method.POST,SingletonNetwork.URLserver + "getMyBooksAvaliable",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONArray allboks = new JSONArray(response);
                            for(int i = 0; i < allboks.length(); i++){
                                JSONObject temp = allboks.getJSONObject(i);
                                Usuario user = new Usuario(temp.getString("UserName"), temp.getString("WhatsApp"), temp.getInt("UserCode"));
                                Livro l = new Livro(temp.getString("Name"), temp.getString("Author"), temp.getString("Photo"), temp.getInt("Year"), temp.getDouble("Price"), 0.00, user);
                                l.setId(temp.getInt("Id"));
                                listadelivros.add(l);
                            }
                            adpter.notifyDataSetChanged();
                        } catch (JSONException e) {

                        }
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
                params.put("user", Integer.toString(Singleton.getInstance().usuariologado.getId()));


                return params;
            }
        };

        // This adds the request to the request queue
        SingletonNetwork.getInstance(MeusLivrosActivity.this)
                .addToRequestQueue(postRequest);
    }
}
