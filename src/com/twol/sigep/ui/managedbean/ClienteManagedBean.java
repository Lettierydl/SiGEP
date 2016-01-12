package com.twol.sigep.ui.managedbean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.exception.FuncionarioNaoAutorizadoException;
import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.model.pessoas.Dependente;
import com.twol.sigep.util.SessionUtil;

@ViewScoped
@ManagedBean(name = "clienteBean")
public class ClienteManagedBean {
	private Facede f;

	private String nomePesquisa;
	private Cliente newCliente;
	private Cliente editCliente;
	private Cliente addDependenteCliente;
	private Dependente newDependente;
	private List<Cliente> listAtualDeClientes;


	public ClienteManagedBean() {
		f = new Facede();
		newCliente = new Cliente();
		newDependente = new Dependente();
		setEditCliente(new Cliente());
		listAtualDeClientes = f.getListaClientes();
	}


	
	public void cadastrarCliente() {
		try {
			f.adicionarCliente(newCliente);
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe
							.getMessage()));
			return;
		} catch (Exception e) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Cliente duplicado não pode ser cadastrado",
					"Cliente duplicado não pode ser cadastrado"));
			return;
		}
		SessionUtil.exibirMensagem(new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Cliente cadastrado com sucesso",
				"Cliente cadastrado com sucesso"));
		newCliente = new Cliente();
	}

	public List<Cliente> getListAtualDeClientes() {
		if (nomePesquisa == null || nomePesquisa.isEmpty()) {
			listAtualDeClientes = f.getListaClientes();
		}
		return listAtualDeClientes;
	}

	public void modificarListaAtualDeClientes() {
		if (nomePesquisa != null && nomePesquisa.length() != 0) {
			listAtualDeClientes = f
					.buscarClientePorCPFOuNomeQueIniciam(nomePesquisa);
			RequestContext.getCurrentInstance().update("tabelaCliente");
		} else {
			listAtualDeClientes = f.getListaClientes();
			RequestContext.getCurrentInstance().update("tabelaCliente");
		}
	}

	public void openEditCliente(Cliente c) {
		this.setEditCliente(c);
		RequestContext.getCurrentInstance().execute("abrirModa('modalEdit');");
		/*
		 * RequestContext.getCurrentInstance().update("formEdit");
		 * RequestContext.getCurrentInstance().update("nomeEdit");
		 * RequestContext.getCurrentInstance().openDialog("modalEdit");
		 * RequestContext.getCurrentInstance().openDialog("#modalEdit");
		 */
	}
	
	public void openCriarDependente(int idCliente) {
		this.setAddDependenteCliente(f.buscarClientePorId(idCliente));
		RequestContext.getCurrentInstance().execute("abrirModa('modalCriarDependente');");
		/*
		 * RequestContext.getCurrentInstance().update("formEdit");
		 * RequestContext.getCurrentInstance().update("nomeEdit");
		 * RequestContext.getCurrentInstance().openDialog("modalEdit");
		 * RequestContext.getCurrentInstance().openDialog("#modalEdit");
		 */
	}

	public void modific(ValueChangeEvent event) {
		nomePesquisa = (String) event.getNewValue();
		modificarListaAtualDeClientes();
	}

	public void editarCliente() {
		try {
			Cliente oud = f.buscarClientePorId(editCliente.getId());
			if (!oud.getNome().equals(editCliente.getNome())) {// modificou
				try {
					f.buscarClientePorNome(editCliente.getNome());
					SessionUtil.exibirMensagem((new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Já existe um cliente com o nome "
									+ editCliente.getNome(),
							"Já existe um cliente com o nome "
									+ editCliente.getNome())));
					editCliente.setNome(oud.getNome());
					editCliente.setCpf(oud.getCpf());
					return;
				} catch (Exception ep) {
				}// ok nao existe
			}
			if (!oud.getCpf().equals(editCliente.getCpf())) {// modificou
				try {
					f.buscarClientePorCPF(editCliente.getCpf());
					SessionUtil.exibirMensagem((new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Já existe um cliente com o CPF "
									+ editCliente.getCpf(),
							"Já existe um cliente com o CPF "
									+ editCliente.getCpf())));
					editCliente.setCpf(oud.getCpf());
					return;
				} catch (Exception ep) {
				}// ok nao existe
			}
			f.atualizarCliente(editCliente);
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe
							.getMessage()));
			return;
		} catch (EntidadeNaoExistenteException e) {
			SessionUtil.exibirMensagem((new FacesMessage(
					FacesMessage.SEVERITY_FATAL,
					"Reveja a conexão com o servidor",
					"Reveja a conexão com o servidor")));
			e.printStackTrace();
		} catch (Exception e) {
			SessionUtil.exibirMensagem((new FacesMessage(
					FacesMessage.SEVERITY_FATAL,
					"Formulário com informações inválidas",
					"Formulário com informações inválidas")));
			e.printStackTrace();
		} finally {

			RequestContext.getCurrentInstance().update("@all");
		}
	}
	
	public void cadastrarDependente(){
		try{
			f.adicionarDependente(newDependente, addDependenteCliente);
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe
							.getMessage()));
			return;
		} catch (Exception e) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Dependente duplicado não pode ser cadastrado",
					"Dependente duplicado não pode ser cadastrado"));
			return;
		}
		
		SessionUtil.exibirMensagem(new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Dependente cadastrado com sucesso",
				"Dependente cadastrado com sucesso"));
		newDependente = new Dependente();
		addDependenteCliente =  new Cliente();
	}
	
	public void removerDependente(String nomeDependente){
		try{
			f.removerDependente(f.buscarDependenteNomeECliente(nomeDependente, addDependenteCliente));
		} catch (EntidadeNaoExistenteException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Dependente não encontrado",  "Dependente não encontrado"));
			return;
		} catch (Exception e) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Dependente não pode ser removido",
					"Dependente não pode ser removido"));
			return;
		}
		
		SessionUtil.exibirMensagem(new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Dependente removido com sucesso",
				"Dependente removido com sucesso"));
	}
	
	public List<String> getNomeDependentes(){
		try{
			return f.getListaNomeDependentes(addDependenteCliente.getId());
		}catch(NullPointerException ne){
			return new ArrayList<String>();
		}
	}
	
	public List<Dependente> getDependentes(){
		return f.getListaDependentes(addDependenteCliente);
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

	public Cliente getAddDependenteCliente() {
		return addDependenteCliente;
	}

	public void setAddDependenteCliente(Cliente addDependenteCliente) {
		this.addDependenteCliente = addDependenteCliente;
	}

	public Dependente getNewDependente() {
		return newDependente;
	}

	public void setNewDependente(Dependente newDependente) {
		this.newDependente = newDependente;
	}
	
	

}
