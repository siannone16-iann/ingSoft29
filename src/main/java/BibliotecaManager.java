
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author davide
 */
public class BibliotecaManager {
    private ObservableList<Libro> catalogo;
    private ObservableList<Utente> registroUtenti;
    private ObservableList<Prestito> registroPrestiti;
    
    //costruttore
    public BibliotecaManager(){
        this.catalogo = FXCollections.observableArrayList();
        this.registroUtenti = FXCollections.observableArrayList();
        this.registroPrestiti = FXCollections.observableArrayList();
        
    }
    
    
    
    public ObservableList<Libro> getCatalogo() {
        return catalogo;
    }

    public ObservableList<Utente> getRegistroUtenti() {
        return registroUtenti;
    }

    public ObservableList<Prestito> getRegistroPrestiti() {
        return registroPrestiti;
    }
    
    
    
    public void aggiungiLibro(String titolo, int isbn, int annoProduzione, int copie){
        Libro nuovoLibro = new Libro(titolo, isbn, annoProduzione, copie);
        catalogo.add(nuovoLibro);
    }
    
     
    
    public void aggiungiUtente(String nome, String cognome, int idUtente, String email){
        Utente nuovoUtente = new Utente(nome, cognome, idUtente, email);
        registroUtenti.add(nuovoUtente);
        
    }
    
    public void aggiungiPrestito(Utente utente, LocalDate dataScadenza, Libro libro, LocalDate dataInzioPrestito){
        Prestito nuovoPrestito = new Prestito(utente, dataScadenza, libro,dataInzioPrestito);
        registroPrestiti.add(nuovoPrestito);
    }
    
}
    
