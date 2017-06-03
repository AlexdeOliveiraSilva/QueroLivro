package br.com.wg7sistemas.querolivro;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wg7sistemas.querolivro.Models.Livro;

/**
 * Created by alexdeoliveirasilva on 02/06/17.
 */

public class AdpterMeusLivros extends RecyclerView.Adapter{
    List<Livro> listadelivros;
    Context contexto;
    MeusLivrosActivity meuslivrosatividade;

    public AdpterMeusLivros(List<Livro> listadelivros, Context contexto, MeusLivrosActivity meuslivrosatividade) {
        this.listadelivros = listadelivros;
        this.contexto = contexto;
        this.meuslivrosatividade = meuslivrosatividade;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.celulameuslivros, parent, false);
        ViewHolverMeusLivros holder = new ViewHolverMeusLivros(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolverMeusLivros celula = (ViewHolverMeusLivros) holder;
        final Livro e  = listadelivros.get(position) ;
        celula.titulo.setText(e.getNome());
        celula.autor.setText(e.getAutor());
        celula.preco.setText(NumberFormat.getCurrencyInstance().format(e.getValor()));
        Picasso.with(contexto).load(e.getFoto()).resize(200,200).into(celula.foto);
        celula.btnExcluirMeusLivros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
                //define o titulo
                builder.setTitle(R.string.app_name);
                //define a mensagem
                builder.setMessage(R.string.string_confirma_excluir_livro);
                //define um botão como positivo
                builder.setPositiveButton(R.string.string_sim, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteBook( e.getId());
                    }
                });

                builder.setNegativeButton(R.string.string_nao, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

                //cria o AlertDialog e abre
                builder.create().show();
            }
        });
    }

    //Método para apagar um livro
    public void deleteBook(final int id){
        final ProgressDialog progDailog = ProgressDialog.show(contexto,
                "Carregando...",
                "Aguarde um instante por favor.", true);


        StringRequest postRequest = new StringRequest(Request.Method.POST,SingletonNetwork.URLserver + "deleteBook",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progDailog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
                        //define o titulo
                        builder.setTitle(R.string.app_name);
                        //define a mensagem
                        builder.setMessage(R.string.string_livroapagado);
                        //define um botão para finalizar
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                meuslivrosatividade.carregaLivros();
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
                params.put("code",Integer.toString(id));
                return params;
            }
        };

        // This adds the request to the request queue
        SingletonNetwork.getInstance(contexto)
                .addToRequestQueue(postRequest);
    }

    @Override
    public int getItemCount() {
        return listadelivros.size();
    }
}
