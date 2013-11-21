package com.twol.sigep.model.pessoas;

import com.twol.sigep.model.excepion.FuncionarioJaLogadoException;
import com.twol.sigep.model.exception.ParametrosInvalidosException;
import com.twol.sigep.model.exception.SenhaIncorretaException;
import com.twol.sigep.util.SessionUtil;

public class ControllerFuncionario {

	private Funcionario logado = null;

	public void logar(Funcionario f, String senha)
			throws SenhaIncorretaException, FuncionarioJaLogadoException,
			ParametrosInvalidosException {
		validarSenha(f, senha);
		if (logado == null) {
			this.logado = f;
			SessionUtil.putFuncionarioLogado(f);
		} else {
			throw new FuncionarioJaLogadoException();
		}
	}

	public void logoff() {
		if (logado != null) {
			SessionUtil.putFuncionarioLogado(null);
			logado = null;
		}
	}
	
	public void setSenhaFuncionarioPrimeiraVes(Funcionario f, String senha)
			throws ParametrosInvalidosException {
		if(f.getSenha() != null || !f.getSenha().isEmpty()){
			throw new ParametrosInvalidosException();
		}
		setSenhaFuncionario(f, senha);
	}

	public void modificarSenhaFuncionario(Funcionario f, String senhaAntiga,
			String novaSenha) throws ParametrosInvalidosException, SenhaIncorretaException {
		validarSenha(f, senhaAntiga);
		setSenhaFuncionario(f, novaSenha);
	}

	private String criptografar(String string) {
		return string;
	}
	
	private void validarSenha(Funcionario f, String senha)
			throws ParametrosInvalidosException, SenhaIncorretaException {
		if (f == null || senha == null) {
			throw new ParametrosInvalidosException();
		}
		if (f.getSenha() != null || f.getSenha().isEmpty()) {
			throw new ParametrosInvalidosException();
		}
		if (!f.getSenha().equals(this.criptografar(senha))) {
			throw new SenhaIncorretaException();
		}
	}
	
	private void setSenhaFuncionario(Funcionario f, String senha)
			throws ParametrosInvalidosException {
		if (f == null || senha == null) {
			throw new ParametrosInvalidosException();
		}
		f.setSenha(criptografar(senha));
		Funcionario.atualizar(f);
	}
}
