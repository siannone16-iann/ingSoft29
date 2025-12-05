import java.time.LocalDate;

public class Prestito {
    private final LocalDate dataScadenza;
    private final Utente utente;
    private final Libro libro;
    private final LocalDate dataInizioPrestito;

    public Prestito(Utente utente, LocalDate dataScadenza, Libro libro, LocalDate dataInizioPrestito){
        this.utente = utente;
        this.dataScadenza = dataScadenza;
        this.libro = libro;
        this.dataInizioPrestito = dataInizioPrestito;
    }

    public LocalDate getDataScadenza() {

        return dataScadenza;
    }

    public Utente getUtente() {

        return utente;
    }

    public Libro getLibro() {

        return libro;
    }

    public LocalDate getDataInizioPrestito() {

        return dataInizioPrestito;
    }

    public String getNomeUtente() {
        return utente.getNome() + " " + utente.getCognome();
    }

    public String getTitoloLibro(){
        return libro.getTitolo();
    }

    public boolean inizioPrestito(){
        if(libro.richiestaPrestito() && utente.richiestaPrestito()){
                return true;
        }
        return false;
    }
    public void finePrestito(){
        utente.finePrestito();
        libro.finePrestito();
    }
    
    public String toCSV(){
        return utente.getIdUtente() +";"+libro.getIsbn()+";"+ dataInizioPrestito.toString()+";"+dataScadenza.toString();
    }
}
