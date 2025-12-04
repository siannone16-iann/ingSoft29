import java.time.LocalDate;

public class Prestito {
    private final LocalDate dataScadenza;
    private final Utente utente;
    private final Libro libro;
    private final LocalDate dataInzioPrestito;

    public Prestito(Utente utente, LocalDate dataScadenza, Libro libro, LocalDate dataInzioPrestito){
        this.utente=utente;
        this.dataScadenza = dataScadenza;
        this.libro = libro;
        this.dataInzioPrestito = dataInzioPrestito;
    }

    public boolean inzioPrestito(){
        if(libro.richiestaPrestito() && utente.richiestaPrestito())
                return true;
        return false;
    }
    public void finePrestito(){
        utente.finePrestito();
        libro.finePrestito();
    }
}
