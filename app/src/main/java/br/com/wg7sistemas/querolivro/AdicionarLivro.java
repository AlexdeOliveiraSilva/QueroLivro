package br.com.wg7sistemas.querolivro;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import br.com.wg7sistemas.querolivro.Models.Livro;
import br.com.wg7sistemas.querolivro.Models.Usuario;

public class AdicionarLivro extends AppCompatActivity {
    private Button btnsalvarnovolivro;
    private ImageButton btnfotolivro;
    private EditText txtnomedolivro, txtautordolivro, txtprecodolivro;
    Bitmap fotodolivro = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_livro);
        btnsalvarnovolivro = (Button) findViewById(R.id.btnsalvarnovolivro);
        btnfotolivro = (ImageButton) findViewById(R.id.btnfotolivro);
        txtnomedolivro = (EditText) findViewById(R.id.txtnomedolivro);
        txtautordolivro = (EditText) findViewById(R.id.txtautordolivro);
        txtprecodolivro = (EditText) findViewById(R.id.txtprecodolivro);
        final Context contexto = this;
        btnfotolivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 2);
            }
        });
        btnsalvarnovolivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensagemderro = "";
                // Testa se o nome do livro esta vazio
                if(txtnomedolivro.getText().toString().equals(""))
                            mensagemderro += "Nome do Livro é campo obrigatório\n";
                //Testa se o livro tem preço
                 if(txtprecodolivro.getText().toString().equals(""))
                    mensagemderro += "Digite um valor para o livro por gentileza.";

                //Se nao tiver mensagem de erro, pode salvar, caso contrario exibe a mensagem
                if(mensagemderro.equals("")){
                            salvar();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
                    //define o titulo
                    builder.setTitle(R.string.app_name);
                    //define a mensagem
                    builder.setMessage(mensagemderro);
                    //define um botão como positivo
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

                    //cria o AlertDialog e abre
                    builder.create().show();



                }
            }
        });
    }


    private void salvar(){
        final ProgressDialog progDailog = ProgressDialog.show(this,
                "Carregando...",
                "Aguarde um instante por favor.", true);

        final Context contexto = this;
        StringRequest postRequest = new StringRequest(Request.Method.POST,SingletonNetwork.URLserver + "addBook",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progDailog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
                        //define o titulo
                        builder.setTitle(R.string.app_name);
                        //define a mensagem
                        builder.setMessage(R.string.string_salvocomsucesso);
                        //define um botão como positivo
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                finish();
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
                params.put("nome", txtnomedolivro.getText().toString());
                params.put("autor", txtautordolivro.getText().toString());
                params.put("preco", txtprecodolivro.getText().toString());
                params.put("user", Integer.toString(Singleton.getInstance().usuariologado.getId()));
                if(fotodolivro != null){
                    //Transforma a imagem em string base64 para enviar para o servidor
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    fotodolivro.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream .toByteArray();
                    String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    params.put("imagem", encoded);
                }else{
                    //Envia sem imagem e o servidor entende que deve colocar a imagem padrão
                    params.put("imagem", "");
                }
                return params;
            }
        };

        // This adds the request to the request queue
        SingletonNetwork.getInstance(AdicionarLivro.this)
                .addToRequestQueue(postRequest);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                if (resultCode == Activity.RESULT_OK) {


                    try {
                        fotodolivro = (Bitmap) data.getExtras().get("data");
                        btnfotolivro.setImageBitmap(fotodolivro);

                    } catch (Exception e) {

                    }
                }
        }
    }


}
