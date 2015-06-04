package com.twol.sigep.model.configuracoes;

import com.twol.sigep.controller.ControllerConfiguracao;
import com.twol.sigep.model.exception.FuncionarioNaoAutorizadoException;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;

public class PermissaoFuncionario {
	
	
	
	public static final String ALTERAR_CONFIGURACOES = "ALTERAR_CONFIGURACOES";
	
	public static final String ALTERAR_FUNCIONARIO = "ALTERAR_FUNCIONARIO";
	public static final String ALTERAR_PRODUTO = "ALTERAR_PRODUTO";
	public static final String ALTERAR_CLIENTES = "ALTERAR_CLIENTES";
	
	public static final String CADASTRAR_FUNCIONARIO = "CADASTRAR_FUNCIONARIO";
	public static final String CADASTRAR_PRODUTO = "CADASTRAR_PRODUTO";
	public static final String CADASTRAR_CLIENTES = "CADASTRAR_CLIENTES";

	public static final String GERAR_RELATORIOS = "GERAR_RELATORIOS";
	
	
	public static boolean isAutorizado(Funcionario func, final String acao) throws FuncionarioNaoAutorizadoException{
		if(ControllerConfiguracao.getQuantidadeConfiguracoes() ==0){
			configuracoesDefalt();
		}
		
		if(ControllerConfiguracao.getValor(acao, func.getTipoDeFuncionario())){
			return true;
		}else{
			throw new FuncionarioNaoAutorizadoException("( "+acao+" )");
		}
	}
	
	public static boolean getValor(String acao, TipoDeFuncionario tipo){
		return ControllerConfiguracao.getValor(acao, tipo);
	}
	
	
	public static void putValor(String acao, boolean valor ,TipoDeFuncionario tipo)throws FuncionarioNaoAutorizadoException{
		ControllerConfiguracao.putValor(acao, valor, tipo);
	}
	
	
	public static void configuracoesDefalt(){
		ControllerConfiguracao.removeAllConfiguracoes();
		
		ControllerConfiguracao.putValor(ALTERAR_FUNCIONARIO, true, TipoDeFuncionario.Gerente);
		ControllerConfiguracao.putValor(ALTERAR_CONFIGURACOES, true, TipoDeFuncionario.Gerente);
		ControllerConfiguracao.putValor(CADASTRAR_FUNCIONARIO, true, TipoDeFuncionario.Gerente);
		
		
		ControllerConfiguracao.putValor(ALTERAR_CLIENTES, true, TipoDeFuncionario.Supervisor);
		ControllerConfiguracao.putValor(ALTERAR_CLIENTES, true, TipoDeFuncionario.Gerente);
		
		ControllerConfiguracao.putValor(ALTERAR_PRODUTO, true, TipoDeFuncionario.Supervisor);
		ControllerConfiguracao.putValor(ALTERAR_PRODUTO, true, TipoDeFuncionario.Gerente);
		
		
		ControllerConfiguracao.putValor(CADASTRAR_PRODUTO, true, TipoDeFuncionario.Supervisor);
		ControllerConfiguracao.putValor(CADASTRAR_PRODUTO, true, TipoDeFuncionario.Gerente);
		
		ControllerConfiguracao.putValor(CADASTRAR_CLIENTES, true, TipoDeFuncionario.Supervisor);
		ControllerConfiguracao.putValor(CADASTRAR_CLIENTES, true, TipoDeFuncionario.Gerente);
		
		ControllerConfiguracao.putValor(GERAR_RELATORIOS, true, TipoDeFuncionario.Supervisor);
		ControllerConfiguracao.putValor(GERAR_RELATORIOS, true, TipoDeFuncionario.Gerente);
		
	}
	
	
	
}
