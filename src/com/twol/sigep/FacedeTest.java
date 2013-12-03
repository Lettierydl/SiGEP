package com.twol.sigep;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Endereco;
import com.twol.sigep.model.pessoas.UF;

public class FacedeTest {

	Facede fac;

	// Maximos
	int MAX_CLIENTES = 5;

	// Clientes
	Cliente[] clientes;

	@Before
	public void setup() {
		fac = new Facede();
		clientes = new Cliente[MAX_CLIENTES];
		fac.limparBancoDeDados();

	}
	
	public void iniciarCliente(int num) {

		clientes[num] = new Cliente();
		clientes[num].setCpf("" + num + num + num + num + num + num + num + num
				+ num);
		Calendar c = Calendar.getInstance();
		c.set(num, num, num);
		clientes[num].setDataDeNascimento(c);
		clientes[num].setEndereco(criarEndereco(num));
		clientes[num].setNome("cliente" + num);

	}
	
	public Endereco criarEndereco(int num){
		Endereco e = new Endereco();
		e.setBairro("Bairro "+num);e.setCep("5800"+num);e.setCidade("Cidade "+num);e.setNumero("0"+num);
		e.setRua("Rua "+num);e.setUf(UF.PB);
		return e;
	}
	
	public void iniciarClientes(int qtd) {
		for (int i = 0; i < qtd; i++) {
			iniciarCliente(i);
		}

	}

	@Test//(expected = PersistenceException.class)
	public void adicionarCliente() {
		Assert.assertEquals(fac.getListaClientes().size(), 0);
		iniciarClientes(1);
		fac.adicionarCliente(clientes[0]);

		Assert.assertEquals(fac.getListaClientes().size(), 1);
		System.out.println( clientes[0].getId());
		Cliente cli = fac.buscarClientePorId(clientes[0].getId());
		Assert.assertEquals(cli.getId(), clientes[0].getId());

	}

	@Test
	public void adicionarVariosClientes() {
		Assert.assertEquals(fac.getListaClientes().size(), 0);
		iniciarClientes(MAX_CLIENTES);
		for (Cliente c : clientes) {
			fac.adicionarCliente(c);
		}

		// Assert.assertEquals(fac.getListaClientes().size(), MAX_CLIENTES);

	}

}
