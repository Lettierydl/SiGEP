package com.twol.sigep.ui.managedbean;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.vendas.ItemDeVenda;
import com.twol.sigep.model.vendas.Venda;
import com.twol.sigep.util.SessionUtil;

@ViewScoped
@ManagedBean(name = "finalBean")
public class FinalizarManagedBean {

	private Venda aFinalizar = null;
	private boolean imprimir = false;
	private double pago = 0;
	private double partePaga;
	private String nomeCliente;
	private Cliente selecionado = null;
	private String observacao;
	private Facede f;
	
	

	public FinalizarManagedBean() {
		try {
			aFinalizar = SessionUtil.getVendaAFinalizar();
			aFinalizar.getDia();
			f = new Facede();
		} catch (NullPointerException ne) {
			ne.printStackTrace();
		}
	}

	public void verificarCliente(){
		try{
		f.buscarClientePorNome(nomeCliente);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Selecione um cliente cadastrado","Cliente não selecionado")));
			return;// nenhum produto encontrado
		}
		RequestContext.getCurrentInstance().execute(
				"setFocoESelecionaValorPago();");
	}
	
	
	public List<Cliente> getClientes() {
		return f.getListaClientes();
	}

	public List<Cliente> completNomeCliente(String query) {
		return f.buscarClientePorCPFOuNomeQueIniciam(query);
	}

	public void finalizarVendaAVista() {
		if(this.pago >= aFinalizar.getTotal()){
			//finaliza
			
			try {
				f.finalizarVendaAVista(aFinalizar);
				
				boolean ret = false;
				try{
					if(imprimir){
						ret = f.imprimirVenda(aFinalizar);
					}else{
						ret = true;
					}
				}catch(Exception | UnsatisfiedLinkError e){}
				if(ret){
					SessionUtil.putNextMensagem(new FacesMessage(
							FacesMessage.SEVERITY_INFO, 
							"Venda finalizada à vista com sucesso", "Venda Finalizada"));
				}else{
					SessionUtil.putNextMensagem(new FacesMessage(
							FacesMessage.SEVERITY_WARN,"Venda Finalizada! \nImpressora com problema",
							"Impressora com problema"));
				}
				
				SessionUtil.redirecionarParaPage("venda.jsf");
			} catch (EntidadeNaoExistenteException e) {
				e.printStackTrace();// venda ou item removido
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			FacesContext.getCurrentInstance().addMessage(
					null,(new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Valor inferior ao total da venda",
							"Valor Invalido")));
		}
	}

	public void finalizarVendaAPrazo() {
		if(this.partePaga >= aFinalizar.getTotal()){
			FacesContext.getCurrentInstance().addMessage(
					null,(new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Valor iqual ou superior ao total da venda",
							"Valor Invalido")));
			return;
		}
		try{
			selecionado = f.buscarClientePorCPFOuNomeIqualA(nomeCliente);
		}catch(NoResultException nr){
			FacesContext.getCurrentInstance().addMessage(
					null,(new FacesMessage(
							FacesMessage.SEVERITY_ERROR, "Selecione o cliente antes de finalizar a venda",
							"Cliente Invalido")));
			return;
		}
		
		try {
			f.finalizarVendaAprazo(aFinalizar, selecionado, partePaga);
			boolean ret = false;
			try{
				if(imprimir){
					ret = f.imprimirVenda(aFinalizar);
				}else{
					ret = true;
				}
			}catch(Exception | UnsatisfiedLinkError e){}
			if(ret){
				SessionUtil.putNextMensagem(new FacesMessage(
						FacesMessage.SEVERITY_INFO,"Venda finalizada à prazo com sucesso",
						"Venda Finalizada"));
			}else{
				SessionUtil.putNextMensagem(new FacesMessage(
						FacesMessage.SEVERITY_WARN,"Venda Finalizada! \nImpressora com problema",
						"Impressora com problema"));
			}
			SessionUtil.redirecionarParaPage("venda.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EntidadeNaoExistenteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void voltarVenda() {
		try {
			SessionUtil.redirecionarParaPage("venda.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public double getTotal() {
		return this.aFinalizar.getTotal();
	}
	
	public double getSubTotal(){
		if((this.aFinalizar.getTotal() - this.partePaga) > 0 ){
			return this.aFinalizar.getTotal() - this.partePaga;
		}else{
			return 0;
		}
	}
	
	
	public List<ItemDeVenda> getItens() {
		List<ItemDeVenda> itens = this.aFinalizar.getItensDeVenda();
		Collections.sort(itens);
		return itens;
	}

	public String getDia() {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
				.format(this.aFinalizar.getDia().getTime());
	}

	public Venda getVedaAFinalizar() {
		return this.aFinalizar;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getTroco() {
		if ((this.pago - aFinalizar.getTotal()) > 0) {
			return new DecimalFormat("0.00").format(this.pago - aFinalizar.getTotal());
		}
		return "0,00";
	}

	public String getPago() {
		if(this.pago != 0){
			return new DecimalFormat("0.00").format(this.pago);
		}
		return "0";
	}

	public void setPago(String pago) {
		try {
			if(pago.length() >= 3){
				pago = pago.replace(".", "").replace(" ", "")
						.replace(",", "");
				pago = pago.substring(0, pago.length()-2) + "," +pago.substring(pago.length()-2);
			}
			this.pago = Double.valueOf(pago.replace(".", "").replace(" ", "").replace(",", "."));
		} catch (NumberFormatException ne) {
			SessionUtil.putNextMensagem(new FacesMessage(FacesMessage.SEVERITY_ERROR,"Formato de valor errado","Formato errado"));
			this.pago = 0;
		}
	}
	
	public String getPartePaga() {
		if(this.partePaga != 0){
			return new DecimalFormat("0.00").format(this.partePaga);
		}
		return "0";
	}

	public void setPartePaga(String partePaga) {
		try {
			if(partePaga.length() >= 3){
				partePaga = partePaga.replace(".", "").replace(" ", "")
				.replace(",", "");
				partePaga = partePaga.substring(0, partePaga.length()-2) + "," +partePaga.substring(partePaga.length()-2);
			}
			this.partePaga = Double.valueOf(partePaga.replace(".", "").replace(" ", "")
					.replace(",", "."));
		} catch (NumberFormatException ne) {
			this.partePaga = 0;
		}
	}
	
	public Cliente getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Cliente selecionado) {
		this.selecionado = selecionado;
	}

	public boolean isImprimir() {
		return imprimir;
	}

	public void setImprimir(boolean imprimir) {
		this.imprimir = imprimir;
	}
}
