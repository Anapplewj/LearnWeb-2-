/**
package org.example.filter;

import org.example.model.JSONResponse;
import org.example.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@WebServlet("/")
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest)request;
        HttpServletResponse resp=(HttpServletResponse)response;
        String servletPath=req.getServletPath();
        //前端敏感资源
        if(servletPath.startsWith("/jsp/")){
            if(!isLogin(req)){//登录允许继续访问
                String scheme=req.getScheme();//http
                String host=req.getServerName();//ip或域名
                int port=req.getServerPort();//port
                String contextPath=req.getContextPath();
                String basePath=scheme+"://"+host+":"+port+"/"+contextPath;
                resp.sendRedirect(basePath);
                return;
            }
        }else if(!servletPath.startsWith("/static/")
            &&!servletPath.equals("/login.html")
            &&!servletPath.equals("/login")
            ){
            chain.doFilter(request, response);
        }else{//未登录返回401状态码,响应体是json格式
            resp.setStatus(401);
            JSONResponse json=new JSONResponse();
            json.setCode("LOG000");
            json.setMessage("用户未登录,不允许访问");
            resp.getWriter().write();
        }
        chain.doFilter(request, response);
    }

    public static boolean isLogin(HttpServletRequest req){
        HttpSession session=req.getSession(false);
        if (session!=null) {
            User user=(User)session.getAttribute("user");
            if(user!=null){
                return true;
            }
        }
        return false;
    }
    @Override
    public void destroy() {

    }
}
**/