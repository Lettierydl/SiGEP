package com.twol.sigep;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.model.exception.PermissaoInvalidaException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Endereco;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.Representante;
import com.twol.sigep.model.pessoas.Telefone;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.model.pessoas.UF;

public class FacedeTestPessoas {

	Facede fac;

	
	@Before
	public void setup() {
		fac = new Facede();
		fac.limparBancoDeDados();
	}
	
	private Cliente iniciarClienteInformacoesAleatorias() {
		int num = (int) (Math.random() * 100);
		Cliente c = new Cliente();
		c.setCpf("00000000"+num);
		Calendar dia = Calendar.getInstance();
		dia.set(199+num, num%11, num);
		c.setDataDeNascimento(dia);
		c.setNome("Cliente Teste " + num);
		c.setEndereco(criarEndereco());
		return c;
	}
	
	private Funcionario iniciarFuncionarioeInformacoesAleatorias(String login) {
		int num = (int) (Math.random() * 100);
		Funcionario f = new Funcionario();
		f.setCpf("00000000"+num);
		f.setNome("Funcionario Teste " + num);
		f.setLogin(login);
		f.setEndereco(criarEndereco());
		return f;
	}
	
	private Representante iniciarRepresentanteInformacoesAleatorias() {
		int num = (int) (Math.random() * 100);
		Representante r = new Representante();
		r.setNome("Representante Teste "+ num);
		r.setRazaoSocialDaEmpresa("Empresa Teste "+num);
		r.setEndereco(criarEndereco());
		return r;
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
	
	
	@Test
	public void limparbanco(){
		fac.limparBancoDeDados();
	}
	

	@Test
	public void adicionarCliente() {
		Assert.assertEquals(fac.getListaClientes().size(), 0);
		Cliente c = iniciarClienteInformacoesAleatorias();
		fac.adicionarCliente(c);

		Assert.assertEquals(fac.getListaClientes().size(), 1);
		Cliente result = fac.buscarClientePorId(c.getId());
		Assert.assertEquals(result.getId(), c.getId());
	}
	
	
	
	
	@Test
	public void adicionarUmTelefoneAoCliente() {
		Assert.assertEquals(fac.getListaClientes().size(), 0);
		Cliente c = this.iniciarClienteInformacoesAleatorias();
		
		c.addTelefone(criarTelefone());
		fac.adicionarCliente(c);

		Assert.assertEquals(fac.getListaClientes().size(), 1);
		Cliente result = fac.buscarClientePorId(c.getId());
		Assert.assertEquals(result.getTelefones().size(), 1);
		Assert.assertArrayEquals(result.getTelefones().toArray(), c.getTelefones().toArray());
	}
	
	@Test
	public void adicionarVariosTelefoneAoCliente() {
		Assert.assertEquals(fac.getListaClientes().size(), 0);
		Cliente c = this.iniciarClienteInformacoesAleatorias();
		
		c.addTelefone(criarTelefone());
		c.addTelefone(criarTelefone());
		c.addTelefone(criarTelefone());
		fac.adicionarCliente(c);

		Assert.assertEquals(fac.getListaClientes().size(), 1);
		Cliente result = fac.buscarClientePorId(c.getId());
		
		Assert.assertEquals(result.getTelefones().size(), 3);
		Assert.assertArrayEquals(result.getTelefones().toArray(), c.getTelefones().toArray());
	}
	
	
	@Test
	public void adicionarVariosClientes() {
		Assert.assertEquals(fac.getListaClientes().size(), 0);
		int numCli = 5;
		for(int i = 1 ; i <= numCli; i++){
			Cliente c = iniciarClienteInformacoesAleatorias();
			fac.adicionarCliente(c);
		}
		Assert.assertEquals(fac.getListaClientes().size(), numCli);
	}
	
	
	//Representante
	@Test
	public void adicionarRepresentante(){
		Assert.assertEquals(fac.getListaRepresentantes().size(), 0);
		Representante r = iniciarRepresentanteInformacoesAleatorias();
		fac.adicionarRepresentante(r);

		Assert.assertEquals(fac.getListaRepresentantes().size(), 1);
		Representante result = fac.buscarRepresentantePeloId(r.getId());
		Assert.assertEquals(result.getId(), r.getId());
	}

	@Test
	public void adicionarVariosRepresentante(){
		Assert.assertEquals(fac.getListaRepresentantes().size(), 0);
		int numCli = 5;
		for(int i = 1 ; i <= numCli; i++){
			Representante r = iniciarRepresentanteInformacoesAleatorias();
			fac.adicionarRepresentante(r);
		}
		Assert.assertEquals(fac.getListaRepresentantes().size(), numCli);
	}
	
	@Test
	public void removerRepresentante(){
		adicionarRepresentante();
		Assert.assertEquals(fac.getListaRepresentantes().size(), 1);
		Representante criado = fac.getListaRepresentantes().get(0);
		fac.removerRepresentante(criado);
		Assert.assertEquals(fac.getListaRepresentantes().size(), 0);
	}
	
	
	@Test
	public void adicionarUmTelefoneAoRepresentante() {
		Assert.assertEquals(fac.getListaRepresentantes().size(), 0);
		Representante r = iniciarRepresentanteInformacoesAleatorias();
		
		r.addTelefone(criarTelefone());
		fac.adicionarRepresentante(r);

		Assert.assertEquals(fac.getListaRepresentantes().size(), 1);
		Representante result = fac.buscarRepresentantePeloId(r.getId());
		Assert.assertEquals(result.getTelefones().size(), 1);
		Assert.assertArrayEquals(result.getTelefones().toArray(), r.getTelefones().toArray());
	}
	
	@Test
	public void removerUmTelefoneDoRepresentante()  {
		this.adicionarUmTelefoneAoRepresentante();
		
		Representante criado = fac.getListaRepresentantes().get(0);
		criado.removerTelefone(criado.getTelefones().get(0));
		fac.atualizarRepresentante(criado);
		
		Representante result = fac.buscarRepresentantePeloId(criado.getId());
		Assert.assertEquals(result.getTelefones().size(), 0);
		Assert.assertEquals(result.getTelefones().size(), criado.getTelefones().size());
	}
	
	
	
}
