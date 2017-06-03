package br.com.wg7sistemas.querolivro;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import java.util.List;
import br.com.wg7sistemas.querolivro.Models.Interesse;


/**
 * Created by alexdeoliveirasilva on 01/06/17.
 */

public class AdpterSolicitacoes extends RecyclerView.Adapter {
    List<Interesse> listadeinteressados;
    Context contexto;

    public AdpterSolicitacoes(List<Interesse> listadeinteressados, Context contexto) {
        this.listadeinteressados = listadeinteressados;
        this.contexto = contexto;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.celulasolicitacoes, parent, false);
        ViewHolderInteresse holder = new ViewHolderInteresse(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderInteresse celula = (ViewHolderInteresse) holder;
        final Interesse e  = listadeinteressados.get(position) ;
        celula.titulo.setText(e.getLivro().getNome());
        celula.nomeinteressado.setText(e.getUsuariointeressado().getNome());
        celula.whatsappinteressado.setText("WhatsApp: " + e.getUsuariointeressado().getWhatsapp());
        celula.dataehorainteresse.setText(e.getDataehora());
        Picasso.with(contexto).load(e.getLivro().getFoto()).resize(200,200).into(celula.foto);

    }

    @Override
    public int getItemCount() {
        return listadeinteressados.size();
    }




}
