package visitor;


import backtraking.Griglia;
import backtraking.Gruppo;
import backtraking.Punto;

import java.io.PrintWriter;

public class XmlExportVisitor implements Visitor {
    private PrintWriter pw;
    //private int size;
    //private LinkedList<Gruppo> gruppi = new LinkedList<>();
    public XmlExportVisitor(PrintWriter ps) {
        this.pw=ps;
    }
    @Override
    public void visit(Griglia griglia) {
        pw.println("<griglia>");
        pw.println("<dimensione>\n"+griglia.getSize()+"\n</dimensione>");
        for(Gruppo g: griglia.getGruppi()){
            g.accept(this);
        }
        pw.println("</griglia>");
    }

    @Override
    public void visit(Gruppo gruppo) {
        pw.println("<gruppo>");
        pw.println(gruppo.getValue()+" "+gruppo.getOperazione());
        for(Punto p: gruppo.getPunti()){
            p.accept(this);
        }
        pw.println("</gruppo>");
    }

    @Override
    public void visit(Punto punto) {
        pw.println("<punto>");
        pw.println("<riga>\n"+punto.getRiga()+"\n</riga>");
        pw.println("<colonna>\n"+punto.getColonna()+"\n</colonna>");
        pw.println("</punto>");
    }
}
