package com.twol.sigep.ui.managedbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.pessoas.Cliente;

@SessionScoped
@ManagedBean(name = "clienteBean")
public class ClienteManagedBean {
	private Facede f;
	
	private String nomePesquisa;
	private Cliente newCliente;
	private Cliente editCliente;
	private List<Cliente> listAtualDeClientes;
	
	
	public ClienteManagedBean(){
		f = new Facede();
		newCliente = new Cliente();
		setEditCliente(new Cliente());
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
	
	
	
	public void openEditCliente(Cliente c){
		this.setEditCliente(c);
		RequestContext.getCurrentInstance().execute("abrirModa('modalEdit');");
		/*
		RequestContext.getCurrentInstance().update("formEdit");
		RequestContext.getCurrentInstance().update("nomeEdit");
		RequestContext.getCurrentInstance().openDialog("modalEdit");
		RequestContext.getCurrentInstance().openDialog("#modalEdit");*/
	}
	
	public void editarCliente(){
		try {
			f.atualizarCliente(editCliente);
		} catch (EntidadeNaoExistenteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestContext.getCurrentInstance().update("@all");
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


	public Cliente getEditCliente() {
		return editCliente;
	}


	public void setEditCliente(Cliente editCliente) {
		this.editCliente = editCliente;
	}	
	
}
