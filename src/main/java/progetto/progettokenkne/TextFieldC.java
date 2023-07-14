package progetto.progettokenkne;

import javafx.scene.control.TextField;

//Classe che estende TextField tale che in una TextField è possibile salvare la posizione (riga colonna) e leggerla. Utile per funzioni varie
public class TextFieldC extends TextField {
    private int riga,colonna;

    public TextFieldC( int x, int y){
        super();
        this.riga=y;
        this.colonna=x;
    }
    public int getRiga() {
        return riga;
    }
    public int getColonna() {
        return colonna;
    }
}
