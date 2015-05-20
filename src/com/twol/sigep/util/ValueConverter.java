package com.twol.sigep.util;

import java.text.DecimalFormat;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass= Double.class, value="valueConverter")
public class ValueConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try{
			return Double.valueOf(arg2.replace(".", "").replace(" ","").replace(",", "."));
		}catch(NumberFormatException ne){
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, 
					"Valor não numérico"
					, "Valor não numérico"));
			return new Double(0);
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		try{
			if(arg2.toString() != "0.0"){
				return new DecimalFormat("0.00").format(arg2);
			}else{
				return  "0";
			}
		}catch(NumberFormatException ne){
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, 
					"Valor não numérico"
					, "Valor não numérico"));
			return "0,00";
		}
	}

}
