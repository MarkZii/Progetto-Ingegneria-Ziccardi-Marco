package progetto.progettokenken;

import backtraking.Griglia;
import backtraking.Problema;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestKenKen {
    @Test
    public void risolviKenKenSingolaSoluzione(){
        try {
            Integer[][] risultatoAtteso = new Integer[4][4];
            risultatoAtteso[0][0]=1;
            risultatoAtteso[0][1]=3;
            risultatoAtteso[0][2]=2;
            risultatoAtteso[0][3]=4;
            risultatoAtteso[1][0]=3;
            risultatoAtteso[1][1]=4;
            risultatoAtteso[1][2]=1;
            risultatoAtteso[1][3]=2;
            risultatoAtteso[2][0]=4;
            risultatoAtteso[2][1]=2;
            risultatoAtteso[2][2]=3;
            risultatoAtteso[2][3]=1;
            risultatoAtteso[3][0]=2;
            risultatoAtteso[3][1]=1;
            risultatoAtteso[3][2]=4;
            risultatoAtteso[3][3]=3;

            /*Per testare, bisogna passare in input al costruttore il file "griglia2.txt" che si trova nella cartella del progetto "java".
            E' stato utilizzato come input un file per evitare di costruirsi oggetti di tipo Gruppo.*/
            Griglia problema = new Griglia(1,"C:\\Users\\mzicc\\Documents\\Progettokenkne\\src\\main\\java\\UnicaSoluzione.txt");
            problema.risolvi();

            Integer[][] matrice = problema.risultati().getFirst();

            assertArrayEquals(risultatoAtteso, matrice);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void risolviKenKenDueSoluzioni(){
        try {
            Integer[][] vettorePSoluzione = new Integer[3][3];
            vettorePSoluzione[0][0]=2;
            vettorePSoluzione[0][1]=3;
            vettorePSoluzione[0][2]=1;
            vettorePSoluzione[1][0]=1;
            vettorePSoluzione[1][1]=2;
            vettorePSoluzione[1][2]=3;
            vettorePSoluzione[2][0]=3;
            vettorePSoluzione[2][1]=1;
            vettorePSoluzione[2][2]=2;

            Integer[][] vettoreSSoluzione = new Integer[3][3];
            vettoreSSoluzione[0][0]=3;
            vettoreSSoluzione[0][1]=1;
            vettoreSSoluzione[0][2]=2;
            vettoreSSoluzione[1][0]=1;
            vettoreSSoluzione[1][1]=2;
            vettoreSSoluzione[1][2]=3;
            vettoreSSoluzione[2][0]=2;
            vettoreSSoluzione[2][1]=3;
            vettoreSSoluzione[2][2]=1;

            LinkedList<Integer[][]> soluzioni = new LinkedList<>();
            soluzioni.add(vettorePSoluzione);
            soluzioni.add(vettoreSSoluzione);

            /*Per testare il caso con 2 soluzioni, bisogna passare in input al costruttore il file "DueSoluzione.txt" che si trova nella cartella del progetto "java".
            E' stato utilizzato come input un file per evitare di costruirsi oggetti di tipo Gruppo.*/
            Griglia problema = new Griglia(2,"C:\\Users\\mzicc\\Documents\\Progettokenkne\\src\\main\\java\\DueSoluzioni.txt");
            problema.risolvi();

            //per testare se trova effettivamente due soluzioni itero due volte (perchè la configurazione passata ammette due soluzioni) e verifico con il metodo assertArrayEquals
            int z=0;
            do{
                Integer[][] matrice = problema.risultati().get(z);
                assertArrayEquals(soluzioni.get(z), matrice);
                z++;
            }while(z<2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void risolviKenKenMultipleSoluzioni(){
        try {
            int numSolAtteso = 12;

            /*Per testare il caso con 2 soluzioni, bisogna passare in input al costruttore il file "DueSoluzione.txt" che si trova nella cartella del progetto "java".
            E' stato utilizzato come input un file per evitare di costruirsi oggetti di tipo Gruppo.*/
            Griglia problema = new Griglia(12,"C:\\Users\\mzicc\\Documents\\Progettokenkne\\src\\main\\java\\multipleSoluzioni.txt");
            problema.risolvi();

            //per testare se trova effettivamente due soluzioni itero due volte (perchè la configurazione passata ammette due soluzioni) e verifico con il metodo assertArrayEquals

            int numSolOttenute = problema.risultati().size();
            assertEquals(numSolAtteso, numSolOttenute);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
