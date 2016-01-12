package com.twol.sigep.controller.teste;

import org.junit.Before;
import org.junit.Test;

import com.twol.sigep.controller.ControllerConfiguracao;

public class ControllerConfiguracaoTest {
	
	ControllerConfiguracao conf;
	
	@Before
	public void setup(){
		conf = new ControllerConfiguracao();
	}
	
	/*
	 * Configuracao
	 */
	@Test
	public void createConfiguracao() {
		//ControllerConfiguracao.putValor(PermissaoFuncionario.ALTERAR_CONFIGURACOES, true, TipoDeFuncionario.Supervisor);
		//assertEquals(ControllerConfiguracao.getValor(PermissaoFuncionario.ALTERAR_CONFIGURACOES, TipoDeFuncionario.Supervisor), true);
	}

}
