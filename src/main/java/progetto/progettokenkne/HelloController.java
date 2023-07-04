package progetto.progettokenkne;

import backtraking.Griglia;
import backtraking.Gruppo;
import backtraking.Punto;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.LinkedList;

public class HelloController {
    public int size=0;
    @FXML private TextField dimensione, sceltaVal;
    @FXML private HBox hBox;
    @FXML private Button carica, salvaGruppo, cancella, confermaStruttura;
    @FXML private GridPane griglia;
    @FXML private Label istruzione, celleScelte, textError, erroreSalva;
    @FXML private VBox scecificheGruppo;
    @FXML private MenuButton sceltaOp;
    @FXML private MenuItem somma, meno, prodotto, divisione;
    private Gruppo gruppo = new Gruppo();
    private LinkedList<Gruppo> gruppi = new LinkedList<>();
    private LinkedList<Punto> punti = new LinkedList<>();
    private LinkedList<Buttonc> button = new LinkedList<>();
    private LinkedList<Buttonc> tuttiButton = new LinkedList<>();
    public void initialize() {
        confermaStruttura.setVisible(false);
        istruzione.setVisible(false);
        scecificheGruppo.setVisible(false);
    }
    @FXML
    protected void grigliaButtonClick() throws IOException {
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("griglia-window.fxml"));
        if(dimensione.getText().equals("")){
            textError.setText("ERRORE: campo vuoto. Reinserire");
        }else {
            size = Integer.parseInt(dimensione.getText());
            if (size < 3 || size > 9) {
                textError.setText("ERRORE: valori errati. Reinserire");
            } else {
                textError.setText("");
                //root = loader.load();
                /*Scene scene = new Scene(loader.load(), 500 , 500);
                stage.setTitle("KenkenGriglia");
                stage.setScene(scene);
                stage.show();*/
                istruzione.setVisible(true);
                hBox.setDisable(true);
                carica.setDisable(true);
                scecificheGruppo.setVisible(true);
                creazioneGrid();
            }
        }
    }
    private void creazioneGrid(){
        //caricamento delle righe e colonne nella griglia
        for (int j = 0; j < size; j++) {griglia.addColumn(j);}
        for (int i = 0; i < size; i++) {griglia.addRow(i);}


        //gestore dei click dei pulsanti
        EventHandler<ActionEvent> buttonClickHandler = event -> {
            Buttonc clickedButton = (Buttonc) event.getSource();
            Punto punto = new Punto(clickedButton.getRiga(),clickedButton.getColonna());
            if(punti.isEmpty() || (!punti.contains(punto) && eAdiacente(punto))){
                button.add(clickedButton);
                tuttiButton.add(clickedButton);
                punti.add(punto);
                clickedButton.setDisable(true);
                String testo = celleScelte.getText();
                testo = testo+"\ncella: "+clickedButton.getRiga()+" "+clickedButton.getColonna();
                celleScelte.setText(testo);
            }
        };

        //aggiunta dei nodi nella griglia
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Buttonc button = new Buttonc(i,j); // Sostituisci con il nodo che desideri aggiungere
                button.setText("premi");
                button.setOnAction(buttonClickHandler);
                griglia.add(button, i, j);
            }
        }
    }

    //Operazioni per salvare il gruppo selezionato
    @FXML
    private void salvaGruppo(){
        int valore = Integer.parseInt(sceltaVal.getText());
        gruppo.setValue(valore);
        gruppo.setPunti(punti);
        if(gruppo.getOperazione().equals(" ") || gruppo.getValue()<=0 || gruppo.getPunti().isEmpty()){
            System.out.println((gruppo.getOperazione().equals(""))+" "+(gruppo.getValue()<=0)+" "+(gruppo.getPunti().isEmpty()));
            erroreSalva.setText("ERRORE, ricontrolla");System.out.println("ERRORE");
        }else {
            erroreSalva.setText("");
            System.out.println(gruppo);
            gruppi.add(gruppo);
            gruppo = new Gruppo();
            punti = new LinkedList<>();
            System.out.println(gruppo);
            celleScelte.setText("");
            sceltaVal.setText("");
            sceltaOp.setText("Scelta operazione");

            button = new LinkedList<>();
            if(tuttiButton.size() == (size)*(size)) confermaStruttura.setVisible(true);
        }
    }
    //operazioni per eliminare l'ultima selezione
    @FXML
    private void cancellaOperazione(){
        for(Buttonc b: button){
            b.setDisable(false);
        }
        erroreSalva.setText("");
        gruppo = new Gruppo();
        punti = new LinkedList<>();
        System.out.println(gruppo);
        celleScelte.setText("");
        sceltaVal.setText("");
        sceltaOp.setText("Scelta operazione");
    }
    @FXML
    private void resettaTutto(){
        confermaStruttura.setVisible(false);
        for(Buttonc b: tuttiButton){
            b.setDisable(false);
        }
        erroreSalva.setText("");
        gruppo = new Gruppo();
        punti = new LinkedList<>();
        gruppi = new LinkedList<>();
        System.out.println(gruppo);
        celleScelte.setText("");
        sceltaVal.setText("");
        sceltaOp.setText("Scelta operazione");
    }
    @FXML
    private void confermaStruttura(){
        System.out.println(gruppi);
        Griglia sc = new Griglia(size,gruppi);
        sc.risolvi();
    }
    //classi per prelevare il valore della operazione
    @FXML
    private void sceltaMeno(){
        gruppo.setOperazione("meno");
        sceltaOp.setText(meno.getText());
    }
    @FXML
    private void sceltaSomma(){
        gruppo.setOperazione("piu");
        sceltaOp.setText(somma.getText());
    }
    @FXML
    private void sceltaMoltiplicazione(){
        gruppo.setOperazione("moltiplicazione");
        sceltaOp.setText(prodotto.getText());
    }
    @FXML
    private void sceltaDivisione(){
        gruppo.setOperazione("divisione");
        sceltaOp.setText(divisione.getText());
    }

    //controllo se la cella è adiacente alle altre
    private boolean eAdiacente(Punto punto) {
        for(Punto p: punti){
            int rigDiff = Math.abs(punto.getRiga() - p.getRiga());
            int colDiff = Math.abs(punto.getColonna() - p.getColonna());
            System.out.println(((rigDiff == 1 && colDiff == 0) || (rigDiff == 0 && colDiff == 1)));
            if(((rigDiff == 1 && colDiff == 0) || (rigDiff == 0 && colDiff == 1))) return true;
        }
        return false;
    }
}