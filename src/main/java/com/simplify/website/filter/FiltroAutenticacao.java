package com.simplify.website.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import org.springframework.core.*;
import org.springframework.core.annotation.Order;

import java.io.IOException;

@WebFilter(urlPatterns = {"/paginainicial", "/paginainicial.html", "/listagemdespesas", "/listagemdespesas.html",
        "/cadastrodespesa", "/cadastrodespesa.html", "/cadastrocategoria", "/cadastrocategoria.html",
        "/controleporcategoria","/controleporcategoria.html", "/perfil", "/perfil.html"})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FiltroAutenticacao implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        String loginURL = httpRequest.getContextPath() + "/login";


        if (session != null && session.getAttribute("usuarioAutenticado") != null) {
            chain.doFilter(request, response);
        } else {

            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }


    }

    @Override
    public void destroy() {
    }
}
