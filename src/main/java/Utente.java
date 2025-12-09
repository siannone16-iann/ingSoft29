/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author salvatoremoccia
 * @brief Rappresenta un utente del sistema , tenendo traccia dei dati anagrafici e dei prestiti attivi.
 */
public class Utente {
    private String nome;
    private String cognome;
    private int idUtente;
    private String email;
    private int prestiti;
    private ObservableList<Libro> libriInPrestito = FXCollections.observableArrayList();

    /**
     * Costruttore completo.
     * Crea un utente  manualmente tutti i dati, incluso il numero di prestiti già in corso.
     *
     *
     * @param nome      Il nome  dell'utente.
     * @param cognome   Il cognome dell'utente.
     * @param idUtente  Il codice identificativo numerico.
     * @param email     L'indirizzo email di contatto.
     * @param prestiti  Il numero di prestiti attualmente attivi.
     */
    public Utente(String nome, String cognome, int idUtente, String email, int prestiti) {
        this.nome = nome;
        this.cognome = cognome;
        this.idUtente = idUtente;
        this.email = email;
        this.prestiti = prestiti;
    }

    /**
     * Costruttore per nuovi iscritti.
     * Crea un nuovo utente impostando  i prestiti a zero.
     *
     *
     * @param nome      Il nome  dell'utente.
     * @param cognome   Il cognome dell'utente.
     * @param idUtente  Il codice identificativo numerico.
     * @param email     L'indirizzo email di contatto.
     */
    public Utente(String nome, String cognome, int idUtente, String email){
        this(nome, cognome, idUtente, email, 0);
    }

    /**
     * Aggiorna le informazioni principali dell'utente .
     *
     * @param nome      Il nuovo nome.
     * @param cognome   Il nuovo cognome.
     * @param email     La nuova email.
     * @param idUtente  Il nuovo ID .
     */
    public void modificaUtente (String nome, String cognome, String email, int idUtente){
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.idUtente=idUtente;
    }
    /**
     * @return Il nome dell'utente.
     */
    public String getNome() {

        return nome;
    }
    /**
     * @return Il cognome dell'utente.
     */
    public String getCognome() {

        return cognome;
    }
    /**
     * @return l'ID utente.
     */
    public int getIdUtente() {

        return idUtente;
    }
    /**
     * @return l'email dell'utente.
     */
    public String getEmail() {

        return email;
    }
    /**
     * @return Prestiti dei libri dell'utente.
     */
    public int getPrestiti() {

        return prestiti;
    }

    /**
     * Imposta  il numero di prestiti.
     *
     *
     * @param prestiti Il nuovo numero di prestiti da assegnare.
     */
    public void setPrestiti(int prestiti) {
        this.prestiti = prestiti;
    }

    /**
     * Tenta di avviare un nuovo prestito per l'utente.
     * Controlla se l'utente ha ancora "slot" liberi. Il limite massimo è fissato a 3 prestiti contemporanei.
     *
     * @return true se il prestito è stato concesso (contatore incrementato);
     * false se l'utente ha già raggiunto il limite massimo di 3.
     */
    public boolean richiestaPrestito(Libro libro) {
        if (libriInPrestito.size() < 3) {
            libriInPrestito.add(libro);
            prestiti++;
            return true;
        }
        return false;
    }

    /**
     * Registra la restituzione di un prestito.
     * Decrementa il contatore dei prestiti attivi, ma solo se l'utente ha effettivamente qualcosa in prestito
     * (evita che il contatore vada in negativo).
     */
    public void finePrestito(Libro libro) {
        libriInPrestito.remove(libro);
        prestiti--;
    }
    /**
     * Converte i dati dell'utente in una stringa formattata per file CSV.
     *
     *
     *
     * @return La stringa pronta per essere scritta su file.
     */
    public String toCSV(){
        StringBuilder libri = new StringBuilder();
        for(int i = 0; i < libriInPrestito.size(); i++) {
            if(i > 0) libri.append(",");
            libri.append(libriInPrestito.get(i).getTitolo());
        }
        return nome + ";" + cognome + ";" + idUtente + ";" + email + ";" + prestiti + ";" + libri.toString();
    }
}    
