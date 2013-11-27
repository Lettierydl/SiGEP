package com.twol.sigep.model.pessoas;

import java.util.List;

import com.twol.sigep.model.Entidade;
import com.twol.sigep.util.Persistencia;

public abstract class Pessoa extends Entidade {

	public abstract List<Telefone> getTelefones();

	public static void salvar(Pessoa e) {
		if (e.getTelefones() != null && !e.getTelefones().isEmpty()) {
			Persistencia.iniciarTrascao();
			try {
				Persistencia.em.persist(e.getTelefones().get(0));
			} finally {
				Persistencia.finalizarTrascao();
			}
			return;
		}
		if (e.getListEntidadeRelacionada() != null
				&& !e.getListEntidadeRelacionada().isEmpty()) {
			Persistencia.iniciarTrascao();
			try {
				Persistencia.em.persist(e.getListEntidadeRelacionada().get(0));
			} finally {
				Persistencia.finalizarTrascao();
			}
			return;
		}
		Persistencia.iniciarTrascao();
		try {
			Persistencia.em.persist(e);
		} finally {
			Persistencia.finalizarTrascao();
		}

	}
}
