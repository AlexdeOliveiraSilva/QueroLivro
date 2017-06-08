package br.com.wg7sistemas.querolivro;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wg7sistemas.querolivro.Models.Interesse;
import br.com.wg7sistemas.querolivro.Models.Livro;
import br.com.wg7sistemas.querolivro.Models.Usuario;

/**
 * Created by alexdeoliveirasilva on 30/05/17.
 */

public class AdapterTelaInicial extends RecyclerView.Adapter {
    List<Livro> listadelivros;
    Context contexto;

    public AdapterTelaInicial(List<Livro> listadelivros, Context contexto) {
        this.listadelivros = listadelivros;
        this.contexto = contexto;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.celulatelainiciallivros, parent, false);
        ViewHolderTelaInicial holder = new ViewHolderTelaInicial(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int s = position;
        ViewHolderTelaInicial celula = (ViewHolderTelaInicial) holder;
        final Livro e  = listadelivros.get(position) ;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdapterTelaInicial.this.contexto);
                //define o titulo
                builder.setTitle(R.string.app_name);
                //define a mensagem
                builder.setMessage("Você deseja demonstrar interesse pelo livro " + listadelivros.get(s).getNome() + " ao seu dono?");
                //define um botão para finalizar
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Implementar solicitar ao servidor
                        solicitarlivro(e.getId());
                    }
                });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

                //cria o AlertDialog e abre
                builder.create().show();
            }
        });


        celula.titulo.setText(e.getNome());
        celula.distancia.setText(e.getDistancia() + " km");
        celula.preco.setText(NumberFormat.getCurrencyInstance().format(e.getValor()));
        Picasso.with(contexto).load(e.getFoto()).resize(200,200).into(celula.foto);
    }


    public  void solicitarlivro(final int iddolivro){
        final ProgressDialog progDailog = ProgressDialog.show(AdapterTelaInicial.this.contexto,
                "Solicitando...",
                "Aguarde um instante por favor.", true);
        StringRequest postRequest = new StringRequest(Request.Method.POST,SingletonNetwork.URLserver + "solicitarlivro",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progDailog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(AdapterTelaInicial.this.contexto);
                        //define o titulo
                        builder.setTitle(R.string.app_name);
                        //define a mensagem
                        builder.setMessage("Sua requisição foi efetuada com sucesso. Aguarde o contato do doador do livro");
                        //define um botão para finalizar
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                //Implementar solicitar ao servidor
                            }
                        });

                        //cria o AlertDialog e abre
                        builder.create().show();
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
                params.put("livro", Integer.toString( iddolivro));
                return params;
            }
        };

        // This adds the request to the request queue
        SingletonNetwork.getInstance(AdapterTelaInicial.this.contexto)
                .addToRequestQueue(postRequest);




    }


    @Override
    public int getItemCount() {
        return listadelivros.size();
    }
}
