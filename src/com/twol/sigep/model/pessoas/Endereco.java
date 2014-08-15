package com.twol.sigep.model.pessoas;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;

import com.twol.sigep.util.OperacaoStringUtil;
import com.twol.sigep.util.Persistencia;

@Table(name = "endereco")
@Entity
public class Endereco{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private int id;
	
    /**
     */
    @Column(nullable = false)
    private String rua = "";

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
    private UF uf = UF.PB;

    public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}	
    
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
	
	private boolean isPreenchido(){
		return rua != null && numero !=null && bairro != null && cidade != null && cep != null;
	}
	
	@Override
	public String toString() {
		if(!isPreenchido()){
			return "Não Preenchido";
		}
		String cep = "";
		if(this.cep!=null && this.cep.length() >= 8){
			cep = ", CEP: "+OperacaoStringUtil.formatarStringParaMascaraDeCep(this.cep);
		}
		return rua +", Nº "+numero+" - Bairro: "+bairro+", "+cidade+" " + uf + cep;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Endereco> recuperarLista(){
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select e from Endereco as e", Endereco.class);
		List<Endereco> enderecos = consulta.getResultList();
		return enderecos;
    }
	
	public static Endereco recuperarEnderecoId(int id){
		Persistencia.restartConnection();
		Query consulta = Persistencia.em
				.createQuery("select e from Endereco as e where e.id = :id", Endereco.class);
		consulta.setParameter("id",id);
		Endereco endereco = (Endereco) consulta.getSingleResult();
		return endereco;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bairro == null) ? 0 : bairro.hashCode());
		result = prime * result + ((cep == null) ? 0 : cep.hashCode());
		result = prime * result + ((cidade == null) ? 0 : cidade.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((rua == null) ? 0 : rua.hashCode());
		result = prime * result + ((uf == null) ? 0 : uf.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Endereco other = (Endereco) obj;
		if (bairro == null) {
			if (other.bairro != null)
				return false;
		} else if (!bairro.equals(other.bairro))
			return false;
		if (cep == null) {
			if (other.cep != null)
				return false;
		} else if (!cep.equals(other.cep))
			return false;
		if (cidade == null) {
			if (other.cidade != null)
				return false;
		} else if (!cidade.equals(other.cidade))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (rua == null) {
			if (other.rua != null)
				return false;
		} else if (!rua.equals(other.rua))
			return false;
		if (uf != other.uf)
			return false;
		return true;
	}
	
	
    
}
