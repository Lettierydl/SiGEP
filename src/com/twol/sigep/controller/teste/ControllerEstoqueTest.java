package com.twol.sigep.controller.teste;

import static org.junit.Assert.*;

import java.util.Calendar;

import javax.persistence.NoResultException;

import org.junit.Before;
import org.junit.Test;

import com.twol.sigep.controller.ControllerEstoque;
import com.twol.sigep.model.estoque.CategoriaProduto;
import com.twol.sigep.model.estoque.FinderProduto;
import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.estoque.Promocao;
import com.twol.sigep.model.estoque.UnidadeProduto;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.exception.PromocaoInvalida;
import com.twol.sigep.model.exception.PromocaoValidaJaExistente;
import com.twol.sigep.util.Persistencia;

public class ControllerEstoqueTest {
	
	ControllerEstoque ce;
	String codigo = "1234567890123";
	
	@Before
	public void setup(){
		ce = new ControllerEstoque(Persistencia.emf);
		ce.removeAllPromocoes();
		ce.removeAllProdutos();
	}

	private Produto iniciarProdutoInformacoesAleatorias(String codigo) {
		int num = (int) (Math.random() * 100);
		Produto p = new Produto();
		p.setCodigoDeBarras(codigo);
		p.setCategoria(CategoriaProduto.Outra);
		p.setDescricao("Produto Teste " + num);
		p.setDescricaoUnidade(UnidadeProduto.UND);
		p.setLimiteMinimoEmEstoque(num);
		p.setQuantidadeEmEstoque(num*2);
		p.setValorDeCompra(num);
		p.setValorDeVenda(num*1.25);
		return p;
	}
	
	private Promocao iniciarPromocaoValidaInformacoesAleatorias(double valorDesconto) {
		int num = (int) (Math.random() * 100);
		Promocao p = new Promocao();
		p.setQuantidadeMaximaDeVendas(num);
		p.setQuantidadeJaVendida(num*0.5);
		
		Calendar dia = Calendar.getInstance();
		dia.set(Calendar.DATE, dia.get(Calendar.DATE)+num);
		p.setDataDeFim(dia);
		dia.set(Calendar.DATE, dia.get(Calendar.DATE)-(num*2));
		p.setDataDeInicio(dia);
		
		return p;
	}
	
	private Promocao iniciarPromocaoInvalidaInformacoesAleatorias(double valorDesconto) {
		int num = (int) (Math.random() * 100);
		Promocao p = new Promocao();
		p.setQuantidadeMaximaDeVendas(num);
		p.setQuantidadeJaVendida(num);
		
		Calendar dia = Calendar.getInstance();
		
		p.setDataDeInicio(dia);
		
		dia.set(Calendar.DATE, dia.get(Calendar.DATE)-num);
		p.setDataDeFim(dia);
		
		
		return p;
	}
	
	
	/*
	 * Produto
	 */
	@Test
	public void createProdutoTest() {
		Produto p = iniciarProdutoInformacoesAleatorias(codigo);
		ce.create(p);
		assertEquals(FinderProduto.produtoComCodigoDeBarras(codigo), p);
	}
	
	@Test
	public void editProdutoTest() throws EntidadeNaoExistenteException, Exception {
		createProdutoTest();
		Produto p = FinderProduto.produtoComCodigoDeBarras(codigo);
		p.setDescricao("Produto Alterado");
		ce.edit(p);
		assertEquals(FinderProduto.produtoComCodigoDeBarras(codigo).getDescricao(), p.getDescricao());
	}
	
	@Test(expected=NoResultException.class )
	public void destroyProdutoTest() throws EntidadeNaoExistenteException  {
		createProdutoTest();
		Produto p = FinderProduto.produtoComCodigoDeBarras(codigo);
		ce.destroy(p);
		FinderProduto.produtoComCodigoDeBarras(codigo);
	}
	
	@Test
	public void removeAllProdutoTest() {
		for(int i =0; i>5;i++){
			Produto p = iniciarProdutoInformacoesAleatorias("123456789012"+i);
			ce.create(p);
		}
		ce.removeAllProdutos();
		assertEquals(ce.getQuantidadeProdutos() , 0);
	}
	
	
	
