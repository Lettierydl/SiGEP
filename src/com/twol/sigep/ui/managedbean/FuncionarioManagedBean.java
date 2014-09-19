package com.twol.sigep.ui.managedbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.model.exception.SenhaIncorretaException;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
@ViewScoped
@ManagedBean(name = "funcionarioBean")
public class FuncionarioManagedBean {
	private String senha;

	private Facede f;
	
	private Funcionario newFuncionario;
	private TipoDeFuncionario tipoDoFuncionario = TipoDeFuncionario.Caixa;
	private String nome = "";
	private List<Funcionario> listAtualDeFuncionarios;
	
	public FuncionarioManagedBean(){
		f = new Facede();
		newFuncionario = new Funcionario();
		listAtualDeFuncionarios = f.getFuncionarios();
	}

	public void cadastrarFuncionario(){
		try{
			validarInformacoesCadastrais(newFuncionario);
		}catch(Exception e){
			return;
		}
		try {
			f.adicionarFuncionario(newFuncionario, senha, tipoDoFuncionario);
		} catch (SenhaIncorretaException e) {
			e.printStackTrace();
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
		if(nome != null && nome.length() != 0){
			listAtualDeFuncionarios = f.buscarFuncionarioPorNomeQueInicia(nome);
			RequestContext.getCurrentInstance().update("tabelaFuncionarios");
		}else{
			listAtualDeFuncionarios = f.getFuncionarios();
			RequestContext.getCurrentInstance().update("tabelaFuncionarios");
		}
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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
	
	
}
