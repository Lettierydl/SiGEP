package com.twol.sigep.ui.managedbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIInput;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.exception.VariasVendasPendentesException;
import com.twol.sigep.model.exception.VendaPendenteException;
import com.twol.sigep.model.vendas.ItemDeVenda;
import com.twol.sigep.model.vendas.Venda;

@ViewScoped
@ManagedBean(name = "vendaBean")
public class VendaManagedBean {

	private Facede f;
	private List<Venda> pendentes;
	private double quantidade = 1;
	private String codigo;

	public VendaManagedBean() {
		f = new Facede();
		int pend = vendasPendentes();
		if (pend > 1) {
			RequestContext.getCurrentInstance().execute(
					"abrirModa('modalVendasPendentes');");
		} else if (pend < 1) {
			iniciarVenda();
		}
	}

	private void iniciarVenda() {
		try {
			f.inicializarVenda();
		} catch (VendaPendenteException e) {
			e.printStackTrace();
		}
	}

	private int vendasPendentes() {
		try {
			Venda v = f.recuperarVendaPendente();
			pendentes = new ArrayList<Venda>();
			pendentes.add(v);
		} catch (VariasVendasPendentesException e) {
			pendentes = new ArrayList<Venda>(e.getVendasPendentes());
		} catch (EntidadeNaoExistenteException e) {
			pendentes = new ArrayList<Venda>();
		}
		return pendentes.size();
	}

	public void digitado(ValueChangeEvent event) {
		try {
			UIInput in = (UIInput) event.getComponent();
			String digitado = (String) event.getNewValue();
			char ultimoCaracter = digitado.charAt(digitado.length() - 1);
			if (ultimoCaracter == '*') {
				quantidade = Double.valueOf(digitado.replace("*", "").replace(
						",", "."));
				codigo = "";
				in.setValue(codigo);
				RequestContext.getCurrentInstance().update("codigo");
				RequestContext.getCurrentInstance().update("quantidade");
			}
		} catch (Exception e) {
		}
		
		// addItemAVenda(p, quantidade);
	}
	public void verificarProduto() {
		Produto p;
		try {
			p = f.buscarProdutoPorDescricaoOuCodigo(codigo);
		} catch (Exception e) {
			return;// nenhum produto encontrado
		}
		addItemAVenda(p, quantidade);
		codigo = ""; quantidade = 1;
		RequestContext.getCurrentInstance().update("@all");
	}

	public void addItemAVenda(Produto p, double quantidade) {
		ItemDeVenda it = new ItemDeVenda();
		it.setQuantidade(quantidade);
		it.setProduto(p);
		try {
			f.addItemAVenda(it);
		} catch (Exception e) {// venda nao existente, erro muito grande
			e.printStackTrace();
		}
	}
	
	public void removerItem(ItemDeVenda it){
		try {
			f.removerItemDaVenda(it);
			RequestContext.getCurrentInstance().update("@all");
		} catch (Exception e) {
			e.printStackTrace();// se venda nao existir
		}
	}

	public List<ItemDeVenda> getItens() {
		List<ItemDeVenda> itens = f.getVendaAtual().getItensDeVenda();
		Collections.sort(itens);
		return itens;
	}

	public ItemDeVenda getUltimoItemVendido() {
		List<ItemDeVenda> itens = f.getVendaAtual().getItensDeVenda();
		Collections.sort(itens);
		try{
			return itens.get(0);
		}catch(IndexOutOfBoundsException i){
			return null;
		}
	}

	public double getTotal() {
		try {
			return f.getVendaAtual().getTotal();
		} catch (Exception e) {
			return 0;
		}
	}

	public double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(double quantidade) {
		this.quantidade = quantidade;
	}

	public List<Venda> getPendentes() {
		return pendentes;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}
