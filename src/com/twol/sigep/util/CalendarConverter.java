package com.twol.sigep.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass= Double.class, value="calendarConverter")
public class CalendarConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(arg2);
		} catch (ParseException e) {
			try {
				return new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(arg2);
			} catch (ParseException e1) {
				return null;
			}
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		Calendar c = (Calendar) arg2;
		return new SimpleDateFormat("dd/MM/yyyy").format(c.getTime());
	}

}
