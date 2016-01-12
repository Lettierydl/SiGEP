package com.twol.sigep.ui.managedbean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.twol.sigep.Facede;
import com.twol.sigep.model.configuracoes.ConfiguracaoSistema;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.exception.FuncionarioNaoAutorizadoException;
import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.vendas.Divida;
import com.twol.sigep.model.vendas.ItemDeVenda;
import com.twol.sigep.model.vendas.Pagamento;
import com.twol.sigep.model.vendas.Pagavel;
import com.twol.sigep.model.vendas.Venda;
import com.twol.sigep.util.OperacaoStringUtil;
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
	
	private StreamedContent filePDF;
	
	int maxResult = 10;

	private Map<Pagavel, Boolean> checked = new HashMap<Pagavel, Boolean>();

	private Pagavel vendaASerPaga = null;

	private List<Pagamento> listAtualDeHistorico;

	private List<Pagavel> listAtualDeVenda;

	private Venda vendaDetalhar;

	public PagamentoManagedBean() {
		f = new Facede();
		newPagamento = new Pagamento();
		newDivida = new Divida();
		listAtualDeHistorico = f.getListaPagamentoHoje();
	}
	
	public void recalcularDebitoDoCliente(){
		try {
			Cliente e = f.buscarClientePorId(newPagamento.getCliente().getId());
			double oud_debito = e.getDebito();
			double new_debito = f.recalcularDebitoDoCliente(e);
			if(oud_debito != new_debito){
				SessionUtil.exibirMensagem(new FacesMessage(
						FacesMessage.SEVERITY_WARN,
						"Débito atulizado para " + OperacaoStringUtil.formatarStringValorMoedaComDescricao(new_debito),
						"Débito atulizado para " + OperacaoStringUtil.formatarStringValorMoedaComDescricao(new_debito)));
				setClienteSelecionado(f.buscarClientePorId(newPagamento.getCliente().getId()));
			}else{
				SessionUtil.exibirMensagem(new FacesMessage(
						FacesMessage.SEVERITY_INFO,
						"Débito já atualizado",
						"Débito já atualizado"));
			}
		} catch (Exception e) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Erro ao recalcular!",
					"Adicione o cliente!"));
		}
	}

	
	public void abrirModalPagar(){
		RequestContext.getCurrentInstance().execute(
				"abrirModa('modalPagar');");
	}
	
	public void cadastrarPagamento() throws IOException {
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
		}else if (newPagamento.getValor() == 0){
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Valor não pode ser zero ",
					"Pagamento com valor zero"));
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
		Cliente cli = newPagamento
				.getCliente();
		newPagamento = new Pagamento();
		
		listAtualDeHistorico = f.getListaPagamentoDoCliente(cli);
		cli = f.buscarClientePorId(cli.getId());
		setNomeClienteParaVenda("");
		setClienteSelecionado(null);
		modificarListaAtualDeVendasPeloCliente();
		 
		SessionUtil.exibirMensagem(new FacesMessage(
				FacesMessage.SEVERITY_INFO,
				"Pagamento realizado com sucesso, \n Saldo devedor: "
						+ new DecimalFormat("0.00").format(cli.getDebito()),
				"Pagamento realizado"));
		RequestContext.getCurrentInstance().update("@all");
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
		if(ConfiguracaoSistema.getLimiteDeRegistro()){
			return f.buscarClientePorCPFOuNomeQueIniciam(query, ConfiguracaoSistema.getMaxResult());
		}else{
			return f.buscarClientePorCPFOuNomeQueIniciam(query);
		}
		
	}

	public void modificarListaAtualDeVendasPeloCliente() {
		List<Cliente> clientes = f
				.buscarClientePorCPFOuNomeQueIniciam(nomeClienteParaVenda);
		
		if(clientes.size() > 1 && clientes.get(1).getNome().startsWith(clientes.get(0).getNome())){
			clientes.remove(1);
		}
		
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
		if(ConfiguracaoSistema.getLimiteDeRegistro()){
			return f.buscarClientePorCPFOuNomeQueIniciam(nome, ConfiguracaoSistema.getMaxResult());
		}else{
			return f.buscarClientePorCPFOuNomeQueIniciam(nome);
		}
	}
	
	public List<Cliente> completNomeClienteDivida(String nome) {
		if(ConfiguracaoSistema.getLimiteDeRegistro()){
			return f.buscarClientePorCPFOuNomeQueIniciam(nome, ConfiguracaoSistema.getMaxResult());
		}else{
			return f.buscarClientePorCPFOuNomeQueIniciam(nome);
		}
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
	
	
	public void abrirModalDetalheVenda(int IdVenda){
		this.vendaDetalhar = f.buscarVendaPeloId(IdVenda);
		this.vendaDetalhar.recalcularTotal();
		this.vendaDetalhar.getItensDeVenda();
		
		RequestContext.getCurrentInstance().execute(
				"abrirModa('modalDetalheVenda');");
	}
	
	public List<ItemDeVenda> getItensDetalheVenda() {
		if(vendaDetalhar == null){
			return new ArrayList<ItemDeVenda>();
		}
		List<ItemDeVenda> itens = vendaDetalhar.getItensDeVenda();
		Collections.sort(itens);
		return itens;
	}
	
	public void gerarPDFDaVenda() throws FileNotFoundException {
		try {
			String path = f.gerarPdfDaVendaVenda(vendaDetalhar, f.buscarItensDaVendaPorIdDaVenda(vendaDetalhar.getId()));

			InputStream stream = new FileInputStream(path);
			filePDF = new DefaultStreamedContent(stream, "application/pdf",
					"Venda.pdf");
			RequestContext.getCurrentInstance().update(":gerarPDFButon");
			return;
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe
							.getMessage()));
		} catch (Exception e) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"PDF não gerado, contate o suporte",
					"PDF não gerada, contate o suporte"));
			RequestContext.getCurrentInstance().update("@all");
		}
	}
	
	public void imprimirVendaDetalheECF(){
		f.imprimirVenda(vendaDetalhar);
	}
	
	@Deprecated
	public void imprimirVendaDetalhe(){
		String path = "";
		try {
			path = f.gerarPdfDaVendaVenda(vendaDetalhar, f.buscarItensDaVendaPorIdDaVenda(vendaDetalhar.getId()));
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe
							.getMessage()));
		}
		RequestContext.getCurrentInstance().execute("window.print('"+path+"');");;
	}
	
	
	public Venda getDetalheVenda(){
		return this.vendaDetalhar;
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
		//this.newPagamento.setValor(clienteSelecionado.getDebito());
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


	public StreamedContent getFilePDF() {
		return filePDF;
	}


	public void setFilePDF(StreamedContent filePDF) {
		this.filePDF = filePDF;
	}
	
	
}
