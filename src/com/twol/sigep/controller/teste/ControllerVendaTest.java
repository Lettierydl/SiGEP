package com.twol.sigep.controller.teste;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import javax.persistence.NoResultException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.twol.sigep.controller.ControllerEstoque;
import com.twol.sigep.controller.ControllerPessoa;
import com.twol.sigep.controller.ControllerVenda;
import com.twol.sigep.controller.find.FindCliente;
import com.twol.sigep.controller.find.FindFuncionario;
import com.twol.sigep.controller.find.FindVenda;
import com.twol.sigep.model.estoque.CategoriaProduto;
import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.estoque.UnidadeProduto;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.model.vendas.ItemDeVenda;
import com.twol.sigep.model.vendas.Pagamento;
import com.twol.sigep.model.vendas.Venda;
import com.twol.sigep.util.Persistencia;

public class ControllerVendaTest {
	
	ControllerVenda ve;
	ControllerPessoa pe;
	ControllerEstoque ce;
	String nome = "Fulano de tal";
	double valor = 50;
	
	@Before
	public void setup(){
		ve = new ControllerVenda(Persistencia.emf);
		pe = new ControllerPessoa(Persistencia.emf);
		ce = new ControllerEstoque(Persistencia.emf);
		ve.removeAllVendas();
		pe.removeAllClientes();
		pe.removeAllFuncionarios();
		ce.removeAllProdutos();
		ce.removeAllPromocoes();
	}
	
	@After
	public void clear(){
		setup();
	}

	private Venda iniciarVendaInformacoesAleatorias() {
		Venda v = new Venda();
		v.setDia(Calendar.getInstance());
		return v;
	}
	
	private Venda iniciarVendaESalvarNoBanco() {
		Venda v = iniciarVendaInformacoesAleatorias();
		ve.create(v);
		return v;
	}
	
