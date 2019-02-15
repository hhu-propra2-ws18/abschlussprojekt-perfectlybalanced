package de.hhu.abschlussprojektverleihplattform.controllers;

import de.hhu.abschlussprojektverleihplattform.service.CookieUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class UserLoginFilter implements Filter {

    @Autowired
    CookieUserService cookieUserService;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request1=(HttpServletRequest) request;

        System.out.println(request1.getRequestURL().toString());

        if(request1.getRequestURL().toString().contains("login") || request1.getRequestURL().toString().contains("logout")){
            chain.doFilter(request,response);
            return;
        }

        try {

            cookieUserService.getUserFromRequest(request1).getUsername();
            chain.doFilter(request,response);
        }catch (Exception e){
            HttpServletResponse response1 = (HttpServletResponse) response;
            response1.sendRedirect("/login");
        }
    }
}
