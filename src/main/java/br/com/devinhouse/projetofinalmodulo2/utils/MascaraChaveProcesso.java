package br.com.devinhouse.projetofinalmodulo2.utils;

public class MascaraChaveProcesso {
	// SGORGAOSETOR NUPROCESSO/NUANO ex: SOFT 1/2021
	public static String gerarChaveProcesso(Character sgOrgaoSetor, Integer nuProcesso, String nuAno) {
		return sgOrgaoSetor + " " + nuProcesso + "/" + nuAno;
	}
}
