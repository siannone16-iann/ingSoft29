/**
 *
 * @author salvatoremoccia
 */

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class Finestre {
    private final BibliotecaManager manager;

    public Finestre (BibliotecaManager manager){
        this.manager=manager;
    }

    public void nuovoLibro() {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Nuovo Libro");
            dialog.setHeaderText("Aggiungi un nuovo libro al catalogo");

            // 1. Bottoni
            ButtonType salvaButtonType = new ButtonType("Salva", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(salvaButtonType, ButtonType.CANCEL);

            // 2. Layout (Griglia)
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField titolo = new TextField();
            TextField autore = new TextField();
            TextField isbn = new TextField();
            TextField anno = new TextField();
            TextField copie = new TextField();

            grid.add(new Label("Titolo:"), 0, 0); grid.add(titolo, 1, 0);
            grid.add(new Label("Autore:"), 0, 1); grid.add(autore, 1, 1);
            grid.add(new Label("ISBN:"), 0, 2);   grid.add(isbn, 1, 2);
            grid.add(new Label("Anno:"), 0, 3);   grid.add(anno, 1, 3);
            grid.add(new Label("Copie:"), 0, 4);  grid.add(copie, 1, 4);

            dialog.getDialogPane().setContent(grid);

            // 3. LOGICA DI SALVATAGGIO (Dentro il filtro)
            Button btnSalva = (Button) dialog.getDialogPane().lookupButton(salvaButtonType);

            btnSalva.addEventFilter(ActionEvent.ACTION, event -> {
                // A. Validazione Campi Vuoti
                if (titolo.getText().trim().isEmpty() ||
                        autore.getText().trim().isEmpty() ||
                        isbn.getText().trim().isEmpty() ||
                        anno.getText().trim().isEmpty() ||
                        copie.getText().trim().isEmpty()) {

                    mostraErrore("Tutti i campi sono obbligatori.");
                    event.consume(); // Blocca la chiusura della finestra
                    return;
                }

                // B. Validazione Numerica e Salvataggio
                try {
                    int vIsbn = Integer.parseInt(isbn.getText());
                    int vAnno = Integer.parseInt(anno.getText());
                    int vCopie = Integer.parseInt(copie.getText());

                    // SE SIAMO QUI, I DATI SONO OK -> SALVIAMO
                    manager.aggiungiLibro(
                            titolo.getText(),
                            autore.getText(),
                            vIsbn,
                            vAnno,
                            vCopie
                    );

                    System.out.println("Libro salvato con successo.");
                    // Non consumiamo l'evento, così il dialog si chiude normalmente

                } catch (NumberFormatException e) {
                    mostraErrore("ISBN, Anno e Copie devono essere numeri interi.");
                    event.consume(); // Blocca la chiusura per errore
                }
            });

            dialog.showAndWait();
        }

        public void nuovoUtente() {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Nuovo Utente");
            dialog.setHeaderText("Registra un nuovo utente");

            // 1. Bottoni
            ButtonType salvaButtonType = new ButtonType("Salva", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(salvaButtonType, ButtonType.CANCEL);

            // 2. Layout
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField nome = new TextField();
            TextField cognome = new TextField();
            TextField email = new TextField();
            TextField id = new TextField();

            grid.add(new Label("Nome:"), 0, 0);    grid.add(nome, 1, 0);
            grid.add(new Label("Cognome:"), 0, 1); grid.add(cognome, 1, 1);
            grid.add(new Label("Email:"), 0, 2);   grid.add(email, 1, 2);
            grid.add(new Label("ID Univ.:"), 0, 3); grid.add(id, 1, 3);

            dialog.getDialogPane().setContent(grid);

            // 3. LOGICA DI SALVATAGGIO
            Button btnSalva = (Button) dialog.getDialogPane().lookupButton(salvaButtonType);

            btnSalva.addEventFilter(ActionEvent.ACTION, event -> {
                // A. Campi Vuoti
                if (nome.getText().trim().isEmpty() ||
                        cognome.getText().trim().isEmpty() ||
                        email.getText().trim().isEmpty() ||
                        id.getText().trim().isEmpty()) {

                    mostraErrore("Tutti i campi sono obbligatori.");
                    event.consume();
                    return;
                }

                // B. Numeri e Salvataggio
                try {
                    int vId = Integer.parseInt(id.getText());

                    // SE SIAMO QUI, SALVIAMO
                    manager.aggiungiUtente(
                            nome.getText(),
                            cognome.getText(),
                            vId,
                            email.getText()
                    );

                    System.out.println("Utente salvato con successo.");

                } catch (NumberFormatException e) {
                    mostraErrore("L'ID Utente deve essere un numero intero.");
                    event.consume();
                }
            });

        dialog.showAndWait();
    }

    private void mostraErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore Input");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}
