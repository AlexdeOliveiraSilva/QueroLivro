package br.com.wg7sistemas.querolivro;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by alexdeoliveirasilva on 03/06/17.
 */

public class ViewHolderInteresse extends RecyclerView.ViewHolder {
    ImageView foto;
    TextView titulo, nomeinteressado, whatsappinteressado, dataehorainteresse;

    public ViewHolderInteresse(View view){
        super(view);
        foto = (ImageView) view.findViewById(R.id.fotolivrocelulaprincipal);
        titulo = (TextView) view.findViewById(R.id.txtnomedolivro);
        nomeinteressado = (TextView) view.findViewById(R.id.txtusuariointeressado);
        whatsappinteressado = (TextView) view.findViewById(R.id.txtWhatsAppInteressado);
        dataehorainteresse = (TextView) view.findViewById(R.id.txtdataehorainteresse);
    }

}
