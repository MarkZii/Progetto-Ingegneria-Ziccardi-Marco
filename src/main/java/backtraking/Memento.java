package backtraking;

public class Memento {
    int[][] griglia;

    Memento(int[][] griglia){
        int size = griglia.length;
        this.griglia = new int[size][size];
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                this.griglia[i][j] = griglia[i][j];
            }
        }
    }

    Memento getGrigiaMemento(){
        return Memento.this;
    }
}
