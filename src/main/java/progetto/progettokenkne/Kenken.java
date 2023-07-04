package progetto.progettokenkne;

import backtraking.Griglia;
import visitor.DocumentVisitor;
import visitor.TextVisitor;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Kenken {

    public static void main(String[] args) throws FileNotFoundException {
        Griglia sc = new Griglia(4,1);

        /*Scanner scanner = new Scanner(System.in);
        System.out.println("Inserisci nome file");
        String file = scanner.nextLine();

        PrintWriter pw = new PrintWriter(file);
        DocumentVisitor dv = new TextVisitor(pw);

        sc.accept(dv);*/

        sc.risolvi();
        //pw.close();
    }
}
