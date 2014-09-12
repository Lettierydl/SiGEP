package com.twol.sigep.ui.managedbean;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import com.twol.sigep.Facede;
import com.twol.sigep.model.exception.LoginIncorretoException;
import com.twol.sigep.model.exception.SenhaIncorretaException;
import com.twol.sigep.util.JSFUiUtil;
import com.twol.sigep.util.OperacaoStringUtil;
import com.twol.sigep.util.SessionUtil;

@ManagedBean(name = "loginBean")
@ViewScoped
public class LoginManagedBean {

	private Facede fac;
	private String login = "";

	private String senha = "";

	public LoginManagedBean() {
		System.out.println("teste");
		fac = new Facede();
	}

	public void logar(ActionEvent ac) {
		try {
			fac.login(login, senha);
		} catch (SenhaIncorretaException e) {
			JSFUiUtil.error(OperacaoStringUtil.MESSAGEM_LOGIN_INVALIDO);
			return;
		} catch (LoginIncorretoException e) {
			JSFUiUtil.error(OperacaoStringUtil.MESSAGEM_LOGIN_INVALIDO);
			return;
		}
		JSFUiUtil.info(OperacaoStringUtil.LOGIN_REALIZADO);
		try{
			SessionUtil.redirecionarParaPage(SessionUtil.PAGE_PRINCIPAL);
		}catch(IOException io){io.printStackTrace();}
		
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
