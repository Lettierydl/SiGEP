package com.twol.sigep.model.pessoas;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.twol.sigep.util.OperacaoStringUtil;


//@RooJpaActiveRecord(finders = { "findFuncionariosByCpfEquals", "findFuncionariosByCpfLike", "findFuncionariosByNomeEquals", "findFuncionariosByNomeLike", "findFuncionariosByTipoDeFuncionario" })
@Table(name = "funcionario")
@Entity()
public class Funcionario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	private int id;
	
    /**
     */
    @Column(nullable = false, unique = true)
    private String nome;
    
    @Column(nullable = false, unique = true, length = 50)
    private String login;

    /**
     */
    @Column(nullable = false)
    private String senha;

    /**
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDeFuncionario tipoDeFuncionario;

    /**
     */
    @Column(nullable = false, length = 11, unique = true)
    private String cpf;
    
    /**
     */
    
    @Column(nullable = true, length = 14)
	private String telefone;
	
	@Column(nullable = true, length = 14)
	private String celular;
	
	@Column(nullable = true, length = 500)
	private String endereco;
    
    /*
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "funcionario")
    private List<Pagamento> pagamentos = new ArrayList<Pagamento>();
    */

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}


	public String getTelefone() {
		return OperacaoStringUtil.formatarStringParaMascaraDeTelefone(telefone);
	}

	public void setTelefone(String telefone) {
		this.telefone = OperacaoStringUtil.retirarMascaraDeTelefone(telefone);
	}

	public String getCelular() {
		return OperacaoStringUtil.formatarStringParaMascaraDeTelefone(celular);
	}

	public void setCelular(String celular) {
		this.celular = OperacaoStringUtil.retirarMascaraDeTelefone(celular);
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public TipoDeFuncionario getTipoDeFuncionario() {
		return tipoDeFuncionario;
	}

	public void setTipoDeFuncionario(TipoDeFuncionario tipoDeFuncionario) {
		this.tipoDeFuncionario = tipoDeFuncionario;
	}

	public String getCpf() {
		return  OperacaoStringUtil.formatarStringParaMascaraDeCPF(cpf);
	}

	public void setCpf(String cpf) {
		this.cpf = OperacaoStringUtil.retirarMascaraDeCPF(cpf);
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : login.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
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
		Funcionario other = (Funcionario) obj;
		if (login == null) {
			if (other.login != null)
				return false;
		} else if (!login.equals(other.login))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (senha == null) {
			if (other.senha != null)
				return false;
		} else if (!senha.equals(other.senha))
			return false;
		return true;
	}
    
}
