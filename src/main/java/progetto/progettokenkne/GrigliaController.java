package progetto.progettokenkne;

import backtraking.Griglia;
import backtraking.Gruppo;
import backtraking.Punto;
import director.TextParseException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class GrigliaController {
    public int size=0;
    @FXML private TextField dimensione, sceltaVal, caricaFile;
    @FXML private HBox inizializzazione;
    @FXML private Button confermaStruttura, esporta, bottomDaFile, precedente, successiva, mostra;
    @FXML private GridPane griglia;
    @FXML private Label istruzione, celleScelte, textError, erroreSalva;
    @FXML private VBox scecificheGruppo;
    @FXML private MenuButton sceltaOp;
    @FXML private MenuItem somma, meno, prodotto, divisione;
    private Gruppo gruppo;
    private LinkedList<Gruppo> gruppi = new LinkedList<>();
    private LinkedList<Punto> punti = new LinkedList<>();
    private LinkedList<Buttonc> button = new LinkedList<>();
    private LinkedList<Buttonc> tuttiButton = new LinkedList<>();
    private boolean daFile = false;
    private LinkedList<TextFieldC> textFields = new LinkedList<>();
    private LinkedList<String> colori = new LinkedList<>();
    private Griglia sc;
    private String operazione;
    private int[][] soluzione;
    private LinkedList<Integer[][]> soluzioni;
    int numSol = 0;

    public void initialize() {
        istruzione.setVisible(false);}
    @FXML
    protected void grigliaButtonClick() {
        if(dimensione.getText().equals("")){
            textError.setText("ERRORE: campo vuoto. Reinserire");
        }else {
            try {
                size = Integer.parseInt(dimensione.getText());
                if (size < 3 || size > 9) {
                    textError.setText("ERRORE: valori errati. Reinserire");
                } else {
                    istruzione.setVisible(true);
                    inizializzazione.setVisible(false);
                    scecificheGruppo.setVisible(true);
                    creazioneGrid();
                }
            }catch (NumberFormatException e){
                textError.setText("ERRORE: solo numeri. Reinserire");
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
        try{
            int valore = Integer.parseInt(sceltaVal.getText());
            if(sceltaOp.getText().equals("Scegli operazione") || valore<=0 || punti.isEmpty()){
                erroreSalva.setText("ERRORE, ricontrolla i campi");
            }else {
                //gruppo.setPunti(punti);
                //gruppo.setValue(valore);
                tuttiButton.addAll(button);
                gruppo = new Gruppo(valore, operazione, punti);
                gruppi.add(gruppo);
                punti = new LinkedList<>();
                button = new LinkedList<>();
                erroreSalva.setText("");
                celleScelte.setText("");
                sceltaVal.setText("");
                sceltaOp.setText("Scegli operazione");
                if(tuttiButton.size() == (size*size)) confermaStruttura.setVisible(true);
            }
        }catch (NumberFormatException e){
            erroreSalva.setText("ERRORE: solo numeri. Reinserire");
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
        sceltaOp.setText("Scegli operazione");
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
        sceltaOp.setText("Scegli operazione");
        System.out.println(tuttiButton.size());
    }
    @FXML
    private void confermaStruttura() {
        try {
            if (daFile) {
                caricaDaFile();
            } else {
                scecificheGruppo.setVisible(false);
                sc = new Griglia(1, size, gruppi);
                disegnaStruttura();
            }
            mostra.setDisable(false);
            istruzione.setText("La griglia è pronta, gioca");
            confermaStruttura.setVisible(false);
            esporta.setDisable(false);
            bottomDaFile.setDisable(true);
            caricaFile.setText("");
            caricaFile.setPromptText("Inserire numero di soluzioni che si vuole avere");
        }catch (Exception e){
            istruzione.setText("ERRORE: file non trovato o formato errato o errore nei TAG");
        }
    }
    private void disegnaStruttura(){
        esporta.setDisable(false);
        griglia.getChildren().clear();
        coloriCasuali();
        //aggiunta delle textfield nella griglia
        int i = 0;
        boolean ok = true;
        soluzione = new int[size][size];
        for(int k=0; k<size; k++) {
            for (int j = 0; j < size; j++)
                System.out.print(soluzione[k][j]);
            System.out.println();
        }
        for (Gruppo g : gruppi) {
            System.out.println(colori);
            String colore = colori.get(i);
            for (Punto p : g.getPunti()) {
                TextFieldC text = new TextFieldC(p.getColonna(), p.getRiga());
                text.setStyle("-fx-border-color: #" + colore + "; -fx-background-color: #" + colore + ";-fx-pref-width: 35px;-fx-pref-height: 30px");
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
                root.getChildren().addAll(label, text);
                griglia.add(root, p.getColonna(), p.getRiga());
                ok = false;
                text.textProperty().addListener((observable, oldValue, newValue) -> {
                    soluzione[text.getRiga()][text.getColonna()] = Integer.parseInt(newValue);
                    System.out.println("TextField " + ": " + newValue);
                });
            }
            ok = true;
            i++;
        }
    }
    private void caricaDaFile() throws Exception { ///MEMENTOOOO
            String nomeFile = caricaFile.getText();
            sc = new Griglia(1, nomeFile);
            size = sc.getSize();
            gruppi = new LinkedList<>(sc.getGruppi());
            disegnaStruttura();
    }

    //classi per prelevare il valore della operazione
    @FXML
    private void sceltaMeno(){
        operazione = "meno";
        sceltaOp.setText(meno.getText());
    }
    @FXML
    private void sceltaSomma(){
        operazione = "piu";
        //gruppo.setOperazione("piu");
        sceltaOp.setText(somma.getText());
    }
    @FXML
    private void sceltaMoltiplicazione(){
        operazione = "moltiplicazione";
        //gruppo.setOperazione("moltiplicazione");
        sceltaOp.setText(prodotto.getText());
    }
    @FXML
    private void sceltaDivisione(){
        operazione = "divisione";
        //gruppo.setOperazione("divisione");
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

    public void coloriCasuali() {
        Random random = new Random();
        int r, g, b;
        for (int i = 0; i < gruppi.size(); i++){//genera size() coloi casuali e poi verirfica che non sia scuro
            do {
                r = random.nextInt(256);
                g = random.nextInt(256);
                b = random.nextInt(256);
            } while (eTropppoScuro(r, g, b));
            String esadecimale = String.format("%02X%02X%02X", r, g, b); //conversione in esadecimale
            colori.add(esadecimale);
        }
    }
    private  boolean eTropppoScuro(int r, int g, int b) {
        double lum = (0.299 * r + 0.587 * g + 0.114 * b) / 255; //calcolo la luminosità dei colori
        System.out.println(lum + " " +(lum < 0.5 && lum >0.2));
        return lum < 0.5 && lum >0.2;//verifico che la luminosità è inferiore a un valore
    }

    public void mostraSoluzione() {
        try {
            sc.setGriglia(new int[size][size]);
            if (!caricaFile.getText().equals("") && !(Integer.parseInt(caricaFile.getText())<=0)) {
                sc.setNum_max_soluzioni(Integer.parseInt(caricaFile.getText()));
                sc.risolvi();
                soluzioni = sc.risultati();
                Integer[][] primo = soluzioni.get(numSol);
                for (TextFieldC t : textFields) {
                    t.setText(String.valueOf(primo[t.getRiga()][t.getColonna()]));
                }
                if (soluzioni.size() == 1) {
                    istruzione.setText("Unica soluzione");
                } else {
                    istruzione.setText("Soluzione numero: " + numSol);
                    successiva.setDisable(false);
                }
                numSol++;
                mostra.setDisable(true);
            } else {
                istruzione.setText("Inserire numero (positivo) di soluzioni da trovare!!");
            }
        }catch (NumberFormatException e){
            istruzione.setText("ERRORE: inserire solo numeri");
        }
    }
    @FXML public void successivaSoluzione(){
        precedente.setDisable(false);
        Integer[][] primo = soluzioni.get(numSol);
        for(TextFieldC t: textFields){
            t.setText(String.valueOf(primo[t.getRiga()][t.getColonna()]));
        }
        istruzione.setText("Soluzione numero: "+numSol);
        if(numSol == soluzioni.size()){
            successiva.setDisable(true);
            return;
        }
        numSol++;
    }
    public void precedenteSoluzione(){
        successiva.setDisable(false);
        Integer[][] primo = soluzioni.get(numSol);
        for(TextFieldC t: textFields){
            t.setText(String.valueOf(primo[t.getRiga()][t.getColonna()]));
        }
        istruzione.setText("Soluzione numero: "+numSol);
        numSol--;
        if(numSol == 00) precedente.setDisable(true);

    }

    public void esportaGriglia() throws IOException {//MEMENTO
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
        sc.setGriglia(new int[size][size]);
        boolean alcuniNull = false;
        for(int i=0; i<size; i++) {
            for (int j = 0; j < size; j++)
                System.out.print(soluzione[i][j]);
            System.out.println();
        }
        for(int i=0; i<size; i++) {
            for (int j = 0; j < size; j++){
                if(soluzione[i][j] != 0){
                    Punto p = new Punto(i,j);
                    if(!sc.assegnabile(soluzione[i][j], p)) {
                        System.out.println("ciaooo");
                        istruzione.setText("Non tutti i valori inseriti sono corretti. Reinserire");
                        return;
                    }else{
                        sc.assegna(soluzione[i][j], p);
                    }
                }else{
                    alcuniNull = true;
                }
            }
        }
        if(!alcuniNull)
            istruzione.setText("HAI TROVATO LA SOLUZIONE, complimenti");
        else
            istruzione.setText("Valori corretti");
    }
    public void caricaGriglia() {
        scecificheGruppo.setVisible(false);
        istruzione.setVisible(true);
        istruzione.setText("");
        inizializzazione.setVisible(false);
        caricaFile.setVisible(true);
        confermaStruttura.setVisible(true);
        daFile = true;
    }
}
