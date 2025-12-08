/**
 *
 * @author salvatoremoccia
 */

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.util.Optional;

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

        Label labelId = new Label("Matricola (automatico): " + manager.getProssimoIdUtente());
        labelId.setStyle("-fx-font-weight: bold; -fx-text-fill: #2196F3;");

        grid.add(new Label("Nome:"), 0, 0);    grid.add(nome, 1, 0);
        grid.add(new Label("Cognome:"), 0, 1); grid.add(cognome, 1, 1);
        grid.add(new Label("Email:"), 0, 2);   grid.add(email, 1, 2);
        grid.add(labelId, 0, 3, 2, 1);

        dialog.getDialogPane().setContent(grid);

        // 3. LOGICA DI SALVATAGGIO
        Button btnSalva = (Button) dialog.getDialogPane().lookupButton(salvaButtonType);

        btnSalva.addEventFilter(ActionEvent.ACTION, event -> {
            // A. Campi Vuoti
            if (nome.getText().trim().isEmpty() ||
                    cognome.getText().trim().isEmpty() ||
                    email.getText().trim().isEmpty()) {

                mostraErrore("Tutti i campi sono obbligatori.");
                event.consume();
                return;
            }

            if (!email.getText().contains("@")) {
                mostraErrore("Inserisci un'email valida.");
                event.consume();
                return;
            }

            manager.aggiungiUtente(
                    nome.getText(),
                    cognome.getText(),
                    email.getText()
            );

            System.out.println("Utente salvato con successo.");
        });

        dialog.showAndWait();
    }

    public void nuovoPrestito() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuovo Prestito");
        dialog.setHeaderText("Registra un nuovo prestito");

        // 1. Bottoni
        ButtonType salvaButtonType = new ButtonType("Salva", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(salvaButtonType, ButtonType.CANCEL);

        // 2. Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // ComboBox per selezionare l'utente
        ComboBox<Utente> comboUtente = new ComboBox<>();
        comboUtente.setItems(manager.getRegistroUtenti());
        comboUtente.setPromptText("Seleziona utente");

        // Per mostrare il nome completo e i prestiti attivi nella ComboBox
        comboUtente.setButtonCell(new ListCell<Utente>() {
            @Override
            protected void updateItem(Utente item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String info = item.getNome() + " " + item.getCognome() +
                            " (ID: " + item.getIdUtente() + ") - " +
                            "Prestiti: " + item.getPrestiti() + "/3";
                    setText(info);
                    // Colora in rosso se ha raggiunto il limite
                    if (item.getPrestiti() >= 3) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
        comboUtente.setCellFactory(param -> new ListCell<Utente>() {
            @Override
            protected void updateItem(Utente item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String info = item.getNome() + " " + item.getCognome() +
                            " (ID: " + item.getIdUtente() + ") - " +
                            "Prestiti: " + item.getPrestiti() + "/3";
                    setText(info);
                    // Colora in rosso se ha raggiunto il limite
                    if (item.getPrestiti() >= 3) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        // ComboBox per selezionare il libro
        ComboBox<Libro> comboLibro = new ComboBox<>();
        comboLibro.setItems(manager.getCatalogo());
        comboLibro.setPromptText("Seleziona libro");

        // Per mostrare il titolo del libro e le copie disponibili nella ComboBox
        comboLibro.setButtonCell(new ListCell<Libro>() {
            @Override
            protected void updateItem(Libro item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String info = item.getTitolo() + " - " + item.getAutore() +
                            " (ISBN: " + item.getIsbn() + ") - " +
                            "Disponibili: " + item.getCopieDisponibili() + "/" + item.getCopie();
                    setText(info);
                    // Colora in rosso se non ci sono copie disponibili
                    if (item.getCopieDisponibili() <= 0) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
        comboLibro.setCellFactory(param -> new ListCell<Libro>() {
            @Override
            protected void updateItem(Libro item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String info = item.getTitolo() + " - " + item.getAutore() +
                            " (ISBN: " + item.getIsbn() + ") - " +
                            "Disponibili: " + item.getCopieDisponibili() + "/" + item.getCopie();
                    setText(info);
                    // Colora in rosso se non ci sono copie disponibili
                    if (item.getCopieDisponibili() <= 0) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        // DatePicker per la data di inizio
        DatePicker dataInizio = new DatePicker();
        dataInizio.setValue(LocalDate.now());

        // DatePicker per la data di scadenza
        DatePicker dataScadenza = new DatePicker();
        dataScadenza.setValue(LocalDate.now().plusDays(30));

        grid.add(new Label("Utente:"), 0, 0);      grid.add(comboUtente, 1, 0);
        grid.add(new Label("Libro:"), 0, 1);       grid.add(comboLibro, 1, 1);
        grid.add(new Label("Data Inizio:"), 0, 2); grid.add(dataInizio, 1, 2);
        grid.add(new Label("Scadenza:"), 0, 3);    grid.add(dataScadenza, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // 3. LOGICA DI SALVATAGGIO
        Button btnSalva = (Button) dialog.getDialogPane().lookupButton(salvaButtonType);

        btnSalva.addEventFilter(ActionEvent.ACTION, event -> {
            // A. Validazione selezioni
            if (comboUtente.getValue() == null || comboLibro.getValue() == null ||
                    dataInizio.getValue() == null || dataScadenza.getValue() == null) {

                mostraErrore("Tutti i campi sono obbligatori.");
                event.consume();
                return;
            }

            // B. Validazione date
            if (dataScadenza.getValue().isBefore(dataInizio.getValue())) {
                mostraErrore("La data di scadenza non può essere precedente alla data di inizio.");
                event.consume();
                return;
            }

            // C. Salvataggio
            String risultato = manager.aggiungiPrestito(
                    comboUtente.getValue(),
                    dataScadenza.getValue(),
                    comboLibro.getValue(),
                    dataInizio.getValue()
            );

            if (risultato.equals("success")) {
                // Mostra messaggio di successo
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Successo");
                success.setHeaderText(null);
                success.setContentText("Prestito registrato con successo!");
                success.showAndWait();
                System.out.println("Prestito salvato con successo.");
            } else {
                // Mostra il messaggio di errore restituito dal manager
                mostraErrore(risultato);
                event.consume(); // Blocca la chiusura del dialog
            }
        });

        dialog.showAndWait();
    }


    public void formModificaLibro(Libro libro){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifica Libro");
        dialog.setHeaderText("Modifica il Libro: " + libro.getTitolo());


        ButtonType aggiornaButton = new ButtonType("Aggiorna", ButtonBar.ButtonData.OK_DONE);
        ButtonType eliminaButton = new ButtonType("Rimuovi", ButtonBar.ButtonData.LEFT);


        dialog.getDialogPane().getButtonTypes().addAll(
                aggiornaButton,
                eliminaButton,
                ButtonType.CANCEL);




        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titolo = new TextField(libro.getTitolo());
        TextField autore = new TextField(libro.getAutore());
        TextField isbn = new TextField(String.valueOf(libro.getIsbn()));
        TextField anno = new TextField(String.valueOf(libro.getAnnoProduzione()));
        TextField copie = new TextField(String.valueOf(libro.getCopie()));

        grid.add(new Label("Titolo:"), 0, 0); grid.add(titolo, 1, 0);
        grid.add(new Label("Autore:"), 0, 1); grid.add(autore, 1, 1);
        grid.add(new Label("ISBN:"), 0, 2);   grid.add(isbn, 1, 2);
        grid.add(new Label("Anno:"), 0, 3);   grid.add(anno, 1, 3);
        grid.add(new Label("Copie:"), 0, 4);  grid.add(copie, 1, 4);

        dialog.getDialogPane().setContent(grid);

        Button btnSalva = (Button) dialog.getDialogPane().lookupButton(aggiornaButton);
        Button btnElimina = (Button) dialog.getDialogPane().lookupButton(eliminaButton);

        btnElimina.setStyle("-fx-font-weight: bold; -fx-background-color: #cb3234;");

        btnElimina.addEventFilter(ActionEvent.ACTION, event -> {
            Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
            conferma.setTitle("Conferma Eliminazione");
            conferma.setHeaderText("Eliminare il libro : " + libro.getTitolo());
            conferma.setContentText("Scrivi CONFERMA per proseguire: ");

            TextField confermaTesto = new TextField();

            confermaTesto.setPromptText("CONFERMA");

            VBox box = new VBox(10);
            box.getChildren().addAll(
                    new Label("Questa operazione è irreversibile."),
                    confermaTesto
            );

            conferma.getDialogPane().setContent(box);
            conferma.getDialogPane().setGraphic(null);
            Button elimina = (Button) conferma.getDialogPane().lookupButton(ButtonType.OK);
            elimina.setStyle("-fx-font-weight: bold; -fx-background-color: #cb3234;");
            elimina.setDisable(true);

            confermaTesto.textProperty().addListener((obs,oldVal, newVal) -> {
                elimina.setDisable(
                        !"CONFERMA".equals(newVal.trim())
                );
            });

            Optional<ButtonType> risultato = conferma.showAndWait();

            if(risultato.isPresent() && risultato.get() == ButtonType.OK) {
                try {

                    manager.eliminaLibro(libro);

                } catch (Exception e) {
                    mostraErrore("Errore durante l'eliminazione: "+ e.getMessage());
                    event.consume();
                }
            }
            else {
                event.consume();
            }

        });

        btnSalva.addEventFilter(ActionEvent.ACTION, event -> {
            // A. Validazione Campi Vuoti
            if (titolo.getText().trim().isEmpty() ||
                    autore.getText().trim().isEmpty() ||
                    isbn.getText().trim().isEmpty() ||
                    anno.getText().trim().isEmpty() ||
                    copie.getText().trim().isEmpty()) {

                mostraErrore("Tutti i campi sono obbligatori.");
                event.consume();
                return;
            }

            try {
                int vIsbn = Integer.parseInt(isbn.getText());
                int vAnno = Integer.parseInt(anno.getText());
                int vCopie = Integer.parseInt(copie.getText());
                try {
                    libro.modificaLibro(
                            titolo.getText(),
                            autore.getText(),
                            vIsbn,
                            vAnno,
                            vCopie
                    );
                    manager.aggiornaLibro(libro);

                } catch (Exception e) {
                    mostraErrore("Errore nella modifica del libro : "+ e.getMessage());
                }

                System.out.println("Libro modificato con successo.");

            } catch (NumberFormatException e) {
                mostraErrore("ISBN, Anno e Copie devono essere numeri interi.");
                event.consume();
            }
        });
        dialog.showAndWait();
    }

    public void formModificaUtente(Utente utente){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifica Utente");
        dialog.setHeaderText("Modifica l'Utente: " + utente.getNome()+ " " + utente.getCognome());

        ButtonType aggiornaButton = new ButtonType("Aggiorna", ButtonBar.ButtonData.OK_DONE);
        ButtonType eliminaButton = new ButtonType("Rimuovi", ButtonBar.ButtonData.LEFT);


        dialog.getDialogPane().getButtonTypes().addAll(
                aggiornaButton,
                eliminaButton,
                ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nome = new TextField(utente.getNome());
        TextField cognome = new TextField(utente.getCognome());
        TextField email = new TextField(utente.getEmail());
        Label idUtente = new Label("Matricola : " + String.valueOf(utente.getIdUtente()));
        idUtente.setStyle("-fx-font-weight: bold; -fx-text-fill: #2196F3;");

        grid.add(new Label("Nome:"), 0, 0);    grid.add(nome, 1, 0);
        grid.add(new Label("Cognome:"), 0, 1); grid.add(cognome, 1, 1);
        grid.add(new Label("Email:"), 0, 2);   grid.add(email, 1, 2);
        grid.add(idUtente, 0, 3, 2, 1);

        dialog.getDialogPane().setContent(grid);


        Button btnSalva = (Button) dialog.getDialogPane().lookupButton(aggiornaButton);
        Button btnElimina = (Button) dialog.getDialogPane().lookupButton(eliminaButton);

        btnElimina.setStyle("-fx-font-weight: bold; -fx-background-color: #cb3234;");

        btnElimina.addEventFilter(ActionEvent.ACTION, event -> {
            Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
            conferma.setTitle("Conferma Eliminazione");
            conferma.setHeaderText("Eliminare il libro : " + utente.getNome() +" "+ utente.getCognome());
            conferma.setContentText("Scrivi CONFERMA per proseguire: ");

            TextField confermaTesto = new TextField();

            confermaTesto.setPromptText("CONFERMA");

            VBox box = new VBox(10);
            box.getChildren().addAll(
                    new Label("Questa operazione è irreversibile."),
                    confermaTesto
            );

            conferma.getDialogPane().setContent(box);
            conferma.getDialogPane().setGraphic(null);

            Button elimina = (Button) conferma.getDialogPane().lookupButton(ButtonType.OK);
            elimina.setStyle("-fx-font-weight: bold; -fx-background-color: #cb3234;");
            elimina.setDisable(true);

            confermaTesto.textProperty().addListener((obs,oldVal, newVal) -> {
                elimina.setDisable(
                        !"CONFERMA".equals(newVal.trim())
                );
            });

            Optional<ButtonType> risultato = conferma.showAndWait();

            if(risultato.isPresent() && risultato.get() == ButtonType.OK) {
                try {

                    manager.eliminaUtente(utente);

                } catch (Exception e) {
                    mostraErrore("Errore durante l'eliminazione: "+ e.getMessage());
                    event.consume();
                }
            }
            else {
                event.consume();
            }

        });

        btnSalva.addEventFilter(ActionEvent.ACTION, event -> {

            if (nome.getText().trim().isEmpty() ||
                    cognome.getText().trim().isEmpty() ||
                    email.getText().trim().isEmpty()) {

                mostraErrore("Tutti i campi sono obbligatori.");
                event.consume();
                return;
            }

            if (!email.getText().contains("@")) {
                mostraErrore("Inserisci un'email valida.");
                event.consume();
                return;
            }

            utente.modificaUtente(
                    nome.getText(),
                    cognome.getText(),
                    email.getText(),
                    utente.getIdUtente()
            );

            manager.aggiornaUtente(utente);

            System.out.println("Utente modificato con successo.");
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