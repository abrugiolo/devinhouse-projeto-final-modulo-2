package br.com.devinhouse.projetofinalmodulo2.utils;

public class MascaraChaveProcesso {
	public static String gerarChaveProcesso(String sgOrgaoSetor, Integer nuProcesso, String nuAno) {
		return sgOrgaoSetor + " " + nuProcesso + "/" + nuAno;
	}
}
