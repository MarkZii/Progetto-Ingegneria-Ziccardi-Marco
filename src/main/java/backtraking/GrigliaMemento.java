package progetto.backtraking;

public class GrigliaMemento {
    int[][] griglia;

    GrigliaMemento(int[][] griglia){
        this.griglia = new int[griglia.length][griglia.length];
        for(int i=0; i< griglia.length; i++){
            for(int j=0; i< griglia.length; i++){
                this.griglia[i][j] = griglia[i][j];
            }
        }
    }

    GrigliaMemento getGrigiaMemento(){
        return GrigliaMemento.this;
    }
}
