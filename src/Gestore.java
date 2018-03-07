/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Stavri Mele
 */
public class Gestore {
    String stato;
    String autore;
    Client c;
    Server s;
    public void setAutore(String nome)
    {
        autore=nome;
    }
    public String getAutore()
    {
        return autore;
    }
    Gestore()
    {
        
    }
}
