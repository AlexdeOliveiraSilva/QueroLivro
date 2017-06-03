package br.com.wg7sistemas.querolivro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConfiguracoesActivity extends AppCompatActivity {
    private EditText txtNumeroWhatsApp;
    private Button btnsalvarconfiguracoes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        txtNumeroWhatsApp = (EditText) findViewById(R.id.txtNumeroWhatsApp);
        btnsalvarconfiguracoes = (Button) findViewById(R.id.btnsalvarconfiguracoes);
        btnsalvarconfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void carregaWhatsApp(){
        txtNumeroWhatsApp.setText("");
    }
}
