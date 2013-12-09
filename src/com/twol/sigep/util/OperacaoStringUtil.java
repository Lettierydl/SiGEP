package com.twol.sigep.util;

public class OperacaoStringUtil {
	
	public static final String MESSAGEM_SENHA_INVALIDA = "Senha incorreta";
	public static final String FUNCIONARIO_JA_LOGADO = "Funcionario já logado";
	public static final String PARAMETROS_INVALIDOS = "Campos inválidos";
	public static final String LOGIN_REALIZADO = "Login realizado com sucesso";
	
	public static String formatarStringQuantidade(double quantidade){
		return (quantidade+"").replace(".", ",");
	}

	public static String formatarStringValorMoedaComDescricao(
			double valor) {
		return (valor+" ").replace(".", ",")+VariaveisDeConfiguracaoUtil.DESCRICAO_MOEDA;
	}
	
	
	
	
	
}
