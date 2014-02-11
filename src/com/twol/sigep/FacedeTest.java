package com.twol.sigep;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.model.exception.PermissaoInvalidaException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.ControllerFuncionario;
import com.twol.sigep.model.pessoas.Endereco;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.Telefone;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
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
	
	public Telefone criarTelefone(int num){
		Telefone t = new Telefone();
		t.setDdd(num%99+"");
		t.setOperadora(num+9999+"7"+num);
		t.setOperadora("Tim");
		return t;
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
	
	@Test//(expected = PersistenceException.class)
	public void adicionarUmTelefoneCliente() {
		Assert.assertEquals(fac.getListaClientes().size(), 0);
		iniciarClientes(1);
		
		clientes[0].addTelefone(criarTelefone(1));
		fac.adicionarCliente(clientes[0]);

		Assert.assertEquals(fac.getListaClientes().size(), 1);
		System.out.println( clientes[0].getId());
		Cliente cli = fac.buscarClientePorId(clientes[0].getId());
		Assert.assertEquals(cli.getTelefones(), clientes[0].getTelefones());

	}

	@Test
	public void adicionarVariosClientes() {
		Assert.assertEquals(fac.getListaClientes().size(), 0);
		iniciarClientes(MAX_CLIENTES);
		for (Cliente c : clientes) {
			fac.adicionarCliente(c);
		}
		 Assert.assertEquals(fac.getListaClientes().size(), MAX_CLIENTES);
	}
	
	@Test
	public void adicionarFuncionario() throws ParametrosInvalidosException, PermissaoInvalidaException{
		Funcionario f = new Funcionario();
		f.setCpf("09699482478");
		f.setLogin("leo");
		f.setNome("Lettiery");
		new ControllerFuncionario().salvarFuncionario(f, "123456",TipoDeFuncionario.Supervisor);
	}

}
