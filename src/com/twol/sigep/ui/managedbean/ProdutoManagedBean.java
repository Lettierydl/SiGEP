package com.twol.sigep.ui.managedbean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.primefaces.context.RequestContext;

import com.twol.sigep.Facede;
import com.twol.sigep.model.estoque.CategoriaProduto;
import com.twol.sigep.model.estoque.Produto;

@ManagedBean(name = "produtoBean")
public class ProdutoManagedBean {
	private Facede f;
	
	private Produto newProduto;
	private String descricao = "";
	private List<Produto> listAtualDeProdutos;
	
	public ProdutoManagedBean(){
		f = new Facede();
		newProduto = new Produto();
		listAtualDeProdutos = f.getListaProdutos();
	}

	public List<String> autoCompletNomeCliente(String nome){
		System.out.println(nome);
		try{
			if(nome.isEmpty()){
				return new ArrayList<String>();
			}
			return f.buscarNomeClientePorNomeQueInicia(nome);
		}finally{
			
		}
	}
	
	public void cadastrarProduto(){
		try{
			validarInformacoesCadastrais(newProduto);
		}catch(Exception e){
			return;
		}
		f.adicionarProduto(newProduto);
		newProduto = new Produto();
		listAtualDeProdutos = f.getListaProdutos();
	}
	
	public void validarInformacoesCadastrais(Produto c){}
	
	public List<Produto> getListAtualDeProduto(){
		if(listAtualDeProdutos == null || listAtualDeProdutos.isEmpty()){
			return f.getListaProdutos();
		}
		return listAtualDeProdutos;
	}
	
	public void modificarListaAtualDeProdutosPelaDescricao(){
		if(descricao != null && descricao.length() != 0){
			listAtualDeProdutos = f.buscarProdutoPorDescricaoQueInicia(descricao);
			RequestContext.getCurrentInstance().update("tabelaProdutos");
		}else{
			listAtualDeProdutos = f.getListaProdutos();
			RequestContext.getCurrentInstance().update("tabelaProdutos");
		}
	}
	
	
	public CategoriaProduto[] getCategorias(){
		return CategoriaProduto.values();
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
}
