package com.twol.sigep.model.pessoas;

import com.twol.sigep.model.exception.FuncionarioJaLogadoException;
import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.model.exception.PermissaoInvalidaException;
import com.twol.sigep.model.exception.SenhaIncorretaException;
import com.twol.sigep.util.SessionUtil;

public class ControllerFuncionario {

	private Funcionario logado = null;

	public void logar(String login, String senha)
			throws SenhaIncorretaException, FuncionarioJaLogadoException,
			ParametrosInvalidosException {
		Funcionario f = validarSenha(login, senha);
		if (logado == null) {
			this.logado = f;
			SessionUtil.putFuncionarioLogado(f);
		} else {
			//throw new FuncionarioJaLogadoException();
		}
	}

	public void logoff() {
		if (logado != null) {
			SessionUtil.putFuncionarioLogado(null);
			logado = null;
		}
	}
	
	public void salvarFuncionario(Funcionario f, String senha, TipoDeFuncionario tipoDoFuncionario) throws ParametrosInvalidosException, PermissaoInvalidaException{
		setSenhaFuncionarioPrimeiraVes(f, senha);
		setTipoDeFuncionario(f, tipoDoFuncionario);
		Funcionario.salvar(f);
	}
	
	//validar TipoDeFuncionario pode ou n��o criar um funcionario com esse tipo
	private void setTipoDeFuncionario(Funcionario f, TipoDeFuncionario tipoDoFuncionario) throws PermissaoInvalidaException {
			if(f.getTipoDeFuncionario() != null){
				throw new PermissaoInvalidaException();
			}
			f.setTipoDeFuncionario(tipoDoFuncionario);
	}

	public void atualizarFuncionario(Funcionario f){
		Funcionario.atualizar(f);
	}

	public void modificarSenhaFuncionario(String login, String senhaAntiga,
			String novaSenha) throws ParametrosInvalidosException, SenhaIncorretaException {
		Funcionario f = validarSenha(login, senhaAntiga);
		setSenhaFuncionario(f, novaSenha);
	}

	private String criptografar(String string) {
		return string;
	}
	
	private Funcionario validarSenha(String login, String senha)
			throws ParametrosInvalidosException, SenhaIncorretaException {
		Funcionario f = null;
		try{
			f = Funcionario.recuperarFuncionarioPorLoginESenha(login,
					criptografar(senha));
		}catch(Exception e){throw new SenhaIncorretaException();}
		return f;
	}
	
	private void setSenhaFuncionario(Funcionario f, String senha)
			throws ParametrosInvalidosException {
		if (f == null || senha == null) {
			throw new ParametrosInvalidosException();
		}
		f.setSenha(criptografar(senha));
		Funcionario.atualizar(f);
	}
	
	private void setSenhaFuncionarioPrimeiraVes(Funcionario f, String senha)
			throws ParametrosInvalidosException {
		if(f.getSenha() != null && !f.getSenha().isEmpty()){
			throw new ParametrosInvalidosException();
		}
		f.setSenha(criptografar(senha));
	}
}
