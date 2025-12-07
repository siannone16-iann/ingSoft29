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
    private String nome;
    private String cognome;
    private int idUtente;
    private String email;
    private int prestiti;

    public Utente(String nome, String cognome, int idUtente, String email, int prestiti) {
        this.nome = nome;
        this.cognome = cognome;
        this.idUtente = idUtente;
        this.email = email;
        this.prestiti = prestiti;
    }
    
    public Utente(String nome, String cognome, int idUtente, String email){
        this(nome, cognome, idUtente, email, 0);
    }

    public void modificaUtente (String nome, String cognome, int idUtente, String email){
        this.nome = nome;
        this.cognome = cognome;
        this.idUtente = idUtente;
        this.email = email;
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

    public void setPrestiti(int prestiti) {
        this.prestiti = prestiti;
    }
    
    

    public boolean richiestaPrestito() {
        if (prestiti < 3) {
            prestiti++;
            return true;
        }
        return false;
    }

    public void finePrestito(){
        if(prestiti > 0){
            prestiti--;
        }
        
    }
    
    public String toCSV(){
        return nome +";"+cognome+";"+idUtente+";"+email+";"+prestiti;
    }
}    
