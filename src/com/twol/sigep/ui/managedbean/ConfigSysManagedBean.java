package com.twol.sigep.ui.managedbean;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.twol.sigep.controller.ControllerPessoa;
import com.twol.sigep.controller.find.FindFuncionario;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.util.OperacaoStringUtil;
import com.twol.sigep.util.Persistencia;
import com.twol.sigep.util.SessionUtil;

@ViewScoped
@ManagedBean(name = "confSysBean")
public class ConfigSysManagedBean {

	private Funcionario newFuncionario;
	private TipoDeFuncionario tipoDoFuncionario = TipoDeFuncionario.Gerente;
	private String nomePesquisa = "";
	private String senha;
	private String senhaEdit;
	private String senhaNewEdit;
	private List<Funcionario> listAtualDeFuncionarios;
	private boolean liberado = false;
	private boolean criptografar = false;

	public ConfigSysManagedBean() {
		newFuncionario = new Funcionario();
		listAtualDeFuncionarios = FindFuncionario.listFuncionarios();
		criptografar = (Math.random()*100)%2 == 0;
		RequestContext.getCurrentInstance().execute(
				"abrirModa('modalSenha');");
	}
	
	public void setSenhaMestre(String senha){
		liberado = OperacaoStringUtil.validarSenhaMestre(senha, criptografar);
		RequestContext.getCurrentInstance().update("@all");
	}
	
	public String getSenhaMestre(){
		return "";
	}
	
	public void cadastrarFuncionario() {
		try {
			newFuncionario.setSenha(OperacaoStringUtil.criptografar(senha));
			newFuncionario.setTipoDeFuncionario(tipoDoFuncionario);
			ControllerPessoa pes = new ControllerPessoa(Persistencia.emf);
			pes.create(newFuncionario);
		} catch (Exception e) {
			SessionUtil
					.exibirMensagem(new FacesMessage(
							FacesMessage.SEVERITY_ERROR, e.getMessage(), e
									.getMessage()));
			return;
		}
		SessionUtil
		.exibirMensagem(new FacesMessage(
				FacesMessage.SEVERITY_WARN, "Funcionário" +newFuncionario.getNome()+" cadastrado", 
				"Funcionário" +newFuncionario.getNome()+" cadastrado"));
		newFuncionario = new Funcionario();
		listAtualDeFuncionarios = FindFuncionario.listFuncionarios();
	}
	
	public void removerFuncionario(int id_funcionario) {
		try {
			ControllerPessoa pes = new ControllerPessoa(Persistencia.emf);
			Funcionario remove = FindFuncionario.funcionarioComId(id_funcionario);
			pes.destroy(remove);
			SessionUtil
			.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_FATAL, "Funcionário" +remove.getNome()+" Removido", 
					"Funcionário" +remove.getNome()+" Removido"));
		} catch (Exception e) {
			SessionUtil
					.exibirMensagem(new FacesMessage(
							FacesMessage.SEVERITY_ERROR, e.getMessage(), e
									.getMessage()));
			e.printStackTrace();
			return;
		}
		
		listAtualDeFuncionarios = FindFuncionario.listFuncionarios();
	}

	public TipoDeFuncionario[] getTiposFuncionario() {
		return TipoDeFuncionario.values();
	}

	public void validarInformacoesCadastrais(Funcionario f) {
	}

	public List<Funcionario> getListAtualDeFuncionarios() {
		if (listAtualDeFuncionarios == null
				|| listAtualDeFuncionarios.isEmpty()) {
			return FindFuncionario.listFuncionarios();
		}
		return listAtualDeFuncionarios;
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

	public boolean isLiberado() {
		return liberado;
	}

	public void setLiberado(boolean liberado) {
		this.liberado = liberado;
	}

	public boolean isCriptografar() {
		return criptografar;
	}

	public void setCriptografar(boolean criptografar) {
		this.criptografar = criptografar;
	}

}
