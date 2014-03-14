package com.twol.sigep.model.vendas;

import java.util.Calendar;
import java.util.List;

import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.estoque.Promocao;
import com.twol.sigep.model.exception.EstadoInvalidoDaVendaAtualException;
import com.twol.sigep.model.exception.LinhaDaVendaNaoEncontradaException;
import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.model.exception.ProdutoABaixoDoEstoqueException;
import com.twol.sigep.model.exception.VendaPendenteException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Dependente;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.util.VariaveisDeConfiguracaoUtil;

public class ControllerVenda {

	private Venda vendaAtual;
	private Funcionario logado;

	public ControllerVenda(Funcionario logado)
			throws ParametrosInvalidosException {
		setFuncionarioLogado(logado);
	}

	public void setFuncionarioLogado(Funcionario logado)
			throws ParametrosInvalidosException {
		if (logado == null) {
			throw new ParametrosInvalidosException();
		}
		this.logado = logado;
		/* errado
		vendaAtual = selecionarVendaPendente(logado);
		try {
			iniciarNovaVenda();
		} catch (VendaPendenteException vpe) {
		}*/
	}

	public void iniciarNovaVenda() throws VendaPendenteException {
		try {
			validarEstadoVendaAtual();
			throw new VendaPendenteException();
		} catch (EstadoInvalidoDaVendaAtualException e) {
			vendaAtual = new Venda();
			vendaAtual.setFuncionario(logado);
			vendaAtual.setDia(Calendar.getInstance());
			Venda.salvar(vendaAtual);
		}
	}

	public Venda addLinhaDaVenda(Produto p, double quantidadeVendida) 
		throws ProdutoABaixoDoEstoqueException {
		if (VariaveisDeConfiguracaoUtil.LIBERAR_VENDA_DE_PRODUTO_SEM_ESTOQUE) {
			LinhaDaVenda lv = criarLinhaDaVenda(p, quantidadeVendida);
			vendaAtual.addLinhaDaVenda(lv);
			Venda.atualizar(vendaAtual);
			return vendaAtual;
		}else{
			if(p.getQuantidadeEmEstoque() < quantidadeVendida){
				LinhaDaVenda lv = criarLinhaDaVenda(p, quantidadeVendida);
				vendaAtual.addLinhaDaVenda(lv);
				Venda.atualizar(vendaAtual);
				return vendaAtual;
			}else{
				throw new ProdutoABaixoDoEstoqueException();
			}
		}
	}

	
	public Venda removeLinhaDaVenda(LinhaDaVenda lv) 
			throws LinhaDaVendaNaoEncontradaException {
		if(vendaAtual.getLinhasDaVenda().contains(lv)){
			vendaAtual.removeLinhaDaVenda(lv);
			LinhaDaVenda.remover(lv);
			Venda.atualizar(vendaAtual);
			return vendaAtual;
		}else{
			throw new LinhaDaVendaNaoEncontradaException();
		}
	}
	
	public Venda finalizarVenda(Cliente c, Dependente d, FormaDePagamento fp) 
			throws ParametrosInvalidosException, EstadoInvalidoDaVendaAtualException{
		switch (fp) {
		case A_Prazo:
			finalizarVendaAPrazo(c, d, fp);
			break;
		case A_Vista:
			finalizarVendaAVista();
			break;

		default:
			finalizarVendaAPrazo(c, d, fp);
			break;
		}
		vendaAtual = null;
		try {
			iniciarNovaVenda();
		} catch (VendaPendenteException e) {//Este erro n��o pode aconteser nesse sen��rio por que acabei de sertar vendaAtul como null
			e.printStackTrace();
			throw new EstadoInvalidoDaVendaAtualException();
		}
		return vendaAtual;
	}
	
	private void finalizarVendaAVista(){
		vendaAtual.setFormaDePagamento(FormaDePagamento.A_Vista);
		vendaAtual.setPaga(true);
		Venda.atualizar(vendaAtual);
	}
	
	private void finalizarVendaAPrazo(Cliente c, Dependente d, FormaDePagamento fp) throws ParametrosInvalidosException{
		if(c == null){
			throw new ParametrosInvalidosException();
		}
		c.acrecentarDebito(vendaAtual.getValorTotalDaVendaComDesconto());
		Cliente.atualizar(c);
		if(d != null){
			vendaAtual.setDependente(d);
		}
		vendaAtual.setCliente(c);
		vendaAtual.setFormaDePagamento(fp);
		vendaAtual.setPaga(false);
		Venda.atualizar(vendaAtual);
	}
	
	
	private double calcularValorDoDesconto(Produto p, double quantidadeVendida) {
		double desconto = 0;
		if(VariaveisDeConfiguracaoUtil.ATIVAR_DESCONTO_DE_PROMOCOES){
			if(p.getPromocaoValida() != null){
				desconto += p.getPromocaoValida().calcularValorDoDesconto(quantidadeVendida);
			}
		}
		return desconto;
	}

	public Venda getVendaAtual() {
		return vendaAtual;
	}

	private static Venda selecionarVendaPendente(Funcionario f) {
		List<Venda> pendentes = new FinderVenda()
				.getVendasNaoFinalizadasPorFuncionario(f);
		Venda melhorEscolha = null;
		for (Venda v : pendentes) {
			if (melhorEscolha == null) {
				melhorEscolha = v;
			} else if (v.getDia().compareTo(melhorEscolha.getDia()) > 0) {
				melhorEscolha = v;
			}
		}
		return melhorEscolha;
	}
	
	private LinhaDaVenda criarLinhaDaVenda(Produto p, double quantidadeVendida) {
		LinhaDaVenda lv = new LinhaDaVenda();
		lv.setProduto(p);
		lv.setQuantidadeVendida(quantidadeVendida);
		lv.setValorDoProdutoVendido(p.getValorDeVenda());
		lv.setValorDoDesconto(calcularValorDoDesconto(p, quantidadeVendida));
		lv.calcularValores();
		return lv;
	}
	
	private void validarEstadoVendaAtual() throws EstadoInvalidoDaVendaAtualException{
		if(vendaAtual == null){
			throw new EstadoInvalidoDaVendaAtualException();
		}
	}

}
