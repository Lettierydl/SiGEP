package com.twol.sigep.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.persistence.NoResultException;

import org.junit.Before;
import org.junit.Test;

import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.pessoas.Endereco;
import com.twol.sigep.model.pessoas.FinderFuncionario;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.Telefone;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.model.pessoas.UF;
import com.twol.sigep.util.Persistencia;

public class ControllerFuncionarioTest {

	ControllerPessoa pe;
	String nome = "Fulano de tal";
	String senha = "123";

	@Before
	public void setup() {
		pe = new ControllerPessoa(Persistencia.emf);
		pe.removeAllTelefones();
		pe.removeAllFuncionarios();
		pe.removeAllEnderecos();
	}

	private Funcionario iniciarFuncionarioInformacoesAleatorias(String nome) {
		int num = (int) (Math.random() * 100);
		Funcionario f = new Funcionario();
		f.setCpf("00000000" + num);
		f.setNome(nome);
		f.setSenha(senha);
		f.setLogin(nome);
		f.setTipoDeFuncionario(TipoDeFuncionario.Supervisor);
		return f;
	}

	public Endereco criarEndereco() {
		int num = (int) (Math.random() * 10);
		Endereco e = new Endereco();
		e.setBairro("Bairro " + num);
		e.setCep("5800" + num);
		e.setCidade("Cidade " + num);
		e.setNumero("0" + num);
		e.setRua("Rua " + num);
		e.setUf(UF.PB);
		return e;
	}

	public Telefone criarTelefone() {
		int num = (int) (Math.random() * 10);
		Telefone t = new Telefone();
		t.setDdd(num % 99 + "");
		t.setTelefone(num + 9999 + "7" + num);
		t.setOperadora("Tim");
		return t;
	}

	/*
	 * Pessoa
	 */
	@Test
	public void createFuncionarioTest() {
		Funcionario p = iniciarFuncionarioInformacoesAleatorias(nome);
		pe.create(p);
		assertEquals(FinderFuncionario.funcionarioComNome(nome), p);
	}
	@Test
	public void editFuncionarioTest() throws EntidadeNaoExistenteException,
			Exception {
		createFuncionarioTest();
		Funcionario p = FinderFuncionario.funcionarioComNome(nome);
		p.setNome("Funcionario Alterado");
		pe.edit(p);
		assertEquals(FinderFuncionario.funcionarioComId(p.getId()).getNome(),
				p.getNome());
	}

	@Test(expected = NoResultException.class)
	public void destroyFuncionarioTest() throws EntidadeNaoExistenteException {
		createFuncionarioTest();
		Funcionario p = FinderFuncionario.funcionarioComNome(nome);
		pe.destroy(p);
		FinderFuncionario.funcionarioComNome(nome);
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

	/*
	 * / Relacionamento Funcionario Endereco
	 */
	@Test
	public void createFuncionarioComEnderecoTest() {
		Funcionario p = iniciarFuncionarioInformacoesAleatorias(nome);
		Endereco e = criarEndereco();
		p.setEndereco(e);
		pe.create(p);
		assertEquals(FinderFuncionario.funcionarioComNome(nome).getEndereco(), e);
	}

	@Test
	public void adicionarEnderecoAoFuncionarioTest()
			throws EntidadeNaoExistenteException, Exception {
		createFuncionarioTest();
		Funcionario p = FinderFuncionario.funcionarioComNome(nome);
		Endereco e = criarEndereco();
		p.setEndereco(e);
		pe.edit(p);
		assertEquals(FinderFuncionario.funcionarioComNome(nome).getEndereco(), e);
	}

	@Test
	public void alterarEnderecoDoFuncionarioTest()
			throws EntidadeNaoExistenteException, Exception {
		createFuncionarioComEnderecoTest();
		Funcionario p = FinderFuncionario.funcionarioComNome(nome);
		Endereco e = p.getEndereco();
		e.setUf(UF.BA);
		e.setBairro("Bairro Alterado");
		pe.edit(p);
		assertEquals(FinderFuncionario.funcionarioComNome(nome).getEndereco()
				.getBairro(), "Bairro Alterado");
	}

	@Test
	public void removerEnderecoDoFuncionarioTest()
			throws EntidadeNaoExistenteException, Exception {
		createFuncionarioComEnderecoTest();
		Funcionario p = FinderFuncionario.funcionarioComNome(nome);
		p.setEndereco(null);
		pe.edit(p);
		assertNull(FinderFuncionario.funcionarioComNome(nome).getEndereco());
	}

	@Test(expected = NoResultException.class)
	public void removerFuncionarioComEnderecoTest()
			throws EntidadeNaoExistenteException, Exception {
		createFuncionarioComEnderecoTest();
		Funcionario p = FinderFuncionario.funcionarioComNome(nome);
		Endereco e = p.getEndereco();
		pe.destroy(p);
		assertNull(Endereco.recuperarEnderecoId(e.getId()));
	}

	/*
	 * / Relacionamento Funcionario Telefone
	 */

	@Test
	public void createFuncionarioComTelefoneTest() {
		Funcionario p = iniciarFuncionarioInformacoesAleatorias(nome);
		Telefone t = criarTelefone();
		p.addTelefone(t);
		pe.create(p);
		assertEquals(FinderFuncionario.funcionarioComNome(nome).getTelefones().get(0),
				t);
	}

	@Test
	public void createFuncionarioComVariosTelefoneTest() {
		Funcionario p = iniciarFuncionarioInformacoesAleatorias(nome);
		int n = 5;
		for (int i = 0; i < n; i++) {
			Telefone t = criarTelefone();
			p.addTelefone(t);
		}
		pe.create(p);
		assertEquals(FinderFuncionario.funcionarioComNome(nome).getTelefones().size(),
				n);
	}

	@Test
	public void adicionarTelefoneAoFuncionarioTest()
			throws EntidadeNaoExistenteException, Exception {
		createFuncionarioTest();
		Funcionario p = FinderFuncionario.funcionarioComNome(nome);

		Telefone t = criarTelefone();
		p.addTelefone(t);
		pe.edit(p);
		assertEquals(FinderFuncionario.funcionarioComNome(nome).getTelefones().get(0),
				t);
	}

	@Test
	public void alterarTelefoneDoFuncionarioTest()
			throws EntidadeNaoExistenteException, Exception {
		createFuncionarioComTelefoneTest();
		Funcionario p = FinderFuncionario.funcionarioComNome(nome);
		Telefone t = p.getTelefones().get(0);
		t.setTelefone("ALTERADO");
		pe.edit(p);
		assertEquals(FinderFuncionario.funcionarioComNome(nome).getTelefones().get(0)
				.getTelefone(), "ALTERADO");
	}

	@Test
	public void removerTelefoneDoFuncionarioTest()
			throws EntidadeNaoExistenteException, Exception {
		createFuncionarioComTelefoneTest();
		Funcionario p = FinderFuncionario.funcionarioComNome(nome);
		Telefone t = p.getTelefones().get(0);
		p.removerTelefone(t);
		pe.edit(p);
		assertEquals(FinderFuncionario.funcionarioComNome(nome).getTelefones().size(),
				0);
	}

	@Test(expected = NoResultException.class)
	public void removerFuncionarioComTelefoneTest()
			throws EntidadeNaoExistenteException, Exception {
		createFuncionarioComTelefoneTest();
		Funcionario p = FinderFuncionario.funcionarioComNome(nome);
		Telefone e = p.getTelefones().get(0);
		pe.destroy(p);
		assertNull(Telefone.recuperarTelefoneId(e.getId()));
	}

}
