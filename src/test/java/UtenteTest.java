/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class UtenteTest {

    @Test
    public void testCostruttoreCompleto() {
        
        Utente u = new Utente("Anna", "Bianchi", 10, "anna@test.com", 2);

        
        assertEquals("Anna", u.getNome());
        assertEquals("Bianchi", u.getCognome());
        assertEquals(10, u.getIdUtente());
        assertEquals("anna@test.com", u.getEmail());
        assertEquals(2, u.getPrestiti());
    }

    @Test
    public void testCostruttoreNuovoUtente() {
        
        Utente u = new Utente("Luca", "Verdi", 20, "luca@test.com");

        assertEquals("Luca", u.getNome());
        assertEquals("Verdi", u.getCognome());
        assertEquals(20, u.getIdUtente());
        assertEquals("luca@test.com", u.getEmail());
        
        assertEquals(0, u.getPrestiti());
    }
    
    @Test
    public void testModificaUtente() {
        Utente u = new Utente("Vecchio", "Nome", 1, "old@test.com");
        u.modificaUtente("Nuovo", "Cognome", "new@test.com", 99);
        
        assertEquals("Nuovo", u.getNome());
        assertEquals("new@test.com", u.getEmail());
    }

    @Test
    public void testRichiestaPrestito() {
        //utente con 0 prestiti
        Utente u = new Utente("Mario", "Rossi", 1, "m@test.com");
        
        
        boolean risultato = u.richiestaPrestito();
        
        assertTrue(risultato, "Il prestito deve essere accettato");
        assertEquals(1, u.getPrestiti());
    }

    @Test
    public void testRichiestaPrestito_LimiteSuperato() {
        //utente con 3 prestiti
        Utente u = new Utente("Mario", "Rossi", 1, "m@test.com", 3);
        
        boolean risultato = u.richiestaPrestito();
        
        assertFalse(risultato, "Il prestito oltre il limite deve essere rifiutato");
        assertEquals(3, u.getPrestiti());
    }

    @Test
    public void testFinePrestito() {
        //utente con 1 prestito
        Utente u = new Utente("Mario", "Rossi", 1, "m@test.com", 1);
        
        
        u.finePrestito();
        
        // Ora deve essere 0
        assertEquals(0, u.getPrestiti());
        //Controllo che non vada in negativo
        u.finePrestito();
        
        assertEquals(0, u.getPrestiti(), "Non può andare in negativo");
    }

    @Test
    public void testToCSV() {
        Utente u = new Utente("Mario", "Rossi", 5, "email@test.com", 2);
        assertTrue(u.toCSV().startsWith("Mario;Rossi;5;email@test.com;2"));
    }
}