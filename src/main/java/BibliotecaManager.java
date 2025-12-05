import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

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
    private final String FILE_LIBRI = "libri.csv";
    private final String FILE_UTENTI = "utenti.csv";
    private final String FILE_PRESTITI = "prestiti.csv";
    
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
        salvaLinbroSuFile();
    }
    
     
    
    public void aggiungiUtente(String nome, String cognome, int idUtente, String email){
        Utente nuovoUtente = new Utente(nome, cognome, idUtente, email);
        registroUtenti.add(nuovoUtente);
        
    }
    
    public void aggiungiPrestito(Utente utente, LocalDate dataScadenza, Libro libro, LocalDate dataInzioPrestito){
        Prestito nuovoPrestito = new Prestito(utente, dataScadenza, libro,dataInzioPrestito);
        registroPrestiti.add(nuovoPrestito);
        if(nuovoPrestito.inzioPrestito()){
            salvaPrestitiSuFile();
        }    
            
    }
    
    public void restituisciLibro(Prestito prestitoDaChiudere){
        if(prestitoDaChiudere != null){
            registroPrestiti.remove(prestitoDaChiudere);
            prestitoDaChiudere.finePrestito();
            salvaPrestitiSuFile();
        }
        
    }
    public void salvaPrestitiSuFile(){
        try(PrintWriter writer = new PrintWriter(new File("prestiti.csv"))){
            for(Prestito p : registroPrestiti){
                writer.println(p.toCSV());
            }
        }catch(FileNotFoundException e){
            System.out.println("Errore nel salvataggio: "+ e.getMessage());
        }    
    }
    public void salvaLinbroSuFile(){
        try(PrintWriter writer = new PrintWriter(new File("libri.csv"))){
            for(Libro l : catalogo){
                writer.println(l.toCSV());
            }
        }catch(FileNotFoundException e){
            System.out.println("Errore nel salvataggio: "+ e.getMessage());
        }
    }

    
    
}
    
