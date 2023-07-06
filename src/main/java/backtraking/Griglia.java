package backtraking;

import builder.GrigliaBuilder;
import director.DocumentParser;
import director.TextParseException;
import visitor.DocumentVisitor;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

public final class Griglia extends Problema<Punto, Integer> {
    private int[][] griglia;
    private final int size;
    private final Punto puntoS;
    private final Punto puntoF;
    private final LinkedList<Gruppo> gruppi = new LinkedList<>();

    private LinkedList<Integer[][]> soluzioni = new LinkedList<>();

    public Griglia(int numSol, int size, LinkedList<Gruppo> gruppi) {
        super(numSol);
        this.size = size;
        for(Gruppo g: gruppi){
            LinkedList<Punto> punti = new LinkedList<>();
            for(Punto p: g.getPunti()){
                punti.add(new Punto(p.getRiga(), p.getColonna()));
            }
            Gruppo ng = new Gruppo(g.getValue(),g.getOperazione(),punti);
            this.gruppi.add(ng);
        }
        puntoS = new Punto(0,0);
        puntoF = new Punto((size-1),(size-1));
        griglia = new int[size][size];
    }



    public Griglia(int numSol, String file) throws Exception {
        super(numSol);
        System.out.println("stran1o");
        GrigliaBuilder builder = new GrigliaBuilder();
        DocumentParser tp = new DocumentParser(builder, file);
        System.out.println("strano2");
        tp.build();
        System.out.println("strano3");
        size = builder.getSize();
        LinkedList<Gruppo> c = builder.getGruppi();
        System.out.println("ciao" +c);
        for(Gruppo g: c){
            LinkedList<Punto> punti = new LinkedList<>();
            for(Punto p: g.getPunti()){
                punti.add(new Punto(p.getRiga(), p.getColonna()));
            }
            Gruppo ng = new Gruppo(g.getValue(),g.getOperazione(),punti);
            this.gruppi.add(ng);
        }
        puntoS = new Punto(0,0);
        puntoF = new Punto((size-1),(size-1));
        griglia = new int[size][size];
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
        Integer[][] copia = new Integer[size][size];
        for(int i=0; i<size; i++)
            for (int j = 0; j < size; j++)
                copia[i][j]=griglia[i][j];
        soluzioni.add(copia);
    }

    public int getSize() {
        return size;
    }

    public LinkedList<Gruppo> getGruppi() {
        LinkedList<Gruppo> ret = new LinkedList<>();
        for(Gruppo g: gruppi){
            LinkedList<Punto> punti = new LinkedList<>();
            for(Punto p: g.getPunti()){
                punti.add(new Punto(p.getRiga(), p.getColonna()));
            }
            Gruppo ng = new Gruppo(g.getValue(),g.getOperazione(),punti);
            ret.add(ng);
        }
        return ret;
    }

    public LinkedList<Integer[][]> risultati(){
        return soluzioni;
    }

    public void accept(DocumentVisitor visitor){
        visitor.visit(this);
    }
}
