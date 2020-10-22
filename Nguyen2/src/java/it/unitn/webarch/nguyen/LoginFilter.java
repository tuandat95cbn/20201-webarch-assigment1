/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unitn.webarch.nguyen;

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
import javax.servlet.http.HttpSession;

/**
 *
 * @author tuandat95cbn
 */
@WebFilter(urlPatterns = {"/", "/ChangePassword", "/Forget"})
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig fc) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1, FilterChain fc) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) sr;
        HttpServletResponse response = (HttpServletResponse) sr1;
        HttpSession session = request.getSession();
        String username = null;
        synchronized (session) {
            username = (String) session.getAttribute("username");
        }
        if (username != null) {
            fc.doFilter(sr, sr1);
        } else {
            response.sendRedirect("Login");
        }

    }

    @Override
    public void destroy() {

    }

}
