package com.twol.sigep.util;

import java.text.DecimalFormat;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = Double.class, value = "valueUnidadeConverter")
public class ValueUnidadeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try {
			if (arg2.isEmpty()) {
				return 0.0;
			}
			return Double.valueOf(arg2.replace(".", "").replace(" ", "")
					.replace(",", "."));
		} catch (NumberFormatException ne) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Valor não numérico",
					"Valor não numérico"));
			return new Double(0);
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		try {
			double valor = (Double) arg2;
			int valor2 = (int) valor;
			if (valor == valor2) {// é inteiro
				return new DecimalFormat("0").format(arg2);
			}
			return new DecimalFormat("0.000").format(arg2);
		} catch (NumberFormatException ne) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Valor não numérico",
					"Valor não numérico"));
			return "" ;
		}
	}

}
