/**
 * @brief Gestisce le finestre di dialogo per l'interazione con l'utente.
 * * Questa classe si occupa di creare e mostrare i dialoghi che servono per l'inserimento,
 * la modifica e l'eliminazione di Libri, Utenti e Prestiti, gestendo la validazione
 * dell'input e la comunicazione con Biblioteca Manager.
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

public class Gestione {
    private final BibliotecaManager manager;

    /**
     * @brief Costruttore della classe Finestre.
     * @param manager La classe che gestisce le funzioni della biblioteca.
     */
    public Gestione (BibliotecaManager manager){
        this.manager=manager;
    }
    /**
     * @brief Apre il form per l'inserimento di un nuovo libro.
     * * Gestisce la validazione dei campi (titolo, autore, ISBN, anno, copie)
     * e invoca il manager per il salvataggio.
     */
    public void nuovoLibro() {
        Dialog<ButtonType> dialog = new Dialog<>();
        stileCSS(dialog);
        dialog.setTitle("Nuovo Libro");
        dialog.setHeaderText("Aggiungi un nuovo libro al catalogo");

        ButtonType salvaButtonType = new ButtonType("Salva", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(salvaButtonType, ButtonType.CANCEL);

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

        Button btnSalva = (Button) dialog.getDialogPane().lookupButton(salvaButtonType);
        Button btnAnnulla = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);

        btnAnnulla.getStyleClass().add("bottone-annulla");

        btnSalva.addEventFilter(ActionEvent.ACTION, event -> {
            if (titolo.getText().trim().isEmpty() ||
                    autore.getText().trim().isEmpty() ||
                    isbn.getText().trim().isEmpty() ||
                    anno.getText().trim().isEmpty() ||
                    copie.getText().trim().isEmpty()) {

                mostraErrore("Tutti i campi sono obbligatori.");
                event.consume();
                return;
            }
            int vIsbn = Integer.parseInt(isbn.getText());
            int vAnno = Integer.parseInt(anno.getText());
            int vCopie = Integer.parseInt(copie.getText());
            if (controllaISBN(titolo.getText(), autore.getText(), vIsbn, vAnno) ) {
                try {
                    manager.aggiungiLibro(
                            titolo.getText(),
                            autore.getText(),
                            vIsbn,
                            vAnno,
                            vCopie
                    );

                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    stileCSS(success);
                    success.getDialogPane().setGraphic(null);
                    success.setTitle("Successo");
                    success.setHeaderText(null);
                    success.setContentText("Libro registrato con successo!");
                    success.showAndWait();
                    System.out.println("Libro salvato con successo.");


                } catch (NumberFormatException e) {
                    mostraErrore("ISBN, Anno e Copie devono essere numeri interi.");
                    event.consume();
                }
            }
            else {
                mostraErrore("ISBN già presente.\n Il resto dei dati non corrisponde al Libro in Catalogo.");
                event.consume();
            }
            });

        dialog.showAndWait();
    }
    /**
     * @brief Apre il form per la registrazione di un nuovo utente.
     * * Visualizza la matricola generata automaticamente e richiede nome, cognome ed email.
     * Include validazione del formato email (il testo deve contenere una @).
     */
    public void nuovoUtente() {
        Dialog<ButtonType> dialog = new Dialog<>();
        stileCSS(dialog);
        dialog.setTitle("Nuovo Utente");
        dialog.setHeaderText("Registra un nuovo utente");

        ButtonType salvaButtonType = new ButtonType("Salva", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(salvaButtonType, ButtonType.CANCEL);

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

        Button btnSalva = (Button) dialog.getDialogPane().lookupButton(salvaButtonType);
        Button btnAnnulla = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);

        btnAnnulla.getStyleClass().add("bottone-annulla");

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

            manager.aggiungiUtente(
                    nome.getText(),
                    cognome.getText(),
                    email.getText()
            );

            Alert success = new Alert(Alert.AlertType.INFORMATION);
            stileCSS(success);
            success.getDialogPane().setGraphic(null);
            success.setTitle("Successo");
            success.setHeaderText(null);
            success.setContentText("Utente registrato con successo!");
            success.showAndWait();
            System.out.println("Utente salvato con successo.");
        });

        dialog.showAndWait();
    }
    /**
     * @brief Apre il form per la creazione di un nuovo prestito.
     * * Utilizza ComboBox per mostrare lo stato degli utenti (quanti prestiti hanno)
     * e libri (copie disponibili). Valida la coerenza delle date (inizio/fine).
     */
    public void nuovoPrestito() {
        Dialog<ButtonType> dialog = new Dialog<>();
        stileCSS(dialog);
        dialog.setTitle("Nuovo Prestito");
        dialog.setHeaderText("Registra un nuovo prestito");

        ButtonType salvaButtonType = new ButtonType("Salva", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(salvaButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<Utente> comboUtente = new ComboBox<>();
        comboUtente.setItems(manager.getRegistroUtenti());
        comboUtente.setPromptText("Seleziona utente");

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
                    if (item.getPrestiti() >= 3) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        ComboBox<Libro> comboLibro = new ComboBox<>();
        comboLibro.setItems(manager.getCatalogo());
        comboLibro.setPromptText("Seleziona libro");

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
                    if (item.getCopieDisponibili() <= 0) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        DatePicker dataInizio = new DatePicker();
        dataInizio.setValue(LocalDate.now());

        DatePicker dataScadenza = new DatePicker();
        dataScadenza.setValue(LocalDate.now().plusDays(30));

        grid.add(new Label("Utente:"), 0, 0);      grid.add(comboUtente, 1, 0);
        grid.add(new Label("Libro:"), 0, 1);       grid.add(comboLibro, 1, 1);
        grid.add(new Label("Data Inizio:"), 0, 2); grid.add(dataInizio, 1, 2);
        grid.add(new Label("Scadenza:"), 0, 3);    grid.add(dataScadenza, 1, 3);

        dialog.getDialogPane().setContent(grid);

        Button btnSalva = (Button) dialog.getDialogPane().lookupButton(salvaButtonType);
        Button btnAnnulla = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);

        btnAnnulla.getStyleClass().add("bottone-annulla");

        btnSalva.addEventFilter(ActionEvent.ACTION, event -> {
            if (comboUtente.getValue() == null || comboLibro.getValue() == null ||
                    dataInizio.getValue() == null || dataScadenza.getValue() == null) {

                mostraErrore("Tutti i campi sono obbligatori.");
                event.consume();
                return;
            }

            if (dataScadenza.getValue().isBefore(dataInizio.getValue())) {
                mostraErrore("La data di scadenza non può essere precedente alla data di inizio.");
                event.consume();
                return;
            }

            String risultato = manager.aggiungiPrestito(
                    comboUtente.getValue(),
                    dataScadenza.getValue(),
                    comboLibro.getValue(),
                    dataInizio.getValue()
            );

            if (risultato.equals("success")) {
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                stileCSS(success);

                success.setTitle("Successo");
                success.setHeaderText(null);
                success.setContentText("Prestito registrato con successo!");
                success.showAndWait();
                System.out.println("Prestito salvato con successo.");
            } else {
                mostraErrore(risultato);
                event.consume();
            }
        });

        dialog.showAndWait();
    }

    /**
     * @brief Apre il form per modificare o eliminare un libro esistente.
     * * Permette di aggiornare i dati (se le copie non scendono sotto quelle prestate)
     * o di eliminare il libro previa conferma esplicita (digitando "CONFERMA").
     * * @param libro L'oggetto Libro da modificare o eliminare.
     */

    public void formModificaLibro(Libro libro){
        Dialog<ButtonType> dialog = new Dialog<>();
        stileCSS(dialog);
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
        btnSalva.setStyle("");
        Button btnElimina = (Button) dialog.getDialogPane().lookupButton(eliminaButton);
        Button btnAnnulla = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);

        btnElimina.getStyleClass().add("bottone-elimina");
        btnAnnulla.getStyleClass().add("bottone-annulla");

        btnElimina.addEventFilter(ActionEvent.ACTION, event -> {
            Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
            stileCSS(conferma);

            conferma.setTitle("Elimina");
            conferma.setHeaderText("Eliminare il libro : " + libro.getTitolo());
            conferma.setContentText("Scrivi CONFERMA per proseguire: ");

            TextField confermaTesto = new TextField();

            confermaTesto.setPromptText("CONFERMA");

            VBox box = new VBox(10);
            box.getChildren().addAll(
                    new Label("Digita CONFERMA per proseguire. \n(Questa operazione è irreversibile)"),
                    confermaTesto
            );

            conferma.getDialogPane().setContent(box);
            conferma.getDialogPane().setGraphic(null);
            Button elimina = (Button) conferma.getDialogPane().lookupButton(ButtonType.OK);
            Button annulla = (Button) conferma.getDialogPane().lookupButton(ButtonType.CANCEL);
            elimina.getStyleClass().add("bottone-elimina");
            annulla.getStyleClass().add("bottone-annulla");
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

                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    stileCSS(success);
                    success.getDialogPane().setGraphic(null);
                    success.setTitle("Successo");
                    success.setHeaderText(null);
                    success.setContentText("Libro eliminato con successo!");
                    success.showAndWait();
                    System.out.println("Libro eliminato con successo.");

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

                if(vCopie >= libro.getCopiePrestate()) {
                    try {

                        libro.modificaLibro(
                                titolo.getText(),
                                autore.getText(),
                                vIsbn,
                                vAnno,
                                vCopie
                        );
                        manager.aggiornaLibro(libro);

                        Alert success = new Alert(Alert.AlertType.INFORMATION);
                        stileCSS(success);
                        success.getDialogPane().setGraphic(null);
                        success.setTitle("Successo");
                        success.setHeaderText(null);
                        success.setContentText("Libro modificato con successo!");
                        success.showAndWait();
                        System.out.println("Libro modificato con successo.");

                    } catch (Exception e) {
                        mostraErrore("Errore nella modifica del libro : " + e.getMessage());
                    }
                    System.out.println("Libro modificato con successo.");
                }
                else {
                    mostraErrore("Il numero di copie che hai inserito : "+vCopie +", è minore di quelle già prestate ("+libro.getCopiePrestate()+ ").");
                    event.consume();
                }
            } catch (NumberFormatException e) {
                mostraErrore("ISBN, Anno e Copie devono essere numeri interi.");
                event.consume();
            }
        });
        dialog.showAndWait();
    }
    /**
     * @brief Apre il form per modificare o eliminare un utente esistente.
     * * Permette l'aggiornamento dei dati anagrafici o l'eliminazione dell'utente
     * previa conferma esplicita.
     * * @param utente L'oggetto Utente da modificare o eliminare.
     */
    public void formModificaUtente(Utente utente){
        Dialog<ButtonType> dialog = new Dialog<>();
        stileCSS(dialog);
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
        Button btnAnnulla = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);

        btnElimina.getStyleClass().add("bottone-elimina");
        btnAnnulla.getStyleClass().add("bottone-annulla");

        btnElimina.addEventFilter(ActionEvent.ACTION, event -> {
            Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
            stileCSS(conferma);

            conferma.setTitle("Elimina");
            conferma.setHeaderText("Eliminare l'Utente : " + utente.getNome() +" "+ utente.getCognome());

            TextField confermaTesto = new TextField();

            confermaTesto.setPromptText("CONFERMA");

            VBox box = new VBox(10);
            box.getChildren().addAll(
                    new Label("Digita CONFERMA per proseguire. \n(Questa operazione è irreversibile)"),
                    confermaTesto
            );

            conferma.getDialogPane().setContent(box);
            conferma.getDialogPane().setGraphic(null);

            Button elimina = (Button) conferma.getDialogPane().lookupButton(ButtonType.OK);
            Button annulla = (Button) conferma.getDialogPane().lookupButton(ButtonType.CANCEL);

            elimina.getStyleClass().add("bottone-elimina");
            annulla.getStyleClass().add("bottone-annulla");

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
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    stileCSS(success);
                    success.getDialogPane().setGraphic(null);
                    success.setTitle("Successo");
                    success.setHeaderText(null);
                    success.setContentText("Utente eliminato con successo!");
                    success.showAndWait();
                    System.out.println("Utente eliminato con successo.");

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

            Alert success = new Alert(Alert.AlertType.INFORMATION);
            stileCSS(success);
            success.getDialogPane().setGraphic(null);
            success.setTitle("Successo");
            success.setHeaderText(null);
            success.setContentText("Utente modificato con successo!");
            success.showAndWait();
            System.out.println("Utente modificato con successo.");

        });

        dialog.showAndWait();
    }

    public void formModificaPrestito(Prestito prestito){
        Dialog<ButtonType> dialog = new Dialog<>();
        stileCSS(dialog);
        dialog.setTitle("Modifica Prestito");
        dialog.setHeaderText("Registra un nuovo prestito");

        ButtonType salvaButtonType = new ButtonType("Aggiorna", ButtonBar.ButtonData.OK_DONE);
        ButtonType eliminaButton = new ButtonType("Rimuovi", ButtonBar.ButtonData.LEFT);
        dialog.getDialogPane().getButtonTypes().addAll(salvaButtonType, eliminaButton ,ButtonType.CANCEL);


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 150, 10, 10));

        grid.add(new Label("Utente:"), 0, 0);      grid.add(new Label(prestito.getNomeUtente()), 1, 0);
        grid.add(new Label("Libro:"), 0, 1);       grid.add(new Label(prestito.getTitoloLibro()), 1, 1);
        grid.add(new Label("Data Inizio:"), 0, 2); grid.add(new Label(""+prestito.getDataInizioPrestito()), 1, 2);

        DatePicker dataScadenza = new DatePicker(prestito.getDataScadenza());
        grid.add(new Label("Scadenza:"), 0, 3);    grid.add(dataScadenza, 1, 3);

        dialog.getDialogPane().setContent(grid);

        Button btnSalva = (Button) dialog.getDialogPane().lookupButton(salvaButtonType);
        Button btnElimina = (Button) dialog.getDialogPane().lookupButton(eliminaButton);
        Button btnAnnulla = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);

        btnElimina.getStyleClass().add("bottone-elimina");
        btnAnnulla.getStyleClass().add("bottone-annulla");

        btnElimina.addEventFilter(ActionEvent.ACTION, event -> {
            Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
            stileCSS(conferma);
            conferma.setTitle("Termina Prestito");
            conferma.setHeaderText("Vuoi terminare il prestito?");

            VBox box = new VBox(10);
            box.getChildren().addAll(
                    new Label("Premendo conferma terminerai il prestito di: "),
                    new Label("Libro : "+prestito.getTitoloLibro()+" preso in prestito da: " + prestito.getNomeUtente()+".")
            );
            conferma.getDialogPane().setContent(box);
            conferma.getDialogPane().setGraphic(null);

            Button elimina = (Button) conferma.getDialogPane().lookupButton(ButtonType.OK);
            Button annulla = (Button) conferma.getDialogPane().lookupButton(ButtonType.CANCEL);

            elimina.getStyleClass().add("bottone-elimina");
            annulla.getStyleClass().add("bottone-annulla");
            Optional<ButtonType> risultato = conferma.showAndWait();

            if(risultato.isPresent() && risultato.get() == ButtonType.OK) {
                try {

                    manager.restituisciLibro(prestito);

                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    stileCSS(success);
                    success.getDialogPane().setGraphic(null);
                    success.setTitle("Successo");
                    success.setHeaderText(null);
                    success.setContentText("Prestito rimosso con successo!");
                    success.showAndWait();
                    System.out.println("Prestito estinto con successo.");

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
            if (dataScadenza.getValue() == null) {
                mostraErrore("Il campo Scadenza è obbligatorio.");
                event.consume();
                return;
            }

            if (dataScadenza.getValue().isBefore(LocalDate.now())) {
                mostraErrore("La data di scadenza non deve essere già passata.");
                event.consume();
                return;
            }
            try {
                prestito.modificaPrestito(dataScadenza.getValue());
                manager.aggiornaPrestito(prestito);


                Alert success = new Alert(Alert.AlertType.INFORMATION);
                stileCSS(success);
                success.getDialogPane().setGraphic(null);
                success.setTitle("Successo");
                success.setHeaderText(null);
                success.setContentText("Prestito modificato con successo!");
                success.showAndWait();
                System.out.println("Prestito modificato con successo.");

            } catch (Exception e) {
                mostraErrore(e.getMessage());
                event.consume();

            }
        });

        dialog.showAndWait();
    }

    public void utentePrestito(Utente utente){
        Dialog<ButtonType> dialog = new Dialog<>();
        stileCSS(dialog);
        dialog.setTitle("Elenco Prestiti: ");
        dialog.setHeaderText("Ecco i Prestiti dell'Utente : "+utente.getNome() + " "+ utente.getCognome());

        ButtonType salvaButtonType = new ButtonType("Fatto", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(salvaButtonType);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        int riga = 0;
        boolean flag = true;
        for(Prestito p : manager.getRegistroPrestiti()) {
            if (p.getUtente() == utente) {
                flag=false;
                Libro l = p.getLibro();
                grid.add(new Label(" Titolo: "+l.getTitolo() + ",   Autore : " + l.getAutore() +",  Scadenza Prestito: "+ p.getDataScadenza()), 0, riga);
                riga++;
            }
        }
        if(flag){
            grid.add(new Label("L'Utente non ha prestiti a carico."), 0, 0);
        }

        dialog.getDialogPane().setContent(grid);

        dialog.showAndWait();
    }

    public boolean controllaISBN(String titolo, String autore, int ISBN, int anno){
        for (Libro l : manager.getCatalogo()){
            if(l.getIsbn() == ISBN){
                if(l.getTitolo().equalsIgnoreCase(titolo) &&
                        l.getAutore().equalsIgnoreCase(autore) &&
                        l.getAnnoProduzione() == anno) {
                        return true;
                }
                else {
                    return false;
                }
            }
        }

        return true;

    }

    /**
     * @brief Mostra un alert di errore a video.
     * @param messaggio Il testo dell'errore da visualizzare.
     */
    private void mostraErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        stileCSS(alert);

        alert.setTitle("Errore Input");
        alert.setHeaderText("Attenzione!");
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    private void stileCSS(Dialog<?> dialog){
        try {
            String css = getClass().getResource("/InterfacciaGrafica.css").toExternalForm();
            dialog.getDialogPane().getStylesheets().add(css);
        } catch (Exception e) {
            System.out.println("Impossibile caricare il CSS per il dialogo: " + e.getMessage());
        }
    }
}