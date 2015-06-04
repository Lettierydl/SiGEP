package com.twol.sigep.ui.managedbean;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.PartialViewContext;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.vendas.Divida;
import com.twol.sigep.model.vendas.Pagamento;
import com.twol.sigep.model.vendas.Pagavel;
import com.twol.sigep.model.vendas.Venda;
import com.twol.sigep.util.SessionUtil;

@ViewScoped
@ManagedBean(name = "pagamentoBean")
public class PagamentoManagedBean {
	private Facede f;

	private Pagamento newPagamento;
	private Divida newDivida;
	private double troco = 0;
	private String nomeClienteParaVenda = "";
	private String nomeClienteDivida = "";
	private String nomeClienteParaHistorico = "";

	private Map<Pagavel, Boolean> checked = new HashMap<Pagavel, Boolean>();

	private Pagavel vendaASerPaga = null;

	private List<Pagamento> listAtualDeHistorico;

	private List<Pagavel> listAtualDeVenda;

	public PagamentoManagedBean() {
		f = new Facede();
		newPagamento = new Pagamento();
		newDivida = new Divida();
		listAtualDeHistorico = f.getListaPagamentoHoje();
	}

	public void cadastrarPagamento() throws IOException {
		double value = newPagamento.getValor();
		newPagamento.setFuncionario(SessionUtil.getFuncionarioLogado());
		
		if (newPagamento.getValor() < 0) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Valor não pode ser negativo -"
							+ new DecimalFormat("0.00").format(newPagamento
									.getCliente().getDebito()),
					"Pagamento com valor negativo"));
			RequestContext.getCurrentInstance().update("@form");
			return;
		}
		
		try {
			troco = f.adicionarPagamento(newPagamento);
		} catch (ParametrosInvalidosException e) {
			e.printStackTrace();
		} catch (EntidadeNaoExistenteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		newPagamento = new Pagamento();
		modificarListaAtualDeVendasPeloCliente();
		listAtualDeHistorico = f.getListaPagamentoDoCliente(newPagamento
				.getCliente());

		SessionUtil.exibirMensagem(new FacesMessage(
				FacesMessage.SEVERITY_INFO,
				"Pagamento realizado com sucesso \n Saldo devedor: "
						+ new DecimalFormat("0.00").format(newPagamento
								.getCliente().getDebito()),
				"Pagamento realizado"));
		RequestContext.getCurrentInstance().update("@form");
	}

	public void cadastrarDivida(){
		Cliente c ;
		try{
			 c = f.buscarClientePorNome(nomeClienteDivida);
		}catch(Exception e){
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Selecione um cliente",
					"Selecione um cliente"));
			return;
		}
		try{
			newDivida.setDia(Calendar.getInstance());
			newDivida.setFuncionario(SessionUtil.getFuncionarioLogado());
			newDivida.setCliente(c);
			f.adicionarDivida(newDivida, c);
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_INFO,
					"Dívida adicionada ao cliente "+ c.getNome(),
					"Dívida adicionada ao cliente "+ c.getNome()));
			newDivida = new Divida();
		}catch(Exception e){
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Reveja o formulário",
					"Reveja o formulário"));
		}
	}
	
	
	
	public void vendaSelecionada() {
		double value = 0;
		for (Pagavel v : listAtualDeVenda) {
			if (this.checked.containsKey(v)
					&& this.checked.get(v)) {
				value += v.getTotal() - v.getPartePaga();
			}
		}
		this.newPagamento.setValor(value);
		RequestContext.getCurrentInstance().update("valorPagamentoInput");
	}

	public List<Cliente> completNomeCliente(String query) {
		return f.buscarClientePorCPFOuNomeQueIniciam(query);
	}

	public void modificarListaAtualDeVendasPeloCliente() {
		List<Cliente> clientes = f
				.buscarClientePorCPFOuNomeQueIniciam(nomeClienteParaVenda);
		if (nomeClienteParaVenda.isEmpty()) {
			listAtualDeVenda = new ArrayList<Pagavel>();
			retirarClienteSelecionado();
		} else if (clientes.size() > 1) {
			listAtualDeVenda = f.buscarPagaveisNaoPagosDosClientes(clientes);
			retirarClienteSelecionado();
		} else if (clientes.size() == 1) {
			setClienteSelecionado(clientes.iterator().next());
			listAtualDeVenda = f.buscarPagaveisNaoPagoDoCliente(clientes
					.iterator().next());
		} else {
			listAtualDeVenda = new ArrayList<Pagavel>();
			retirarClienteSelecionado();
		}
	}

	public List<Cliente> completNomeClienteHistorico(String nome) {
		return f.buscarClientePorCPFOuNomeQueIniciam(nome);
	}
	
	public List<Cliente> completNomeClienteDivida(String nome) {
		return f.buscarClientePorCPFOuNomeQueIniciam(nome);
	}

	public void modificarListaAtualDeHistoricoDoCliente() {
		List<Cliente> clientes = f
				.buscarClientePorCPFOuNomeQueIniciam(nomeClienteParaHistorico);
		if (nomeClienteParaHistorico.isEmpty()) {
			listAtualDeHistorico = f.getListaPagamentoHoje();
		} else if (clientes.size() > 1) {
			listAtualDeHistorico = f.getListaPagamentoDosClientes(clientes);
		} else if (clientes.size() == 1) {
			listAtualDeHistorico = f.getListaPagamentoDoCliente(clientes
					.iterator().next());
		} else {
			listAtualDeHistorico = new ArrayList<Pagamento>();
		}
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

	public void setListAtualDeVenda(List<Pagavel> listAtualDeVenda) {
		this.listAtualDeVenda = listAtualDeVenda;
	}

	public String getNomeClienteParaHistorico() {
		return nomeClienteParaHistorico;
	}

	public void setNomeClienteParaHistorico(String nomeClienteParaHistorico) {
		this.nomeClienteParaHistorico = nomeClienteParaHistorico;
	}

	public List<Pagamento> getListAtualDeHistoricos() {
		return listAtualDeHistorico;
	}

	public List<Pagavel> getListAtualDeVenda() {
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

	public Pagavel getVendaASerPaga() {
		return vendaASerPaga;
	}

	public void setVendaASerPaga(Pagavel vendaASerPaga) {
		this.vendaASerPaga = vendaASerPaga;
		setClienteSelecionado(vendaASerPaga.getCliente());
		setNomeClienteParaVenda(getClienteSelecionado().getNome());
		if (vendaASerPaga.getTotal() <= getClienteSelecionado()
				.getDebito()) {
			this.newPagamento.setValor(vendaASerPaga.getValorNaoPago());
		} else {
			this.newPagamento.setValor(vendaASerPaga.getCliente().getDebito());
		}
		RequestContext.getCurrentInstance().update(":pagamanetoForm");
	}

	public void limparObsPagamento() {
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

	public Map<Pagavel, Boolean> getChecked() {
		return checked;
	}

	public void setChecked(Map<Pagavel, Boolean> checked) {
		this.checked = checked;
	}

	public String getNomeClienteDivida() {
		return nomeClienteDivida;
	}

	public void setNomeClienteDivida(String nomeClienteDivida) {
		this.nomeClienteDivida = nomeClienteDivida;
	}

	public Divida getNewDivida() {
		return newDivida;
	}

	public void setNewDivida(Divida newDivida) {
		this.newDivida = newDivida;
	}
	
	
}
