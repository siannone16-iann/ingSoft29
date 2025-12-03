
/**
 *
 * @author simoneiannone
 */

public class Libro {

    private final String Titolo;
    private int copie;
    private int copiePrestate;
    private final int isbn;
    private final int annoProduzione;

    public Libro(String titolo, int isbn, int annoProduzione, int copie) {
        Titolo = titolo;
        this.isbn = isbn;
        this.annoProduzione = annoProduzione;
        this.copie = copie;
        this.copiePrestate = 0;
    }

    public boolean isAvailable(){
        if (copiePrestate < copie) return true;
        return false;
    }

    public boolean prestito(){
        if(isAvailable()){
            copiePrestate++;
            return true;
        }
        else return false;   //gestione eccezione da valutare se farlo in BibliotecaManager
    }

}
