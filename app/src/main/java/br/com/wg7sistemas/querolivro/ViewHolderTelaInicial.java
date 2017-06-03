package br.com.wg7sistemas.querolivro;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by alexdeoliveirasilva on 30/05/17.
 */

public class ViewHolderTelaInicial extends RecyclerView.ViewHolder{
    ImageView foto;
    TextView titulo, distancia, preco;

    public ViewHolderTelaInicial(View view){
            super(view);
        foto = (ImageView) view.findViewById(R.id.fotolivrocelulaprincipal);
        titulo = (TextView) view.findViewById(R.id.textTitulodoLivro);
        distancia = (TextView) view.findViewById(R.id.textDistancia);
        preco = (TextView) view.findViewById(R.id.textPreco);

    }
}
