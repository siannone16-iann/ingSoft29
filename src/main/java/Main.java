import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        System.out.println("--- 1. INIZIO METODO START ---");

        try {
            String nomeFile = "/interfaccia.fxml";
            
            URL urlDelFile = getClass().getResource(nomeFile);
            
            // Verifica se trova il file
            if (urlDelFile == null) {
                System.out.println("ERRORE GRAVE: Non trovo il file: " + nomeFile);
                System.out.println("Assicurati che 'interfaccia.fxml' sia dentro src/main/resources");
                return; // Esce per evitare crash peggiori
            } else {
                System.out.println("--- 2. FILE TROVATO: " + urlDelFile);
            }

            // Caricamento
            FXMLLoader loader = new FXMLLoader(urlDelFile);
            Parent root = loader.load();
            System.out.println("--- 3. FXML CARICATO CORRETTAMENTE ---");

            BibliotecaManager manager = new BibliotecaManager();

            interfacciaController controller = loader.getController();
            controller.setDati(
                    manager.getCatalogo(),
                    manager.getRegistroUtenti(),
                    manager.getRegistroPrestiti()
            );


            Scene scene = new Scene(root);
            primaryStage.setTitle("Gestione Biblioteca Universitaria");
            primaryStage.setScene(scene);

            primaryStage.show();

            // Mostra la finestra
            System.out.println("--- 4. FINESTRA MOSTRATA ---");

        } catch (IOException e) {
            System.out.println("--- ERRORE NEL CARICAMENTO ---");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
        // --------------------------------
         // 1. Creazione del Manager
        // (Appena creato, caricherà anche i dati vecchi dai file se esistono)
        BibliotecaManager manager = new BibliotecaManager();

        // -----------------------------------------------------------
        // TEST 1: AGGIUNTA LIBRI
        // Parametri: Titolo, Autore, ISBN, Anno, Copie
        // -----------------------------------------------------------
        System.out.println("\n[1] Aggiunta Libri al Catalogo...");

        manager.aggiungiLibro("Il Nome della Rosa", "Umberto Eco", 1001, 1980, 3);
        manager.aggiungiLibro("1984", "George Orwell", 1002, 1949, 5);
        manager.aggiungiLibro("La Divina Commedia", "Dante Alighieri", 1003, 1320, 1);
        manager.aggiungiLibro("Libro di Cicciogamer89", "Cicciogamer89", 1004, 2025, 5);
        

        // Verifica veloce stampando la dimensione della lista
        System.out.println("    Libri nel catalogo: " + manager.getCatalogo().size());


        // -----------------------------------------------------------
        // TEST 2: AGGIUNTA UTENTI
        // Parametri: Nome, Cognome, ID, Email
        // -----------------------------------------------------------
        System.out.println("\n[2] Aggiunta Utenti al Registro...");

        manager.aggiungiUtente("Mario", "Rossi", 1, "mario.rossi@email.com");
        manager.aggiungiUtente("Luigi", "Verdi", 2, "luigi.verdi@email.com");
        manager.aggiungiUtente("Mirko", "Alessandrini", 3, "mirko.alessandrini@email.com");

        System.out.println("    Utenti nel registro: " + manager.getRegistroUtenti().size());


        // -----------------------------------------------------------
        // TEST 3: CREAZIONE PRESTITO
        // Parametri: Oggetto Utente, Data Scadenza, Oggetto Libro, Data Inizio
        // -----------------------------------------------------------
        System.out.println("\n[3] Creazione di un Prestito...");

        /* * IMPORTANTE: Il metodo aggiungiPrestito vuole OGGETTI veri, non stringhe o ID.
         * Dato che li abbiamo appena aggiunti nelle liste, li recuperiamo con .get()
         */

        // Controllo di sicurezza per evitare crash se le liste sono vuote
        if (!manager.getCatalogo().isEmpty() && !manager.getRegistroUtenti().isEmpty()) {

            // Prendo il primo utente della lista (Mario Rossi)
            Utente utenteSelezionato = manager.getRegistroUtenti().get(0);

            // Prendo il secondo libro della lista (1984)
            Libro libroSelezionato = manager.getCatalogo().get(1);

            // Definisco le date
            LocalDate dataInizio = LocalDate.now();              // Oggi
            LocalDate dataScadenza = LocalDate.now().plusDays(30); // Tra 30 giorni

            // Chiamata al metodo
            manager.aggiungiPrestito(utenteSelezionato, dataScadenza, libroSelezionato, dataInizio);
            manager.aggiungiPrestito(utenteSelezionato, dataScadenza, libroSelezionato, dataInizio);
            manager.aggiungiPrestito(utenteSelezionato, dataScadenza, libroSelezionato, dataInizio);
            

            System.out.println("    Prestito registrato per: " + utenteSelezionato.getCognome() + 
                               " -> Libro: " + libroSelezionato.getTitolo());
            } else {
            System.out.println("    ERRORE: Non ci sono utenti o libri per creare un prestito.");
        }

        System.out.println("\n--- TEST COMPLETATO (Controlla i file .csv creati nel progetto) ---");

        launch(args);
        
    }
}