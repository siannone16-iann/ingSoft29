import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LibroTest {

    private Libro libroStandard;


    @BeforeEach
    void setUp() {

        libroStandard = new Libro("Il Signore degli Anelli", "Tolkien", 12345, 1954, 5);
    }

    @Test
    void testCostruttoreNuovoAcquisto() {

        Libro nuovo = new Libro("Harry Potter", "Rowling", 98765, 1997, 10);

        Assertions.assertEquals(0, nuovo.getCopiePrestate(), "Le copie prestate iniziali devono essere 0");
        Assertions.assertEquals(10, nuovo.getCopie(), "Le copie totali devono essere 10");
    }

    @Test
    void testCostruttoreCompleto() {

        Libro esistente = new Libro("Dune", "Herbert", 11111, 1965, 10, 3);

        Assertions.assertEquals(3, esistente.getCopiePrestate());
        Assertions.assertEquals(7, esistente.getCopieDisponibili()); // 10 - 3 = 7
    }

    @Test
    void testRichiestaPrestitoSuccesso() {

        boolean esito = libroStandard.richiestaPrestito();

        Assertions.assertTrue(esito, "Il prestito dovrebbe essere accettato");
        Assertions.assertEquals(1, libroStandard.getCopiePrestate(), "Il contatore prestiti deve salire a 1");
    }

    @Test
    void testRichiestaPrestitoFallimento() {

        Libro libroEsaurito = new Libro("Esaurito", "X", 0, 2000, 2, 2);

        boolean esito = libroEsaurito.richiestaPrestito();

        Assertions.assertFalse(esito, "Il prestito dovrebbe fallire perchè non ci sono copie");
        Assertions.assertEquals(2, libroEsaurito.getCopiePrestate(), "Il numero di prestiti non deve cambiare");
    }

    @Test
    void testFinePrestito() {


        Libro libroInPrestito = new Libro("Test", "Autore", 1, 2020, 5, 1);

        libroInPrestito.finePrestito();

        Assertions.assertEquals(0, libroInPrestito.getCopiePrestate(), "Il contatore deve tornare a 0");
    }

    @Test
    void testFinePrestitoNonNegativo() {

        libroStandard.finePrestito();

        Assertions.assertEquals(0, libroStandard.getCopiePrestate(), "Le copie prestate non possono essere negative");
    }

    @Test
    void testGetStato() {
        Assertions.assertEquals("Si", libroStandard.getStato(), "Il libro ha copie, dovrebbe essere 'Si'");

        // Creiamo caso esaurito
        Libro esaurito = new Libro("X", "Y", 1, 2000, 1, 1);
        Assertions.assertEquals("No", esaurito.getStato(), "Il libro è finito, dovrebbe essere 'No'");
    }

    @Test
    void testAumentaCopie() {

        libroStandard.aumentaCopie(3);

        Assertions.assertEquals(8, libroStandard.getCopie(), "5 + 3 dovrebbe fare 8");
    }

    @Test
    void testModificaLibro() {
        libroStandard.modificaLibro("Nuovo Titolo", "Nuovo Autore", 999, 2025, 20);

        Assertions.assertEquals("Nuovo Titolo", libroStandard.getTitolo());
        Assertions.assertEquals(20, libroStandard.getCopie());
        Assertions.assertEquals(999, libroStandard.getIsbn());
    }

    @Test
    void testToCSV() {

        String atteso = "Il Signore degli Anelli;Tolkien;12345;1954;5;0";

        Assertions.assertEquals(atteso, libroStandard.toCSV());
    }
}