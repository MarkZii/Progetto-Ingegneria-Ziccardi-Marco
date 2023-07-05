package progetto.progettokenkne;

import backtraking.Griglia;
import backtraking.Gruppo;
import backtraking.Punto;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import visitor.DocumentVisitor;
import visitor.TextVisitor;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Random;

public class GrigliaController {
    public int size=0;
    @FXML private TextField dimensione, sceltaVal, caricaFile;
    @FXML private HBox inizializzazione;
    @FXML private Button confermaStruttura, esporta;
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
    private int contButton = 0;
    private boolean daFile = false;
    private LinkedList<TextFieldC> textFields = new LinkedList<>();

    private LinkedList<String> colori = new LinkedList<>();
    private Griglia sc;
    public void initialize() {}
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
                inizializzazione.setVisible(false);
                scecificheGruppo.setVisible(true);
                creazioneGrid();
                //caricamento delle righe e colonne nella griglia
                for (int j = 0; j < size; j++) {
                    griglia.addColumn(j);
                    griglia.addRow(j);
                }
            }
        }
    }
    private void creazioneGrid(){


        //gestore dei click dei pulsanti
        EventHandler<ActionEvent> buttonClickHandler = event -> {
            Buttonc clickedButton = (Buttonc) event.getSource();
            Punto punto = new Punto(clickedButton.getRiga(),clickedButton.getColonna());
            if(punti.isEmpty() || (!punti.contains(punto) && eAdiacente(punto))){
                button.add(clickedButton);
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
                Buttonc button = new Buttonc("premi",i,j);
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
            erroreSalva.setText("ERRORE, ricontrolla");
        }else {
            tuttiButton.addAll(button);
            System.out.println(tuttiButton.size());
            gruppi.add(gruppo);
            gruppo = new Gruppo();
            punti = new LinkedList<>();
            button = new LinkedList<>();
            erroreSalva.setText("");
            celleScelte.setText("");
            sceltaVal.setText("");
            sceltaOp.setText("scelta operazione");
            if(tuttiButton.size() == (size*size)) confermaStruttura.setVisible(true);
        }
    }
    //operazioni per eliminare l'ultima selezione
    @FXML
    private void cancellaOperazione(){
        for(Buttonc b: button) {
            b.setDisable(false);
        }
        gruppo = new Gruppo();
        punti = new LinkedList<>();
        button = new LinkedList<>();
        System.out.println(gruppi);
        System.out.println(punti);
        erroreSalva.setText("");
        celleScelte.setText("");
        sceltaVal.setText("");
        sceltaOp.setText("scelta operazione");
        System.out.println(tuttiButton.size());
    }
    @FXML
    private void resettaTutto(){
        confermaStruttura.setVisible(false);
        for(Buttonc b: tuttiButton) {
            b.setDisable(false);
        }
        tuttiButton = new LinkedList<>();
        gruppo = new Gruppo();
        punti = new LinkedList<>();
        gruppi = new LinkedList<>();
        System.out.println(gruppi);
        System.out.println(punti);
        erroreSalva.setText("");
        celleScelte.setText("");
        sceltaVal.setText("");
        sceltaOp.setText("scelta operazione");
        System.out.println(tuttiButton.size());
    }
    @FXML
    private void confermaStruttura(){
        if(daFile){
            caricaFile.setVisible(false);
            caricaDaFile();
        } else {
            sc = new Griglia(1, size, gruppi);
            disegnaStruttura();
        }
    }
    private void disegnaStruttura(){
        esporta.setDisable(false);
        gruppi = new LinkedList<>(sc.getGruppi());
        coloriCasuali();
        //aggiunta delle textfield nella griglia
        int i = 0;
        boolean ok = true;
        for (Gruppo g : gruppi) {
            String colore = colori.get(i);
            for (Punto p : g.getPunti()) {
                System.out.println("cjeigvjiefjgvbethnbui");
                TextFieldC text = new TextFieldC(p.getColonna(), p.getRiga());
                text.setStyle("-fx-border-color: #" + colore + ";-fx-border-width: 2px; -fx-background-color: #" + colore + ";-fx-pref-width: 35px;-fx-pref-height: 35px");
                Label label = new Label();
                VBox root = new VBox();
                textFields.add(text);
                if (ok) {
                    switch (g.getOperazione()) {
                        case "piu":
                            label.setText(g.getValue() + "+");
                            break;
                        case "meno":
                            label.setText(g.getValue() + "-");
                            break;
                        case "divisione":
                            label.setText(g.getValue() + "/");
                            break;
                        case "moltiplicazione":
                            label.setText(g.getValue() + "x");
                            break;
                    }
                    label.setStyle("-fx-text-fill: #" + colore + ";");
                }
                System.out.println(p.getColonna()+" "+p.getRiga());
                root.getChildren().addAll(label, text);
                griglia.add(root, p.getColonna(), p.getRiga());
                ok = false;
            }
            ok = true;
            i++;
        }
        istruzione.setText("La griglia è pronta, gioca");
        scecificheGruppo.setVisible(false);
        confermaStruttura.setVisible(false);
        esporta.setDisable(false);
    }
    private void caricaDaFile() {
        try {
            String nomeFile = caricaFile.getText();
            sc = new Griglia(1, nomeFile);
            size = sc.getSize();
            disegnaStruttura();

        }catch (Exception e){}

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
        int riga = punto.getRiga(), colonna = punto.getColonna();
        for(Punto p: punti){
            int rigDiff = Math.abs(riga - p.getRiga());
            int colDiff = Math.abs(colonna - p.getColonna());
            if(((rigDiff == 1 && colDiff == 0) || (rigDiff == 0 && colDiff == 1))) return true;
        }
        return false;
    }

    private void coloriCasuali() {
        Random random = new Random();
        for (int i = 0; i < gruppi.size(); i++) {
            int numero = random.nextInt(16777216);
            String hexNumero = Integer.toHexString(numero);
            while (hexNumero.length() < 6)
                hexNumero = "0" + hexNumero;
            colori.add(hexNumero);
        }
    }
    public void mostraSoluzione() {
        sc.risolvi();
        int[][] soluzione = sc.risultato();
        System.out.println(soluzione);
        int i=0;
        for(TextFieldC t: textFields){
            System.out.println("ciao "+soluzione[t.getRiga()][t.getColonna()]);
            t.setText(String.valueOf(soluzione[t.getRiga()][t.getColonna()]));
            i++;
        }
    }

    public void esportaGriglia(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("esporta-griglia.fxml"));
        Parent root = loader.load();
        EsportaController ec = loader.getController();
        ec.inizializzazione(sc);
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }
    public void verificaSoluzione(){

    }
    public void caricaGriglia() throws IOException {
        inizializzazione.setVisible(false);
        caricaFile.setVisible(true);
        confermaStruttura.setVisible(true);
        daFile = true;

    }
}
