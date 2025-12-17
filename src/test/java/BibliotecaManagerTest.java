import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;



public class BibliotecaManagerTest {

    private BibliotecaManager manager;
    private Path tempLibri;
    private Path tempUtenti;
    private Path tempPrestiti;

    @BeforeEach
    void setUp() throws IOException {
        tempLibri = Files.createTempFile("libri_test_", ".csv");
        tempUtenti = Files.createTempFile("utenti_test_", ".csv");
        tempPrestiti = Files.createTempFile("prestiti_test_", ".csv");

        manager = new BibliotecaManager( tempLibri.toString(),tempUtenti.toString(),tempPrestiti.toString());

        manager.getCatalogo().clear();
        manager.getRegistroUtenti().clear();
        manager.getRegistroPrestiti().clear();
    }
    @AfterEach
    void tearDown() throws IOException {
        if (tempLibri != null && Files.exists(tempLibri)) {
            Files.delete(tempLibri);
        }
        if (tempUtenti != null && Files.exists(tempUtenti)) {
            Files.delete(tempUtenti);
        }
        if (tempPrestiti != null && Files.exists(tempPrestiti)) {
            Files.delete(tempPrestiti);
        }
    }

    @Test
    void testInizializzazione() {
        Assertions.assertNotNull(manager.getCatalogo());
        Assertions.assertNotNull(manager.getRegistroUtenti());
        Assertions.assertNotNull(manager.getRegistroPrestiti());
    }

    @Test
    void testAggiungiLibroNuovo() {
        manager.aggiungiLibro("Il Codice Da Vinci", "Dan Brown", 111, 2003, 5);

        Assertions.assertEquals(1, manager.getCatalogo().size());
        Libro l = manager.getCatalogo().get(0);
        Assertions.assertEquals("Il Codice Da Vinci", l.getTitolo());
        Assertions.assertEquals(5, l.getCopie());
    }

    @Test
    void testAggiungiLibroEsistente() {
        manager.aggiungiLibro("Matrix", "Wachowski", 222, 1999, 2);
        manager.aggiungiLibro("Matrix", "Wachowski", 222, 1999, 3);

        Assertions.assertEquals(1, manager.getCatalogo().size());
        Assertions.assertEquals(5, manager.getCatalogo().get(0).getCopie());
    }

    @Test
    void testEliminaLibroSuccesso() {
        manager.aggiungiLibro("Test Delete", "Autore", 333, 2020, 1);
        Libro l = manager.getCatalogo().get(0);

        manager.eliminaLibro(l);

        Assertions.assertEquals(0, manager.getCatalogo().size());
    }

    @Test
    void testEliminaLibroFallimentoCopieNonDisponibili() {
        manager.aggiungiLibro("Test Fail Delete", "Autore", 444, 2020, 1);
        manager.aggiungiUtente("Mario", "Rossi", "mario@email.com");

        Libro l = manager.getCatalogo().get(0);
        Utente u = manager.getRegistroUtenti().get(0);

        manager.aggiungiPrestito(u, LocalDate.now().plusDays(30), l, LocalDate.now());

        manager.eliminaLibro(l);

        Assertions.assertEquals(1, manager.getCatalogo().size());
    }

    @Test
    void testAggiungiUtente() {
        int idIniziale = manager.getProssimoIdUtente();
        manager.aggiungiUtente("Luca", "Bianchi", "luca@email.com");

        Assertions.assertEquals(1, manager.getRegistroUtenti().size());
        Utente u = manager.getRegistroUtenti().get(0);
        Assertions.assertEquals("Luca", u.getNome());
        Assertions.assertEquals(idIniziale, u.getIdUtente());
        Assertions.assertEquals(idIniziale + 1, manager.getProssimoIdUtente());
    }

    @Test
    void testEliminaUtenteSuccesso() {
        manager.aggiungiUtente("Giulia", "Verdi", "giulia@email.com");
        Utente u = manager.getRegistroUtenti().get(0);

        manager.eliminaUtente(u);

        Assertions.assertEquals(0, manager.getRegistroUtenti().size());
    }

    @Test
    void testAggiungiPrestitoSuccesso() {
        manager.aggiungiLibro("Java Programming", "Gosling", 555, 2010, 3);
        manager.aggiungiUtente("Paolo", "Neri", "paolo@email.com");

        Libro l = manager.getCatalogo().get(0);
        Utente u = manager.getRegistroUtenti().get(0);

        String risultato = manager.aggiungiPrestito(u, LocalDate.now().plusDays(15), l, LocalDate.now());

        Assertions.assertEquals("success", risultato);
        Assertions.assertEquals(1, manager.getRegistroPrestiti().size());
        Assertions.assertEquals(1, u.getPrestiti());
        Assertions.assertEquals(1, l.getCopiePrestate());
    }

    @Test
    void testAggiungiPrestitoFallimentoLimiteUtente() {
        manager.aggiungiLibro("Libro 1", "A", 1, 2000, 10);
        manager.aggiungiLibro("Libro 2", "B", 2, 2000, 10);
        manager.aggiungiLibro("Libro 3", "C", 3, 2000, 10);
        manager.aggiungiLibro("Libro 4", "D", 4, 2000, 10);

        manager.aggiungiUtente("Lory", "Test", "lory@test.com");
        Utente u = manager.getRegistroUtenti().get(0);

        manager.aggiungiPrestito(u, LocalDate.now(), manager.getCatalogo().get(0), LocalDate.now());
        manager.aggiungiPrestito(u, LocalDate.now(), manager.getCatalogo().get(1), LocalDate.now());
        manager.aggiungiPrestito(u, LocalDate.now(), manager.getCatalogo().get(2), LocalDate.now());

        String risultato = manager.aggiungiPrestito(u, LocalDate.now(), manager.getCatalogo().get(3), LocalDate.now());

        Assertions.assertNotEquals("success", risultato);
        Assertions.assertEquals(3, manager.getRegistroPrestiti().size());
    }

    @Test
    void testAggiungiPrestitoFallimentoLibroNonDisponibile() {
        manager.aggiungiLibro("Raro", "Z", 999, 1900, 1);
        manager.aggiungiUtente("User 1", "Uno", "u1@test.com");
        manager.aggiungiUtente("User 2", "Due", "u2@test.com");

        Libro l = manager.getCatalogo().get(0);
        Utente u1 = manager.getRegistroUtenti().get(0);
        Utente u2 = manager.getRegistroUtenti().get(1);

        manager.aggiungiPrestito(u1, LocalDate.now(), l, LocalDate.now());

        String risultato = manager.aggiungiPrestito(u2, LocalDate.now(), l, LocalDate.now());

        Assertions.assertNotEquals("success", risultato);
        Assertions.assertEquals(1, manager.getRegistroPrestiti().size());
    }

    @Test
    void testRestituisciLibro() {
        manager.aggiungiLibro("Restituzione", "R", 777, 2021, 5);
        manager.aggiungiUtente("Anna", "Gialli", "anna@test.com");

        Libro l = manager.getCatalogo().get(0);
        Utente u = manager.getRegistroUtenti().get(0);

        manager.aggiungiPrestito(u, LocalDate.now(), l, LocalDate.now());
        Prestito p = manager.getRegistroPrestiti().get(0);

        manager.restituisciLibro(p);

        Assertions.assertEquals(0, manager.getRegistroPrestiti().size());
        Assertions.assertEquals(0, l.getCopiePrestate());
    }
}