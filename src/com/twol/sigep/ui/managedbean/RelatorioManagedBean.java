package com.twol.sigep.ui.managedbean;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
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
import org.primefaces.model.chart.DonutChartModel;

import com.twol.sigep.Facede;
import com.twol.sigep.model.exception.FuncionarioNaoAutorizadoException;
import com.twol.sigep.util.SessionUtil;

@ViewScoped
@ManagedBean(name = "relatBean")
public class RelatorioManagedBean {
	private Facede f;
	
	private StreamedContent file;
	private StreamedContent filePlanilha;
	
	private Date diaInicioEnt = new Date();
	private Date diaFimEnt = new Date();
	
	private Date diaInicioVend = new Date();
	private Date diaFimVend = new Date();
	
	private Date diaInicioProd = new Date();
	private Date diaFimProd = new Date();
	
	private Date diaInicioGer = new Date();
	private Date diaFimGer = new Date();

	public RelatorioManagedBean() {
		f = new Facede();
		criarEntrada();
		criarVendas();
		criarProduto();
	}

	private DonutChartModel entrada;
	private DonutChartModel vendas;
	private BarChartModel produtos;
	private DonutChartModel geral;

	
	public double[] getValEntrada() {
		try {
			return f.getRelatorioDeEntradaDeCaixa(diaInicioEnt, diaFimEnt);
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe
							.getMessage()));
		}catch (Exception e) {
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
		}catch (Exception e) {
		}
		double[] val = { 0.0, 0.0, 0.0 ,0.0, 0.0 ,0.0 };
		return val;
	}
	
	public double[] getValProduto() {
		try {
			return f.getRelatorioDeProduto(diaInicioProd, diaFimProd);
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe
							.getMessage()));
		}catch (Exception e) {
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
        yAxis.setMax((int)val[0]+(val[0]*0.3));
        
        
        produtos.setLegendPosition("rs");
        produtos.setShadow(false);
        produtos.setMouseoverHighlight(true);
        produtos.setAnimate(true);
	}

	public void gerarRelatorioBalancoProdutosPDF() throws FileNotFoundException{
		try {
			String path = f.gerarPdfRelatorioBalancoProdutos(diaInicioGer, diaFimGer);
			
			InputStream stream = new FileInputStream(path);
		    file = new DefaultStreamedContent(stream, "application/pdf", "Relatorio_Balanco_Produto.pdf");
		    RequestContext.getCurrentInstance().update("@all");
			return;
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe
							.getMessage()));
		}catch (Exception e) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Planilha não gerada, reveja suas datas", "Planilha não gerada, reveja suas datas"));
			RequestContext.getCurrentInstance().update("@all");
		}
	}
	
	public void gerarRelatorioBalancoProdutosPlanilha() throws FileNotFoundException{
		try {
			String path = f.gerarPlanilhaRelatorioBalancoProdutos(diaInicioGer, diaFimGer);
			
			InputStream stream = new FileInputStream(path);
			filePlanilha = new DefaultStreamedContent(stream, "application/xls", "Relatorio_Balanco_Produto.xls");
			RequestContext.getCurrentInstance().update("@all");
			return;
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe
							.getMessage()));
		}catch (Exception e) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Planilha não gerada, reveja suas datas", "Planilha não gerada, reveja suas datas"));
			RequestContext.getCurrentInstance().update("@all");
		}
	}
	
	//Gets and Sets
	
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
	
	
}
