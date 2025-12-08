/**
 *
 * @author salvatoremoccia
 */

import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class interfacciaController {

    @FXML
    private Button btUtente;
    @FXML
    private Button btLibro;
    @FXML
    private Button btPrestito;
    @FXML
    private ToggleButton bLibri;
    @FXML
    private ToggleButton bUtenti;
    @FXML
    private ToggleButton bPrestiti;
    @FXML
    private VBox prestiti;
    @FXML
    private VBox utenti;
    @FXML
    private VBox libri;
    @FXML
    private Label tPrestiti;
    @FXML
    private Label tUtenti;
    @FXML
    private Label tLibri;
    @FXML
    private TableColumn<Utente, String> utenteNome;
    @FXML
    private TableColumn<Utente, String> utenteCognome;
    @FXML
    private TableColumn<Utente, String> utenteEmail;
    @FXML
    private TableColumn<Utente, Integer> utenteID;
    @FXML
    private TableColumn<Utente, Integer> utentePrestiti;
    @FXML
    private TableColumn<Prestito, String> prestitoUtente;
    @FXML
    private TableColumn<Prestito, String> prestitoLibro;
    @FXML
    private TableColumn<Prestito, LocalDate> prestitoInizio;
    @FXML
    private TableColumn<Prestito, LocalDate> prestitoFine;
    @FXML
    private TableColumn<Libro, String> libroTitolo;
    @FXML
    private TableColumn<Libro, String> libroAutore;
    @FXML
    private TableColumn<Libro, Integer> libroAnno;
    @FXML
    private TableColumn<Libro,Integer> libroIsbn;
    @FXML
    private TableColumn<Libro, Integer> libroCopie;
    @FXML
    private TableColumn<Libro, Integer> libroCopieD;
    @FXML
    private TableColumn<Libro, String> libroDisponibile;
    @FXML
    private ToggleGroup gruppoMenu;
    @FXML
    private TableView<Utente> tabellaUtente;
    @FXML
    private TableView<Prestito> tabellaPrestiti;
    @FXML
    private TableView<Libro> tabellaLibri;

    @FXML
    private TableColumn<Libro, Void> libroModifica;
    @FXML
    private TableColumn<Utente, Void> utenteModifica;

    @FXML
    private TextField barraRicercaLibri;
    @FXML
    private TextField barraRicercaUtenti;

    private BibliotecaManager manager;
    private Finestre gestioneForm;


    @FXML
    private void initialize(){
        bLibri.setSelected(true);
        libri.setVisible(true);
        utenti.setVisible(false);
        prestiti.setVisible(false);

        libroTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        libroAutore.setCellValueFactory(new PropertyValueFactory<>("autore"));
        libroIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        libroAnno.setCellValueFactory(new PropertyValueFactory<>("annoProduzione"));
        libroCopie.setCellValueFactory(new PropertyValueFactory<>("copie"));
        libroCopieD.setCellValueFactory(new PropertyValueFactory<>("copieDisponibili"));
        libroDisponibile.setCellValueFactory(new PropertyValueFactory<>("stato"));

        utenteNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        utenteCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        utenteEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        utenteID.setCellValueFactory(new PropertyValueFactory<>("idUtente"));
        utentePrestiti.setCellValueFactory(new PropertyValueFactory<>("prestiti"));

        prestitoUtente.setCellValueFactory(new PropertyValueFactory<>("nomeUtente"));
        prestitoLibro.setCellValueFactory(new PropertyValueFactory<>("titoloLibro"));
        prestitoInizio.setCellValueFactory(new PropertyValueFactory<>("dataInizioPrestito"));
        prestitoFine.setCellValueFactory(new PropertyValueFactory<>("dataScadenza"));

        btLibro.setOnAction(event -> {
            if(gestioneForm != null) gestioneForm.nuovoLibro();
                });
        btUtente.setOnAction(event -> {
            if(gestioneForm != null) gestioneForm.nuovoUtente();
                });
        btPrestito.setOnAction(event -> {
            if(gestioneForm != null) gestioneForm.nuovoPrestito();
        });

        BottoneModifica();
    }

    public void setManager(BibliotecaManager manager) {
        this.manager = manager;
        this.gestioneForm = new Finestre(manager);

        FilteredList<Libro> libriFiltrati = new FilteredList<>(manager.getCatalogo(), b -> true);

        if (barraRicercaLibri != null) {
            barraRicercaLibri.textProperty().addListener((observable, oldValue, newValue) -> {
                libriFiltrati.setPredicate(libro -> {
                    // Se la barra è vuota, mostra tutti i libri
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String filtro = newValue.toLowerCase();
                    if (libro.getTitolo().toLowerCase().contains(filtro)) {
                        return true;
                    }
                    else if (libro.getAutore().toLowerCase().contains(filtro)) {
                        return true;
                    }
                    else if (String.valueOf(libro.getIsbn()).contains(filtro)) {
                        return true;
                    }
                    return false;
                });
            });
        }

        SortedList<Libro> libriOrdinati = new SortedList<>(libriFiltrati);
        libriOrdinati.comparatorProperty().bind(tabellaLibri.comparatorProperty());
        this.tabellaLibri.setItems(libriOrdinati);

        FilteredList<Utente> utentiFiltrati = new FilteredList<>(manager.getRegistroUtenti(), u -> true);

        if (barraRicercaUtenti != null) {
            barraRicercaUtenti.textProperty().addListener((observable, oldValue, newValue) -> {
                utentiFiltrati.setPredicate(utente -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String filtro = newValue.toLowerCase();

                    if (utente.getCognome().toLowerCase().contains(filtro)) {
                        return true;
                    }
                    else if (String.valueOf(utente.getIdUtente()).contains(filtro)) {
                        return true;
                    }

                    return false;
                });
            });
        }

        SortedList<Utente> utentiOrdinati = new SortedList<>(utentiFiltrati);
        utentiOrdinati.comparatorProperty().bind(tabellaUtente.comparatorProperty());
        this.tabellaUtente.setItems(utentiOrdinati);
        this.tabellaPrestiti.setItems(manager.getRegistroPrestiti());
    }

    private void BottoneModifica() {
        Callback<TableColumn<Libro, Void>, TableCell<Libro, Void>> cellFactory = new Callback<TableColumn<Libro, Void>, TableCell<Libro, Void>>() {
            @Override
            public TableCell<Libro, Void> call(final TableColumn<Libro, Void> param) {
                final TableCell<Libro, Void> cell = new TableCell<Libro, Void>() {

                    private final Button btn = new Button("Modifica");

                    {

                        btn.setStyle("-fx-background-color: #bcbcbc; -fx-text-fill: white;");

                        btn.setOnAction((ActionEvent event) -> {

                            Libro libroSelezionato = getTableView().getItems().get(getIndex());


                            if (gestioneForm != null) {

                                gestioneForm.formModificaLibro(libroSelezionato);
                                tabellaPrestiti.refresh();
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };


        Callback<TableColumn<Utente, Void>, TableCell<Utente, Void>> cellFactoryUtente = new Callback<TableColumn<Utente, Void>, TableCell<Utente, Void>>() {
            @Override
            public TableCell<Utente, Void> call(final TableColumn<Utente, Void> param) {
                final TableCell<Utente, Void> cella = new TableCell<Utente, Void>() {

                    private final Button btn = new Button("Modifica");

                    {
                        btn.setStyle("-fx-background-color: #bcbcbc; -fx-text-fill: white;");

                        btn.setOnAction((ActionEvent event) -> {

                            Utente utenteSelezionato = getTableView().getItems().get(getIndex());

                            if (gestioneForm != null) {

                                gestioneForm.formModificaUtente(utenteSelezionato);
                                tabellaPrestiti.refresh();
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn); // Mostra il bottone solo se la riga esiste
                        }
                    }
                };
                return cella;
            }
        };


        libroModifica.setCellFactory(cellFactory);
        utenteModifica.setCellFactory(cellFactoryUtente);
    }

    @FXML
    private void switchTab(ActionEvent event) {
        ToggleButton bottone = (ToggleButton) event.getSource();

        prestiti.setVisible(false);
        utenti.setVisible(false);
        libri.setVisible(false);

        if (bottone.getId().equals("bUtenti")) {
            utenti.setVisible(true);
        } else if (bottone.getId().equals("bLibri")) {
            libri.setVisible(true);
        } else if (bottone.getId().equals("bPrestiti")) {
            prestiti.setVisible(true);
        }
    }
}