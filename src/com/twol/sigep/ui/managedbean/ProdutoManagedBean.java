package com.twol.sigep.ui.managedbean;

import java.io.IOException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.model.estoque.CategoriaProduto;
import com.twol.sigep.model.estoque.Produto;
import com.twol.sigep.model.estoque.UnidadeProduto;
import com.twol.sigep.model.exception.EntidadeNaoExistenteException;
import com.twol.sigep.model.exception.FuncionarioNaoAutorizadoException;
import com.twol.sigep.util.SessionUtil;

@ViewScoped
@ManagedBean(name = "produtoBean")
public class ProdutoManagedBean {
	private Facede f;

	private Produto newProduto;
	private Produto editProduto;
	private String descricaoPesquisa = "";
	private List<Produto> listAtualDeProdutos;

	public ProdutoManagedBean() {
		f = new Facede();
		newProduto = new Produto();
		listAtualDeProdutos = f.getListaProdutos();
	}

	public void cadastrarProduto() throws IOException {
		try {
			f.adicionarProduto(newProduto);
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe
							.getMessage()));
			return;
		} catch (Exception e) {
			e.printStackTrace();
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Produto não cadastrado reveja o formulario",
					"Produto não cadastrado reveja o formulario"));
			RequestContext.getCurrentInstance().execute(
					"abrirModa('modalEdit');");
			return;

		}
		SessionUtil.exibirMensagem((new FacesMessage(
				FacesMessage.SEVERITY_INFO, "Produto cadastrado",
				"Produto cadastrado")));
		newProduto = new Produto();
		listAtualDeProdutos = f.getListaProdutos();
		RequestContext.getCurrentInstance().update("@all");
	}

	public List<Produto> getListAtualDeProduto() {
		if (listAtualDeProdutos == null || listAtualDeProdutos.isEmpty()) {
			return f.getListaProdutos();
		}
		return listAtualDeProdutos;
	}

	public void modificarListaAtualDeProdutosPelaDescricao() {
		if (descricaoPesquisa != null && descricaoPesquisa.length() != 0) {
			listAtualDeProdutos = f
					.buscarProdutoPorDescricaoOuCodigoQueInicia(descricaoPesquisa);
			RequestContext.getCurrentInstance().update("tabelaProdutos");
		} else {
			listAtualDeProdutos = f.getListaProdutos();
			RequestContext.getCurrentInstance().update("tabelaProdutos");
		}
	}

	public void openEditProduto(Produto p) {
		this.setEditProduto(p);
		RequestContext.getCurrentInstance().execute("abrirModa('modalEdit');");
		/*
		 * RequestContext.getCurrentInstance().update("formEdit");
		 * RequestContext.getCurrentInstance().update("nomeEdit");
		 * RequestContext.getCurrentInstance().openDialog("modalEdit");
		 * RequestContext.getCurrentInstance().openDialog("#modalEdit");
		 */
	}

	public void modific(ValueChangeEvent event) {
		descricaoPesquisa = (String) event.getNewValue();
		modificarListaAtualDeProdutosPelaDescricao();
	}

	public void editarProduto() {
		try {
			Produto oud = f.buscarProdutoPorId(editProduto.getId());
			if (!oud.getCodigoDeBarras()
					.equals(editProduto.getCodigoDeBarras())) {// modificou
				try {
					f.buscarProdutoPorCodigo(editProduto.getCodigoDeBarras());
					SessionUtil.exibirMensagem((new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Já existe um produto com o código "
									+ editProduto.getCodigoDeBarras(),
							"Já existe um produto com o código "
									+ editProduto.getCodigoDeBarras())));
					editProduto.setCodigoDeBarras(oud.getCodigoDeBarras());
					editProduto.setDescricao(oud.getDescricao());
					return;
				} catch (Exception ep) {
				}// ok nao existe
			}
			if (!oud.getDescricao().equals(editProduto.getDescricao())) {// modificou
				try {
					f.buscarProdutoPorDescricao(editProduto.getDescricao());
					SessionUtil.exibirMensagem((new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Já existe um produto com a descrição "
									+ editProduto.getDescricao(),
							"Já existe um produto com a descrição "
									+ editProduto.getDescricao())));
					editProduto.setDescricao(oud.getDescricao());
					return;
				} catch (Exception ep) {
				}// ok nao existe
			}
			f.atualizarProduto(editProduto);
		} catch (FuncionarioNaoAutorizadoException fe) {
			SessionUtil.exibirMensagem(new FacesMessage(
					FacesMessage.SEVERITY_ERROR, fe.getMessage(), fe
							.getMessage()));
			return;
		} catch (EntidadeNaoExistenteException e) {
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
		}finally{
			RequestContext.getCurrentInstance().update("@all");
		}
	}

	public CategoriaProduto[] getCategorias() {
		return CategoriaProduto.values();
	}

	public UnidadeProduto[] getUnidadesProduto() {
		return UnidadeProduto.values();
	}

	public Produto getNewProduto() {
		return newProduto;
	}

	public void setNewProduto(Produto newProduto) {
		this.newProduto = newProduto;
	}

	public List<Produto> getListAtualDeProdutos() {
		return listAtualDeProdutos;
	}

	public void setListAtualDeProdutos(List<Produto> listAtualDeProdutos) {
		this.listAtualDeProdutos = listAtualDeProdutos;
	}

	public String getDescricaoPesquisa() {
		return descricaoPesquisa;
	}

	public void setDescricaoPesquisa(String descricao) {
		this.descricaoPesquisa = descricao;
	}

	public Produto getEditProduto() {
		return editProduto;
	}

	public void setEditProduto(Produto editProduto) {
		this.editProduto = editProduto;
	}

}
