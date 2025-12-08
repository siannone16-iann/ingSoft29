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
        try {
            String nomeFile = "/interfaccia.fxml";
            URL urlDelFile = getClass().getResource(nomeFile);

            if (urlDelFile == null) {
                System.err.println("ERRORE: File non trovato: " + nomeFile);
                return;
            }

            FXMLLoader loader = new FXMLLoader(urlDelFile);
            Parent root = loader.load();

            BibliotecaManager manager = new BibliotecaManager();

            interfacciaController controller = loader.getController();
            controller.setManager(manager);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Gestione Biblioteca Universitaria");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Errore nel caricamento dell'interfaccia");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}