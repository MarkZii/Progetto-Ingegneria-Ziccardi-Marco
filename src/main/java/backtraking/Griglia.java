package backtraking;

import builder.GrigliaBuilder;
import director.DocumentParser;
import director.TextParseException;
import visitor.DocumentVisitor;

import java.util.LinkedList;
import java.util.Scanner;

public class Griglia extends Problema<Punto, Integer> {
    private int[][] griglia;
    private int size;
    private Punto puntoS;
    private Punto puntoF;
    private LinkedList<Gruppo> gruppi = new LinkedList<>();
    public Griglia(int size, LinkedList<Gruppo> gruppi){
        super(1);
        this.size=size;
        this.gruppi = new LinkedList<>(gruppi);
        puntoS = new Punto(0,0);
        puntoF = new Punto((size-1),(size-1));
        griglia = new int[size][size];

        for(int i=0; i<size; i++)
            for(int j=0; j<size; j++)
                griglia[i][j] = 0;
    }
    public Griglia(int size, int numSol){
        super(numSol);
        try {
            leggiDaFile();
        }catch (Exception e){
            e.printStackTrace();
        }

        this.size=size;
        puntoS = new Punto(0,0);
        puntoF = new Punto((size-1),(size-1));
        griglia = new int[size][size];

        for(int i=0; i<size; i++)
            for(int j=0; j<size; j++)
                griglia[i][j] = 0;
/*
        Punto p1 = new Punto(0,0);
        Punto p2 = new Punto(0,1);
        LinkedList<Punto> l = new LinkedList<>();
        l.add(p1);
        l.add(p2);
        Gruppo g1 = new Gruppo(2, "divisione", l);

        Punto p3 = new Punto(0,2);
        Punto p4 = new Punto(1,2);
        LinkedList<Punto> l1 = new LinkedList<>();
        l1.add(p3);
        l1.add(p4);
        Gruppo g2 = new Gruppo(7, "piu", l1);

        Punto p5 = new Punto(1,0);
        Punto p6 = new Punto(2,0);
        LinkedList<Punto> l2 = new LinkedList<>();
        l2.add(p5);
        l2.add(p6);
        Gruppo g3 = new Gruppo(1, "meno", l2);

        Punto p7 = new Punto(1,1);
        Punto p8 = new Punto(2,1);
        LinkedList<Punto> l3 = new LinkedList<>();
        l3.add(p7);
        l3.add(p8);
        Gruppo g4 = new Gruppo(3, "meno", l3);

        Punto p9 = new Punto(0,3);
        LinkedList<Punto> l4 = new LinkedList<>();
        l4.add(p9);
        Gruppo g5 = new Gruppo(4, "piu", l4);

        Punto p10 = new Punto(1,3);
        Punto p11 = new Punto(2,3);
        LinkedList<Punto> l5 = new LinkedList<>();
        l5.add(p10);
        l5.add(p11);
        Gruppo g6 = new Gruppo(2, "meno", l5);

        Punto p12 = new Punto(3,0);
        Punto p13 = new Punto(3,1);
        LinkedList<Punto> l6 = new LinkedList<>();
        l6.add(p12);
        l6.add(p13);
        Gruppo g7 = new Gruppo(1, "meno", l6);

        Punto p14 = new Punto(2,2);
        Punto p15 = new Punto(3,2);
        Punto p16 = new Punto(3,3);
        LinkedList<Punto> l7 = new LinkedList<>();
        l7.add(p14);
        l7.add(p15);
        l7.add(p16);
        Gruppo g8 = new Gruppo(4, "moltiplicazione", l7);

        gruppi.add(g1);
        gruppi.add(g2);
        gruppi.add(g3);
        gruppi.add(g4);
        gruppi.add(g5);
        gruppi.add(g6);
        gruppi.add(g7);
        gruppi.add(g8);

*/
        scriviSoluzione(0);
    }

    private void leggiDaFile() throws TextParseException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il percorso del file:  ");
        String text = scanner.nextLine();

        GrigliaBuilder builder = new GrigliaBuilder();
        DocumentParser tp = new DocumentParser(builder, text);
        tp.build();

        size = builder.getSize();
        gruppi = new LinkedList<>(builder.getGruppi());

