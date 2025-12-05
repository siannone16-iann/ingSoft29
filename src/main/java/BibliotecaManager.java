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
        
        caricaDati();
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
    
    
    
    public void aggiungiLibro(String titolo, String autore, int isbn, int annoProduzione, int copie){
        Libro nuovoLibro = new Libro(titolo, autore, isbn, annoProduzione, copie);
        catalogo.add(nuovoLibro);
        salvaLibroSuFile();
    }
    
     
    
    public void aggiungiUtente(String nome, String cognome, int idUtente, String email){
        Utente nuovoUtente = new Utente(nome, cognome, idUtente, email);
        registroUtenti.add(nuovoUtente);
        salvaUtentesufile();  
    }
    
    public void aggiungiPrestito(Utente utente, LocalDate dataScadenza, Libro libro, LocalDate dataInzioPrestito){
        //Prestito nuovoPrestito = new Prestito(utente, dataScadenza, libro,dataInzioPrestito);
        
        /*if(nuovoPrestito.inzioPrestito(libro, utente)){
            registroPrestiti.add(nuovoPrestito);
            salvaPrestitiSuFile();
            System.out.println("prestito salvato");
        }    
        System.out.println("prestito non salvato");
        */
        if(utente.getPrestiti() < 3 && libro.getCopie() > libro.getCopiePrestate()){
            Prestito nuovoPrestito = new Prestito(utente, dataScadenza, libro,dataInzioPrestito);
            registroPrestiti.add(nuovoPrestito);
            salvaPrestitiSuFile();
            System.out.println("prestito salvato");
        }
        else System.out.println("prestito non salvato");

    }
    
    public void restituisciLibro(Prestito prestitoDaChiudere){
        if(prestitoDaChiudere != null){
            registroPrestiti.remove(prestitoDaChiudere);
            prestitoDaChiudere.finePrestito();
            salvaPrestitiSuFile();
        }
        
    }
    public void salvaPrestitiSuFile(){
        try(PrintWriter writer = new PrintWriter(new File("src/main/resources/prestiti.csv"))){
            for(Prestito p : registroPrestiti){
                writer.println(p.toCSV());
            }
        }catch(FileNotFoundException e){
            System.out.println("Errore nel salvataggio: "+ e.getMessage());
        }    
    }
    public void salvaLibroSuFile(){
        try(PrintWriter writer = new PrintWriter(new File("src/main/resources/libri.csv"))){
            for(Libro l : catalogo){
                writer.println(l.toCSV());
            }
        }catch(FileNotFoundException e){
            System.out.println("Errore nel salvataggio: "+ e.getMessage());
        }
    }
    public void salvaUtentesufile(){
        try(PrintWriter writer = new PrintWriter(new File("src/main/resources/utenti.csv"))){
            for(Utente u: registroUtenti){
                writer.println(u.toCSV());
            }
        }catch(FileNotFoundException e){
            System.out.println("Errrore nel salvataggio: "+ e.getMessage());
        }
    }
    public void caricaDati() {
        // 1. CARICA LIBRI
        File fLibri = new File(FILE_LIBRI);
        if(fLibri.exists()){
            try(Scanner scanner = new Scanner(fLibri)){
                while(scanner.hasNextLine()){
                    String[] dati = scanner.nextLine().split(";");
                    // CSV Libro: Titolo;ISBN;Anno;Copie
                    catalogo.add(new Libro(dati[0], dati[1], Integer.parseInt(dati[2]), Integer.parseInt(dati[3]), Integer.parseInt(dati[4])));
                }
            } catch(Exception e) { System.out.println("Err Caricamento Libri: " + e.getMessage()); }
        }

        // 2. CARICA UTENTI
        File fUtenti = new File(FILE_UTENTI);
        if(fUtenti.exists()){
            try(Scanner scanner = new Scanner(fUtenti)){
                while(scanner.hasNextLine()){
                    String[] dati = scanner.nextLine().split(";");
                    // CSV Utente: Nome;Cognome;ID;Email
                    registroUtenti.add(new Utente(dati[0], dati[1], Integer.parseInt(dati[2]), dati[3]));
                }
            } catch(Exception e) { System.out.println("Err Caricamento Utenti: " + e.getMessage()); }
        }

        // 3. CARICA PRESTITI (Solo dopo aver caricato Utenti e Libri!)
        File fPrestiti = new File(FILE_PRESTITI);
        if(fPrestiti.exists()){
            try(Scanner scanner = new Scanner(fPrestiti)){
                while(scanner.hasNextLine()){
                    String[] dati = scanner.nextLine().split(";");
                    // CSV Prestito: ID_Utente;ISBN_Libro;Scadenza;Inizio
                    
                    int idUtente = Integer.parseInt(dati[0]);
                    int isbnLibro = Integer.parseInt(dati[1]);
                    LocalDate scadenza = LocalDate.parse(dati[2]);
                    LocalDate inizio = LocalDate.parse(dati[3]);

                    // Cerchiamo gli oggetti "veri" usando gli ID letti dal file
                    Utente u = trovaUtentePerId(idUtente);
                    Libro l = trovaLibroPerIsbn(isbnLibro);

                    // Se esistono entrambi (integrità dati), ricreiamo il prestito
                    if(u != null && l != null){
                        registroPrestiti.add(new Prestito(u, scadenza, l, inizio));
                    }
                }
            } catch(Exception e) { System.out.println("Err Caricamento Prestiti: " + e.getMessage()); }
        }
    }
    
    // --- HELPER PER IL CARICAMENTO ---
    private Utente trovaUtentePerId(int id) {
        for(Utente u : registroUtenti) if(u.getIdUtente() == id) return u;
        return null;
    }

    private Libro trovaLibroPerIsbn(int isbn) {
        for(Libro l : catalogo) if(l.getIsbn() == isbn) return l;
        return null;
    }
    
}
    
