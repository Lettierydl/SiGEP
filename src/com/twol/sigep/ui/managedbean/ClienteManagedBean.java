package com.twol.sigep.ui.managedbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Endereco;
import com.twol.sigep.model.pessoas.Telefone;
import com.twol.sigep.model.pessoas.UF;

@ViewScoped
@ManagedBean(name = "clienteBean")
public class ClienteManagedBean {
	private Facede f;
	
	private String nomeOuCpf;
	private Cliente newCliente;
	private Endereco newEndereco;
	private UF uf = UF.PB;
	private Telefone newTelefone;
	private List<Cliente> listAtualDeClientes;
	
	
	public ClienteManagedBean(){
		f = new Facede();
		newCliente = new Cliente();
		setNewEndereco(new Endereco());
		setNewTelefone(new Telefone());
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
		if(nomeOuCpf == null || nomeOuCpf.isEmpty()){
			listAtualDeClientes = f.getListaClientes();
		}
			return listAtualDeClientes;
	}
	
	public void modificarListaAtualDeClientes(){
		listAtualDeClientes = f.buscarClientePorCPFOuNomeQueIniciam(nomeOuCpf);
		
		if(nomeOuCpf != null && nomeOuCpf.length() != 0){
			listAtualDeClientes = f.buscarClientePorCPFOuNomeQueIniciam(nomeOuCpf);
			RequestContext.getCurrentInstance().update("tabelaCliente");
		}else{
			listAtualDeClientes = f.getListaClientes();
			RequestContext.getCurrentInstance().update("tabelaCliente");
		}
		
		
	}
	
	public void addEnderecoCliente(AjaxBehaviorEvent event){
		newEndereco.setUf(uf);
		newCliente.setEndereco(newEndereco);
		RequestContext.getCurrentInstance().update("labelEndereco");
	}
	
	public void addTelefoneCliente(){
		newCliente.addTelefone(newTelefone);
		RequestContext.getCurrentInstance().update("tabelaTelefones");
		newTelefone = new Telefone();
	}
	
	public void removerTelefoneCliente(Telefone telefone){
		newCliente.removerTelefone(telefone);
		RequestContext.getCurrentInstance().update("tabelaTelefones");
	}
	
	public UF[] getUfs(){
		return UF.values();
	}
	
	public String getNomeOuCpf() {
		return nomeOuCpf;
	}


	public void setNomeOuCpf(String nomeOuCpf) {
		this.nomeOuCpf = nomeOuCpf;
	}


	public Cliente getNewCliente() {
		return newCliente;
	}


	public void setNewCliente(Cliente newCliente) {
		this.newCliente = newCliente;
	}


	public Telefone getNewTelefone() {
		return newTelefone;
	}


	public void setNewTelefone(Telefone newTelefone) {
		this.newTelefone = newTelefone;
	}


	public Endereco getNewEndereco() {
		return newEndereco;
	}


	public void setNewEndereco(Endereco newEndereco) {
		this.newEndereco = newEndereco;
	}


	public UF getUf() {
		return uf;
	}


	public void setUf(UF uf) {
		this.uf = uf;
	}
	
	
	
}
