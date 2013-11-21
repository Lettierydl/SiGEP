package com.twol.sigep.util;

public class OperacaoStringUtil {
	
	public static String formatarStringQuantidade(double quantidade){
		return (quantidade+"").replace(".", ",");
	}

	public static String formatarStringValorMoedaComDescricao(
			double valor) {
		return (valor+" ").replace(".", ",")+VariaveisDeConfiguracaoUtil.DESCRICAO_MOEDA;
	}
	
	
	
	
	
}
