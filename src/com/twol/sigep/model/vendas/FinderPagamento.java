package com.twol.sigep.model.vendas;

import java.util.List;

import javax.persistence.Query;

import com.twol.sigep.model.pessoas.Cliente;
import com.twol.sigep.util.Persistencia;

public class FinderPagamento {
	
	@SuppressWarnings("unchecked")
	public static List<Pagamento> pagamentosDeHoje() {
		
		String stringQuery = "select p FROM Pagamento as p ";
		stringQuery += "WHERE day(p.data) = day(curdate()) and p.data >= curdate()"
					+ " order by p.data DESC";
		
		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, Pagamento.class);
		
		List<Pagamento> pagamentos = (List<Pagamento>) query.getResultList();
		return pagamentos;
	}
	
	
	public static List<Pagamento> pagamentosDoCliente(Cliente cliente) {
		String stringQuery = "select p FROM Pagamento as p ";
		stringQuery += "WHERE (p.cliente != :cli) ";
		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, Pagamento.class);
		
			query.setParameter("cli", cliente);
		
		@SuppressWarnings("unchecked")
		List<Pagamento> pagamentos = (List<Pagamento>) query.getResultList();
		return pagamentos;
	}


	public static List<Pagamento> pagamentosDosClientes(List<Cliente> clientes) {
		//vai rodando pelo cliente_id //for clientes cli{ p.cliente = cli or }
			String stringQuery = "select p FROM Pagamento as p ";
			stringQuery += "WHERE (p.cliente != NULL) ";
				for(int i = 0; i< clientes.size(); i++){
					if(i>0){
						stringQuery += "or p.cliente = :cli"+i+" ";
					}else{
						stringQuery += "and ( p.cliente = :cli"+i+" ";
					}
				}
				
				stringQuery+= ") order by p.data DESC";
				
				Persistencia.restartConnection();
				Query query = Persistencia.em.createQuery(stringQuery, Pagamento.class);
				
				for(int i = 0; i< clientes.size(); i++){
					query.setParameter("cli"+i, clientes.get(i));
				}
				
				@SuppressWarnings("unchecked")
				List<Pagamento> pagamentos = (List<Pagamento>) query.getResultList();
				return pagamentos;
	}
	
	
	@SuppressWarnings("unchecked")
	public static Pagamento pagamentoId(int id) {
		
		String stringQuery = "select p FROM Pagamento as p where p.id = :id";
		
		Persistencia.restartConnection();
		Query query = Persistencia.em.createQuery(stringQuery, Pagamento.class);
		query.setParameter("id", id);
		
		Pagamento pagamento = (Pagamento) query.getSingleResult();
		return pagamento;
	}
	
	
	
	
	
}
