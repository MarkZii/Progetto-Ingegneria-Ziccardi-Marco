package visitor;

import backtraking.Griglia;
import backtraking.Gruppo;
import backtraking.Punto;

public interface Visitor {
    void visit(Griglia griglia);
    void visit(Gruppo griglia);
    void visit(Punto griglia);
}
