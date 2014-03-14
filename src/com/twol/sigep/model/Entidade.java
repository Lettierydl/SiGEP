package com.twol.sigep.model;
import java.util.List;

import com.twol.sigep.util.Persistencia;

public abstract class Entidade {
	
	public abstract int getId();
	
	protected List<?> getListEntidadeRelacionada(){
		return null;
	}
	
	public static void salvar(Entidade e)  {
		if (e.getListEntidadeRelacionada() != null
				&& !e.getListEntidadeRelacionada().isEmpty()) {
			Persistencia.iniciarTrascao();
			try{
				Persistencia.em.persist(e.getListEntidadeRelacionada().get(0));
			}finally{
				Persistencia.finalizarTrascao();
			}
		} else {
			Persistencia.iniciarTrascao();
			try{
				Persistencia.em.persist(e);
			}catch(Exception ex){
				ex.printStackTrace();
				Persistencia.restartConnection();
				return;
			}
			Persistencia.finalizarTrascao();
		}
	}

	public static void atualizar(Entidade e) {
		Persistencia.iniciarTrascao();
		try{
			Persistencia.em.merge(e);
		}finally{
			Persistencia.finalizarTrascao();
		}
	}

	public static void remover(Entidade e) {
		Persistencia.iniciarTrascao();
		try{
			Persistencia.em.remove(Persistencia.em.getReference(e.getClass(), e.getId()));
		}finally{
			Persistencia.finalizarTrascao();
		}
	}
	
	
	
}
