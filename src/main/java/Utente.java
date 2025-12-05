/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salvatoremoccia
 */
public class Utente {
    private final String nome;
    private final String cognome;
    private final int idUtente;
    private final String email;
    private int prestiti;

    public Utente(String nome, String cognome, int idUtente, String email) {
        this.nome = nome;
        this.cognome = cognome;
        this.idUtente = idUtente;
        this.email = email;
        this.prestiti = 0;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public String getEmail() {
        return email;
    }

    public int getPrestiti() {
        return prestiti;
    }

    public boolean richiestaPrestito() {
        if (prestiti < 3) {
            prestiti++;
            return true;
        }
        return false;
    }

    public void finePrestito(){
        prestiti--;
    }
    
    public String toCSV(){
        return nome +";"+cognome+";"+idUtente+";"+";"+email;
    }
}    
