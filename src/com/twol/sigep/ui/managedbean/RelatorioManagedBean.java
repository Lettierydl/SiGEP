package com.twol.sigep.ui.managedbean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.DonutChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import com.twol.sigep.Facede;
import com.twol.sigep.model.exception.FuncionarioNaoAutorizadoException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.vendas.Divida;
import com.twol.sigep.model.vendas.ItemDeVenda;
import com.twol.sigep.model.vendas.Pagamento;
import com.twol.sigep.model.vendas.Pagavel;
import com.twol.sigep.model.vendas.Venda;
import com.twol.sigep.util.SessionUtil;

@ViewScoped
@ManagedBean(name = "relatBean")
public class RelatorioManagedBean {
	private Facede f;

	private StreamedContent file;
	private StreamedContent filePlanilha;
	
	private StreamedContent fileVenda;
	
	private List<Pagavel> pagaveis;
	private Pagavel view;

	int maxResult = 10;

	private Date diaInicioEnt = new Date();
	private Date diaFimEnt = new Date();

	private Date diaInicioVend = new Date();
	private Date diaFimVend = new Date();

	private Date diaInicioProd = new Date();
	private Date diaFimProd = new Date();

	private Date diaInicioHis = new Date();
	private Date diaFimHis = new Date();

	private Date diaInicioGer = new Date();
	private Date diaFimGer = new Date();

	private String nomeCliente;

	public RelatorioManagedBean() {
		f = new Facede();
		criarEntrada();
		criarVendas();
		criarProduto();
		criarHistorico();
	}

	private DonutChartModel entrada;
	private DonutChartModel vendas;
	private BarChartModel produtos;
	private LineChartModel historico;
	private DonutChartModel geral;

