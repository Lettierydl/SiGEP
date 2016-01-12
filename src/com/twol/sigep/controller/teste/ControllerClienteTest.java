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

public class ControllerClienteTest {
	
	ControllerPessoa pe;
	FindCliente fcli; 
	String nome = "Fulano de tal";
	
	@Before
	public void setup(){
		pe = new ControllerPessoa();
		pe.removeAllClientes();
		fcli = new FindCliente();
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
		assertEquals(fcli.clienteComNome(nome), p);
	}
	
	@Test
	public void editClienteTest() throws EntidadeNaoExistenteException, Exception {
		createClienteTest();
		Cliente p = fcli.clienteComNome(nome);
		p.setNome("Cliente Alterado");
		pe.edit(p);
		assertEquals(fcli.clienteComId(p.getId()).getNome(), p.getNome());
	}
	
	@Test(expected=NoResultException.class )
	public void destroyClienteTest() throws EntidadeNaoExistenteException  {
		createClienteTest();
		Cliente p = fcli.clienteComNome(nome);
		pe.destroy(p);
		fcli.clienteComNome(nome);
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
