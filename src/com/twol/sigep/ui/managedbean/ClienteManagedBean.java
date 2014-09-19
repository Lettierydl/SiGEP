package com.twol.sigep.ui.managedbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIData;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.model.pessoas.Cliente;

@ViewScoped
@ManagedBean(name = "clienteBean")
public class ClienteManagedBean {
	private Facede f;
	
	private String nomePesquisa;
	private Cliente newCliente;
	private List<Cliente> listAtualDeClientes;
	
	
	public ClienteManagedBean(){
		f = new Facede();
		newCliente = new Cliente();
		listAtualDeClientes = f.getListaClientes();
	}
	
	
	public void cadastrarCliente(){
		try{
			validarInformacoesCadastrais(newCliente);
		}catch(Exception e){
			return;
		}
		f.adicionarCliente(newCliente);
		newCliente = new Cliente();
	}
	
	public void validarInformacoesCadastrais(Cliente c){}
	
	public List<Cliente> getListAtualDeClientes(){
		if(nomePesquisa == null || nomePesquisa.isEmpty()){
			listAtualDeClientes = f.getListaClientes();
		}
			return listAtualDeClientes;
	}
	
	public void modificarListaAtualDeClientes(){
		if(nomePesquisa != null && nomePesquisa.length() != 0){
			listAtualDeClientes = f.buscarClientePorCPFOuNomeQueIniciam(nomePesquisa);
			RequestContext.getCurrentInstance().update("tabelaCliente");
		}else{
			listAtualDeClientes = f.getListaClientes();
			RequestContext.getCurrentInstance().update("tabelaCliente");
		}
	}
	
	
	//recuperar linha do click
	public void editCliente(AjaxBehaviorEvent event){
		System.out.println(event.getComponent().getClientId());
		System.out.println(event);
		System.out.println(event.getBehavior());
		System.out.println(event.getSource());
		UIData dataTable = (UIData) event.getSource();
		System.out.println(dataTable.getRowData());
	}
	
	
	public String getNomePesquisa() {
		return nomePesquisa;
	}


	public void setNomePesquisa(String nomePesquisa) {
		this.nomePesquisa = nomePesquisa;
	}


	public Cliente getNewCliente() {
		return newCliente;
	}


	public void setNewCliente(Cliente newCliente) {
		this.newCliente = newCliente;
	}	
	
}
