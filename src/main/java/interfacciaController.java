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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    }

    public void setManager(BibliotecaManager manager) {
        this.manager = manager;
        this.gestioneForm = new Finestre(manager);

        this.tabellaLibri.setItems(manager.getCatalogo());
        this.tabellaUtente.setItems(manager.getRegistroUtenti());
        this.tabellaPrestiti.setItems(manager.getRegistroPrestiti());
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