	private ItemDeVenda iniciarItemDeVendaInformacoesAleatorias() {
		ItemDeVenda i = new ItemDeVenda();
		i.setQuantidade(20);
		return i;
	}
	private ItemDeVenda iniciarItemDeVendaInformacoesAleatorias(Produto p) {
		ItemDeVenda i = new ItemDeVenda();
		i.setQuantidade(20);
		i.setProduto(p);
		return i;
	}
	private Produto iniciarProdutoESalvarNoBanco() {
		int num = (int) (Math.random() * 100);
		Produto p = new Produto();
		p.setCodigoDeBarras("1234"+ ((int) (Math.random() * 1000000000)));
		p.setCategoria(CategoriaProduto.Outra);
		p.setDescricao("Produto Teste " + num);
		p.setDescricaoUnidade(UnidadeProduto.UND);
		p.setLimiteMinimoEmEstoque(num);
		p.setQuantidadeEmEstoque(num*2);
		p.setValorDeCompra(num);
		p.setValorDeVenda(num*1.25);
		ce.create(p);
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
	private Pagamento iniciarPagamentoInformacoesAleatorias(double valor) {
		Pagamento p = new Pagamento();
		p.setObservacao("OBS...");
		p.setValor(valor);
		return p;
	}
	
	
	/*
	 * Venda
	 */
	@Test
	public void createVendaTest() {
		Venda p = iniciarVendaInformacoesAleatorias();
		ve.create(p);
		assertEquals(FindVenda.vendaId(p.getId()), p);
	}
	
	@Test
	public void editVendaTest() throws EntidadeNaoExistenteException, Exception {
		createVendaTest();
		Venda v = iniciarVendaESalvarNoBanco();
		v.setPartePaga(20);
		ve.edit(v);
		assertEquals( FindVenda.vendaId(v.getId()).getPartePaga() , v.getPartePaga(),0);
	}
	
	@Test(expected=NoResultException.class )
	public void destroyVendaTest() throws EntidadeNaoExistenteException  {
		Venda p = iniciarVendaESalvarNoBanco();
		ve.destroy(p);
		FindVenda.vendaId(p.getId());
	}
	
	@Test
	public void removeAllVendaTest() {
		for(int i =0; i>5;i++){
			this.createVendaTest();
		}
		ve.removeAllVendas();
		assertEquals(ve.getQuantidadeVendas() , 0);
	}


	/*
	 * Relacionamento Venda Cliente 
	 */
	@Test
	public void createVendaComClienteTest() {
		Cliente c = iniciarClienteInformacoesAleatorias(nome);
		pe.create(c);
		Venda p = iniciarVendaInformacoesAleatorias();
		p.setCliente(c);
		ve.create(p);
		assertEquals(FindVenda.vendaId(p.getId()).getCliente(), p.getCliente());	
	}
	
	@Test
	public void adicionarClienteAoVendaTest() throws EntidadeNaoExistenteException, Exception {
		Cliente c = iniciarClienteInformacoesAleatorias(nome);
		pe.create(c);
		
		Venda p = iniciarVendaESalvarNoBanco();
		p.setCliente(c);
		ve.edit(p);
		
		assertEquals(FindVenda.vendaId(p.getId()).getCliente(), p.getCliente());	
	}
	
	@Test(expected=NoResultException.class)
	public void removerVendaComClienteTest() throws EntidadeNaoExistenteException, Exception {
		Cliente c = iniciarClienteInformacoesAleatorias(nome);
		pe.create(c);
		
		Venda p = iniciarVendaESalvarNoBanco();
		p.setCliente(c);
		ve.edit(p);
		ve.destroy(p);
		
		assertEquals(FindCliente.clienteComId(c.getId()),c);
		assertNull(FindVenda.vendaId(p.getId()));
	}
	
	
	/*
	 * Relacionamento Venda Funcionario 
	 */
	@Test
	public void createVendaComFuncionarioTest() {
		Funcionario c = iniciarFuncionarioInformacoesAleatorias(nome);
		pe.create(c);
		Venda p = iniciarVendaInformacoesAleatorias();
		p.setFuncionario(c);
		ve.create(p);
		assertEquals(FindVenda.vendaId(p.getId()).getFuncionario(), p.getFuncionario());	
	}
	
	@Test
	public void adicionarFuncionarioAoVendaTest() throws EntidadeNaoExistenteException, Exception {
		Funcionario c = iniciarFuncionarioInformacoesAleatorias(nome);
		pe.create(c);
		
		Venda p = iniciarVendaESalvarNoBanco();
		p.setFuncionario(c);
		ve.edit(p);
		
		assertEquals(FindVenda.vendaId(p.getId()).getFuncionario(), p.getFuncionario());	
	}
	
	@Test(expected=NoResultException.class)
	public void removerVendaComFuncionarioTest() throws EntidadeNaoExistenteException, Exception {
		Funcionario c = iniciarFuncionarioInformacoesAleatorias(nome);
		pe.create(c);
		
		Venda p = iniciarVendaESalvarNoBanco();
		p.setFuncionario(c);
		ve.edit(p);
		ve.destroy(p);
		
		assertEquals(FindFuncionario.funcionarioComId(c.getId()),c);
		assertNull(FindVenda.vendaId(p.getId()));
	}
	
	
	
	
	/*
	 * Relacionamento Venda ItemDeVenda 
	 */
	@Test
	public void createVendaComItemTest() {
		Venda v = iniciarVendaInformacoesAleatorias();
		ItemDeVenda i = iniciarItemDeVendaInformacoesAleatorias(iniciarProdutoESalvarNoBanco());
		v.addItemDeVenda(i);
		ve.create(v);
		
		assertEquals(FindVenda.vendaId(v.getId()).getItensDeVenda().get(0) , i);	
	}
	
	@Test(expected=NullPointerException.class)
	public void createItemSemProdutoTest() {
		Venda v = iniciarVendaESalvarNoBanco();
		ItemDeVenda i = iniciarItemDeVendaInformacoesAleatorias();
		v.addItemDeVenda(i);
	}
	
	@Test
	public void adicionarItemAVendaTest() throws EntidadeNaoExistenteException, Exception {
		Venda v = iniciarVendaESalvarNoBanco();
		ItemDeVenda i = iniciarItemDeVendaInformacoesAleatorias(iniciarProdutoESalvarNoBanco());
		v.addItemDeVenda(i);
		ve.edit(v);
		
		assertEquals(FindVenda.vendaId(v.getId()).getItensDeVenda().get(0) , i);	
	}
	
	@Test(expected=NoResultException.class)
	public void removerVendaComItemTest() throws EntidadeNaoExistenteException, Exception {
		Venda v = iniciarVendaESalvarNoBanco();
		ItemDeVenda i = iniciarItemDeVendaInformacoesAleatorias(iniciarProdutoESalvarNoBanco());
		v.addItemDeVenda(i);
		ve.edit(v);
		
		ve.destroy(v);
		assertEquals(FindVenda.vendaId(v.getId()).getItensDeVenda().get(0) , i);	
		assertNull(FindVenda.itemDeVendaId(i.getId()));
	}
	
	
	@Test
	public void adicionarVariosItemAVendaTest() throws EntidadeNaoExistenteException, Exception {
		Venda v = iniciarVendaESalvarNoBanco();
		int n = 5;
		for(int j = 0 ; j<n ; j++){
			ItemDeVenda i = iniciarItemDeVendaInformacoesAleatorias(iniciarProdutoESalvarNoBanco());
			v.addItemDeVenda(i);
			ve.edit(v);
		}
		assertEquals(FindVenda.vendaId(v.getId()).getItensDeVenda().size() , n);	
	}
	
	@Test
	public void removerUmItemDaVendaTest() throws EntidadeNaoExistenteException, Exception {
		ve.iniciarNovaVenda();
		Venda v = ve.getAtual();
		int n = 5;
		for(int j = 0 ; j<n ; j++){
			ItemDeVenda i = iniciarItemDeVendaInformacoesAleatorias(iniciarProdutoESalvarNoBanco());
			v.addItemDeVenda(i);
			ve.edit(v);
		}
		assertEquals(FindVenda.vendaId(v.getId()).getItensDeVenda().size() , n);
		ItemDeVenda ir = FindVenda.vendaId(v.getId()).getItensDeVenda().get(0);
		ve.removerItem(ir);
		assertFalse(FindVenda.vendaId(v.getId()).getItensDeVenda().contains(ir) );
	}
	
	@Test
	public void removerItemDaVendaEVerificarValorTest() throws EntidadeNaoExistenteException, Exception {
		ve.iniciarNovaVenda();
		Venda v = ve.getAtual();
		int n = 5;
		double d= 0;
		double t = 0;
		for(int j = 0 ; j<n ; j++){
			ItemDeVenda i = iniciarItemDeVendaInformacoesAleatorias(iniciarProdutoESalvarNoBanco());
			i.setDesconto(i.getProduto().getValorDeVenda()*0.25);
			v.addItemDeVenda(i);
			ve.edit(v);
			d+=i.getDesconto();
			t+=i.getTotal();
		}
		assertEquals(FindVenda.vendaId(v.getId()).getItensDeVenda().size() , n);
		
		ItemDeVenda ir = FindVenda.vendaId(v.getId()).getItensDeVenda().get(0);
		ve.removerItem(ir);
		
		assertFalse(FindVenda.vendaId(v.getId()).getItensDeVenda().contains(ir) );
		assertEquals(FindVenda.vendaId(v.getId()).getDesconto() , d-ir.getDesconto(),0);	
		assertEquals(FindVenda.vendaId(v.getId()).getTotal() , t-ir.getTotal(),0);	
	}
	
	@Test
	public void verificarValorDaVendaTest() throws EntidadeNaoExistenteException, Exception {
		Venda v = iniciarVendaESalvarNoBanco();
		int t = 0;
		for(int j = 0 ; j<5 ; j++){
			ItemDeVenda i = iniciarItemDeVendaInformacoesAleatorias(iniciarProdutoESalvarNoBanco());
			v.addItemDeVenda(i);
			ve.edit(v);
			t+=i.getTotal();
		}
		assertEquals(FindVenda.vendaId(v.getId()).getTotal() , t,0);	
	}
	
	@Test
	public void verificarValorDaVendaComDescontoTest() throws EntidadeNaoExistenteException, Exception {
		Venda v = iniciarVendaESalvarNoBanco();
		double d= 0;
		double t = 0;
		for(int j = 0 ; j<50 ; j++){
			ItemDeVenda i = iniciarItemDeVendaInformacoesAleatorias(iniciarProdutoESalvarNoBanco());
			i.setDesconto(i.getProduto().getValorDeVenda()*0.25);
			v.addItemDeVenda(i);
			ve.edit(v);
			d+=i.getDesconto();
			t+=i.getTotalComDesconto();
		}
		assertEquals(FindVenda.vendaId(v.getId()).getDesconto() , d,0);	
		assertEquals(FindVenda.vendaId(v.getId()).getTotalComDesconto() , t,0);	
	}
	
	
	@Test
	public void verificarValorDeVendaComPagamentoTotalTest() throws EntidadeNaoExistenteException, Exception {
		Venda v = iniciarVendaESalvarNoBanco();
		Cliente c = iniciarClienteInformacoesAleatorias("Cliente");
		pe.create(c);
		v.setCliente(c);
		for(int j = 0 ; j<50 ; j++){
			ItemDeVenda i = iniciarItemDeVendaInformacoesAleatorias(iniciarProdutoESalvarNoBanco());
			v.addItemDeVenda(i);
			ve.edit(v);
		}
		//nao precisa salvar o pagamento porque é apenas teste
		Pagamento p = iniciarPagamentoInformacoesAleatorias(v.getTotal());
		p.setCliente(c);
		
		ve.abaterValorDoPagamentoNaVenda(p);
		
		assertEquals(FindVenda.vendaId(v.getId()).getPartePaga() , v.getTotal(),0);
		assertTrue(FindVenda.vendaId(v.getId()).isPaga());
	}
	
	@Test
	public void verificarValorDeVendaComPagamentoParcialTest() throws EntidadeNaoExistenteException, Exception {
		Venda v = iniciarVendaESalvarNoBanco();
		Cliente c = iniciarClienteInformacoesAleatorias("Cliente");
		pe.create(c);
		v.setCliente(c);
		for(int j = 0 ; j<50 ; j++){
			ItemDeVenda i = iniciarItemDeVendaInformacoesAleatorias(iniciarProdutoESalvarNoBanco());
			v.addItemDeVenda(i);
			ve.edit(v);
		}
		//nao precisa salvar o pagamento porque é apenas teste
		double restante = v.getTotal()*0.5;
		Pagamento p = iniciarPagamentoInformacoesAleatorias(restante);
		p.setCliente(c);
		
		ve.abaterValorDoPagamentoNaVenda(p);
		
		assertEquals(FindVenda.vendaId(v.getId()).getPartePaga() , restante,0);
		assertFalse(FindVenda.vendaId(v.getId()).isPaga());
	}
	
	@Test//paga uma venda e parte de outra
	public void verificarValorDeVendaComPagamentoMistoTest() throws EntidadeNaoExistenteException, Exception {
		Venda v = iniciarVendaESalvarNoBanco();
		Venda v2 = iniciarVendaESalvarNoBanco();
		Cliente c = iniciarClienteInformacoesAleatorias("Cliente");
		pe.create(c);
		v.setCliente(c);
		v2.setCliente(c);
		for(int j = 0 ; j<5 ; j++){
			v.addItemDeVenda(iniciarItemDeVendaInformacoesAleatorias(iniciarProdutoESalvarNoBanco()));
			v2.addItemDeVenda(iniciarItemDeVendaInformacoesAleatorias(iniciarProdutoESalvarNoBanco()));
			ve.edit(v);
			ve.edit(v2);
		}
		Venda vmenor = v.getTotal() < v2.getTotal()? v : v2;
		Venda vmaior = v.getTotal() > v2.getTotal()? v : v2;
		//nao precisa salvar o pagamento porque é apenas teste
		double valor = vmenor.getTotal() + (vmaior.getTotal()*0.25);
		Pagamento p = iniciarPagamentoInformacoesAleatorias(valor);
		p.setCliente(c);
		
		ve.abaterValorDoPagamentoNaVenda(p);
		
		assertEquals(FindVenda.vendaId(vmenor.getId()).getPartePaga() , vmenor.getTotal(),0);
		assertTrue(FindVenda.vendaId(vmenor.getId()).isPaga());
		
		assertEquals(FindVenda.vendaId(vmaior.getId()).getPartePaga() , vmaior.getTotal()*0.25,0);
		assertFalse(FindVenda.vendaId(vmaior.getId()).isPaga());
	}
	

}
