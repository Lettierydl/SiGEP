package com.twol.sigep.controller;

import javax.persistence.NoResultException;

import com.twol.sigep.controller.find.FindFuncionario;
import com.twol.sigep.model.exception.LoginIncorretoException;
import com.twol.sigep.model.exception.SenhaIncorretaException;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.util.SessionUtil;

public class ControllerLogin {

	private Funcionario logado = null;

	public Funcionario getLogado() {
		return logado;
	}

	public void logar(String login, String senha)
			throws SenhaIncorretaException, LoginIncorretoException {
		if (logado == null) {
			Funcionario f = validarSenha(login, senha);
			this.logado = f;
			SessionUtil.putFuncionarioLogado(f);
			//registrar login em algum canto
		} else {
			logoff();
			logar(login,senha);
		}
	}

	public void logoff() {
		if (logado != null) {
			SessionUtil.putFuncionarioLogado(null);
			logado = null;
			//registrar logoff em algum canto
		}
	}
	
	
	public void atribuirSenhaETipoAoFuncionario(Funcionario f, String senha, TipoDeFuncionario tipoDeFuncionario) throws SenhaIncorretaException{
		if(f.getSenha()==null){
			f.setSenha(this.criptografar(senha));
			f.setTipoDeFuncionario(tipoDeFuncionario);
		}else{
			throw new SenhaIncorretaException("Senha já cadastrada para este funcionario");
		}
	}
	
	public void alterarSenhaDoFuncionario(Funcionario f,  String novaSenha) throws SenhaIncorretaException, LoginIncorretoException{
		validarSenha(f.getLogin(), f.getSenha());
		f.setSenha(this.criptografar(novaSenha));
	}

	private Funcionario validarSenha(String login, String senha)
			throws SenhaIncorretaException, LoginIncorretoException {
		Funcionario f = null;
		try {
			f = FindFuncionario.funcionarioComLoginESenha(login,
					criptografar(senha));
		} catch (Exception e) {
			try{
				FindFuncionario.funcionarioComLogin(login);
			}catch(NoResultException nre){
				throw new LoginIncorretoException();
			}
			throw new SenhaIncorretaException();
		}
		return f;
	}
	
	private String criptografar(String string) {
		return string;
	}

}
