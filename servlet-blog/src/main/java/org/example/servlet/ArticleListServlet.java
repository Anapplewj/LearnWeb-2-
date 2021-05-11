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
import java.util.List;

@WebServlet("/articleList")
public class ArticleListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        JSONResponse json = new JSONResponse();
        try{
            //1 获取请求数据/业务需要的数据
            HttpSession session = req.getSession(false);
            User user = (User) session.getAttribute("user");
            //2 业务处理：根据用户id查询关联的文章列表
            List<Article> articles = ArticleDAO.query(user.getId());

            json.setSuccess(true);
            json.setData(articles);
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
