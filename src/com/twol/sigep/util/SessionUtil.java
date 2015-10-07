package com.twol.sigep.util;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.model.vendas.Venda;

public class SessionUtil {

	public static final String KEY_USUARIO_LOGADO = "USUARIO_LOGADO";
	public static final String KEY_VENDA_ATUAL = "VENDA_ATUAL";
	public static final String KEY_VENDA_A_FINALIZAR = "VENDA_A_FINALIZAR";
	public static final String KEY_NEXT_PAGE = "NEXT_PAGE";
	public static final String KEY_NEXT_MESAGEM = "NEXT_MESAGEM";
	public static final String PAGE_PRINCIPAL = "restrito/home.jsf";
	public static final String PAGE_INICIAL = "/CloudSistem/";
	public static final String PAGE_PAGAMENTO = "pagamento.jsf";
	public static final String PAGE_CLIENTE = "cliente.jsf";
	public static final String PAGE_PRODUTO = "produto.jsf";
	public static final String PAGE_FUNCIONARIO = "funcionario.jsf";
	
	

    public static HttpServletRequest obterRequest() {
		return (HttpServletRequest) obterContext()
                .getExternalContext().getRequest();
    }

    public static HttpServletResponse obterResponse() {
			return (HttpServletResponse) obterContext()
                            .getExternalContext().getResponse();
    }

    public static FacesContext obterContext() {
            return FacesContext.getCurrentInstance();
    }

    public static HttpSession obterSession() {
        return (HttpSession) obterContext().getExternalContext().getSession(
                        false);
}
    
	public static void putFuncionarioLogado(Funcionario f){
    	obterSession().setAttribute(KEY_USUARIO_LOGADO, f);
    }
    
    public static Funcionario getFuncionarioLogado() {
    	try{
    		Funcionario funcionario = (Funcionario) obterSession().getAttribute(
                    KEY_USUARIO_LOGADO);
    		return funcionario;
    	}catch(NullPointerException ne){
    		return null;
    	}
    }
	
    
    public static void putVendaAtual(Venda venda){
    	obterSession().setAttribute(KEY_VENDA_ATUAL, venda);
    }
    
    public static Venda getVendaAtual() throws NullPointerException{
    	return (Venda) obterSession().getAttribute(KEY_VENDA_ATUAL);
    }
    
    public static void putVendaAFinalizar(Venda venda){
    	obterSession().setAttribute(KEY_VENDA_A_FINALIZAR, venda);
    }
    
    public static Venda getVendaAFinalizar() throws NullPointerException{
    	return (Venda) obterSession().getAttribute(KEY_VENDA_A_FINALIZAR);
    }
    
    public static void removerVendaAFinalizar(){
    	obterSession().removeAttribute(KEY_VENDA_A_FINALIZAR);
    }
    
    public static String getNextPage(){
    	return (String) obterSession().getAttribute(KEY_NEXT_PAGE);
    }
   
    public static void putNextPage(String url){
    	obterSession().setAttribute(KEY_NEXT_PAGE, url);
    }
    
    public static FacesMessage getNextMensagem(){
    	return (FacesMessage) obterSession().getAttribute(KEY_NEXT_MESAGEM);
    }
    
    public static void exibirNextMensagem(){
    	exibirMensagem(getNextMensagem());
    }
    
    public static void removerNextMensagem(){
    	obterSession().removeAttribute(KEY_NEXT_MESAGEM);
    }
    
    public static void putNextMensagem(FacesMessage msg){
    	obterSession().setAttribute(KEY_NEXT_MESAGEM, msg);
    }
    
    public static void exibirMensagem(FacesMessage msg){
    	FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public static void redirecionarParaPage(String page) throws IOException {
		obterResponse().sendRedirect(page);
	}
    
	
}
