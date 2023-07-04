package builder;

public interface GrigliaBuilderIF {

	void createGriglia(String dimensione);

	void impostaGruppo();

	void impostaPunto(String rigaEColonna);

	void impostaVincoli(String numeroEDimensione);

}