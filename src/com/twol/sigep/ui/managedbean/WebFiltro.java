package com.twol.sigep.ui.managedbean;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.util.SessionUtil;

/**
 * Servlet Filter implementation class WebFiltro
 */
@WebFilter("/restrito")
public class WebFiltro implements Filter {

    /**
     * Default constructor. 
     */
    public WebFiltro() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		Funcionario funcionarioLogado = SessionUtil.getFuncionarioLogado();

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
