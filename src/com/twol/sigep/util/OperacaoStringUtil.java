package com.twol.sigep.util;

public class OperacaoStringUtil {
	
	public static final String MESSAGEM_LOGIN_INVALIDO = "Login inválido";
	public static final String FUNCIONARIO_JA_LOGADO = "Funcionario já logado";
	public static final String PARAMETROS_INVALIDOS = "Campos inválidos";
	public static final String LOGIN_REALIZADO = "Login realizado com sucesso";
	public static final String AREA_RESTRITA_APEAS_PARA_FUNCIONARIO_LOGADO = "Área restrita apenas à funcionarios logados no sistema";
	
	public static String formatarStringQuantidade(double quantidade){
		return (quantidade+"").replace(".", ",");
	}

	public static String formatarStringValorMoedaComDescricao(
			double valor) {
		return (valor+" ").replace(".", ",")+VariaveisDeConfiguracaoUtil.DESCRICAO_MOEDA;
	}
	
	
	
	
	
}
