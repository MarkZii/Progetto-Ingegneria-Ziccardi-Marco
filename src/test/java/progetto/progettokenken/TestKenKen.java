package progetto.progettokenken;

import backtraking.Griglia;
import backtraking.Problema;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestKenKen {
    @Test
    public void risolviKenKenSingolaSoluzione(){
        try {
            int[] risultatoAtteso = new int[16];
            risultatoAtteso[0]=1;
            risultatoAtteso[1]=3;
            risultatoAtteso[2]=2;
            risultatoAtteso[3]=4;
            risultatoAtteso[4]=3;
            risultatoAtteso[5]=4;
            risultatoAtteso[6]=1;
            risultatoAtteso[7]=2;
            risultatoAtteso[8]=4;
            risultatoAtteso[9]=2;
            risultatoAtteso[10]=3;
            risultatoAtteso[11]=1;
            risultatoAtteso[12]=2;
            risultatoAtteso[13]=1;
            risultatoAtteso[14]=4;
            risultatoAtteso[15]=3;

            /*Per testare, bisogna passare in input al costruttore il file "griglia2.txt" che si trova nella cartella del progetto "java".
            E' stato utilizzato come input un file per evitare di costruirsi oggetti di tipo Gruppo.*/
            Griglia problema = new Griglia(1,"C:\\Users\\mzicc\\Documents\\Progettokenkne\\src\\main\\java\\UnicaSoluzione.txt");
            problema.risolvi();

            int[] risultatoOttenuto = new int[16];
            Integer[][] matrice = problema.risultati().getFirst();
            int k=0;
            for(int i=0; i<4; i++){
                for (int j=0; j<4; j++){
                    risultatoOttenuto[k]=matrice[i][j];
                    k++;
                }
            }

            assertArrayEquals(risultatoAtteso, risultatoOttenuto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void risolviKenKenMultipleSoluzioni(){
        try {
            int[] vettorePSoluzione = new int[9];
            vettorePSoluzione[0]=2;
            vettorePSoluzione[1]=3;
            vettorePSoluzione[2]=1;
            vettorePSoluzione[3]=1;
            vettorePSoluzione[4]=2;
            vettorePSoluzione[5]=3;
            vettorePSoluzione[6]=3;
            vettorePSoluzione[7]=1;
            vettorePSoluzione[8]=2;

            int[] vettoreSSoluzione = new int[9];
            vettoreSSoluzione[0]=3;
            vettoreSSoluzione[1]=1;
            vettoreSSoluzione[2]=2;
            vettoreSSoluzione[3]=1;
            vettoreSSoluzione[4]=2;
            vettoreSSoluzione[5]=3;
            vettoreSSoluzione[6]=2;
            vettoreSSoluzione[7]=3;
            vettoreSSoluzione[8]=1;

            /*Per testare il caso con 2 soluzioni, bisogna passare in input al costruttore il file "DueSoluzione.txt" che si trova nella cartella del progetto "java".
            E' stato utilizzato come input un file per evitare di costruirsi oggetti di tipo Gruppo.*/
            Griglia problema = new Griglia(2,"C:\\Users\\mzicc\\Documents\\Progettokenkne\\src\\main\\java\\DueSoluzioni.txt");
            problema.risolvi();

            //per testare se trova effettivamente due soluzioni itero due volte (perchè la configurazione passata ammette due soluzioni) e verifico con il metodo assertArrayEquals
            int[] risultatoOttenuto = new int[9];
            int z=0;
            do{
                Integer[][] matrice = problema.risultati().get(z);
                int k=0;
                for(int i=0; i<3; i++){
                    for (int j=0; j<3; j++){
                        risultatoOttenuto[k]=matrice[i][j];
                        k++;
                    }
                }
                if(z==0)
                    assertArrayEquals(vettorePSoluzione, risultatoOttenuto);
                else
                    assertArrayEquals(vettoreSSoluzione, risultatoOttenuto);
                z++;
            }while(z<2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
