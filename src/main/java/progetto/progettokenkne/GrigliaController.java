package progetto.progettokenkne;

import backtraking.Griglia;
import backtraking.Gruppo;
import backtraking.Punto;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

//classe che gestisce il foglio "kenken-view.fxml"
public class GrigliaController {
    public int size=0;
    @FXML private TextField dimensione, sceltaVal, caricaFile;
    @FXML private HBox inizializzazione, boxGriglia;
    @FXML private Button confermaStruttura, esporta, bottomDaFile, precedente, successiva, mostra, verifica;
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
        istruzione.setVisible(false);
    }

    //gestisce l'inserimento della dimensione della griglia
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
                    boxGriglia.setVisible(true);
                    istruzione.setVisible(true);
                    istruzione.setText("Specifica la configurazione di gioco");
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
        griglia.getChildren().clear();
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

    //operazioni per la gestione della eliminazione l'ultima selezione delgruppo
    @FXML
    private void cancellaOperazione(){
        for(Buttonc b: button)
            b.setDisable(false);
        gruppo = new Gruppo();
        punti = new LinkedList<>();
        button = new LinkedList<>();
        erroreSalva.setText("");
        celleScelte.setText("");
        sceltaVal.setText("");
        sceltaOp.setText("Scegli operazione");
    }

    //operazione per la gestione della eliminazione di tutti i gruppi selezionati
    @FXML
    private void resettaTutto(){
        confermaStruttura.setVisible(false);
        for(Buttonc b: tuttiButton)
            b.setDisable(false);
        tuttiButton = new LinkedList<>();
        gruppo = new Gruppo();
        punti = new LinkedList<>();
        gruppi = new LinkedList<>();
        erroreSalva.setText("");
        celleScelte.setText("");
        sceltaVal.setText("");
        sceltaOp.setText("Scegli operazione");
    }

    //operazioni per caricare la griglia configurata
    @FXML
    private void confermaStruttura() {
        try {
            verifica.setDisable(false);
            caricaFile.setVisible(true);
            if (daFile) { //l'if serve a capire se la griglia da capire bisogna prelevarla da file o da selezione
                caricaDaFile();
            } else {
                scecificheGruppo.setVisible(false);
                sc = new Griglia(1, size, gruppi);
                disegnaStruttura();
            }
            boxGriglia.setVisible(true);
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
        int i = 0;
        boolean ok = true;
        soluzione = new int[size][size];
        for (Gruppo g : gruppi) {
            String colore = colori.get(i);
            for (Punto p : g.getPunti()) {
                //aggiunta delle textfield nella griglia
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

                //listener per stare in ascolto di un inserimeno di valori nelle text field
                text.textProperty().addListener((observable, oldValue, newValue) -> {
                    try {
                        int val = Integer.parseInt(newValue);
                        if(val <= 0 || val > size) throw new Exception();
                        soluzione[text.getRiga()][text.getColonna()] = val;
                    }catch (Exception e){
                        istruzione.setText("ERRORE, solo numeri o nell'intervallo <1,"+size+">");
                        istruzione.setTextFill(Color.RED);
                    }
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
        sceltaOp.setText(somma.getText());
    }
    @FXML
    private void sceltaMoltiplicazione(){
        operazione = "moltiplicazione";
        sceltaOp.setText(prodotto.getText());
    }
    @FXML
    private void sceltaDivisione(){
        operazione = "divisione";
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

    //Prelevo n colori con n pari al numero dei gruppi per poter colorare i diversi gruppi della griglia
    public void coloriCasuali() {
        Random random = new Random();
        int r, g, b;
        for (int i = 0; i < gruppi.size(); i++){//genera size() colori casuali e poi verirfica che non sia scuro e gia presente
            double bianco;
            String esadecimale = null;
            do {
                r = random.nextInt(256);
                g = random.nextInt(256);
                b = random.nextInt(256);
                bianco = 100-(((double) (765 - (r+g+b)) / 765) * 100);  //mi permette di determinare la percentuale di bianco nel coloro e quindi evitare quelli troppo chiari e troppo scuri
                esadecimale = String.format("%02X%02X%02X", r, g, b); //conversione in esadecimale
            } while (!(bianco >= 35 && bianco <=65) && colori.contains(esadecimale));

            colori.add(esadecimale);
        }
    }

    public void mostraSoluzione() {
        int sol = 0;
        try {
            sc.setGriglia(new int[size][size]);
            if (!caricaFile.getText().equals("") && !(Integer.parseInt(caricaFile.getText())<=0)) {
                sol = Integer.parseInt(caricaFile.getText());
                sc.setNum_max_soluzioni(sol); //setto il numero di soluzioni da trovare
                sc.risolvi(); //invoco la risoluzione
                soluzioni = sc.risultati(); //prelevo tutte le soluzioni
                Integer[][] primo = soluzioni.get(numSol);

                //le mostro a video e se sono più di uno attivo i pulsanti
                for (TextFieldC t : textFields)
                    t.setText(String.valueOf(primo[t.getRiga()][t.getColonna()]));

                istruzione.setTextFill(Color.GREEN);
                if (soluzioni.size() == 1) {
                    istruzione.setText("Unica soluzione");
                } else {
                    if(sol > soluzioni.size())
                        istruzione.setText("Non ci sono "+sol+" soluzioni. Soluzione numero: " + (numSol+1));
                    else
                        istruzione.setText("Soluzione numero: " + (numSol+1));

                    successiva.setDisable(false);
                }
                numSol++;
                mostra.setDisable(true);
                verifica.setDisable(true);
                caricaFile.setVisible(false);
            } else {
                istruzione.setText("Inserire numero (positivo) di soluzioni da trovare!!");
            }
        }catch (NumberFormatException e){
            istruzione.setText("ERRORE: inserire solo numeri");
            istruzione.setTextFill(Color.RED);
        }catch (Exception e){
            istruzione.setText("Non ci sono soluzioni");
            istruzione.setTextFill(Color.RED);
        }
    }
    @FXML
    public void successivaSoluzione(){
        precedente.setDisable(false);
        Integer[][] primo = soluzioni.get(numSol);
        for(TextFieldC t: textFields){
            t.setText(String.valueOf(primo[t.getRiga()][t.getColonna()]));
        }
        istruzione.setText("Soluzione numero: "+(numSol+1));
        if(numSol == (soluzioni.size()-1)){
            successiva.setDisable(true);
            numSol--;
            return;
        }else{
            istruzione.setText("Soluzione numero: "+(numSol+1));
        }
        numSol++;
    }
    public void precedenteSoluzione(){
        successiva.setDisable(false);
        Integer[][] primo = soluzioni.get(numSol);
        for(TextFieldC t: textFields){
            t.setText(String.valueOf(primo[t.getRiga()][t.getColonna()]));
        }
        istruzione.setText("Soluzione numero: "+(numSol+1));
        if(numSol == 0){
            precedente.setDisable(true);
            numSol++;
            return;
        }
        numSol--;
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
            for (int j = 0; j < size; j++){
                if(soluzione[i][j] != 0){
                    Punto p = new Punto(i,j);
                    if(!sc.assegnabile(soluzione[i][j], p)) {
                        istruzione.setTextFill(Color.RED);
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
        istruzione.setTextFill(Color.GREEN);
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

    @FXML
    public void ricominciaDaCapo(){
        System.out.println("ciadoajsoff");
        sc = null;
        gruppi = new LinkedList<>();;
        punti = new LinkedList<>();;
        button = new LinkedList<>();;
        tuttiButton = new LinkedList<>();;
        daFile = false;
        textFields = new LinkedList<>();;
        colori = new LinkedList<>();;
        inizializzazione.setVisible(true);
        boxGriglia.setVisible(false);
        esporta.setDisable(true);
        verifica.setDisable(true);
        mostra.setDisable(true);
        successiva.setDisable(true);
        precedente.setDisable(true);
        istruzione.setVisible(false);
        caricaFile.setVisible(false);
        bottomDaFile.setDisable(false);
        istruzione.setTextFill(Color.BLACK);
        celleScelte.setText("");
        sceltaVal.setText("");
    }
}
