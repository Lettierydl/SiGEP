package com.twol.sigep.ui.managedbean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.exception.VariasVendasPendentesException;
import com.twol.sigep.model.exception.VendaPendenteException;
import com.twol.sigep.model.vendas.ItemDeVenda;
import com.twol.sigep.model.vendas.Venda;
import com.twol.sigep.util.SessionUtil;

@ViewScoped
@ManagedBean(name = "vendaBean")
public class VendaManagedBean {

	private Facede f;
	private List<Venda> pendentes;
	private double quantidade = 1;
	private String codigo;

	public VendaManagedBean() {
		f = new Facede();
		verificarVendasPendentes();
		if (SessionUtil.getNextMensagem() != null) {
			FacesContext.getCurrentInstance().addMessage(null,
					SessionUtil.getNextMensagem());
			SessionUtil.removerNextMensagem();
		}
	}

	public void verificarVendasPendentes() {
		int pend = vendasPendentes();

		if (pend > 1) {// nao vai acontecer porque a venda por usuario esta
						// singleton
			RequestContext.getCurrentInstance().execute(
					"abrirModa('modalVendasPendentes');");
		} else if (pend < 1) {
			iniciarVenda();
		}

		if (f.getVendaAtual() == null) {// verifica se iniciou venda atual
			f.setAtualComVendaPendenteTemporariamente();// vai deixar para
														// escolher a venda no
														// modal
		}
	}

	public void retomarVendaPendete(Venda v) {
		try {
			f.selecionarVendaPendente(v.getId());
			RequestContext.getCurrentInstance().execute(
					"fecharModal('modalRemoverItem');");
			RequestContext.getCurrentInstance().update("@form");
		} catch (EntidadeNaoExistenteException e) {// venda deletada por outro
													// caixa
			e.printStackTrace();
		} catch (Exception e) {// erro com o banco
			e.printStackTrace();
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
		} catch (Exception e) {
			e.printStackTrace();// erro nao deve ocorrer, erro coneccao
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
			FacesContext.getCurrentInstance().addMessage(
					null,(new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Produto ( "+codigo+" ) não cadastrado",
							"Produto Invalido")));
			return;// nenhum produto encontrado
		}
		addItemAVenda(p, quantidade);
		codigo = "";
		quantidade = 1;
		RequestContext.getCurrentInstance().update("@all");
	}

	public void addItemAVenda(Produto p, double quantidade) {
		ItemDeVenda it = new ItemDeVenda();
		it.setQuantidade(quantidade);
		it.setProduto(p);
		try {
			f.addItemAVenda(it);
		} catch (Exception e) {// venda nao existente, erro grande
			e.printStackTrace();
		}
	}

	public List<String> getListDescricao() {
		if (codigo != null && codigo.length() > 3) {
			return f.buscarDescricaoProdutoPorDescricaoQueInicia(codigo);
		} else {
			return new ArrayList<String>();
		}
	}

	public List<String> completMetodo(String query) {
		return f.buscarDescricaoProdutoPorDescricaoQueInicia(query);
	}

	private ItemDeVenda removerItem = null;

	public void setRemoverItem(ItemDeVenda it) {
		this.removerItem = it;
		RequestContext.getCurrentInstance().execute(
				"abrirModa('modalRemoverItem');");
	}

	public void cancelarItemRemovido() {
		this.removerItem = null;
		RequestContext.getCurrentInstance().execute(
				"fecharModal('modalRemoverItem');");
	}

	public void removerItem() {
		try {
			f.removerItemDaVenda(removerItem);
			RequestContext.getCurrentInstance().execute(
					"fecharModal('modalRemoverItem');");
			this.removerItem = null;
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Item Removido",
					"Item removido com sucesso"));
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
		try {
			List<ItemDeVenda> itens = f.getVendaAtual().getItensDeVenda();
			Collections.sort(itens);
			return itens.get(0);
		} catch (IndexOutOfBoundsException i) {
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

	public void finalizarVenda(String uri) throws IOException {
		if (this.getTotal() != 0) {
			SessionUtil.putVendaAFinalizar(this.f.getVendaAtual());
			try {
				f.atualizarDataVendaAtual();
			} catch (Exception e) {// bug banco
				e.printStackTrace();
			}
			SessionUtil.redirecionarParaPage(uri);
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Não há itens nessa venda",
					"Venda Vazia"));
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