	public double[] getValEntrada() {
		try {
			return f.getRelatorioDeEntradaDeCaixa(diaInicioEnt, diaFimEnt);
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe
							.getMessage()));
		} catch (Exception e) {
		}
		double[] val = { 0.0, 0.0 };
		return val;
	}

	public double[] getValVendas() {
		try {
			return f.getRelatorioDeVendas(diaInicioVend, diaFimVend);
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe
							.getMessage()));
		} catch (Exception e) {
		}
		double[] val = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		return val;
	}

	public double[] getValProduto() {
		try {
			return f.getRelatorioDeProduto(diaInicioProd, diaFimProd);
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe
							.getMessage()));
		} catch (Exception e) {
		}
		double[] val = { 0.0, 0.0 };
		return val;
	}

	public void criarEntrada() {
		entrada = new DonutChartModel();

		Map<String, Number> circle1 = new LinkedHashMap<String, Number>();
		double[] val = getValEntrada();
		circle1.put("Vendas à vista", val[0]);
		circle1.put("Pagamentos", val[1]);

		entrada.addCircle(circle1);
		entrada.setLegendPosition("r");
		entrada.setShowDataLabels(true);
		entrada.setShadow(false);
		entrada.setMouseoverHighlight(true);
	}

	public void criarVendas() {
		vendas = new DonutChartModel();

		Map<String, Number> circle1 = new LinkedHashMap<String, Number>();
		double[] val = getValVendas();
		circle1.put("Vendas à vista", val[0]);
		circle1.put("Vendas a prazo", val[1]);
		circle1.put("Dívidas", val[2]);
		vendas.addCircle(circle1);
		vendas.setLegendPosition("r");
		vendas.setShowDataLabels(true);
		vendas.setShadow(false);
		vendas.setMouseoverHighlight(true);
	}

	public void criarProduto() {
		produtos = new BarChartModel();

		double[] val = getValProduto();
		ChartSeries tot = new ChartSeries();
		tot.set("Total vendido", val[0]);
		tot.setLabel("Total vendido");

		ChartSeries luc = new ChartSeries();
		luc.setLabel("Lucro");
		luc.set("Lucro", val[1]);

		produtos.addSeries(tot);
		produtos.addSeries(luc);

		Axis yAxis = produtos.getAxis(AxisType.Y);
		yAxis.setMin(0);
		yAxis.setMax((int) val[0] + (val[0] * 0.3));

		produtos.setLegendPosition("rs");
		produtos.setShadow(false);
		produtos.setMouseoverHighlight(true);
		produtos.setAnimate(true);
	}

	public void criarHistorico() {
		historico = new LineChartModel();
		
		historico.setTitle("Grafico");
		
		Cliente c;

		try {
			c = f.buscarClientePorNome(nomeCliente);
		} catch (Exception e) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Cliente " + nomeCliente
							+ " não encontrado", "Cliente " + nomeCliente
							+ " não encontrado"));
			return;
		}

		List<Pagamento> pagamentos= new ArrayList<Pagamento>();
		pagaveis = new ArrayList<Pagavel>();
		
		try {
			pagamentos = f.getListaPagamentoDoCliente(c, diaInicioHis,
					diaFimHis);
		} catch (Exception e) {}
		
		try {
			pagaveis = f.buscarPagaveisDoCliente(c, diaInicioHis,
					diaFimHis);
		} catch (Exception e) {}
		

		LineChartSeries lineVendas = new LineChartSeries();
		lineVendas.setLabel("Vendas");

		LineChartSeries linePagamentos = new LineChartSeries();
		linePagamentos.setLabel("Pagamentos");

		try {

			for (Pagavel p : pagaveis) {
				String data = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(p
						.getDia().getTime());
				lineVendas.set(data, p.getTotal());
			}

			for (Pagamento p : pagamentos) {
				String data = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(p
						.getData().getTime());
				linePagamentos.set(data, p.getValor());
			}

		} catch (Exception e) {
			e.printStackTrace();
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Erro ao gerar grafo",
					"Erro ao gerar grafo"));
		}

		historico.addSeries(linePagamentos);
		historico.addSeries(lineVendas);
		
		
		historico.setLegendPosition("rs");
		historico.setShadow(false);
		historico.setMouseoverHighlight(true);
		
		
		
		historico.setZoom(true);
		historico.getAxis(AxisType.Y).setLabel("Valores (R$)");
        DateAxis axis = new DateAxis("Datas");
        axis.setTickAngle(-50);
        //axis.setMax("2014-02-01");
        axis.setTickFormat("%b %#d, %y");
         
        historico.getAxes().put(AxisType.X, axis);
		
		
		historico.setAnimate(true);
	}

	public void gerarPDFDaVenda() throws FileNotFoundException {
		try {
			String path = f.gerarPdfDaVendaVenda((Venda) view, this.getListItensViewVenda());

			InputStream stream = new FileInputStream(path);
			fileVenda = new DefaultStreamedContent(stream, "application/pdf",
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
					"PDF não gerado, reveja suas datas",
					"PDF não gerada, reveja suas datas"));
			RequestContext.getCurrentInstance().update("@all");
		}
	}
	
	
	public void gerarRelatorioBalancoProdutosPDF() throws FileNotFoundException {
		try {
			String path = f.gerarPdfRelatorioBalancoProdutos(diaInicioGer,
					diaFimGer);

			InputStream stream = new FileInputStream(path);
			file = new DefaultStreamedContent(stream, "application/pdf",
					"Relatorio_Balanco_Produto.pdf");
			RequestContext.getCurrentInstance().update("@all");
			return;
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe
							.getMessage()));
		} catch (Exception e) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Planilha não gerada, reveja suas datas",
					"Planilha não gerada, reveja suas datas"));
			RequestContext.getCurrentInstance().update("@all");
		}
	}

	public void gerarRelatorioBalancoProdutosPlanilha()
			throws FileNotFoundException {
		try {
			String path = f.gerarPlanilhaRelatorioBalancoProdutos(diaInicioGer,
					diaFimGer);

			InputStream stream = new FileInputStream(path);
			filePlanilha = new DefaultStreamedContent(stream,
					"application/xls", "Relatorio_Balanco_Produto.xls");
			RequestContext.getCurrentInstance().update("@all");
			return;
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe
							.getMessage()));
		} catch (Exception e) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Planilha não gerada, reveja suas datas",
					"Planilha não gerada, reveja suas datas"));
			RequestContext.getCurrentInstance().update("@all");
		}
	}

	public List<Cliente> completNomeCliente(String query) {
		return f.buscarClientePorCPFOuNomeQueIniciam(query, maxResult);
	}

	public void openViewPag(Pagavel pag){
		this.view = pag;
		if(this.view instanceof Venda){
			RequestContext.getCurrentInstance().execute("abrirModa('modalViewVenda');");
		}else{
			RequestContext.getCurrentInstance().execute("abrirModa('modalViewDivida');");
		}
		
	}
	
	
	
	// Gets and Sets

	public String getMaxDia() {
		return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
	}

	public StreamedContent getFile() {
		return file;
	}

	public StreamedContent getFilePlanilha() {
		return filePlanilha;
	}

	public String getMinDia() {
		return f.getMinDiaRegistroVenda();
	}

	public DonutChartModel getVendas() {
		return vendas;
	}

	public BarChartModel getProdutos() {
		return produtos;
	}

	public DonutChartModel getGeral() {
		return geral;
	}

	public DonutChartModel getEntrada() {
		return entrada;
	}

	public Date getDiaInicioEnt() {
		return diaInicioEnt;
	}

	public void setDiaInicioEnt(Date diaInicioEnt) {
		this.diaInicioEnt = diaInicioEnt;
	}

	public Date getDiaFimEnt() {
		return diaFimEnt;
	}

	public void setDiaFimEnt(Date diaFimEnt) {
		this.diaFimEnt = diaFimEnt;
	}

	public Date getDiaInicioVend() {
		return diaInicioVend;
	}

	public void setDiaInicioVend(Date diaInicioVend) {
		this.diaInicioVend = diaInicioVend;
	}

	public Date getDiaFimVend() {
		return diaFimVend;
	}

	public void setDiaFimVend(Date diaFimVend) {
		this.diaFimVend = diaFimVend;
	}

	public Date getDiaInicioProd() {
		return diaInicioProd;
	}

	public void setDiaInicioProd(Date diaInicioProd) {
		this.diaInicioProd = diaInicioProd;
	}

	public Date getDiaFimProd() {
		return diaFimProd;
	}

	public void setDiaFimProd(Date diaFimProd) {
		this.diaFimProd = diaFimProd;
	}

	public Date getDiaInicioGer() {
		return diaInicioGer;
	}

	public void setDiaInicioGer(Date diaInicioGer) {
		this.diaInicioGer = diaInicioGer;
	}

	public Date getDiaFimGer() {
		return diaFimGer;
	}

	public void setDiaFimGer(Date diaFimGer) {
		this.diaFimGer = diaFimGer;
	}

	public Date getDiaInicioHis() {
		return diaInicioHis;
	}

	public void setDiaInicioHis(Date diaInicioHis) {
		this.diaInicioHis = diaInicioHis;
	}

	public Date getDiaFimHis() {
		return diaFimHis;
	}

	public void setDiaFimHis(Date diaFimHis) {
		this.diaFimHis = diaFimHis;
	}

	public LineChartModel getHistorico() {
		return historico;
	}

	public void setHistorico(LineChartModel historico) {
		this.historico = historico;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public List<Pagavel> getPagaveis() {
		return pagaveis;
	}

	public void setPagaveis(List<Pagavel> pagaveis) {
		this.pagaveis = pagaveis;
	}

	public Venda getViewVenda() {
		try{
			return  (Venda) view;
		}catch(ClassCastException cs){
			return null;
		}
	}
	
	public List<ItemDeVenda> getListItensViewVenda(){
		Venda v = getViewVenda();
		if(v != null){
			return f.buscarItensDaVendaPorIdDaVenda(v.getId());
		}
		return new ArrayList<ItemDeVenda>();
	}
	
	public Divida getViewDivida() {
		try{
			return (Divida) view;
		}catch(ClassCastException cs){
			return null;
		}
	}

	public StreamedContent getFileVenda() {
		return fileVenda;
	}

	public void setFileVenda(StreamedContent fileVenda) {
		this.fileVenda = fileVenda;
	}

	

	
}
