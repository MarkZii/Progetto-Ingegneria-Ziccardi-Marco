package backtraking;

public class Memento {
    int[][] griglia;

    Memento(int[][] griglia){
        this.griglia = new int[griglia.length][griglia.length];
        for(int i=0; i< griglia.length; i++){
            for(int j=0; i< griglia.length; i++){
                this.griglia[i][j] = griglia[i][j];
            }
        }
    }

    Memento getGrigiaMemento(){
        return Memento.this;
    }
}
