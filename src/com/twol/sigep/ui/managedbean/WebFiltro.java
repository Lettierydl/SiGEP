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
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		
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
			funcionarioLogado = (Funcionario) httpRequest.getSession().getAttribute(SessionUtil.KEY_USUARIO_LOGADO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(funcionarioLogado == null){
			httpResponse.sendRedirect(SessionUtil.PAGE_INICIAL);
			httpRequest.getSession().setAttribute(SessionUtil.KEY_NEXT_PAGE, httpRequest.getRequestURI());
			return;
		}
		
		if(httpRequest.getSession().getAttribute(SessionUtil.KEY_NEXT_PAGE) != null){
			if(httpRequest.getSession().getAttribute(SessionUtil.KEY_VENDA_A_FINALIZAR) == null && httpRequest.getSession().getAttribute(SessionUtil.KEY_NEXT_PAGE).toString().contains("finalizar")){
				httpResponse.sendRedirect("venda.jsf");
				httpRequest.getSession().removeAttribute(SessionUtil.KEY_NEXT_PAGE);
			}else{
				httpResponse.sendRedirect((String) httpRequest.getSession().getAttribute(SessionUtil.KEY_NEXT_PAGE));
				httpRequest.getSession().removeAttribute(SessionUtil.KEY_NEXT_PAGE);
			}
			
		}
		
	
		chain.doFilter(httpRequest, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
