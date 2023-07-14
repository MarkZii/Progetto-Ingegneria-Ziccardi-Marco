package progetto.progettokenkne;

import javafx.scene.control.Button;

//Classe che estende Button tale che in un button è possibile salvare la posizione (riga colonna) e leggerla. Utile per funzioni varie
public class Buttonc extends Button {
    private int riga,colonna;

    public Buttonc(String testo, int x, int y){
        super(testo);
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
