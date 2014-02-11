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

import com.twol.sigep.model.Entidade;
import com.twol.sigep.util.OperacaoStringUtil;
import com.twol.sigep.util.Persistencia;

@Table(name = "endereco")
@Entity
public class Endereco extends Entidade{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private int id;

	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}
	
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
				.createNamedQuery("select endereco from Endereco endereco");
		List<Endereco> enderecos = consulta.getResultList();
		return enderecos;
    }
	
	
    
}
