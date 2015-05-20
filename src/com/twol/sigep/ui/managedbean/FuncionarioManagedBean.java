package com.twol.sigep.ui.managedbean;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.exception.FuncionarioNaoAutorizadoException;
import com.twol.sigep.model.exception.LoginIncorretoException;
import com.twol.sigep.model.exception.SenhaIncorretaException;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.util.SessionUtil;
@ViewScoped
@ManagedBean(name = "funcionarioBean")
public class FuncionarioManagedBean {
	

	private Facede f;
	
	private Funcionario newFuncionario;
	private Funcionario editFuncionario;
	private TipoDeFuncionario tipoDoFuncionario = TipoDeFuncionario.Caixa;
	private String nomePesquisa = "";
	private String senha;
	private String senhaEdit;
	private String senhaNewEdit;
	private List<Funcionario> listAtualDeFuncionarios;
	
	public FuncionarioManagedBean(){
		f = new Facede();
		newFuncionario = new Funcionario();
		listAtualDeFuncionarios = f.getFuncionarios();
	}

	public void cadastrarFuncionario(){
		try {
			f.adicionarFuncionario(newFuncionario, senha, tipoDoFuncionario);
		} catch (SenhaIncorretaException e) {
			e.printStackTrace();
		} catch (FuncionarioNaoAutorizadoException e) {
			SessionUtil.exibirMensagem(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						e.getMessage(),
						e.getMessage()));
			return;
		}
		newFuncionario = new Funcionario();
		listAtualDeFuncionarios = f.getFuncionarios();
	}
	
	public TipoDeFuncionario[] getTiposFuncionario(){
		return TipoDeFuncionario.values();
	}
	
	public void validarInformacoesCadastrais(Funcionario f){}
	
	public List<Funcionario> getListAtualDeFuncionarios(){
		if(listAtualDeFuncionarios == null || listAtualDeFuncionarios.isEmpty()){
			return f.getFuncionarios();
		}
		return listAtualDeFuncionarios;
	}
	
	public void modificarListaAtualDeFuncionarioPeloNome(){
		if(nomePesquisa != null && nomePesquisa.length() != 0){
			listAtualDeFuncionarios = f.buscarFuncionarioPorNomeQueInicia(nomePesquisa);
			RequestContext.getCurrentInstance().update("tabelaFuncionarios");
		}else{
			listAtualDeFuncionarios = f.getFuncionarios();
			RequestContext.getCurrentInstance().update("tabelaFuncionarios");
		}
	}
	
	public void openEditFuncionario(Funcionario f){
		this.setEditFuncionario(f);
		RequestContext.getCurrentInstance().execute("abrirModa('modalEdit');");
		/*
		RequestContext.getCurrentInstance().update("formEdit");
		RequestContext.getCurrentInstance().update("nomeEdit");
		RequestContext.getCurrentInstance().openDialog("modalEdit");
		RequestContext.getCurrentInstance().openDialog("#modalEdit");*/
	}
	
	public void modific(ValueChangeEvent event){
		nomePesquisa = (String) event.getNewValue();
		modificarListaAtualDeFuncionarioPeloNome();
	}
	
	public void editarFuncionario(){
		if(senhaNewEdit!=null && !senhaNewEdit.isEmpty() ){
			try {
				f.alterarSenhaDoFuncionario(editFuncionario, senhaEdit, senhaNewEdit);
			} catch (SenhaIncorretaException e) {
				e.printStackTrace();
			} catch (LoginIncorretoException e) {
				e.printStackTrace();
			}
		}
		try {
			f.atualizarFuncionario(editFuncionario);
		} catch (EntidadeNaoExistenteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		RequestContext.getCurrentInstance().update("@all");
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Funcionario getNewFuncionario() {
		return newFuncionario;
	}

	public void setNewFuncionario(Funcionario newFuncionario) {
		this.newFuncionario = newFuncionario;
	}

	public String getNomePesquisa() {
		return nomePesquisa;
	}

	public void setNomePesquisa(String nome) {
		this.nomePesquisa = nome;
	}

	public TipoDeFuncionario getTipoDoFuncionario() {
		return tipoDoFuncionario;
	}

	public void setTipoDoFuncionario(TipoDeFuncionario tipoDoFuncionario) {
		this.tipoDoFuncionario = tipoDoFuncionario;
	}
	
	public Funcionario getFuncionarioLogado(){
		return f.getFuncionarioLogado();
	}

	public Funcionario getEditFuncionario() {
		return editFuncionario;
	}

	public void setEditFuncionario(Funcionario editFuncionario) {
		this.editFuncionario = editFuncionario;
	}

	public String getSenhaEdit() {
		return senhaEdit;
	}

	public void setSenhaEdit(String senhaEdit) {
		this.senhaEdit = senhaEdit;
	}

	public String getSenhaNewEdit() {
		return senhaNewEdit;
	}

	public void setSenhaNewEdit(String senhaNewEdit) {
		this.senhaNewEdit = senhaNewEdit;
	}
	
	
}
