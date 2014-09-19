package com.twol.sigep.controller.teste;

import static org.junit.Assert.assertEquals;

import javax.persistence.NoResultException;

import org.junit.Before;
import org.junit.Test;

import com.twol.sigep.controller.ControllerPessoa;
import com.twol.sigep.controller.find.FindFuncionario;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.util.Persistencia;

public class ControllerFuncionarioTest {

	ControllerPessoa pe;
	String nome = "Fulano de tal";
	String senha = "123";

	@Before
	public void setup() {
		pe = new ControllerPessoa(Persistencia.emf);
		pe.removeAllFuncionarios();
	}

	private Funcionario iniciarFuncionarioInformacoesAleatorias(String nome) {
		int num = (int) (Math.random() * 100);
		Funcionario f = new Funcionario();
		f.setCpf("00000000" + num);
		f.setNome(nome);
		f.setSenha(senha);
		f.setLogin(nome);
		f.setTipoDeFuncionario(TipoDeFuncionario.Supervisor);
		f.setEndereco("rua da casa dele, sem numero, "+num);
		f.setTelefone(""+num*100);
		return f;
	}

	/*
	 * Pessoa
	 */
	@Test
	public void createFuncionarioTest() {
		Funcionario p = iniciarFuncionarioInformacoesAleatorias(nome);
		pe.create(p);
		assertEquals(FindFuncionario.funcionarioComNome(nome), p);
	}
	@Test
	public void editFuncionarioTest() throws EntidadeNaoExistenteException,
			Exception {
		createFuncionarioTest();
		Funcionario p = FindFuncionario.funcionarioComNome(nome);
		p.setNome("Funcionario Alterado");
		pe.edit(p);
		assertEquals(FindFuncionario.funcionarioComId(p.getId()).getNome(),
				p.getNome());
	}

	@Test(expected = NoResultException.class)
	public void destroyFuncionarioTest() throws EntidadeNaoExistenteException {
		createFuncionarioTest();
		Funcionario p = FindFuncionario.funcionarioComNome(nome);
		pe.destroy(p);
		FindFuncionario.funcionarioComNome(nome);
	}

	@Test
	public void removeAllFuncionarioTest() {
		for (int i = 0; i > 5; i++) {
			Funcionario p = iniciarFuncionarioInformacoesAleatorias(nome);
			pe.create(p);
		}
		pe.removeAllFuncionarios();
		assertEquals(pe.getQuantidadeFuncionarios(), 0);
	}

}
