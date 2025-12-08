
/**
 *
 * @author simoneiannone
 * @brief Rappresenta un libro nell'archivio della biblioteca.
 * Gestisce sia i dati editoriali (titolo, autore) che l'inventario (copie totali vs copie prestate).
 */

public class Libro {

    private String titolo;
    private String autore;
    private int copie;
    private int copiePrestate;
    private int isbn;
    private int annoProduzione;

    /**
     * Costruttore completo.
     * Utile per caricare un libro da un database o file CSV, dove conosciamo già lo storico
     * di quante copie sono attualmente fuori in prestito.
     *
     * @param titolo         Il titolo del libro.
     * @param autore         L'autore principale.
     * @param isbn           Il codice univoco del libro (usato come identificativo).
     * @param annoProduzione L'anno di pubblicazione.
     * @param copie          Il numero totale di copie fisiche possedute dalla biblioteca.
     * @param copiePrestate  Il numero di copie che sono attualmente in mano agli utenti.
     */
    public Libro(String titolo, String autore, int isbn, int annoProduzione, int copie, int copiePrestate) {
        this.titolo = titolo;
        this.autore = autore;
        this.isbn = isbn;
        this.annoProduzione = annoProduzione;
        this.copie = copie;
        this.copiePrestate = copiePrestate;
    }

    /**
     * Costruttore per nuovi acquisti.
     * Crea un libro impostando automaticamente le copie prestate a 0.
     * Da usare quando si inserisce un nuovo volume nell'inventario per la prima volta.
     *
     * @param titolo         Il titolo del libro.
     * @param autore         L'autore principale.
     * @param isbn           Il codice univoco del libro.
     * @param annoProduzione L'anno di pubblicazione.
     * @param copie          Il numero di copie acquistate.
     */
    public Libro(String titolo, String autore, int isbn, int annoProduzione, int copie){
        this(titolo, autore, isbn, annoProduzione, copie, 0);
    }

    /**
     * Aggiorna tutti i dati del libro in un colpo solo.
     * Utile per correggere errori di inserimento o aggiornare il numero di copie totali.
     *
     * @param titolo         Il nuovo titolo.
     * @param autore         Il nuovo autore.
     * @param isbn           Il nuovo ISBN.
     * @param annoProduzione Il nuovo anno.
     * @param copie          Il nuovo numero totale di copie.
     */
    public void modificaLibro(String titolo, String autore, int isbn, int annoProduzione, int copie){
        this.titolo=titolo;
        this.autore=autore;
        this.isbn=isbn;
        this.annoProduzione=annoProduzione;
        this.copie=copie;
    }

    /**
     * @return Il titolo del libro.
     */
    public String getTitolo() {

        return titolo;
    }

    /**
     * @return Il titolo del libro.
     */
    public String getAutore() {
        return autore;
    }

    /**
     * @return copie del libro.
     */
    public int getCopie() {
        return copie;
    }

    /**
     * @return copie Prestate.
     */
    public int getCopiePrestate() {

        return copiePrestate;
    }

    /**
     * @return Isbn.
     */
    public int getIsbn() {

        return isbn;
    }

    /**
     * @return Anno di pubblicazione del libro.
     */
    public int getAnnoProduzione() {
        return annoProduzione;
    }

    /**
     * Verifica la disponibilità del libro.
     * Controlla se ci sono copie rimaste (copie totali > copie prestate).
     *
     * @return Una stringa: "Si" se il libro è disponibile, "No" se sono tutti in prestito.
     */
    public String getStato(){
        if (copiePrestate < copie){ 
            return "Si";
        }
        else return "No";
    }

    /**
     * Tenta di prelevare una copia per un prestito.
     * Se il libro è disponibile (Stato = "Si"), incrementa il contatore delle copie prestate.
     *
     * @return true se l'operazione è riuscita (c'era una copia disponibile), false se non ci sono copie.
     */
    public boolean richiestaPrestito(){
        if(getStato().equals("Si")){
            copiePrestate++;
            return true;
        }
        return false;   //gestione eccezione da valutare se farlo in BibliotecaManager
    }

    /**
     * Registra il rientro di una copia.
     * Decrementa il contatore delle copie prestate, rendendo una copia nuovamente disponibile.
     * Effettua un controllo per evitare che le copie prestate diventino negative.
     */
    public void finePrestito(){
        if(copiePrestate > 0){
            copiePrestate--;
        }
    }

    /**
     * Calcola quante copie sono fisicamente presenti in biblioteca ora.
     *
     * @return La differenza tra copie totali e copie prestate.
     */
    public int getCopieDisponibili(){
        return copie-copiePrestate;
    }

    /**
     * Aggiunge nuove copie all'inventario esistente.
     * Utile quando la biblioteca acquista ulteriori volumi dello stesso libro.
     *
     * @param numCopie Il numero di nuove copie da aggiungere al totale.
     */
    public void aumentaCopie(int numCopie){

        copie = copie+numCopie;
    }

    /**
     * Sovrascrive il numero totale di copie.
     *
     * @param copie Il nuovo numero totale.
     */
    public void setCopie(int copie) {
        this.copie = copie;
    }

    /**
     * Esporta i dati del libro in formato CSV.
     *
     *
     * @return La stringa formattata per il file.
     */
    public String toCSV(){
     return titolo +";"+autore+";"+isbn+";"+annoProduzione+";"+copie+";"+copiePrestate;
    }


}
