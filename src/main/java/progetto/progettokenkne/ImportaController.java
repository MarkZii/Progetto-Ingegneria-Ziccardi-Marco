package progetto.progettokenkne;

import backtraking.Griglia;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import visitor.DocumentVisitor;
import visitor.TextVisitor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ImportaController {
    @FXML
    private TextField nomeFile, nomePercorso;
    @FXML private Label errore;
    private Griglia griglia;
    @FXML
    public void esportaFile(){
        String nomeF = nomeFile.getText();
        String percorso = nomePercorso.getText();
        if(!nomeF.equals("") && !percorso.equals("")) {
            errore.setText("");
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(percorso + "\\" + nomeF);
            } catch (FileNotFoundException e) {}
            DocumentVisitor visitor = new TextVisitor(pw);
            griglia.accept(visitor);
            pw.close();
        }else{
            errore.setText("ERRORE, riempire entrambi i campi");
        }
    }
    public void inizializzazione(Griglia g){
        griglia=g;
    }

}
