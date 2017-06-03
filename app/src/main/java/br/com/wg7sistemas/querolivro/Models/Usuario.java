package br.com.wg7sistemas.querolivro.Models;

/**
 * Created by alexdeoliveirasilva on 30/05/17.
 */

public class Usuario {
    private String nome, whatsapp;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario(String nome, String whatsapp, int id) {
        this.nome = nome;
        this.whatsapp = whatsapp;
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }
}
