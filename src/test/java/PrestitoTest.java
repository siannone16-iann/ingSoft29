import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

class PrestitoTest {

    private Prestito prestito;
    private Libro libro;
    private Utente utente;
    private LocalDate inizio;
    private LocalDate scadenza;

    @BeforeEach
    void setUp() {
        libro = new Libro("Il Signore degli Anelli", "Tolkien", 12345, 1954, 5);
        utente = new Utente("Mario", "Rossi", 1, "mario@email.com");
        inizio = LocalDate.now();
        scadenza = LocalDate.now().plusDays(30);

        prestito = new Prestito(utente, scadenza, libro, inizio);
    }

    @Test
    void testCostruttoreEGetters() {
        Assertions.assertEquals(utente, prestito.getUtente());
        Assertions.assertEquals(libro, prestito.getLibro());
        Assertions.assertEquals(inizio, prestito.getDataInizioPrestito());
        Assertions.assertEquals(scadenza, prestito.getDataScadenza());
    }

    @Test
    void testGetNomeUtente() {
        Assertions.assertEquals("Mario Rossi", prestito.getNomeUtente());
    }

    @Test
    void testGetTitoloLibro() {
        Assertions.assertEquals("Il Signore degli Anelli", prestito.getTitoloLibro());
    }

    @Test
    void testModificaPrestito() {
        LocalDate nuovaScadenza = LocalDate.now().plusDays(60);
        prestito.modificaPrestito(nuovaScadenza);

        Assertions.assertEquals(nuovaScadenza, prestito.getDataScadenza());
    }

    @Test
    void testInizioPrestitoSuccesso() {
        boolean esito = prestito.inizioPrestito();

        Assertions.assertTrue(esito);
        Assertions.assertEquals(1, libro.getCopiePrestate());
        Assertions.assertEquals(1, utente.getPrestiti());
    }

    @Test
    void testInizioPrestitoFallimentoLibroNonDisponibile() {
        Libro libroEsaurito = new Libro("Esaurito", "B", 999, 2000, 1, 1);
        Prestito prestitoFail = new Prestito(utente, scadenza, libroEsaurito, inizio);

        boolean esito = prestitoFail.inizioPrestito();

        Assertions.assertFalse(esito);
        Assertions.assertEquals(1, libroEsaurito.getCopiePrestate());
        Assertions.assertEquals(0, utente.getPrestiti());
    }

    @Test
    void testFinePrestito() {
        prestito.inizioPrestito();

        prestito.finePrestito();

        Assertions.assertEquals(0, libro.getCopiePrestate());
        Assertions.assertEquals(0, utente.getPrestiti());
    }

    @Test
    void testScadutoFalse() {
        Assertions.assertFalse(prestito.Scaduto());
    }

    @Test
    void testScadutoTrue() {
        LocalDate scadenzaPassata = LocalDate.now().minusDays(1);
        prestito.modificaPrestito(scadenzaPassata);

        Assertions.assertTrue(prestito.Scaduto());
    }

    @Test
    void testToCSV() {
        String atteso = "1;12345;" + inizio.toString() + ";" + scadenza.toString();
        Assertions.assertEquals(atteso, prestito.toCSV());
    }
}