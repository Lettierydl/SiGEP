package com.twol.sigep.model.configuracoes;

public class ConfiguracaoSistema {
	
	private static int maxResult = 100;// max reult for consulta
	private static boolean limiteDeRegistro = false;
	
	
	public static int getMaxResult(){
		return maxResult;
	}
	
	public static boolean getLimiteDeRegistro(){
		return limiteDeRegistro;
	}
}
