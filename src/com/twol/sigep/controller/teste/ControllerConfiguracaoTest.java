package com.twol.sigep.controller.teste;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import javax.persistence.NoResultException;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;

import com.twol.sigep.controller.ControllerConfiguracao;
import com.twol.sigep.controller.ControllerPessoa;
import com.twol.sigep.controller.find.FindCliente;
import com.twol.sigep.model.configuracoes.PermissaoFuncionario;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.pessoas.Cliente;
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
		conf.putValor(PermissaoFuncionario.ALTERAR_CONFIGURACOES, true, TipoDeFuncionario.Supervisor);
		assertEquals(conf.getValor(PermissaoFuncionario.ALTERAR_CONFIGURACOES, TipoDeFuncionario.Supervisor), true);
	}

}
