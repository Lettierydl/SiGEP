package com.twol.sigep;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.twol.sigep.model.estoque.CategoriaProduto;
import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.estoque.Promocao;
import com.twol.sigep.model.estoque.UnidadeProduto;
import com.twol.sigep.model.exception.PromocaoInvalida;
import com.twol.sigep.model.exception.PromocaoValidaJaExistente;

public class FacedeTestEstoque {

	Facede fac;

	
	@Before
	public void setup() {
		fac = new Facede();
		fac.limparBancoDeDados();
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
		dia.set(Calendar.DATE, dia.get(Calendar.DATE)+(num*2));
		p.setDataDeInicio(dia);
		
		return p;
	}
	
	private Promocao iniciarPromocaoInvalidaInformacoesAleatorias(double valorDesconto) {
		int num = (int) (Math.random() * 100);
		Promocao p = new Promocao();
		p.setQuantidadeMaximaDeVendas(num);
		p.setQuantidadeJaVendida(num*0.5);
		
		Calendar dia = Calendar.getInstance();
		dia.set(Calendar.DATE, dia.get(Calendar.DATE)+num);
		p.setDataDeFim(dia);
		dia.set(Calendar.DATE, dia.get(Calendar.DATE)+(num*2));
		p.setDataDeInicio(dia);
		
		return p;
	}


	@Test
	public void adicionarProduto() {
		Assert.assertEquals(fac.getListaClientes().size(), 0);
		Produto p = iniciarProdutoInformacoesAleatorias("000001");
		fac.adicionarProduto(p);

		Assert.assertEquals(fac.getListaProdutos().size(), 1);
		Produto result = fac.buscarProdutoPorId(p.getId());
		Assert.assertEquals(result, p);
	}
	
	@Test
	public void adicionarVariosProduto() {
		Assert.assertEquals(fac.getListaClientes().size(), 0);
		Produto p = iniciarProdutoInformacoesAleatorias("000001");
		fac.adicionarProduto(p);

		Assert.assertEquals(fac.getListaProdutos().size(), 1);
		Produto result = fac.buscarProdutoPorId(p.getId());
		Assert.assertEquals(result, p);
	}
	
	@Test
	public void removerProduto() {
		adicionarProduto();
		Assert.assertEquals(fac.getListaProdutos().size(), 1);
		Produto criado = fac.getListaProdutos().get(0);
		fac.removerProduto(criado);
		Assert.assertEquals(fac.getListaProdutos().size(), 0);
	}
	
	
	@Test
	public void adicionarUmaPromocaoValidaAoProduto() throws PromocaoValidaJaExistente, PromocaoInvalida {
		Assert.assertEquals(fac.getListaProdutos().size(), 0);
		Produto c = this.iniciarProdutoInformacoesAleatorias("000001");
		fac.adicionarProduto(c);
		Assert.assertEquals(fac.getListaProdutos().size(), 1);
		
		Promocao p = this.iniciarPromocaoValidaInformacoesAleatorias(c.getValorDeVenda()*0.25);
		
		c.addPromocaoValida(p);
		c.addPromocaoValida(p);
		fac.atualizarProduto(c);

		Produto result = fac.buscarProdutoPorId(c.getId());
		Assert.assertEquals(result.getPromocoes().size(), 1);
		Assert.assertArrayEquals(result.getPromocoes().toArray(), c.getPromocoes().toArray());
		Assert.assertEquals(result.getPromocaoValida(), c.getPromocaoValida());
	}
	
	
	@Test(expected = PromocaoValidaJaExistente.class)
	public void adicionarVariasPromocaosValidasAoProduto() throws PromocaoValidaJaExistente, PromocaoInvalida {
		Assert.assertEquals(fac.getListaProdutos().size(), 0);
		Produto c = this.iniciarProdutoInformacoesAleatorias("000001");
		fac.adicionarProduto(c);
		Assert.assertEquals(fac.getListaProdutos().size(), 1);
		
		Promocao p = this.iniciarPromocaoValidaInformacoesAleatorias(c.getValorDeVenda()*0.25);
		
		c.addPromocaoValida(p);
		c.addPromocaoValida(p);
	}
	
	@Test(expected = PromocaoInvalida.class)
	public void adicionarUmaPromocaoInvalidaAoProduto() throws PromocaoValidaJaExistente, PromocaoInvalida {
		Assert.assertEquals(fac.getListaProdutos().size(), 0);
		Produto c = this.iniciarProdutoInformacoesAleatorias("000001");
		fac.adicionarProduto(c);
		Assert.assertEquals(fac.getListaProdutos().size(), 1);
		
		Promocao p = this.iniciarPromocaoInvalidaInformacoesAleatorias(c.getValorDeVenda()*0.25);
		
		c.addPromocaoValida(p);
	}
	
	@Test
	public void desativarUmaPromocaoValida() throws PromocaoValidaJaExistente, PromocaoInvalida {
		this.adicionarUmaPromocaoValidaAoProduto();
		Produto p = fac.getListaProdutos().get(0);
		p.getPromocaoValida().desativar();
		fac.atualizarProduto(p);
		Assert.assertNull(fac.getListaProdutos().get(0).getPromocaoValida());	
	}
	
	
}
