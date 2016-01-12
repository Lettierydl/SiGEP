package com.twol.sigep.model;
import java.util.List;

public abstract class Entidade {
	
	public abstract int getId();
	
	
	protected List<?> getListEntidadeRelacionada(){
		return null;
	}
	
	
	
	
}