package com.twol.sigep.util;

public class OperacaoStringUtil {
	
	public static final String MESSAGEM_LOGIN_INVALIDO = "Login inválido";
	public static final String FUNCIONARIO_JA_LOGADO = "Funcionario já logado";
	public static final String PARAMETROS_INVALIDOS = "Campos inválidos";
	public static final String LOGIN_REALIZADO = "Login realizado com sucesso";
	public static final String AREA_RESTRITA_APEAS_PARA_FUNCIONARIO_LOGADO = "Área restrita apenas para funcionarios logados no sistema";
	
	public static String formatarStringQuantidade(double quantidade){
		return (quantidade+"").replace(".", ",");
	}

	public static String formatarStringValorMoedaComDescricao(
			double valor) {
		return (valor+" ").replace(".", ",")+VariaveisDeConfiguracaoUtil.DESCRICAO_MOEDA;
	}
	
	public static String formatarStringParaMascaraDeCep(String cep){
		return cep ==null? "" :cep.substring(0, 5)+"-"+cep.substring(5);
	}
	
	public static String formatarStringParaMascaraDeTelefone(String telefone) {
		return telefone ==null ? "" : "("+ telefone.substring(0, 2) +")"+telefone.substring(2, 6)+"-"+telefone.substring(6);
	}
	
	public static String retirarMascaraDeCPF(String cpf){
		return cpf.replace(".", "").replace("-", "");
	}

	public static String retirarMascaraDeTelefone(String telefone) {
		return telefone.replace("(", "").replace(")", "").replace("-", "");
	}

	public static String formatarStringParaMascaraDeCPF(String cpf) {
		return cpf ==null? "" :cpf.substring(0, 3)+"."+cpf.substring(3, 6)+"."+cpf.substring(6, 9)+"-"+cpf.substring(9);
	}


	
	
}
