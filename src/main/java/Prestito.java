import java.time.LocalDate;

/**
 * @brief Rappresenta l'operazione di prestito vera e propria.
 * Questa classe agisce come collegamento tra un Utente e un Libro, definendo l'arco temporale del possesso.
 */
public class Prestito {
    private  LocalDate dataScadenza;
    private final Utente utente;
    private final Libro libro;
    private final LocalDate dataInizioPrestito;

    /**
     * Crea un nuovo oggetto Prestito.
     * Collega un utente a un libro specifico e stabilisce le date di inizio e fine.
     * * @param utente             La persona che prende in prestito il libro.
     * @param dataScadenza       La data entro cui il libro deve tornare in biblioteca.
     * @param libro              Il volume oggetto del prestito.
     * @param dataInizioPrestito La data in cui il prestito viene registrato (solitamente oggi).
     */
    public Prestito(Utente utente, LocalDate dataScadenza, Libro libro, LocalDate dataInizioPrestito){
        this.utente = utente;
        this.dataScadenza = dataScadenza;
        this.libro = libro;
        this.dataInizioPrestito = dataInizioPrestito;
    }

    /**
     * @return La data limite per la restituzione del libro.
     */
    public LocalDate getDataScadenza() {

        return dataScadenza;
    }

    public void modificaPrestito(LocalDate scadenza){
        this.dataScadenza=scadenza;
    }

    /**
     * @return L'Utente intestatario del prestito.
     */
    public Utente getUtente() {

        return utente;
    }

    /**
     * @return  libro.
     */
    public Libro getLibro() {

        return libro;
    }

    /**
     * @return La data inizio prestito del  libro.
     */
    public LocalDate getDataInizioPrestito() {

        return dataInizioPrestito;
    }

    /**
     * @return Una stringa "Nome Utente".
     */
    public String getNomeUtente() {
        return utente.getNome() + " " + utente.getCognome();
    }

    /**
     * @return il titolo del libro.
     */
    public String getTitoloLibro(){
        return libro.getTitolo();
    }

    /**
     * Tenta di ufficializzare l'inizio del prestito.
     * Esegue una doppia verifica:
     * 1. Controlla se il libro è disponibile (chiama libro.richiestaPrestito).
     * 2. Controlla se l'utente può prendere altri libri (chiama utente.richiestaPrestito).
     * * Se entrambe le condizioni sono vere, i contatori interni di libro e utente vengono aggiornati.
     * * @return true se il prestito è andato a buon fine, false se il libro non c'è o l'utente ha troppi prestiti.
     */
    public boolean inizioPrestito(){
        if(libro.richiestaPrestito() && utente.richiestaPrestito(libro)){
            return true;
        }
        return false;
    }

    /**
     * Chiude la procedura di prestito.
     * Segnala all'oggetto Utente che ha liberato uno slot e all'oggetto Libro che è tornato disponibile.
     * Da chiamare quando il libro viene fisicamente restituito.
     */
    public void finePrestito(){
        utente.finePrestito(libro);
        libro.finePrestito();
    }

    /**
     * Prepara i dati del prestito per il salvataggio su file.
     *
     * * @return Stringa formattata
     */
    public String toCSV(){
        return utente.getIdUtente() +";"+libro.getIsbn()+";"+ dataInizioPrestito.toString()+";"+dataScadenza.toString();
    }

    /**
     * Verifica se siamo in ritardo con la restituzione.
     * Confronta la data odierna con la data di scadenza impostata.
     * * @return true se oggi è successivo alla data di scadenza (prestito scaduto), false se siamo ancora in tempo.
     */
    public boolean Scaduto(){
        return LocalDate.now().isAfter(this.dataScadenza);//Prendo la data di oggi e controllo se è successiva a quella della scadenza
    }
}
