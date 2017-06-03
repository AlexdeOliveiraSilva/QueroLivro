package br.com.wg7sistemas.querolivro;


import br.com.wg7sistemas.querolivro.Models.Usuario;

/**
 * Created by alexdeoliveirasilva on 03/06/17.
 */


public class Singleton {

    public Usuario usuariologado =  new Usuario("Alex", "(00) 0909-9090", 1);


    private static Singleton sharedInstance;
    private Singleton() {

    }

    public static synchronized Singleton getInstance() {
        if (sharedInstance == null)
            sharedInstance = new Singleton();
        return sharedInstance;
    }





}
