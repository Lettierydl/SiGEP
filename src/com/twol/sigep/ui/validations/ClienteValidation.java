package com.twol.sigep.ui.validations;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import com.twol.sigep.Facede;

@FacesValidator
public class ClienteValidation implements Validator {
	private static Facede f = new Facede();

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ValidatorException {
		if (arg1.getId().equals("inputNome")) {
			String name = arg2.toString();
			try {
				f.buscarClientePorNome(name);
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Cliente já cadastrado com o nome " + name,
						"Cliente já cadastrado com o nome " + name));
			} catch (NoResultException nre) {
				
			}catch (NonUniqueResultException e) {
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_FATAL,
						"Existe mais de um cliente cadastrado com o nome " + name,
						"Existe mais de um cliente cadastrado com o nome " + name));
			}
			

		}
		if (arg1.getId().equals("inputCPF")) {
			String cpf = arg2.toString().replace(".", "").replace("-", "");
			try {
				f.buscarClientePorCPF(cpf);
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Cliente já cadastrado com o CPF: " + cpf,
						"Cliente já cadastrado com o CPF: " + cpf));
			} catch (NoResultException nre) {
			}catch (NonUniqueResultException e) {
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_FATAL,
						"Existe mais de um cliente cadastrado com o CPF: " + cpf,
						"Existe mais de um cliente cadastrado com o CPF: " + cpf));
			}

		}
	}

}
