
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

    public Libro(String titolo, String autore, int isbn, int annoProduzione, int copie) {
        this.titolo = titolo;
        this.autore = autore;
        this.isbn = isbn;
        this.annoProduzione = annoProduzione;
        this.copie = copie;
        this.copiePrestate = 0;
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
        if (copiePrestate < copie) return "Si";
        return "No";
    }

    public boolean richiestaPrestito(){
        if(getStato().equals("Si")){
            copiePrestate++;
            return true;
        }
        else return false;   //gestione eccezione da valutare se farlo in BibliotecaManager
    }
    public void finePrestito(){
        copiePrestate--;
    }

    public void aumentaCopie(int numCopie){
        copie = copie+numCopie;
    }

    public String toCSV(){
        return titolo +";"+isbn+";"+annoProduzione+";"+copie+";"+copiePrestate;
    }

}
