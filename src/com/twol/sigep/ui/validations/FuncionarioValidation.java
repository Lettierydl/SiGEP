package com.twol.sigep.ui.validations;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.persistence.NoResultException;

import com.twol.sigep.Facede;

@FacesValidator
public class FuncionarioValidation implements Validator {
	private static Facede f = new Facede();

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ValidatorException {
		if (arg1.getId().equals("inputNome")) {
			String nome = arg2.toString();
			try {
				f.buscarFuncionarioPorNome(nome);
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Funcionário já cadastrado com o nome " + nome,
						"Funcionário já cadastrado com o nome " + nome));
			} catch (NoResultException nre) {
			}
			

		}
		if (arg1.getId().equals("inputCPF")) {
			String cpf = arg2.toString();
			try {
				f.buscarFuncionarioPorCPF(cpf);
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Funcionário já cadastrado com o CPF " + cpf,
						"Funcionário já cadastrado com o CPF " + cpf));
			} catch (NoResultException nre) {
			}
		}
		
		if (arg1.getId().equals("inputLogin")) {
			String login = arg2.toString();
			try {
				f.buscarFuncionarioPorLogin(login);
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Funcionário já cadastrado com o login " + login,
						"Funcionário já cadastrado com o login " + login));
			} catch (NoResultException nre) {
			}
		}
		
	}

}
