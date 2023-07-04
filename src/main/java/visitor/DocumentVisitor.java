package progetto.visitor;

import progetto.backtraking.Griglia;
import progetto.backtraking.Gruppo;
import progetto.backtraking.Punto;

public interface DocumentVisitor {
    void visit(Griglia griglia);

    void visit(Gruppo griglia);

    void visit(Punto griglia);
}
