package com.twol.sigep.controller.teste;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Calendar;

import javax.persistence.NoResultException;

import org.junit.Before;
import org.junit.Test;

import com.twol.sigep.controller.ControllerPessoa;
import com.twol.sigep.controller.find.FindCliente;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Endereco;
import com.twol.sigep.model.pessoas.Telefone;
import com.twol.sigep.model.pessoas.UF;
import com.twol.sigep.util.Persistencia;

public class ControllerClienteTest {
	
	ControllerPessoa pe;
	String nome = "Fulano de tal";
	
	@Before
	public void setup(){
		pe = new ControllerPessoa(Persistencia.emf);
		pe.removeAllTelefones();
		pe.removeAllClientes();
		pe.removeAllEnderecos();
	}

	private Cliente iniciarClienteInformacoesAleatorias(String nome) {
		int num = (int) (Math.random() * 100);
		Cliente c = new Cliente();
		c.setCpf("00000000"+num);
		Calendar dia = Calendar.getInstance();
		dia.set(199+num, num%11, num);
		c.setDataDeNascimento(dia);
		c.setNome(nome);
		return c;
	}
	
	public Endereco criarEndereco(){
		int num = (int) (Math.random() * 10);
		Endereco e = new Endereco();
		e.setBairro("Bairro "+num);e.setCep("5800"+num);e.setCidade("Cidade "+num);e.setNumero("0"+num);
		e.setRua("Rua "+num);e.setUf(UF.PB);
		return e;
	}
	public Telefone criarTelefone(){
		int num = (int) (Math.random() * 10);
		Telefone t = new Telefone();
		t.setDdd(num%99+"");
		t.setTelefone(num+9999+"7"+num);
		t.setOperadora("Tim");
		return t;
	}
	
	
	/*
	 * Pessoa
	 */
	@Test
	public void createClienteTest() {
		Cliente p = iniciarClienteInformacoesAleatorias(nome);
		pe.create(p);
		assertEquals(FindCliente.clientesComNome(nome), p);
	}
	
	@Test
	public void editClienteTest() throws EntidadeNaoExistenteException, Exception {
		createClienteTest();
		Cliente p = FindCliente.clientesComNome(nome);
		p.setNome("Cliente Alterado");
		pe.edit(p);
		assertEquals(FindCliente.clienteComId(p.getId()).getNome(), p.getNome());
	}
	
	@Test(expected=NoResultException.class )
	public void destroyClienteTest() throws EntidadeNaoExistenteException  {
		createClienteTest();
		Cliente p = FindCliente.clientesComNome(nome);
		pe.destroy(p);
		FindCliente.clientesComNome(nome);
	}
	
	@Test
	public void removeAllClienteTest() {
		for(int i =0; i>5;i++){
			Cliente p = iniciarClienteInformacoesAleatorias(nome);
			pe.create(p);
		}
		pe.removeAllClientes();
		assertEquals(pe.getQuantidadeClientes() , 0);
	}
	
	/*/
	 * Relacionamento Cliente Endereco
	 */
	@Test
	public void createClienteComEnderecoTest() {
		Cliente p = iniciarClienteInformacoesAleatorias(nome);
		Endereco e = criarEndereco();
		p.setEndereco(e);
		pe.create(p);
		assertEquals(FindCliente.clientesComNome(nome).getEndereco(), e);	
	}
	
	@Test
	public void adicionarEnderecoAoClienteTest() throws EntidadeNaoExistenteException, Exception {
		createClienteTest();
		Cliente p = FindCliente.clientesComNome(nome);
		Endereco e = criarEndereco();
		p.setEndereco(e);
		pe.edit(p);
		assertEquals(FindCliente.clientesComNome(nome).getEndereco(), e);	
	}
	
	@Test
	public void alterarEnderecoDoClienteTest() throws EntidadeNaoExistenteException, Exception {
		createClienteComEnderecoTest();
		Cliente p = FindCliente.clientesComNome(nome);
		Endereco e = p.getEndereco();
		e.setUf(UF.BA);
		e.setBairro("Bairro Alterado");
		pe.edit(p);
		assertEquals(FindCliente.clientesComNome(nome).getEndereco().getBairro(), "Bairro Alterado");	
	}
	
	@Test
	public void removerEnderecoDoClienteTest() throws EntidadeNaoExistenteException, Exception {
		createClienteComEnderecoTest();
		Cliente p = FindCliente.clientesComNome(nome);
		p.setEndereco(null);
		pe.edit(p);
		assertNull(FindCliente.clientesComNome(nome).getEndereco());	
	}
	
	@Test(expected=NoResultException.class)
	public void removerClienteComEnderecoTest() throws EntidadeNaoExistenteException, Exception {
		createClienteComEnderecoTest();
		Cliente p = FindCliente.clientesComNome(nome);
		Endereco e = p.getEndereco();
		pe.destroy(p);
		assertNull(Endereco.recuperarEnderecoId(e.getId()));	
	}
	
	
	/*/
	 * Relacionamento Cliente Telefone
	 */
	
	@Test
	public void createClienteComTelefoneTest() {
		Cliente p = iniciarClienteInformacoesAleatorias(nome);
		Telefone t = criarTelefone();
		p.addTelefone(t);
		pe.create(p);
		assertEquals(FindCliente.clientesComNome(nome).getTelefones().get(0), t);	
	}
	
	@Test
	public void createClienteComVariosTelefoneTest() {
		Cliente p = iniciarClienteInformacoesAleatorias(nome);
		int n = 5;
		for(int i=0;i<n;i++){
			Telefone t = criarTelefone();
			p.addTelefone(t);
		}
		pe.create(p);
		assertEquals(FindCliente.clientesComNome(nome).getTelefones().size(), n);	
	}
	
	
	@Test
	public void adicionarTelefoneAoClienteTest() throws EntidadeNaoExistenteException, Exception {
		createClienteTest();
		Cliente p = FindCliente.clientesComNome(nome);
		
		Telefone t = criarTelefone();
		p.addTelefone(t);
		pe.edit(p);
		assertEquals(FindCliente.clientesComNome(nome).getTelefones().get(0), t);
	}
	
	@Test
	public void alterarTelefoneDoClienteTest() throws EntidadeNaoExistenteException, Exception {
		createClienteComTelefoneTest();
		Cliente p = FindCliente.clientesComNome(nome);
		Telefone t = p.getTelefones().get(0);
		t.setTelefone("ALTERADO");
		pe.edit(p);
		assertEquals(FindCliente.clientesComNome(nome).getTelefones().get(0).getTelefone(), "ALTERADO");	
	}
	
	@Test
	public void removerTelefoneDoClienteTest() throws EntidadeNaoExistenteException, Exception {
		createClienteComTelefoneTest();
		Cliente p = FindCliente.clientesComNome(nome);
		Telefone t = p.getTelefones().get(0);
		p.removerTelefone(t);
		pe.edit(p);
		assertEquals(FindCliente.clientesComNome(nome).getTelefones().size(),0);	
	}
	
	@Test(expected=NoResultException.class)
	public void removerClienteComTelefoneTest() throws EntidadeNaoExistenteException, Exception {
		createClienteComTelefoneTest();
		Cliente p = FindCliente.clientesComNome(nome);
		Telefone e = p.getTelefones().get(0);
		pe.destroy(p);
		assertNull(Telefone.recuperarTelefoneId(e.getId()));	
	}
	

}
