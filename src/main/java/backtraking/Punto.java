package backtraking;

import visitor.DocumentVisitor;

public final class Punto {
    private int riga;
    private int colonna;

    public Punto(int riga, int colonna) {
        this.riga = riga;
        this.colonna = colonna;
    }

    public void setRiga(int riga) {
        this.riga = riga;
    }

    public void setColonna(int colonna) {
        this.colonna = colonna;
    }

    public void rigaInc() {
        riga++;
    }

    public void colonnaInc() {
        colonna++;
    }

    public int getRiga() {
        return riga;
    }

    public int getColonna() {
        return colonna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Punto scelta = (Punto) o;
        return riga == scelta.riga && colonna == scelta.colonna;
    }

    public void rigaDec() {
        riga--;
    }
    public void colonnaDec() {
        colonna--;
    }

    public void accept(DocumentVisitor visitor){
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Punto{riga="+riga+", colonna="+colonna+'}';
    }
}
