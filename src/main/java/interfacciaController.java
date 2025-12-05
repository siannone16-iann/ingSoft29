import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;


public class interfacciaController {

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
    private TableColumn<?, ?> utenteNome;
    @FXML
    private TableColumn<?, ?> utenteCognome;
    @FXML
    private TableColumn<?, ?> utenteEmail;
    @FXML
    private TableColumn<?, ?> utentePrestiti;
    @FXML
    private TableColumn<?, ?> prestitoUtente;
    @FXML
    private TableColumn<?, ?> prestitoLibro;
    @FXML
    private TableColumn<?, ?> prestitoInizio;
    @FXML
    private TableColumn<?, ?> prestitoFine;
    @FXML
    private TableColumn<?, ?> libroTitolo;
    @FXML
    private TableColumn<?, ?> libroAutore;
    @FXML
    private TableColumn<?, ?> libroAnno;
    @FXML
    private TableColumn<?, ?> libroCopie;
    @FXML
    private TableColumn<?, ?> libroDisponibile;
    @FXML
    private ToggleGroup gruppoMenu;
    @FXML
    private TableView<?> tabellaUtente;
    @FXML
    private TableView<?> tabellaPrestiti;
    @FXML
    private TableView<?> tabellaLibri;


    @FXML
    private void initialize(){
        bLibri.setSelected(true);
        libri.setVisible(true);
        utenti.setVisible(false);
        prestiti.setVisible(false);
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