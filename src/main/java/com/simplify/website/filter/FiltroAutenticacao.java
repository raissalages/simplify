package com.simplify.website.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import org.springframework.core.*;
import org.springframework.core.annotation.Order;

import java.io.IOException;

@WebFilter(urlPatterns = {"/paginainicial", "/listagemdespesas", "/cadastrodespesa"})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FiltroAutenticacao implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code, if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        String loginURL = httpRequest.getContextPath() + "/login";

        boolean loginRequest = httpRequest.getRequestURI().equals(loginURL);

        // Check if the user is authenticated (you can customize this condition)
        if (session != null && session.getAttribute("usuarioAutenticado") != null) {
            System.out.println(loginRequest);
            System.out.println();
            // User is authenticated, continue with the request
            System.out.println("Usuário autenticado. Continuando com a requisição.");

            chain.doFilter(request, response);
        } else {
            // User is not authenticated, redirect to the login page
            System.out.println("Usuário não autenticado. Redirecionando para a página de login.");

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }
        System.out.println("Filtro de Autenticação: Concluído");


    }

    @Override
    public void destroy() {
        // Cleanup code, if needed
    }
}
