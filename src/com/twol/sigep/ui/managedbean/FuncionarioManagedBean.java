package com.twol.sigep.ui.managedbean;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.exception.FuncionarioNaoAutorizadoException;
import com.twol.sigep.model.exception.LoginIncorretoException;
import com.twol.sigep.model.exception.SenhaIncorretaException;
import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.pessoas.TipoDeFuncionario;
import com.twol.sigep.util.SessionUtil;

@ViewScoped
@ManagedBean(name = "funcionarioBean")
public class FuncionarioManagedBean {

	private Facede f;

	private Funcionario newFuncionario;
	private Funcionario editFuncionario;
	private TipoDeFuncionario tipoDoFuncionario = TipoDeFuncionario.Caixa;
	private String nomePesquisa = "";
	private String senha;
	private String senhaEdit;
	private String senhaNewEdit;
	private List<Funcionario> listAtualDeFuncionarios;

	public FuncionarioManagedBean() {
		f = new Facede();
		newFuncionario = new Funcionario();
		listAtualDeFuncionarios = f.getFuncionarios();
	}

	public void cadastrarFuncionario() {
		try {
			f.adicionarFuncionario(newFuncionario, senha, tipoDoFuncionario);
		} catch (SenhaIncorretaException e) {
			e.printStackTrace();
		} catch (FuncionarioNaoAutorizadoException e) {
			SessionUtil
					.exibirMensagem(new FacesMessage(
							FacesMessage.SEVERITY_ERROR, e.getMessage(), e
									.getMessage()));
			return;
		}
		newFuncionario = new Funcionario();
		listAtualDeFuncionarios = f.getFuncionarios();
	}

	public TipoDeFuncionario[] getTiposFuncionario() {
		return TipoDeFuncionario.values();
	}

	public void validarInformacoesCadastrais(Funcionario f) {
	}

	public List<Funcionario> getListAtualDeFuncionarios() {
		if (listAtualDeFuncionarios == null
				|| listAtualDeFuncionarios.isEmpty()) {
			return f.getFuncionarios();
		}
		return listAtualDeFuncionarios;
	}

	public void modificarListaAtualDeFuncionarioPeloNome() {
		if (nomePesquisa != null && nomePesquisa.length() != 0) {
			listAtualDeFuncionarios = f
					.buscarFuncionarioPorNomeQueInicia(nomePesquisa);
			RequestContext.getCurrentInstance().update("tabelaFuncionarios");
		} else {
			listAtualDeFuncionarios = f.getFuncionarios();
			RequestContext.getCurrentInstance().update("tabelaFuncionarios");
		}
	}

	public void openEditFuncionario(Funcionario f) {
		this.setEditFuncionario(f);
		RequestContext.getCurrentInstance().execute("abrirModa('modalEdit');");
		/*
		 * RequestContext.getCurrentInstance().update("formEdit");
		 * RequestContext.getCurrentInstance().update("nomeEdit");
		 * RequestContext.getCurrentInstance().openDialog("modalEdit");
		 * RequestContext.getCurrentInstance().openDialog("#modalEdit");
		 */
	}

	public void modific(ValueChangeEvent event) {
		nomePesquisa = (String) event.getNewValue();
		modificarListaAtualDeFuncionarioPeloNome();
	}

	public void editarFuncionario() {
		if (senhaNewEdit != null && !senhaNewEdit.isEmpty()) {
			try {
				f.alterarSenhaDoFuncionario(editFuncionario, senhaEdit,
						senhaNewEdit);
			} catch (SenhaIncorretaException e) {
				SessionUtil.exibirMensagem((new FacesMessage(
						FacesMessage.SEVERITY_FATAL, "Senha incorreta",
						"Senha incorreta")));
			} catch (LoginIncorretoException e) {
				SessionUtil.exibirMensagem((new FacesMessage(
						FacesMessage.SEVERITY_FATAL, "Login incorreto",
						"Login incorreto")));
			}
		}

		try {
			Funcionario oud = f.buscarFuncionarioPorId(editFuncionario.getId());
			if (!oud.getNome().equals(editFuncionario.getNome())) {// modificou
				try {
					f.buscarFuncionarioPorNome(editFuncionario.getNome());
					SessionUtil.exibirMensagem((new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Já existe um funcionário com o nome "
									+ editFuncionario.getNome(),
							"Já existe um funcionário com o nome "
									+ editFuncionario.getNome())));
					editFuncionario.setNome(oud.getNome());
					editFuncionario.setCpf(oud.getCpf());
					return;
				} catch (Exception ep) {
				}// ok nao existe
			}
			if (!oud.getCpf().equals(editFuncionario.getCpf())) {// modificou
				try {
					f.buscarFuncionarioPorCPF(editFuncionario.getCpf());
					SessionUtil.exibirMensagem((new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Já existe um funcionário com o CPF "
									+ editFuncionario.getCpf(),
							"Já existe um funcionário com o CPF "
									+ editFuncionario.getCpf())));
					editFuncionario.setCpf(oud.getCpf());
					return;
				} catch (Exception ep) {
				}// ok nao existe
			}
			f.atualizarFuncionario(editFuncionario);

		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe
							.getMessage()));
			return;
		}

		catch (EntidadeNaoExistenteException e) {
			SessionUtil.exibirMensagem((new FacesMessage(
					FacesMessage.SEVERITY_FATAL,
					"Reveja a conexão com o servidor",
					"Reveja a conexão com o servidor")));
			e.printStackTrace();
		} catch (Exception e) {
			SessionUtil.exibirMensagem((new FacesMessage(
					FacesMessage.SEVERITY_FATAL,
					"Formulário com informações inválidas",
					"Formulário com informações inválidas")));
			e.printStackTrace();
		} finally {

			RequestContext.getCurrentInstance().update("@all");
		}

	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Funcionario getNewFuncionario() {
		return newFuncionario;
	}

	public void setNewFuncionario(Funcionario newFuncionario) {
		this.newFuncionario = newFuncionario;
	}

	public String getNomePesquisa() {
		return nomePesquisa;
	}

	public void setNomePesquisa(String nome) {
		this.nomePesquisa = nome;
	}

	public TipoDeFuncionario getTipoDoFuncionario() {
		return tipoDoFuncionario;
	}

	public void setTipoDoFuncionario(TipoDeFuncionario tipoDoFuncionario) {
		this.tipoDoFuncionario = tipoDoFuncionario;
	}

	public Funcionario getFuncionarioLogado() {
		return f.getFuncionarioLogado();
	}

	public Funcionario getEditFuncionario() {
		return editFuncionario;
	}

	public void setEditFuncionario(Funcionario editFuncionario) {
		this.editFuncionario = editFuncionario;
	}

	public String getSenhaEdit() {
		return senhaEdit;
	}

	public void setSenhaEdit(String senhaEdit) {
		this.senhaEdit = senhaEdit;
	}

	public String getSenhaNewEdit() {
		return senhaNewEdit;
	}

	public void setSenhaNewEdit(String senhaNewEdit) {
		this.senhaNewEdit = senhaNewEdit;
	}

}
