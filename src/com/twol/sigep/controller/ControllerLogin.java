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
		if(logado == null){
			if(SessionUtil.getFuncionarioLogado() != null){
				this.logado = SessionUtil.getFuncionarioLogado();
			}
		}
		return logado;
	}

	public void logar(String login, String senha)
			throws SenhaIncorretaException, LoginIncorretoException {
		if (logado == null) {
			Funcionario f = validarSenha(login, senha);
			this.logado = f;
			//registrar login em algum canto
		} else {
			logoff();
			logar(login,senha);
		}
	}

	public void logoff() {
			SessionUtil.putFuncionarioLogado(null);
			logado = null;
	}
	
	
	public void atribuirSenhaETipoAoFuncionario(Funcionario f, String senha, TipoDeFuncionario tipoDeFuncionario) throws SenhaIncorretaException{
		if(f.getSenha() == null){
			f.setSenha(this.criptografar(senha));
			f.setTipoDeFuncionario(tipoDeFuncionario);
		}else{
			throw new SenhaIncorretaException("Senha já cadastrada para este funcionario");
		}
	}
	
	public void alterarSenhaDoFuncionario(Funcionario f, String senha, String novaSenha) throws SenhaIncorretaException, LoginIncorretoException{
		validarSenha(f, senha);
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
	
	/*valida sem consultar no banco*/
	private void validarSenha(Funcionario f , String senha)
			throws SenhaIncorretaException {
		if(!f.getSenha().equals(criptografar(senha))){
			throw new SenhaIncorretaException();
		}
	}
	
	private String criptografar(String string) {
		return string;
	}

}
