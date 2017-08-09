package net.coderodde.web.db.fun.controllers;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter implements javax.servlet.Filter {

    private final int id;
    private final String firstName;
    
    public MyFilter(int id, String firstName) {
        this.id = id;
        this.firstName = firstName;
    }
    
    @Override
    public void init(FilterConfig filterConfig)
            throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest hsr = (HttpServletRequest) request;
        String requestURI = hsr.getRequestURI();

        ((HttpServletResponse) response)
                .sendRedirect("/DBWebFun/show/" + id + "/" + firstName);
    }

    @Override
    public void destroy() {

    }
}
