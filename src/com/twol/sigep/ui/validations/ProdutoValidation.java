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
public class ProdutoValidation implements Validator {
	private static Facede f = new Facede();

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ValidatorException {
		if (arg1.getId().equals("inputCodigo")) {
			String cod = arg2.toString();
			try {
				f.buscarProdutoPorCodigo(cod);
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Produto já cadastrado com o código " + cod,
						"Produto já cadastrado com o código " + cod));
			} catch (NoResultException nre) {
			}

		}
		if (arg1.getId().equals("inputDescricao")) {
			String desc = arg2.toString();
			try {
				f.buscarProdutoPorDescricaoOuCodigo(desc);
				throw new ValidatorException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Produto já cadastrado com a descrição " + desc,
						"Produto já cadastrado com a descrição " + desc));
			} catch (NoResultException nre) {
			}

		}

	}

}
