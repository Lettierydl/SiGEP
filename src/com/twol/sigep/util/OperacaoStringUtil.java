package com.twol.sigep.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class OperacaoStringUtil {

	public static final String MESSAGEM_LOGIN_INVALIDO = "Login inválido";
	public static final String FUNCIONARIO_JA_LOGADO = "Funcionario já logado";
	public static final String PARAMETROS_INVALIDOS = "Campos inválidos";
	public static final String LOGIN_REALIZADO = "Login realizado com sucesso";
	public static final String AREA_RESTRITA_APEAS_PARA_FUNCIONARIO_LOGADO = "Área restrita apenas para funcionarios logados no sistema";

	public static final String DESCRICAO_DIVIDA_ANTIGO_SISTEMA = "Dívida do Antigo Sistema";

	
	public static String formatarStringQuantidade(double quantidade) {
		return (quantidade + "").replace(".", ",");
	}

	public static String formatarStringValorMoedaComDescricao(double valor) {
		return (valor + " ").replace(".", ",")
				+ VariaveisDeConfiguracaoUtil.DESCRICAO_MOEDA;
	}

	public static String formatarStringParaMascaraDeCep(String cep) {
		return cep == null || cep.isEmpty() ? "" : cep.substring(0, 5) + "-"
				+ cep.substring(5);
	}

	public static String formatarStringParaMascaraDeTelefone(String telefone) {
		return (telefone == null || telefone.isEmpty()) ? "" : "("
				+ telefone.substring(0, 2) + ")" + telefone.substring(2, 6)
				+ "-" + telefone.substring(6);
	}

	public static String retirarMascaraDeCPF(String cpf) {
		return cpf.replace(".", "").replace("-", "");
	}

	public static String retirarMascaraDeTelefone(String telefone) {
		return (telefone == null || telefone.isEmpty()) ? "" : telefone
				.replace("(", "").replace(")", "").replace("-", "");
	}

	public static String formatarStringParaMascaraDeCPF(String cpf) {
		return cpf == null || cpf.isEmpty() ? "" : cpf.substring(0, 3) + "."
				+ cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-"
				+ cpf.substring(9);
	}

	public static String criptografar(String string) {
		StringBuffer sb = new StringBuffer(string);
		sb.reverse();
		return sb.toString();
	}

	public static boolean validarSenhaMestre(String senha, boolean criptografar) {
		String senha_cript = senha;
		if (criptografar) {
			// criptografar md5 e colocar em senha_cript
			senha_cript = senha;
			if (null == senha)
				return false;
			try {
				// Create MessageDigest object for MD5
				MessageDigest digest = MessageDigest.getInstance("MD5");
				// Update input string in message digest
				digest.update(senha.getBytes(), 0, senha.length());
				// Converts message digest value in base 16 (hex)
				senha_cript = new BigInteger(1, digest.digest()).toString(16);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}

		return senha_cript.equals("4f02a2f9d2fd686bd865990e8f1838a3");
	}

}
