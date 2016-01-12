package com.twol.sigep.ui.managedbean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.NoResultException;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.model.configuracoes.ConfiguracaoSistema;
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
	private double quantidadeProd = 1;
	private String codigo;
	private String codigoProd;
	private String nomeBusca;
	private String nomeBuscaProd;

	private static LinkedList<ItemDeVenda> mercadorias;
	private static double totalMercadorias;

	public VendaManagedBean() {
		f = new Facede();
		verificarVendasPendentes();
		try {
			f.refreshValorDeVendaAtual();
		} catch (Exception e) {
		}

		if (SessionUtil.getNextMensagem() != null) {
			// System.out.println("em Vendas: "+SessionUtil.getNextMensagem().getDetail());
			FacesContext.getCurrentInstance().addMessage(null,
					SessionUtil.getNextMensagem());
			SessionUtil.removerNextMensagem();
		} else {
			// System.out.println("em Vendas chegou null");
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
		try {
			return pendentes.size();
		} catch (NullPointerException ne) {
			return 1;
		}
	}

	private String digitado_ant_prod = "";

	public void digitadoMercadoria(ValueChangeEvent event) {
		try {
			UIInput in = (UIInput) event.getComponent();
			String digitado = (String) event.getNewValue();

			if (digitado.length() - digitado_ant_prod.length() > 4
					&& digitado.startsWith(digitado_ant_prod)
					&& digitado_ant_prod.matches("^[0-9]*$")) {
				try {

					f.buscarProdutoPorCodigo(digitado);
					codigoProd = digitado;

					inserirProdutoNasMercadorias();
					codigoProd = "";
					in.setValue(codigoProd);
					in.resetValue();
					//
					// RequestContext.getCurrentInstance().update("quantidadeProd");

					RequestContext.getCurrentInstance().update("codigoProd");
					RequestContext.getCurrentInstance().update(
							"produtosConsultados");
					RequestContext.getCurrentInstance().update("@form");
					RequestContext.getCurrentInstance().execute(
							"$('#codigoProd_input').val('');");
					return;
				} catch (NoResultException e) {
				} catch (Exception e) {
				}
			}

			codigoProd = digitado.split(" R$")[0];
			char ultimoCaracter = ' ';
			try {
				ultimoCaracter = digitado.charAt(digitado.length() - 1);
			} catch (NullPointerException nu) {
				return;
			}
			if (ultimoCaracter == '*') {
				quantidadeProd = Double.valueOf(digitado.replace("*", "")
						.replace(",", "."));
				codigoProd = "";
				in.setValue(codigoProd);
				RequestContext.getCurrentInstance().update("codigoProd");
				RequestContext.getCurrentInstance().update("quantidadeProd");
				RequestContext.getCurrentInstance().execute(
						"$('#codigoProd_input').val('');");
			} else if (digitado.length() == 13) {
				if (codigoProd.startsWith("2")
						|| f.buscarProdutoPorCodigo(digitado) != null) {
					codigoProd = digitado;

					inserirProdutoNasMercadorias();
					codigoProd = "";
					in.setValue(codigoProd);
					in.resetValue();
					//
					// RequestContext.getCurrentInstance().update("quantidadeProd");

					RequestContext.getCurrentInstance().update("codigoProd");
					RequestContext.getCurrentInstance().update(
							"produtosConsultados");
					RequestContext.getCurrentInstance().update("@form");
					RequestContext.getCurrentInstance().execute(
							"$('#codigoProd_input').val('');");
				}
			}
		} catch (Exception e) {

		}
		digitado_ant_prod = codigoProd;

		// addItemAVenda(p, quantidade);
	}

	public void verificarProdutoPorNome() {
		Produto p;
		try {
			nomeBusca = nomeBusca.replace(" R$ ", ";").split(";")[0];
			p = f.buscarProdutoPorDescricao(nomeBusca);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Produto ( "
							+ nomeBusca
							+ " ) não cadastrado com essa descrição",
							"Produto Invalido")));
			return;// nenhum produto encontrado
		}
		if (quantidade == 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					(new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Quantidade não pode ser zero",
							"Quantidade Invalida")));
			quantidade = 1;
			RequestContext.getCurrentInstance().update("@all");
			return;
		} else if (quantidade < 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					(new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Quantidade não pode negativa",
							"Quantidade Invalida")));
			quantidade = 1;
			RequestContext.getCurrentInstance().update("quantidade");
			return;
		}
		addItemAVenda(p, quantidade);
		codigo = "";
		nomeBusca = "";
		quantidade = 1;
		// RequestContext.getCurrentInstance().update("@all");
	}

	public void verificarProduto() {
		Produto p;
		try {
			codigo = codigo.replace(" R$ ", ";").split(";")[0];
			if (codigo.startsWith("2") && codigo.length() == 13) {
				String vTotal = codigo.substring(8, 10) + "."
						+ codigo.substring(10, 12);
				double total = Double.valueOf(vTotal);

				p = f.buscarProdutoPorDescricaoOuCodigo(codigo.substring(0, 7));
				this.quantidade = total / p.getValorDeVenda();
			} else {
				p = f.buscarProdutoPorDescricaoOuCodigo(codigo);
			}
		} catch (Exception e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							(new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									"Produto ( " + codigo + " ) não cadastrado",
									"Produto Invalido")));
			return;// nenhum produto encontrado
		}

		if (quantidade == 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					(new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Quantidade não pode ser zero",
							"Quantidade Invalida")));
			quantidade = 1;
			RequestContext.getCurrentInstance().update("@all");
			return;
		} else if (quantidade < 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					(new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Quantidade não pode negativa",
							"Quantidade Invalida")));
			quantidade = 1;
			RequestContext.getCurrentInstance().update("quantidade");
			return;
		}
		addItemAVenda(p, quantidade);
		codigo = "";
		quantidade = 1;
		RequestContext.getCurrentInstance().update("@all");
	}

	public void inserirProdutoNasMercadorias() {

		Produto p;
		try {
			codigoProd = codigoProd.replace(" R$ ", ";").split(";")[0];
			if (codigoProd.startsWith("2") && codigoProd.length() == 13) {
				String vTotal = codigoProd.substring(8, 10) + "."
						+ codigoProd.substring(10, 12);
				double total = Double.valueOf(vTotal);

				p = f.buscarProdutoPorDescricaoOuCodigo(codigoProd.substring(0,
						7));
				this.quantidadeProd = total / p.getValorDeVenda();
			} else {
				p = f.buscarProdutoPorDescricaoOuCodigo(codigoProd);
			}
			if (quantidadeProd == 0) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						(new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Quantidade não pode ser zero",
								"Quantidade Invalida")));
				quantidadeProd = 1;
				RequestContext.getCurrentInstance().update("@form");
				return;
			} else if (quantidadeProd < 0) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						(new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Quantidade não pode negativa",
								"Quantidade Invalida")));
				quantidadeProd = 1;
				RequestContext.getCurrentInstance().update("quantidadeProd");
				return;
			}

			ItemDeVenda it = new ItemDeVenda(p, quantidadeProd);
			mercadorias.addFirst(it);
			totalMercadorias += it.getTotal();

			codigoProd = "";
			quantidadeProd = 1;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Produto ( "
							+ codigoProd + " ) não cadastrado",
							"Produto Invalido")));

			return;// nenhum produto encontrado
		}

	}

	public void inserirProdutoNasMercadoriasPorNome() {

		Produto p;
		try {
			nomeBuscaProd = nomeBuscaProd.replace(" R$ ", ";").split(";")[0];
			p = f.buscarProdutoPorDescricaoOuCodigo(nomeBuscaProd);
			if (quantidadeProd == 0) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						(new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Quantidade não pode ser zero",
								"Quantidade Invalida")));
				quantidadeProd = 1;
				RequestContext.getCurrentInstance().update("@form");
				return;
			} else if (quantidadeProd < 0) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						(new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Quantidade não pode negativa",
								"Quantidade Invalida")));
				quantidadeProd = 1;
				RequestContext.getCurrentInstance().update("quantidadeProd");
				return;
			}

			ItemDeVenda it = new ItemDeVenda(p, quantidadeProd);
			mercadorias.addFirst(it);
			totalMercadorias += it.getTotal();

			codigoProd = "";
			nomeBuscaProd = "";
			quantidadeProd = 1;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Produto ( "
							+ nomeBuscaProd + " ) não cadastrado",
							"Produto Invalido")));

			return;// nenhum produto encontrado
		}

	}

	public void addItemAVenda(Produto p, double quantidade) {
		ItemDeVenda it = new ItemDeVenda();
		it.setProduto(p);
		it.setQuantidade(quantidade);
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
		if (ConfiguracaoSistema.getLimiteDeRegistro()) {
			return f.buscarDescricaoEPrecoProdutoPorDescricaoQueInicia(query,
					ConfiguracaoSistema.getMaxResult());
		} else {
			return f.buscarDescricaoEPrecoProdutoPorDescricaoQueInicia(query);
		}
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

	public void removerItemDaVenda() {
		try {
			if (removerItem == null) {
				RequestContext.getCurrentInstance().execute(
						"fecharModal('modalRemoverItem');");
				this.removerItem = null;
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_WARN, "Item Já Removido",
						"Item já removido"));
				return;
			}
			f.removerItemDaVenda(removerItem);
			f.refreshValorDeVendaAtual();
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
		List<ItemDeVenda> itens = f.buscarItensDaVendaPorIdDaVenda(f
				.getVendaAtual().getId());
		Collections.sort(itens);
		return itens;
	}

	public ItemDeVenda getUltimoItemVendido() {
		try {
			List<ItemDeVenda> itens = f.buscarItensDaVendaPorIdDaVenda(f
					.getVendaAtual().getId());
			Collections.sort(itens);
			return itens.get(0);
		} catch (IndexOutOfBoundsException i) {
			return null;
		}
	}

	public ItemDeVenda getUltimoItemMercadorias() {
		try {
			List<ItemDeVenda> itens = this.getMercadorias();
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

			RequestContext.getCurrentInstance().update("@all");

			// System.out.println(uri);
			if (uri.equalsIgnoreCase("finalizar_a_vista.jsf")) {
				RequestContext.getCurrentInstance().execute(
						"window.location = 'finalizar_a_vista.jsf';");
			} else if (uri.equalsIgnoreCase("finalizar_a_prazo.jsf")) {
				RequestContext.getCurrentInstance().execute(
						"window.location = 'finalizar_a_prazo.jsf';");
			}

			// SessionUtil.redirecionarParaPage(uri);
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Não há itens nessa venda", "Venda Vazia"));
		}
	}

	public void refreshValorVendaAtual() {
		try {
			f.refreshValorDeVendaAtual();
		} catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_WARN, "Venda não sincronizada",
					"Venda não sincronizada"));
			return;
		}
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
				"Venda atualizada", "Venda atualizada"));
		RequestContext.getCurrentInstance().update("@all");
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

	public String getCodigoProd() {
		return codigoProd;
	}

	public void setCodigoProd(String codigoProd) {
		this.codigoProd = codigoProd;
	}
	
	public double quantidadeVendida30Dias(int idProduto){
		return f.getRelatorioDeProduto30Dias(idProduto);
	}
	
	public List<ItemDeVenda> getMercadorias() {
		if (mercadorias == null) {
			mercadorias = new LinkedList<ItemDeVenda>();
		}
		return mercadorias;
	}

	public double getTotalMercadorias() {
		if (totalMercadorias == 0 && !getMercadorias().isEmpty()) {
			double total = 0;
			for (ItemDeVenda it : mercadorias) {
				total += it.getTotal();
			}
			return total;
		}
		return totalMercadorias;

	}

	public double getQuantidadeProd() {
		return quantidadeProd;
	}

	public void setQuantidadeProd(double quantidadeProd) {
		this.quantidadeProd = quantidadeProd;
	}

	public String getNomeBusca() {
		return nomeBusca;
	}

	public void setNomeBusca(String nomeBusca) {
		this.nomeBusca = nomeBusca;
	}

	public String getNomeBuscaProd() {
		return nomeBuscaProd;
	}

	public void setNomeBuscaProd(String nomeBuscaProd) {
		this.nomeBuscaProd = nomeBuscaProd;
	}

	/*
	 * 
	 * private String digitado_ant = ""; public void digitado(ValueChangeEvent
	 * event) { try { String digitado = (String) event.getNewValue();
	 * 
	 * if(digitado.contains("\n")){ f.buscarProdutoPorCodigo(digitado); codigo =
	 * digitado; verificarProduto(); codigo = ""; UIInput in = (UIInput)
	 * event.getComponent(); in.setValue(codigo);
	 * RequestContext.getCurrentInstance().update("codigo");
	 * RequestContext.getCurrentInstance().update("quantidade");
	 * RequestContext.getCurrentInstance().update("itensVenda");
	 * RequestContext.getCurrentInstance().update("formPrincipal"); }else
	 * if(digitado.length() - digitado_ant.length() > 4 &&
	 * digitado.startsWith(digitado_ant) && digitado.matches("^[0-9]*$")){ try{
	 * f.buscarProdutoPorCodigo(digitado);
	 * 
	 * codigo = digitado; verificarProduto(); codigo = "";
	 * 
	 * UIInput in = (UIInput) event.getComponent(); in.setValue(codigo);
	 * RequestContext.getCurrentInstance().update("codigo");
	 * RequestContext.getCurrentInstance().update("quantidade");
	 * RequestContext.getCurrentInstance().update("itensVenda");
	 * RequestContext.getCurrentInstance().update("formPrincipal"); return;
	 * }catch (NoResultException e) {} catch (Exception e) {} }
	 * 
	 * 
	 * char ultimoCaracter = ' '; try{ ultimoCaracter =
	 * digitado.charAt(digitado.length() - 1); }catch(NullPointerException nu){
	 * return; } if (ultimoCaracter == '*') { quantidade =
	 * Double.valueOf(digitado.replace("*", "").replace( ",", ".")); codigo =
	 * "";
	 * 
	 * UIInput in = (UIInput) event.getComponent(); in.setValue(codigo);
	 * RequestContext.getCurrentInstance().update("codigo");
	 * RequestContext.getCurrentInstance().update("quantidade"); }else
	 * if(digitado.length() == 13){ if(codigo.startsWith("2") ||
	 * f.buscarProdutoPorCodigo(digitado) != null){ codigo = digitado;
	 * 
	 * verificarProduto(); codigo = "";
	 * 
	 * UIInput in = (UIInput) event.getComponent(); in.setValue(codigo);
	 * RequestContext.getCurrentInstance().update("codigo");
	 * RequestContext.getCurrentInstance().update("quantidade");
	 * RequestContext.getCurrentInstance().update("itensVenda");
	 * RequestContext.getCurrentInstance().update("formPrincipal"); } } } catch
	 * (Exception e) {
	 * 
	 * }
	 * 
	 * digitado_ant = codigo; // addItemAVenda(p, quantidade); }
	 */

}
