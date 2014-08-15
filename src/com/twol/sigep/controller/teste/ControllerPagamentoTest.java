package com.twol.sigep.controller.teste;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Calendar;

import javax.persistence.NoResultException;

import org.junit.Before;
import org.junit.Test;

import com.twol.sigep.controller.ControllerPagamento;
import com.twol.sigep.controller.ControllerPessoa;
import com.twol.sigep.controller.find.FindCliente;
import com.twol.sigep.controller.find.FindFuncionario;
import com.twol.sigep.controller.find.FindPagamento;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.model.vendas.Pagamento;
import com.twol.sigep.util.Persistencia;

public class ControllerPagamentoTest {
	
	ControllerPagamento pa;
	ControllerPessoa pe;
	String nome = "Fulano de tal";
	double valor = 50;
	
	@Before
	public void setup(){
		pa = new ControllerPagamento(Persistencia.emf);
		pe = new ControllerPessoa(Persistencia.emf);
		pa.removeAllPagamentos();
		pe.removeAllClientes();
		pe.removeAllFuncionarios();
	}

	private Pagamento iniciarPagamentoInformacoesAleatorias(double valor) {
		Pagamento p = new Pagamento();
		p.setObservacao("OBS...");
		p.setValor(valor);
		return p;
	}
	
	private Pagamento iniciarPagamentoESalvarNoBanco(){
		Pagamento p = iniciarPagamentoInformacoesAleatorias(valor);
		pa.create(p);
		return p;
	}
	
	private Funcionario iniciarFuncionarioInformacoesAleatorias(String nome) {
		int num = (int) (Math.random() * 100);
		Funcionario f = new Funcionario();
		f.setCpf("00000000" + num);
		f.setNome(nome);
		f.setSenha("123");
		f.setLogin(nome);
		f.setTipoDeFuncionario(TipoDeFuncionario.Supervisor);
		return f;
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
	
	
	/*
	 * Pagamento
	 */
	@Test
	public void createPagamentoTest() {
		Pagamento p = iniciarPagamentoInformacoesAleatorias(valor);
		pa.create(p);
		assertEquals(FindPagamento.pagamentoId(p.getId()), p);
	}
	
	@Test
	public void editPagamentoTest() throws EntidadeNaoExistenteException, Exception {
		Pagamento p = iniciarPagamentoESalvarNoBanco();
		p.setObservacao("Cliente Alterado");
		pa.edit(p);
		assertEquals( FindPagamento.pagamentoId(p.getId()).getObservacao() , p.getObservacao());
	}
	
	@Test(expected=NoResultException.class )
	public void destroyClienteTest() throws EntidadeNaoExistenteException  {
		Pagamento p = iniciarPagamentoESalvarNoBanco();
		pa.destroy(p);
		FindPagamento.pagamentoId(p.getId());
	}
	
	@Test
	public void removeAllPagamentoTest() {
		for(int i =0; i>5;i++){
			Pagamento p = iniciarPagamentoInformacoesAleatorias(valor*i);
			pa.create(p);
		}
		pa.removeAllPagamentos();
		assertEquals(pa.getQuantidadePagamentos() , 0);
	}
	
	/*
	 * Relacionamento Pagamento Cliente 
	 */
	@Test
	public void createPagamentoComClienteTest() {
		Cliente c = iniciarClienteInformacoesAleatorias(nome);
		pe.create(c);
		Pagamento p = iniciarPagamentoInformacoesAleatorias(valor);
		p.setCliente(c);
		pa.create(p);
		assertEquals(FindPagamento.pagamentoId(p.getId()).getCliente(), p.getCliente());	
	}
	
	@Test
	public void adicionarClienteAoPagamentoTest() throws EntidadeNaoExistenteException, Exception {
		Cliente c = iniciarClienteInformacoesAleatorias(nome);
		pe.create(c);
		
		Pagamento p = iniciarPagamentoESalvarNoBanco();
		p.setCliente(c);
		pa.edit(p);
		
		assertEquals(FindPagamento.pagamentoId(p.getId()).getCliente(), p.getCliente());	
	}
	
	@Test(expected=NoResultException.class)
	public void removerPagamentoComClienteTest() throws EntidadeNaoExistenteException, Exception {
		Cliente c = iniciarClienteInformacoesAleatorias(nome);
		pe.create(c);
		
		Pagamento p = iniciarPagamentoESalvarNoBanco();
		p.setCliente(c);
		pa.edit(p);
		pa.destroy(p);
		
		assertEquals(FindCliente.clienteComId(c.getId()),c);
		assertNull(FindPagamento.pagamentoId(p.getId()));
	}
	
	
	/*
	 * Relacionamento Pagamento Funcionario 
	 */
	@Test
	public void createPagamentoComFuncionarioTest() {
		Funcionario c = iniciarFuncionarioInformacoesAleatorias(nome);
		pe.create(c);
		Pagamento p = iniciarPagamentoInformacoesAleatorias(valor);
		p.setFuncionario(c);
		pa.create(p);
		assertEquals(FindPagamento.pagamentoId(p.getId()).getFuncionario(), p.getFuncionario());	
	}
	
	@Test
	public void adicionarFuncionarioAoPagamentoTest() throws EntidadeNaoExistenteException, Exception {
		Funcionario c = iniciarFuncionarioInformacoesAleatorias(nome);
		pe.create(c);
		
		Pagamento p = iniciarPagamentoESalvarNoBanco();
		p.setFuncionario(c);
		pa.edit(p);
		
		assertEquals(FindPagamento.pagamentoId(p.getId()).getFuncionario(), p.getFuncionario());	
	}
	
	@Test(expected=NoResultException.class)
	public void removerPagamentoComFuncionarioTest() throws EntidadeNaoExistenteException, Exception {
		Funcionario c = iniciarFuncionarioInformacoesAleatorias(nome);
		pe.create(c);
		
		Pagamento p = iniciarPagamentoESalvarNoBanco();
		p.setFuncionario(c);
		pa.edit(p);
		pa.destroy(p);
		
		assertEquals(FindFuncionario.funcionarioComId(c.getId()),c);
		assertNull(FindPagamento.pagamentoId(p.getId()));
	}

}
