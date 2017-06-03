package br.com.wg7sistemas.querolivro.Models;

/**
 * Created by alexdeoliveirasilva on 30/05/17.
 */

public class Livro {
    private String nome, autor, foto;
    //Distância em km
    private int ano, id;
    private double valor, distancia;
    private Usuario donodolivro;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getDonodolivro() {
        return donodolivro;
    }

    public void setDonodolivro(Usuario donodolivro) {
        this.donodolivro = donodolivro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public Livro(String nome, String autor, String foto, int ano, double valor, double distancia, Usuario donodolivro) {
        this.nome = nome;
        this.autor = autor;
        this.foto = foto;
        this.ano = ano;
        this.valor = valor;
        this.distancia = distancia;
        this.donodolivro = donodolivro;
    }

    public double getValor() {
        return valor;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
