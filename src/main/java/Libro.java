
/**
 *
 * @author simoneiannone
 */

public class Libro {

    private final String titolo;
    private final String autore;
    private int copie;
    private int copiePrestate;
    private final int isbn;
    private final int annoProduzione;

    public Libro(String titolo, String autore, int isbn, int annoProduzione, int copie, int copiePrestate) {
        this.titolo = titolo;
        this.autore = autore;
        this.isbn = isbn;
        this.annoProduzione = annoProduzione;
        this.copie = copie;
        this.copiePrestate = copiePrestate;
    }
    
    public Libro(String titolo, String autore, int isbn, int annoProduzione, int copie){
        this(titolo, autore, isbn, annoProduzione, copie, 0);
    }

    public String getTitolo() {

        return titolo;
    }

    public String getAutore() {
        return autore;
    }

    public int getCopie() {
        return copie;
    }

    public int getCopiePrestate() {

        return copiePrestate;
    }

    public int getIsbn() {

        return isbn;
    }

    public int getAnnoProduzione() {
        return annoProduzione;
    }

    public String getStato(){
        if (copiePrestate < copie){ 
            return "Si";
        }
        else return "No";
    }

    public boolean richiestaPrestito(){
        if(getStato().equals("Si")){
            copiePrestate++;
            return true;
        }
        return false;   //gestione eccezione da valutare se farlo in BibliotecaManager
    }
    public void finePrestito(){
        if(copiePrestate > 0){
            copiePrestate--;
        }
    }

    public int getCopieDisponibili(){
        return copie-copiePrestate;
    }

    public void aumentaCopie(int numCopie){

        copie = copie+numCopie;
    }

    public void setCopie(int copie) {
        this.copie = copie;
    }

    public String toCSV(){
        return titolo +";"+autore+";"+isbn+";"+annoProduzione+";"+copie+";"+copiePrestate;
    }


}
