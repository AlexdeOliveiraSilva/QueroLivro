package br.com.wg7sistemas.querolivro;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by alexdeoliveirasilva on 02/06/17.
 */

public class ViewHolverMeusLivros  extends RecyclerView.ViewHolder {
    ImageView foto;
    TextView titulo, autor, preco;
    ImageButton btnExcluirMeusLivros;

    public ViewHolverMeusLivros(View view){
        super(view);
        foto = (ImageView) view.findViewById(R.id.fotolivrocelulaprincipal);
        titulo = (TextView) view.findViewById(R.id.textTitulodoLivro);
        autor = (TextView) view.findViewById(R.id.txtAutorcelulameuslivros);
        preco = (TextView) view.findViewById(R.id.textPreco);
        btnExcluirMeusLivros = (ImageButton) view.findViewById(R.id.btnExcluirMeusLivros);

    }
}
