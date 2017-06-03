package br.com.wg7sistemas.querolivro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void loginwithfacebook(View e){
        Intent a = new Intent(this, TelaPrincipalActivity.class);
        startActivity(a);
    }
}
