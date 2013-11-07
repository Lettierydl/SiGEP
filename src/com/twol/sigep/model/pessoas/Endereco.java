package com.twol.sigep.model.pessoas;

import java.util.List;

import javax.persistence.*;

import com.twol.sigep.util.Persistencia;

@Entity(name = "endereco")
public class Endereco {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
    /**
     */
    @Column(nullable = false)
    private String rua;

    /**
     */
    @Column(nullable = false, length = 10)
    private String numero;

    /**
     */
    @Column(nullable = false)
    private String bairro;

    /**
     */
    @Column(nullable = false)
    private String cidade;

    /**
     */
    @Column(nullable = false, length = 8)
    private String cep;

    /**
     */
    @Enumerated(EnumType.STRING)
    private UF uf;

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public UF getUf() {
		return uf;
	}

	public void setUf(UF uf) {
		this.uf = uf;
	}
    
    
	public static void salvar(Endereco e){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.persist(e);
    	Persistencia.em.getTransaction().commit();
    }
	
	public static void atualizar(Endereco e){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.merge(e);
    	Persistencia.em.getTransaction().commit();
    }
	
	public static void remover(Endereco e){
    	Persistencia.em.getTransaction().begin();
    	Persistencia.em.remove(e);
    	Persistencia.em.getTransaction().commit();
    }
	
	@SuppressWarnings("unchecked")
	public static List<Endereco> recuperarLista(){
		Persistencia.em.getTransaction().begin();
		Query consulta = Persistencia.em
				.createNamedQuery("select endereco from Endereco endereco");
		List<Endereco> enderecos = consulta.getResultList();
		Persistencia.em.getTransaction().commit();
		return enderecos;
    }
	
	
    
}
