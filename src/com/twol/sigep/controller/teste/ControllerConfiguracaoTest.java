package com.twol.sigep.controller.teste;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.twol.sigep.controller.ControllerConfiguracao;
import com.twol.sigep.model.configuracoes.PermissaoFuncionario;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.util.Persistencia;

public class ControllerConfiguracaoTest {
	
	ControllerConfiguracao conf;
	
	@Before
	public void setup(){
		conf = new ControllerConfiguracao(Persistencia.emf);
	}
	
	/*
	 * Configuracao
	 */
	@Test
	public void createConfiguracao() {
		ControllerConfiguracao.putValor(PermissaoFuncionario.ALTERAR_CONFIGURACOES, true, TipoDeFuncionario.Supervisor);
		assertEquals(ControllerConfiguracao.getValor(PermissaoFuncionario.ALTERAR_CONFIGURACOES, TipoDeFuncionario.Supervisor), true);
	}

}
