package com.twol.sigep.ui.managedbean;

import java.util.List;

import javax.faces.bean.ManagedBean;

import com.twol.sigep.Facede;


@ManagedBean(name="tmb")
public class TesteManagedBena {

	
	private String text;
	
	public List<String> complet(String q){
		System.out.println(q);
		Facede f = new Facede();
		return f.buscarNomeClientePorNomeQueInicia(q);
	}

	
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
	
	
	
	
	
}
