/**
 * 
 */
package br.edu.unitri.model;

/**
 * @author Marcos
 *
 */
public enum TipoDependente {

	MAE("M�e"), PAI("Pai"), FILHO("Filho"), FILHA("Filha"), NETO("Neto"), NETA(
			"Neta"), IRMAO("Irm�o"), IRMA("Irm�"), CONJUGE("Conjuge"), AVO(
			"Avos");

	public final String opcao;

	TipoDependente(String opcao) {
		this.opcao = opcao;
	}

	public String toString() {
		return opcao;
	}

}