	/*
	 * Promocao
	 */
	@Test
	public void createPromocaoTest() {
		Promocao p = this.iniciarPromocaoValidaInformacoesAleatorias(50);
		ce.create(p);
		assertEquals(Promocao.recuperarPromocao(p.getId()), p);
	}
	
	@Test
	public void editPromocaoTest() throws EntidadeNaoExistenteException, Exception {
		createPromocaoTest();
		Promocao p = Promocao.recuperarLista().get(0);
		p.setValorDoDesconto(30);
		ce.edit(p);
		assertEquals(Promocao.recuperarLista().get(0).getValorDoDesconto(), p.getValorDoDesconto(), 0);
	}
	
	@Test(expected=NoResultException.class )
	public void destroyPromocaoTest() throws EntidadeNaoExistenteException  {
		createPromocaoTest();
		Promocao p = Promocao.recuperarLista().get(0);
		ce.destroy(p);
		Promocao.recuperarPromocao(p.getId());
	}
	
	@Test
	public void removeAllPromocaoTest() {
		for(int i =0; i>5;i++){
			Promocao p = this.iniciarPromocaoValidaInformacoesAleatorias(50);
			ce.create(p);
		}
		ce.removeAllPromocoes();
		assertEquals(ce.getQuantidadePromocoes() , 0);
	}
	
	
	/*
	 * Relacionamento Produto e Promocao
	 */
	
	@Test
	public void createUmaPromocaoValidaAoProduto() throws EntidadeNaoExistenteException, Exception {
		createProdutoTest();
		Produto c = FinderProduto.produtoComCodigoDeBarras(codigo);
		
		Promocao p = this.iniciarPromocaoValidaInformacoesAleatorias(c.getValorDeVenda()*0.25);
		p.setProduto(c);
		ce.create(p);
		
		Produto result = FinderProduto.produtoComCodigoDeBarras(codigo);
		assertEquals(result.getPromocoes().size(), 1);
		assertArrayEquals(result.getPromocoes().toArray(), c.getPromocoes().toArray());
		assertEquals(result.getPromocaoValida(), c.getPromocaoValida());
	}
	
	@Test(expected=NoResultException.class )
	public void destroyUmProdutoComUmaPromocao() throws EntidadeNaoExistenteException, Exception {
		createUmaPromocaoValidaAoProduto();
		Produto c = FinderProduto.produtoComCodigoDeBarras(codigo);
		ce.destroy(c);
		
		FinderProduto.produtoComCodigoDeBarras(codigo);
	}
	
	@Test
	public void destroyUmaPromocaoDeUmProduto() throws EntidadeNaoExistenteException, Exception {
		createUmaPromocaoValidaAoProduto();
		Produto c = FinderProduto.produtoComCodigoDeBarras(codigo);
		
		ce.destroy(c.getPromocaoValida());
		c = FinderProduto.produtoComCodigoDeBarras(codigo);
		assertNull(c.getPromocaoValida());
	}
	
	
	@Test(expected = PromocaoValidaJaExistente.class)
	public void creatVariasPromocaosValidasAoProduto() throws PromocaoValidaJaExistente, PromocaoInvalida {
		createProdutoTest();
		Produto c = FinderProduto.produtoComCodigoDeBarras(codigo);
		
		Promocao p = this.iniciarPromocaoValidaInformacoesAleatorias(c.getValorDeVenda()*0.25);
		p.setProduto(c);
		ce.create(p);
		
		Promocao p2 = this.iniciarPromocaoValidaInformacoesAleatorias(c.getValorDeVenda()*0.25);
		p2.setProduto(c);
		ce.create(p2);
	}
	
	@Test(expected = PromocaoInvalida.class)
	public void creatUmaPromocaoInvalidaAoProduto() throws PromocaoValidaJaExistente, PromocaoInvalida {
		createProdutoTest();
		Produto c = FinderProduto.produtoComCodigoDeBarras(codigo);
		
		Promocao p = this.iniciarPromocaoInvalidaInformacoesAleatorias(c.getValorDeVenda()*0.25);
		p.setProduto(c);
		ce.create(p);
	}
	
	
	
	

}
