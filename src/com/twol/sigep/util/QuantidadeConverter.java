package com.twol.sigep.util;

import java.text.DecimalFormat;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass= Double.class, value="quantidadeConverter")
public class QuantidadeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		return Double.valueOf(arg2.replace(".", "").replace(" ","").replace(",", "."));
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		return new DecimalFormat("0.000").format(arg2);
	}

}
