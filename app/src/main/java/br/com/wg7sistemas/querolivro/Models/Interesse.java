package br.com.wg7sistemas.querolivro.Models;

/**
 * Created by alexdeoliveirasilva on 03/06/17.
 */

public class Interesse {
    Livro livro;
    Usuario usuariointeressado;
    String dataehora;
    int id;

    public Interesse(Livro livro, Usuario usuariointeressado, String dataehora, int id) {
        this.livro = livro;
        this.usuariointeressado = usuariointeressado;
        this.dataehora = dataehora;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Livro getLivro() {

        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Usuario getUsuariointeressado() {
        return usuariointeressado;
    }

    public void setUsuariointeressado(Usuario usuariointeressado) {
        this.usuariointeressado = usuariointeressado;
    }

    public String getDataehora() {
        return dataehora;
    }

    public void setDataehora(String dataehora) {
        this.dataehora = dataehora;
    }
}
