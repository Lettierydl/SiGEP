package com.twol.sigep.ui.managedbean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Pagamento;
import com.twol.sigep.model.vendas.Venda;

@ManagedBean(name = "pagamentoBean")
@ViewScoped
public class PagamentoManagedBean {
	private Facede f;
	
	private Pagamento newPagamento;
	private String nomeCliente = "";

	private List<Pagamento> listAtualDePagamento;
	
	private List<Venda> listAtualDeVenda;
	
	public PagamentoManagedBean(){
		f = new Facede();
		newPagamento = new Pagamento();
		listAtualDePagamento = f.getListaPagamentoHoje();
		setListAtualDeVenda(f.getListaVendasNaoPagasDeHoje());
	}

	public void cadastrarPagamento(){
		try{
			validarInformacoes(newPagamento);
		}catch(Exception e){
			return;
		}
		f.adicionarPagamento(newPagamento);
		newPagamento = new Pagamento();
		listAtualDePagamento = f.getListaPagamentoDoCliente(newPagamento.getCliente());
	}
	
	public void validarInformacoes(Pagamento c){}
	
	public List<Pagamento> getListAtualDePagamentos(){
		return listAtualDePagamento;
	}
	
	public List<Venda> getListAtualDeVenda() {
		return listAtualDeVenda;
	}
	
	public void modificarListaAtualDePagamentoPeloCliente(){
		List<Cliente> clientes = f.buscarClientePorCPFOuNomeQueIniciam(nomeCliente);
		if(nomeCliente.isEmpty()){
			listAtualDePagamento = f.getListaPagamentoHoje();
		}else if(clientes.size() > 1 ){
			listAtualDePagamento = f.getListaPagamentoDosClientes(clientes);
		}else if(clientes.size() == 1){
			listAtualDePagamento = f.getListaPagamentoDoCliente(clientes.iterator().next());
			}else{
			listAtualDePagamento = new ArrayList<Pagamento>();
		}
		RequestContext.getCurrentInstance().update("tabelaPagamentos");
	}
	
	public List<String> autoCompletNomeCliente(String nome){
		nomeCliente = nome;
		System.out.println(nome);
		try{
			if(nome.isEmpty()){
				return new ArrayList<String>();
			}
			return f.buscarNomeClientePorNomeQueInicia(nome);
		}finally{
			modificarListaAtualDePagamentoPeloCliente();
		}
	}
	
	
	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
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

	
		
}
