package backtraking;

import visitor.TextVisitor;

import java.util.LinkedList;

public final class Gruppo {
    private LinkedList<Punto> punti;
    private int value;
    private String operazione;

    public Gruppo() {
        value=0;
        operazione=" ";
        punti= new LinkedList<>();
    }
    public Gruppo(int v, String o, LinkedList<Punto> p){
        value=v;
        operazione=o;
        punti= new LinkedList<>(p);
    }
    public Gruppo(LinkedList<Punto> p){
        punti= new LinkedList<>(p);
    }

    public void addPunti(Punto punto) {
        this.punti.add(punto);
    }

    public void setPunti(LinkedList<Punto> punti) {
        this.punti = punti;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setOperazione(String operazione) {
        this.operazione = operazione;
    }



    public int getValue() {
        return value;
    }

    public LinkedList<Punto> getPunti() {
        return punti;
    }

    public String getOperazione() {
        return operazione;
    }

    public void accept(TextVisitor visitor){
        visitor.visit(this);
    }
    @Override
    public String toString() {
        return "Gruppo{"+value+operazione+"  "+punti+"}";
    }
}
