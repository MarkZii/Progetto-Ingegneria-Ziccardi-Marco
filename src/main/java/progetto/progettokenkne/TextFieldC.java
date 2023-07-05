package progetto.progettokenkne;

import javafx.application.Application;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
