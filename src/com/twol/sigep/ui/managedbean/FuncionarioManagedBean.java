package com.twol.sigep.ui.managedbean;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.model.exception.SenhaIncorretaException;
import com.twol.sigep.model.pessoas.Endereco;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.Telefone;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.model.pessoas.UF;
@ViewScoped
@ManagedBean(name = "funcionarioBean")
public class FuncionarioManagedBean {
	private String senha;

	private Facede f;
	
	private Funcionario newFuncionario;
	private TipoDeFuncionario tipoDoFuncionario = TipoDeFuncionario.Caixa;
	private String nome = "";
	private Endereco newEndereco;
	private UF uf = UF.PB;
	private Telefone newTelefone;
	private List<Funcionario> listAtualDeFuncionarios;
	
	public FuncionarioManagedBean(){
		f = new Facede();
		newFuncionario = new Funcionario();
		newTelefone = new Telefone();
		newEndereco = new Endereco();
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
	
	public void addEnderecoFuncionario(AjaxBehaviorEvent event){
		newEndereco.setUf(uf);
		newFuncionario.setEndereco(newEndereco);
		RequestContext.getCurrentInstance().update("labelEndereco");
	}
	
	public void addTelefoneFuncionario(){
		newFuncionario.addTelefone(newTelefone);
		RequestContext.getCurrentInstance().update("tabelaTelefones");
		newTelefone = new Telefone();
	}
	
	public void removerTelefoneFuncionario(Telefone telefone){
		newFuncionario.removerTelefone(telefone);
		RequestContext.getCurrentInstance().update("tabelaTelefones");
	}
	
	public UF[] getUfs(){
		return UF.values();
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

	public Telefone getNewTelefone() {
		return newTelefone;
	}

	public void setNewTelefone(Telefone newTelefone) {
		this.newTelefone = newTelefone;
	}
	
	public Funcionario getFuncionarioLogado(){
		return f.getFuncionarioLogado();
	}
	
	
}