        System.out.println(gruppi);

    }

    @Override
    protected Punto primoPuntoDiScelta() {
        return new Punto(0,0);

    }

    @Override
    protected Punto prossimoPuntoDiScelta(Punto ps) {
        if(puntoS.getColonna() == size-1){
            puntoS.rigaInc();
            puntoS.setColonna(0);
        }else {
            puntoS.colonnaInc();
        }
        return puntoS;

    }

    @Override
    protected Punto ultimoPuntoDiScelta() {
        return puntoF;
    }

    @Override
    protected Integer primaScelta(Punto ps) {
        return griglia[ps.getRiga()][ps.getColonna()]+1;
    }

    @Override
    protected Integer prossimaScelta(Integer integer) {
        return integer+1;
    }

    @Override
    protected Integer ultimaScelta(Punto ps) {
        return size;
    }

    @Override
    protected boolean assegnabile(Integer scelta, Punto puntoDiScelta) {
        //Verifica se il numero è presente nella stessa riga o colonna
        for (int i = 0; i < size; i++) {
            if (griglia[puntoDiScelta.getRiga()][i] == scelta || griglia[i][puntoDiScelta.getColonna()] == scelta) {
                return false;
            }
        }
        //Verifca del fatto che rispetta i vincoli il gruppo
        boolean valore = true;
        assegna(scelta,puntoDiScelta);
        for(Gruppo g: gruppi){
            LinkedList<Punto> lista = g.getPunti();
            if(lista.contains(puntoDiScelta) && gruppoPieno(lista)){
                switch (g.getOperazione()){
                    case "piu":
                        valore = verificaSomma(lista, g.getValue());
                        break;
                    case "meno":
                        valore = verificaMeno(lista, g.getValue());
                        break;
                    case "divisione":
                        valore = verificaDivisione(lista, g.getValue());
                        break;
                    case "moltiplicazione":
                        valore = verificaMoltiplicazione(lista, g.getValue());
                        break;
                }
            }
        }
        deassegna(scelta,puntoDiScelta);
        return valore;
    }

    private boolean verificaSomma(LinkedList<Punto> lista, int value) {
        int sum = 0;
        for(Punto p: lista){
            sum += griglia[p.getRiga()][p.getColonna()];
        }
        return sum == value;
    }
    private boolean verificaMeno(LinkedList<Punto> lista, int value) {
        int sub = 0;
        for(Punto p: lista){
            if(sub<0)
                sub = (-1*sub) - griglia[p.getRiga()][p.getColonna()];
            else
                sub = sub- griglia[p.getRiga()][p.getColonna()];
        }
        if(sub<0)
            return -1*(sub) == value;
        else
            return sub == value;

    }
    private boolean verificaMoltiplicazione(LinkedList<Punto> lista, int value) {
        int prod = 1;
        for(Punto p: lista){
            prod *= griglia[p.getRiga()][p.getColonna()];
        }
        return prod == value;
    }
    public boolean verificaDivisione(LinkedList<Punto> lista, int value) {
        int div = 0;
        int i=0;
        for(Punto p: lista){
            if(i==0){
                div = griglia[p.getRiga()][p.getColonna()];
                i=1;
            }else {
                if (div / griglia[p.getRiga()][p.getColonna()] == 0)
                    div = griglia[p.getRiga()][p.getColonna()] / div;
                else
                    div = div / griglia[p.getRiga()][p.getColonna()];
            }
        }
        return div == value;
    }
    private boolean gruppoPieno(LinkedList<Punto> lista) {
        for(Punto p: lista){
            if(griglia[p.getRiga()][p.getColonna()] == 0){
                return false;
            }
        }
        return true;
    }

    @Override
    protected void assegna(Integer scelta, Punto puntoDiScelta) {
        griglia[puntoDiScelta.getRiga()][puntoDiScelta.getColonna()] = scelta;
    }

    @Override
    protected void deassegna(Integer scelta, Punto puntoDiScelta) {
        griglia[puntoDiScelta.getRiga()][puntoDiScelta.getColonna()] = 0;

    }

    @Override
    protected Punto precedentePuntoDiScelta(Punto puntoDiScelta) {
        if(puntoS.getColonna() == 0){
            puntoS.rigaDec();
            puntoS.setColonna(size-1);
        }else {
            puntoS.colonnaDec();
        }
        return puntoS;
    }

    @Override
    protected Integer ultimaSceltaAssegnataA(Punto puntoDiScelta) {
        return griglia[puntoDiScelta.getRiga()][puntoDiScelta.getColonna()];
    }

    @Override
    protected void scriviSoluzione(int nr_sol) {
        System.out.println("Soluzione nr " + nr_sol);
        for(int i=0; i<size; i++) {
            for (int j = 0; j < size; j++)
                System.out.print(griglia[i][j]);
            System.out.println();
        }
    }

    public int getSize() {
        return size;
    }

    public LinkedList<Gruppo> getGruppi() {
        return new LinkedList<>(gruppi);
    }

    //per implementare il memento sarà scrivi soluzione a introdurre un comando così che il command lo salva.
    public GrigliaMemento save(){
        return new GrigliaMemento(griglia);
    }
    public void restore(GrigliaMemento memento){
        for(int i=0; i< griglia.length; i++){
            for(int j=0; i< griglia.length; i++){
                this.griglia[i][j] = griglia[i][j];
            }
        }
    }

    public void accept(DocumentVisitor visitor){
        visitor.visit(this);
    }
}
