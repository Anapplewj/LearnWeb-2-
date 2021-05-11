package org.example.servlet;

import org.example.dao.ArticleDAO;
import org.example.model.Article;
import org.example.model.JSONResponse;
import org.example.model.User;
import org.example.util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/articleDelete")
public class ArticleDeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        JSONResponse json = new JSONResponse();
        try{
            String ids=req.getParameter("ids");
            int n=ArticleDAO.delete(ids.split(","));
            json.setSuccess(true);
        }catch (Exception e){
            e.printStackTrace();
            json.setCode("ERROR");
            json.setMessage("系统出错，请联系管理员");
        }

        //返回响应（设置json字符串到body）
        String s = JSONUtil.serialize(json);
        resp.getWriter().println(s);
    }
}
