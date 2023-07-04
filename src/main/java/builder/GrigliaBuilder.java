package progetto.builder;

import progetto.backtraking.Griglia;
import progetto.backtraking.Gruppo;
import progetto.backtraking.Punto;
import progetto.visitor.DocumentVisitor;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class GrigliaBuilder implements GrigliaBuilderIF {
    private int size;
    private LinkedList<Gruppo> gruppi = new LinkedList<>();
    public GrigliaBuilder(){}
    @Override
    public void createGriglia(String dimensione) {
        StringTokenizer st = new StringTokenizer(dimensione, " ");
        size = Integer.parseInt(st.nextToken());
    }

    @Override
    public void impostaGruppo() {
        Gruppo g1 = new Gruppo(new LinkedList<Punto>());
        gruppi.add(g1);
    }

    @Override
    public void impostaPunto(String rigaEColonna) {
        StringTokenizer st = new StringTokenizer(rigaEColonna, " ");
        Gruppo temp = gruppi.getLast();
        Punto punto = new Punto(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        temp.addPunti(punto);
    }

    @Override
    public void impostaVincoli(String numeroEDimensione) {
        StringTokenizer st = new StringTokenizer(numeroEDimensione, " ");
        Gruppo temp = gruppi.getLast();
        temp.setValue(Integer.parseInt(st.nextToken()));
        temp.setOperazione(st.nextToken());
    }

    public int getSize(){
        return size;
    }
    public LinkedList getGruppi(){
        return new LinkedList<Gruppo>(gruppi);
    }
}
