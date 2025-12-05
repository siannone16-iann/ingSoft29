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
        launch(args);
    }
}