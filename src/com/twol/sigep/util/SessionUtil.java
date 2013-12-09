package com.twol.sigep.util;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.twol.sigep.model.pessoas.Funcionario;

public class SessionUtil {

	public static final String KEY_USUARIO_LOGADO = "USUARIO_LOGADO";
	public static final String PAGE_PRINCIPAL = "restrito/principal.jsf";
	
	

    public static HttpServletRequest obterRequest() {
            obterContext();
			return (HttpServletRequest) FacesContext.getCurrentInstance()
                            .getExternalContext().getRequest();
    }

    public static HttpServletResponse obterResponse() {
            obterContext();
			return (HttpServletResponse) FacesContext.getCurrentInstance()
                            .getExternalContext().getResponse();
    }

    public static FacesContext obterContext() {
            return FacesContext.getCurrentInstance();
    }

    public static HttpSession obterSession() {
            return (HttpSession) obterContext().getExternalContext().getSession(
                            false);
    }
    
    @SuppressWarnings("deprecation")
	public static void putFuncionarioLogado(Funcionario f){
    	obterSession().putValue(KEY_USUARIO_LOGADO, f);
    }
    
    public static Funcionario getFuncionarioLogado() {
    	Funcionario funcionario = (Funcionario) obterSession().getAttribute(
                            KEY_USUARIO_LOGADO);
            return funcionario;
    }
	
    public static void redirecionarParaPage(String page) throws IOException {
		obterResponse().sendRedirect(page);
	}
	
}
