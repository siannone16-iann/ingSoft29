
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
        
    Utente utente0 = new Utente("Davide", "Martelli", 0, "ciao0@email.com");
    Utente utente1 = new Utente("Simone", "Iannone", 1, "ciao1@email.com");
    Utente utente2 = new Utente("Salvatore", "Moccia", 2, "ciao2@email.com");
    Utente utente3 = new Utente("Lorenzo", "Nevola", 3, "ciao3@email.com");
    Utente utente4 = new Utente("Pinco", "Pallino", 4, "ciao4@email.com");
    
    Libro libro0 = new Libro("titolo0", 0, 2010, 2);
    Libro libro1 = new Libro("titolo1", 1, 2011, 3);
    Libro libro2 = new Libro("titolo2", 2, 2011, 1);
    Libro libro3 = new Libro("titolo3", 3, 2019, 1);
    Libro libro4 = new Libro("titolo4", 4, 2024, 4);
    
    Prestito prestito0 = new Prestito(utente0, LocalDate.of(2025, 12, 31), libro0,LocalDate.now());
    Prestito prestito1 = new Prestito(utente1, LocalDate.of(2025, 11, 28), libro1,LocalDate.now());
    Prestito prestito2 = new Prestito(utente3, LocalDate.of(2026, 01, 01), libro4,LocalDate.now());
        
        aggiungiLibro(libro0);
        aggiungiLibro(libro1);
        aggiungiLibro(libro2);
        aggiungiLibro(libro3);
        aggiungiLibro(libro4);
        
        aggiungiUtente(utente0);
        aggiungiUtente(utente1);
        aggiungiUtente(utente2);
        aggiungiUtente(utente3);
        aggiungiUtente(utente4);
        
        aggiungiPrestito(prestito0);
        aggiungiPrestito(prestito1);
        aggiungiPrestito(prestito2);
        
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
    
    
    
    public void aggiungiLibro(Libro nuovoLibro){
        catalogo.add(nuovoLibro);
    }
    
    public void aggiungiUtente(Utente nuovoUtente){
        registroUtenti.add(nuovoUtente);
        
    }
    
    public void aggiungiPrestito(Prestito nuovoPrestito){
        registroPrestiti.add(nuovoPrestito);
    }
    
}
    
