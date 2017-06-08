package br.com.wg7sistemas.querolivro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import br.com.wg7sistemas.querolivro.Models.Livro;
import br.com.wg7sistemas.querolivro.Models.Usuario;

public class LoginActivity extends AppCompatActivity {
    LoginButton btnfacebook;
    CallbackManager callbackManager;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnfacebook = (LoginButton) findViewById(R.id.btnLogincomfacebook);
        btnfacebook.setReadPermissions("email");
        btnfacebook.setReadPermissions("user_friends");
        btnfacebook.setReadPermissions("public_profile");
        btnfacebook.setReadPermissions("user_birthday");
        callbackManager = CallbackManager.Factory.create();

        try {
            PackageInfo info = null;

                info = getPackageManager().getPackageInfo("br.com.wg7sistemas.querolivro", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = null;

                md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        btnfacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                try {
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    tryLoginFacebook(id, name);
                                  //  LoginManager.getInstance().logOut();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                // App code
                Log.v("LoginActivity", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });

        checkLogin();
    }


    public void tryLoginFacebook(final String id, final String name) {
        final ProgressDialog progDailog = ProgressDialog.show(this,
                "Carregando...",
                "Aguarde um instante por favor.", true);

        StringRequest postRequest = new StringRequest(Request.Method.POST,SingletonNetwork.URLserver + "login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progDailog.dismiss();
                        try {
                                JSONObject temp =  new JSONObject(response);
                                JSONObject dadousuario = temp.getJSONObject("usuario");
                                Usuario user = new Usuario(dadousuario.getString("Name"), dadousuario.getString("WhatsApp"), dadousuario.getInt("Code"));
                                Singleton.getInstance().usuariologado = user;

                                //Persitir Objeto do usuário logado
                                    Gson gson = new GsonBuilder()
                                            .create();
                                    String jsonobjeto = gson.toJson(user);
                                    SharedPreferences sharedPref = LoginActivity.this.getSharedPreferences("br.com.wg7sistemas.querolivro.lg", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("conteudo", jsonobjeto);
                                    editor.commit();

                                    //Abre tela de inicio
                                    Intent a = new Intent(LoginActivity.this, TelaPrincipalActivity.class);
                                    startActivity(a);

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
                params.put("idfacebook", id);
                params.put("name", name);
                return params;
            }
        };

        // This adds the request to the request queue
        SingletonNetwork.getInstance(LoginActivity.this)
                .addToRequestQueue(postRequest);


    }

    private void checkLogin(){
        SharedPreferences sharedPref = getSharedPreferences("br.com.wg7sistemas.querolivro.lg", Context.MODE_PRIVATE);
        String texto = sharedPref.getString("conteudo", null);
        if(texto != null && !texto.equals("")){
            Gson gson = new GsonBuilder()
                    .create();
            Usuario  userlog = gson.fromJson(texto, Usuario.class);
            Singleton.getInstance().usuariologado = userlog;
            Intent intent = new Intent(LoginActivity.this, TelaPrincipalActivity.class);
            LoginActivity.this.startActivity(intent);
            finish();
        }
    }





}
