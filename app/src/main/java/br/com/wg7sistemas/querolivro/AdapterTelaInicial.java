package br.com.wg7sistemas.querolivro;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;

import br.com.wg7sistemas.querolivro.Models.Livro;

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
        ViewHolderTelaInicial celula = (ViewHolderTelaInicial) holder;
        final Livro e  = listadelivros.get(position) ;
        celula.titulo.setText(e.getNome());
        celula.distancia.setText(e.getDistancia() + " km");
        celula.preco.setText(NumberFormat.getCurrencyInstance().format(e.getValor()));
        Picasso.with(contexto).load(e.getFoto()).resize(200,200).into(celula.foto);
    }

    @Override
    public int getItemCount() {
        return listadelivros.size();
    }
}
