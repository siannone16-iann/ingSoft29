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
    private String FILE_LIBRI = "dati/libri.csv";
    private String FILE_UTENTI = "dati/utenti.csv";
    private String FILE_PRESTITI = "dati/prestiti.csv";
    private int prossimoIdUtente;

    /**
     * @brief Costruttore: inizializza liste, carica dati e calcola ID.
     */
    public BibliotecaManager(){
        this.catalogo = FXCollections.observableArrayList();
        this.registroUtenti = FXCollections.observableArrayList();
        this.registroPrestiti = FXCollections.observableArrayList();
        this.prossimoIdUtente = 1;

        caricaDati();
        aggiornaIdUtente();
    }
    //Aggiorna l'id Utente
    private void aggiornaIdUtente() {
        int maxId = 0;
        for (Utente u : registroUtenti) {
            if (u.getIdUtente() > maxId) {
                maxId = u.getIdUtente();
            }
        }
        prossimoIdUtente = maxId + 1;
    }
    /**
     * @brief Fornisce il prossimo ID univoco.
     * @return Intero disponibile per nuovo utente.
     */
    public int getProssimoIdUtente() {
        return prossimoIdUtente;
    }


    /**
     * @brief Getter catalogo libri.
     * @return Lista osservabile per la UI.
     */
    public ObservableList<Libro> getCatalogo() {
        return catalogo;
    }
    /**
     * @brief Getter registro utenti.
     * @return Lista osservabile per la UI.
     */
    public ObservableList<Utente> getRegistroUtenti() {
        return registroUtenti;
    }
    /**
     * @brief Getter registro prestiti.
     * @return Lista osservabile per la UI.
     */
    public ObservableList<Prestito> getRegistroPrestiti() {
        return registroPrestiti;
    }


    /**
     * @brief Aggiunge libro nuovo o incrementa copie se esiste.
     * @param titolo Titolo opera.
     * @param autore Autore opera.
     * @param isbn Codice univoco.
     * @param annoProduzione Anno pubblicazione.
     * @param copie Numero copie da aggiungere.
     */
    public void aggiungiLibro(String titolo, String autore, int isbn, int annoProduzione, int copie){
        // Controlla se esiste già lo STESSO libro (tutti i campi uguali)
        for(Libro l : catalogo){
            if(l.getIsbn() == isbn &&
                    l.getTitolo().equalsIgnoreCase(titolo) &&
                    l.getAutore().equalsIgnoreCase(autore) &&
                    l.getAnnoProduzione() == annoProduzione){
                // È lo stesso libro, aggiungo solo le copie
                int vecchieCopie = l.getCopie();
                int nuoveCopie = copie + vecchieCopie;
                l.setCopie(nuoveCopie);

                aggiornaLibro(l);

                System.out.println("Copie aggiunte al libro esistente: " + titolo);

                return;
            }
        }
        // È un libro nuovo, lo aggiungo al catalogo
        Libro nuovoLibro = new Libro(titolo, autore, isbn, annoProduzione, copie);
        catalogo.add(nuovoLibro);
        salvaLibroSuFile();
        System.out.println("Nuovo libro aggiunto: " + titolo);
    }

    /**
     * @brief Crea e salva nuovo utente con ID automatico.
     * @param nome Nome battesimo.
     * @param cognome Cognome.
     * @param email Indirizzo contatto.
     */
    public void aggiungiUtente(String nome, String cognome, String email){
        int nuovoId = prossimoIdUtente;

        Utente nuovoUtente = new Utente(nome, cognome, nuovoId, email);
        registroUtenti.add(nuovoUtente);

        prossimoIdUtente++;

        salvaUtentesufile();
    }
    /**
     * @brief Tenta creazione prestito (max 3 per utente).
     * @param utente Chi richiede.
     * @param dataScadenza Data rientro prevista.
     * @param libro Libro richiesto.
     * @param dataInzioPrestito Data avvio.
     * @return "success" se ok, messaggio errore altrimenti.
     */
    public String aggiungiPrestito(Utente utente, LocalDate dataScadenza, Libro libro, LocalDate dataInzioPrestito){
        // Controllo 1: L'utente ha già raggiunto il limite di 3 prestiti?
        if(utente.getPrestiti() >= 3){
            System.out.println("ERRORE: L'utente " + utente.getNome() + " " + utente.getCognome() +
                    " ha già raggiunto il limite massimo di 3 prestiti attivi.");
            return "L'utente ha già raggiunto il limite massimo di 3 prestiti attivi.";
        }

        // Controllo 2: Il libro è disponibile?
        if(!libro.getStato().equals("Si")){
            System.out.println("ERRORE: Il libro '" + libro.getTitolo() + "' non è disponibile. " +
                    "Copie disponibili: " + libro.getCopieDisponibili());
            return "Il libro non è disponibile. Tutte le copie sono in prestito.";
        }

        // Se tutti i controlli passano, procedo con il prestito
        if(utente.richiestaPrestito() && libro.richiestaPrestito()){
            Prestito nuovoPrestito = new Prestito(utente, dataScadenza, libro, dataInzioPrestito);
            registroPrestiti.add(nuovoPrestito);
            //Forzo l'aggiornamento delle liste registroUtenti e catalogo
            int i = registroUtenti.indexOf(utente);
            registroUtenti.set(i, utente);
            
            int j = catalogo.indexOf(libro);
            catalogo.set(j, libro);
            
            salvaPrestitiSuFile();
            salvaLibroSuFile();
            salvaUtentesufile();
            System.out.println("Prestito salvato con successo: " + utente.getCognome() +
                    " -> " + libro.getTitolo() +
                    " (Prestiti utente: " + utente.getPrestiti() + "/3)");
            return "success";
        }
        else {
            System.out.println("ERRORE: Impossibile creare il prestito per motivi sconosciuti.");
            return "Errore durante la creazione del prestito.";
        }
    }
    /**
     * @brief Chiude prestito, libera copia e salva.
     * @param prestitoDaChiudere Oggetto prestito da rimuovere.
     */
    public void restituisciLibro(Prestito prestitoDaChiudere){
        if(prestitoDaChiudere != null){
            registroPrestiti.remove(prestitoDaChiudere);
            prestitoDaChiudere.finePrestito();
            salvaPrestitiSuFile();
            salvaLibroSuFile();
            salvaUtentesufile();
        }

    }
    /**
     * @brief Rimuove libro se copie > 0.
     * @param l Libro da eliminare.
     */
    public void eliminaLibro(Libro l){
        if( l != null && l.getCopieDisponibili() > 0) {
            catalogo.remove(l);
            salvaLibroSuFile();
            System.out.println("Il libro : " +l.getTitolo()+" è stato eliminato con successo.");
        }
        else
            System.out.println("Non è stato possibile procedere con l'eliminazione. \n Verifica le copie disponibili.");
    }
    /**
     * @brief Rimuove utente se prestiti == 0.
     * @param u Utente da eliminare.
     */
    public void eliminaUtente(Utente u){
        if (u != null && u.getPrestiti() == 0){
            registroUtenti.remove(u);
            salvaUtentesufile();
            System.out.println("L'utente : "+u.getNome()+" "+u.getCognome()+" è stato eliminato dal registro.");
        }
        else
            System.out.println("Non è stato possibile procedere con l'eliminazione. \n Verificare i prestiti a carico dell'utente.");
    }
    /**
     * @brief Aggiorna dati libro e file.
     * @param l Libro modificato.
     */
    public void aggiornaLibro(Libro l){
        int index = catalogo.indexOf(l);
        catalogo.set(index, l);

        salvaLibroSuFile();
    }
    /**
     * @brief Aggiorna dati utente e file.
     * @param u Utente modificato.
     */
    public void aggiornaUtente(Utente u){
        int index = registroUtenti.indexOf(u);
        registroUtenti.set(index, u);

        salvaUtentesufile();
    }

    public void aggiornaPrestito(Prestito p){
        int index = registroPrestiti.indexOf(p);
        registroPrestiti.set(index, p);

        salvaPrestitiSuFile();
    }

    /**
     * @brief Sovrascrive CSV prestiti.
     */
    public void salvaPrestitiSuFile(){
        try(PrintWriter writer = new PrintWriter(new File(FILE_PRESTITI))){
            for(Prestito p : registroPrestiti){
                writer.println(p.toCSV());
            }
        }catch(FileNotFoundException e){
            System.out.println("Errore nel salvataggio: "+ e.getMessage());
        }
    }
    /**
     * @brief Sovrascrive CSV libri.
     */
    public void salvaLibroSuFile(){
        try(PrintWriter writer = new PrintWriter(new File(FILE_LIBRI))){
            for(Libro l : catalogo){
                writer.println(l.toCSV());
            }
        }catch(FileNotFoundException e){
            System.out.println("Errore nel salvataggio: "+ e.getMessage());
        }
    }
    /**
     * @brief Sovrascrive CSV utenti.
     */
    public void salvaUtentesufile(){
        try(PrintWriter writer = new PrintWriter(new File(FILE_UTENTI))){
            for(Utente u: registroUtenti){
                writer.println(u.toCSV());
            }
        }catch(FileNotFoundException e){
            System.out.println("Errrore nel salvataggio: "+ e.getMessage());
        }
    }
    
    /**
     * @brief Importa Libri, Utenti e Prestiti dai file CSV.
     */
    public void caricaDati() {
        // CARICA LIBRI
        File fLibri = new File(FILE_LIBRI);
        if(fLibri.exists()){
            try(Scanner scanner = new Scanner(fLibri)){
                while(scanner.hasNextLine()){
                    String riga = scanner.nextLine().trim();
                    String[] dati = riga.split(";");

                    if (dati.length < 6) {
                        System.out.println("Riga ignorata (dati insufficienti): " + riga);
                        continue;
                    }

                  
                    catalogo.add(new Libro(dati[0], dati[1], Integer.parseInt(dati[2]), Integer.parseInt(dati[3]), Integer.parseInt(dati[4]), Integer.parseInt(dati[5])));
                }
            } catch(Exception e) { System.out.println("Err Caricamento Libri: " + e.getMessage()); }
        }

        // CARICA UTENTI
        File fUtenti = new File(FILE_UTENTI);
        if(fUtenti.exists()){
            try(Scanner scanner = new Scanner(fUtenti)){
                while(scanner.hasNextLine()){
                    String riga = scanner.nextLine().trim();
                    String[] dati = riga.split(";");

                    if (dati.length < 5) {
                        System.out.println("Riga ignorata (dati insufficienti): " + riga);
                        continue;
                    }
                    
                    registroUtenti.add(new Utente(dati[0], dati[1], Integer.parseInt(dati[2]), dati[3], Integer.parseInt(dati[4])));
                }
            } catch(Exception e) { System.out.println("Err Caricamento Utenti: " + e.getMessage()); }
        }

        // CARICA PRESTITI 
        File fPrestiti = new File(FILE_PRESTITI);
        if(fPrestiti.exists()){
            try(Scanner scanner = new Scanner(fPrestiti)){
                while(scanner.hasNextLine()){
                    String[] dati = scanner.nextLine().split(";");
                    

                    int idUtente = Integer.parseInt(dati[0]);
                    int isbnLibro = Integer.parseInt(dati[1]);
                    LocalDate scadenza = LocalDate.parse(dati[2]);
                    LocalDate inizio = LocalDate.parse(dati[3]);

                    // Trovo utente e Id
                    Utente u = trovaUtentePerId(idUtente);
                    Libro l = trovaLibroPerIsbn(isbnLibro);

                    // Se esistono entrambi ricreiamo il prestito
                    if(u != null && l != null){
                        registroPrestiti.add(new Prestito(u, scadenza, l, inizio));
                    }
                }
            } catch(Exception e) { System.out.println("Err Caricamento Prestiti: " + e.getMessage()); }
        }
    }

    
    private Utente trovaUtentePerId(int id) {
        for(Utente u : registroUtenti) if(u.getIdUtente() == id) return u;
        return null;
    }

    /**
     * @brief Costruttore con percorsi personalizzati (per i test).
     */
    public BibliotecaManager(String fileLibri, String fileUtenti, String filePrestiti){
        this.FILE_LIBRI = fileLibri;
        this.FILE_UTENTI = fileUtenti;
        this.FILE_PRESTITI = filePrestiti;

        this.catalogo = FXCollections.observableArrayList();
        this.registroUtenti = FXCollections.observableArrayList();
        this.registroPrestiti = FXCollections.observableArrayList();
        this.prossimoIdUtente = 1;

        caricaDati();
        aggiornaIdUtente();
    }

    private Libro trovaLibroPerIsbn(int isbn) {
        for(Libro l : catalogo) if(l.getIsbn() == isbn) return l;
        return null;
    }

}