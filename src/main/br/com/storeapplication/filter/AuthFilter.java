package br.com.storeapplication.filter;

import br.com.storeapplication.shared.Paginas;
import br.com.storeapplication.shared.Sessao;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(false);
        boolean logado = session != null && session.getAttribute(Sessao.USUARIO_SESSAO) != null;

        if (logado) {
            chain.doFilter(req, res);
        } else {
            response.sendRedirect(request.getContextPath() + Paginas.INDEX);
        }
    }

    @Override
    public void init(FilterConfig config) {}

    @Override
    public void destroy() {}

}
