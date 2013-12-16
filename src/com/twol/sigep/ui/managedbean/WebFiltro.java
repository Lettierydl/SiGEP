package com.twol.sigep.ui.managedbean;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.twol.sigep.model.pessoas.Funcionario;
import com.twol.sigep.util.SessionUtil;

/**
 * Servlet Filter implementation class WebFiltro
 */
@WebFilter("/restrito/*")
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
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		Funcionario funcionarioLogado = null;
		try {
			funcionarioLogado = (Funcionario) httpRequest.getSession()
					.getAttribute(SessionUtil.KEY_USUARIO_LOGADO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(funcionarioLogado == null){
			httpResponse.sendRedirect(SessionUtil.PAGE_INICIAL);
			return;
		}
		
		chain.doFilter(httpRequest, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
