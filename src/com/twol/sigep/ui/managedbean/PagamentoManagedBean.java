package com.twol.sigep.ui.managedbean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.vendas.Pagamento;
import com.twol.sigep.model.vendas.Venda;

@ViewScoped
@ManagedBean(name = "pagamentoBean")
public class PagamentoManagedBean {
	private Facede f;
	
	private Pagamento newPagamento;
	private double troco = 0;
	private String nomeClienteParaVenda = "";
	private String nomeClienteParaHistorico = "";
	
	private Venda vendaASerPaga = null;
	
	private List<Pagamento> listAtualDeHistorico;
	
	private List<Venda> listAtualDeVenda;
	
	public PagamentoManagedBean(){
		f = new Facede();
		newPagamento = new Pagamento();
		listAtualDeHistorico = f.getListaPagamentoHoje();
		setListAtualDeVenda(f.getListaVendasNaoPagasDeHoje());
	}

	public void cadastrarPagamento(){
		System.out.println("Chamou metodo");
		try{
			validarInformacoes(newPagamento);
		}catch(Exception e){
			return;
		}
		try {
			troco = f.adicionarPagamento(newPagamento);
		} catch (ParametrosInvalidosException e) {
			e.printStackTrace();
		}
		newPagamento = new Pagamento();
		listAtualDeHistorico = f.getListaPagamentoDoCliente(newPagamento.getCliente());
		//RequestContext.getCurrentInstance().execute("$('#modalResumo').modal('show')");
	}
	
	public void validarInformacoes(Pagamento c){
		
	}
	
	public List<String> autoCompletClienteVenda(String nome){
		try{
			return f.buscarNomeClientePorNomeQueInicia(nome);
		}finally{
			nomeClienteParaVenda = nome;
			//modificarListaAtualDeVendasPeloCliente();
		}
	}
	
	
	public void modificarListaAtualDeVendasPeloCliente(){
		List<Cliente> clientes = f.buscarClientePorCPFOuNomeQueIniciam(nomeClienteParaVenda);
		if(nomeClienteParaVenda.isEmpty()){
			listAtualDeVenda = f.getListaVendasNaoPagasDeHoje();
			retirarClienteSelecionado();
		}else if(clientes.size() > 1 ){
			listAtualDeVenda = f.buscarVendasNaoPagasDosClientes(clientes);
			retirarClienteSelecionado();
		}else if(clientes.size() == 1){
			setClienteSelecionado(clientes.iterator().next());
			listAtualDeVenda = f.buscarVendaNaoPagaDoCliente(clientes.iterator().next());
		}else{
			listAtualDeVenda = new ArrayList<Venda>();
			retirarClienteSelecionado();
		}
		RequestContext.getCurrentInstance().update(":pagamanetoForm");
		RequestContext.getCurrentInstance().update(":tabelaVendas");
	}
	
	public List<String> autoCompletClienteHistorico(String nome){
		try{
			return f.buscarNomeClientePorNomeQueInicia(nome);
		}finally{
			nomeClienteParaHistorico = nome;
			modificarListaAtualDeHistoricoDoCliente();
		}
	}
	
	public void modificarListaAtualDeHistoricoDoCliente(){
		List<Cliente> clientes = f.buscarClientePorCPFOuNomeQueIniciam(nomeClienteParaHistorico);
		if(nomeClienteParaHistorico.isEmpty()){
			listAtualDeHistorico = f.getListaPagamentoHoje();
		}else if(clientes.size() > 1 ){
			listAtualDeHistorico = f.getListaPagamentoDosClientes(clientes);
		}else if(clientes.size() == 1){
			listAtualDeHistorico = f.getListaPagamentoDoCliente(clientes.iterator().next());
		}else{
			listAtualDeHistorico = new ArrayList<Pagamento>();
		}
		RequestContext.getCurrentInstance().update("tabelaHistorico");
	}
	
	
	
	public String getNomeClienteParaVenda() {
		return nomeClienteParaVenda;
	}

	public void setNomeClienteParaVenda(String nomeCliente) {
		this.nomeClienteParaVenda = nomeCliente;
	}

	public Pagamento getNewPagamento() {
		return newPagamento;
	}

	public void setNewPagamento(Pagamento newPagamento) {
		this.newPagamento = newPagamento;
	}

	public void setListAtualDeVenda(List<Venda> listAtualDeVenda) {
		this.listAtualDeVenda = listAtualDeVenda;
	}

	public String getNomeClienteParaHistorico() {
		return nomeClienteParaHistorico;
	}

	public void setNomeClienteParaHistorico(String nomeClienteParaHistorico) {
		this.nomeClienteParaHistorico = nomeClienteParaHistorico;
	}
	
	public List<Pagamento> getListAtualDeHistoricos(){
		return listAtualDeHistorico;
	}
	
	public List<Venda> getListAtualDeVenda() {
		return listAtualDeVenda;
	}

	public Cliente getClienteSelecionado() {
		return this.newPagamento.getCliente();
	}

	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.newPagamento.setValor(clienteSelecionado.getDebito());
		this.newPagamento.setCliente(clienteSelecionado);
		RequestContext.getCurrentInstance().update(":pagamanetoForm");
	}

	public Venda getVendaASerPaga() {
		return vendaASerPaga;
	}

	public void setVendaASerPaga(Venda vendaASerPaga) {
		this.vendaASerPaga = vendaASerPaga;
		setClienteSelecionado(vendaASerPaga.getCliente());
		setNomeClienteParaVenda(getClienteSelecionado().getNome());
		if(vendaASerPaga.getValorTotalDaVendaComDesconto() <= getClienteSelecionado().getDebito()){
			this.newPagamento.setValor(vendaASerPaga.getValorNaoPagoDaVenda());
		}else{
			this.newPagamento.setValor(vendaASerPaga.getCliente().getDebito());
		}
		RequestContext.getCurrentInstance().update(":pagamanetoForm");
	}
	
	public void limparObsPagamento(){
		newPagamento.setObservacao("");
	}
	public double getTroco() {
		return troco;
	}

	public void setTroco(double troco) {
		this.troco = troco;
	}
	
	private void retirarClienteSelecionado() {
		newPagamento.setCliente(null);
		newPagamento.setValor(0);
	}
		
}
