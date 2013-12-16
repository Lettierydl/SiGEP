package com.twol.sigep.ui.managedbean;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import com.twol.sigep.model.exception.FuncionarioJaLogadoException;
import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.model.exception.SenhaIncorretaException;
import com.twol.sigep.model.pessoas.ControllerFuncionario;
import com.twol.sigep.util.JSFUiUtil;
import com.twol.sigep.util.OperacaoStringUtil;
import com.twol.sigep.util.SessionUtil;

@ManagedBean(name = "loginBean")
@ViewScoped
public class LoginManagedBean {

	private ControllerFuncionario controller;
	private String login = "";

	private String senha = "";

	public LoginManagedBean() {
		controller = new ControllerFuncionario();
	}

	public void logar(ActionEvent ac) {
		try {
			controller.logar(login, senha);
		} catch (SenhaIncorretaException e) {
			JSFUiUtil.error(OperacaoStringUtil.MESSAGEM_LOGIN_INVALIDO);
			return;
		} catch (FuncionarioJaLogadoException e) {
			JSFUiUtil.error(OperacaoStringUtil.FUNCIONARIO_JA_LOGADO);
			return;
		} catch (ParametrosInvalidosException e) {
			JSFUiUtil.error(OperacaoStringUtil.PARAMETROS_INVALIDOS);
			return;
		}
		//JSFUiUtil.info(OperacaoStringUtil.LOGIN_REALIZADO);
		try{
		 
			SessionUtil.redirecionarParaPage(SessionUtil.PAGE_PRINCIPAL);
		}catch(IOException io){io.printStackTrace();}
		
	}

	public boolean validarLogin() {
		return true;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
}
