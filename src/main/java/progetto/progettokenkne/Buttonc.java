package progetto.progettokenkne;

import javafx.scene.control.Button;

public class Buttonc extends Button {
    private int riga,colonna;

    public Buttonc(int x, int y){
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
