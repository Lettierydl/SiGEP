package com.twol.sigep.controller.teste;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import javax.persistence.NoResultException;

import org.junit.Before;
import org.junit.Test;

import com.twol.sigep.controller.ControllerPessoa;
import com.twol.sigep.controller.find.FindCliente;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.util.Persistencia;

public class ControllerClienteTest {
	
	ControllerPessoa pe;
	String nome = "Fulano de tal";
	
	@Before
	public void setup(){
		pe = new ControllerPessoa(Persistencia.emf);
		pe.removeAllClientes();
	}

	private Cliente iniciarClienteInformacoesAleatorias(String nome) {
		int num = (int) (Math.random() * 100);
		Cliente c = new Cliente();
		c.setCpf("00000000"+num);
		Calendar dia = Calendar.getInstance();
		dia.set(199+num, num%11, num);
		c.setDataDeNascimento(dia);
		c.setNome(nome);
		c.setEndereco("rua da casa dele, sem numero, "+num);
		c.setTelefone(""+num*100);
		return c;
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
	

}
